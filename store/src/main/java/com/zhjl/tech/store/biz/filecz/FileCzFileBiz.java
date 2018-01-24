package com.zhjl.tech.store.biz.filecz;

import com.zhjl.tech.common.exception.NormalException;
import com.zhjl.tech.common.exception.StoreBizException;
import com.zhjl.tech.common.constant.JsonConfig;
import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.message.CreateFileDataMessage;
import com.zhjl.tech.store.annotationdemo.atlog.ZhijlLog;
import com.zhjl.tech.store.model.store.*;
import com.zhjl.tech.store.service.store.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.net.*;
import java.util.Date;

/**
 * 生成fileToken并推送至渠道方
 */
@Service
@Slf4j
public class FileCzFileBiz {

    @ZhijlLog(rquestMethod="rabbitMQ",ChannelorderSn="r",orderSn = "p[0].getOrdersn")
    public String solveFileCzFile(CreateFileDataMessage createFileDataMessage) {

            // 根据地址下载文件
            Attest attest = solveFile(createFileDataMessage);

            //异步回调
            fileCzNotify.fileCzSuccessNotify(attest);

            // 保存上链数据
            fileCzFileBiz.saveDataToBlockChain(createFileDataMessage);

            return attest.getChannelOrdersn();
    }

    /**
     * 解析URL下载文件
     */
    @Transactional(rollbackFor = Exception.class)
    public Attest solveFile(CreateFileDataMessage createFileDataMessage) {

        Attest attest = solveFileCzFileDos.getAttestWithLock(createFileDataMessage);
        AttestFile attestFile = attestFileService.getAttestFileByOrdersn(createFileDataMessage.getOrdersn());

        //如果状态为  文件存证接收处理完成  则进行处理
        Status status = statusService.getStatusByOrdersn(createFileDataMessage.getOrdersn());

        HttpURLConnection conn = null;
        try {
            URL url = new URL(attestFile.getFileAddr());
            conn = (HttpURLConnection) url.openConnection();

            //设置超时间
            conn.setConnectTimeout(timeOut);
            conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
            //判断超时
            if (conn.getResponseCode() == -1) {
                log.error("请求超时.ordersn={}", createFileDataMessage.getOrdersn());
                //失败回调
                fileCzNotify.fileCzFiledNotify(attest, JsonConfig.TimeOut, JsonConfig.TimeOutDesc);
                throw new StoreBizException(JsonConfig.TimeOutDesc + ",ordersn=" + attest.getOrdersn());
            }

            //写入文件，获取Hash和字节数并进行计算
            readerFile.saveFileToLocalOss(conn.getInputStream(), attest);

            //更新Attest表
            attest.setState(SysConfig.BizSolveOk);
            attest.setStateTime(new Date());
            attest.setUpdateTime(new Date());
            attestService.updateSelectiveById(attest);

            //更新订单状态
            status.setStateBiz(State.Receive_File_Gen_BlockFile_Ok);
            status.setUpdateTime(new Date());
            statusService.updateSelectiveById(status);

            log.info("[文件存储系统工作结束]文件存证订单完成.ordersn={}",createFileDataMessage.getOrdersn());
        } catch (MalformedURLException e) {
            log.error("url路径格式不正确.{}", attestFile.getFileAddr(), e);
            attest.setState(SysConfig.SolveFaild);
            attest.setStateTime(new Date());
            attest.setUpdateTime(new Date());
            attestService.updateSelectiveById(attest);
            //更改状态表业务状态 为文件存证处理失败
            status.setStateBiz(State.File_Failed);
            status.setUpdateTime(new Date());
            statusService.updateSelectiveById(status);
            //失败回调
            StringBuilder sb = new StringBuilder();
            sb = sb.append(JsonConfig.ErrorFileAddrDesc).append(attestFile.getFileAddr());
            fileCzNotify.fileCzFiledNotify(attest, JsonConfig.ErrorFileAddr, sb.toString());
            throw new StoreBizException(sb.toString() + ",ordersn=" + attest.getOrdersn());
        } catch (IOException e) {
            log.error("获取文件流异常.ordersn={}",createFileDataMessage.getOrdersn(), e);
            attest.setState(SysConfig.SolveFaild);
            attest.setStateTime(new Date());
            attest.setUpdateTime(new Date());
            attestService.updateSelectiveById(attest);
            //更改状态表业务状态 为文件存证处理失败
            status.setStateBiz(State.File_Failed);
            status.setUpdateTime(new Date());
            statusService.updateSelectiveById(status);

            //失败回调
            fileCzNotify.fileCzFiledNotify(attest, JsonConfig.FileStreamException, JsonConfig.FileStreamExceptionDesc);
            throw new StoreBizException(JsonConfig.FileStreamExceptionDesc + "，ordersn=" + attest.getOrdersn());
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
        return attest;
    }

    /**
     * 处理完成的数据保存在待上链表中
     *
     * @param createFileDataMessage
     */
    @Transactional(rollbackFor = Exception.class)
    @ZhijlLog(rquestMethod="Normal",orderSn = "p[0].getOrdersn")
    public void saveDataToBlockChain(CreateFileDataMessage createFileDataMessage) {

        //根据订单号查找对应订单记录
        Attest attest = attestService.getAttestByOrdersn(createFileDataMessage.getOrdersn());
        if (attest == null) {
            log.warn("没有找到指定的订单,ordersn:{}", createFileDataMessage.getOrdersn());
            return;
        }
        AttestChained attestChained1 = attestChainedService.getAttestChainedByOrdersn(attest.getOrdersn());
        if (attestChained1 != null) {
            log.warn("该订单已存在于上链表中,ordersn:{}", attest.getOrdersn());
            return;
        }
        //保存上链数据至attestChained表
        AttestChained attestChained = new AttestChained();
        attestChained.setOrdersn(attest.getOrdersn());
        attestChained.setChannelOrdersn(attest.getChannelOrdersn());
        attestChained.setRefreshTime(new Date());//更新时间初始值为收到数据时间
        attestChained.setState(SysConfig.UnFinishChainedAddCz);
        attestChained.setNum(0);
        attestChainedService.insertSelective(attestChained);
        log.info("已保存待上链信息.ordersn:{}", createFileDataMessage.getOrdersn());
    }


    private int timeOut = 5000;


    @Resource
    private IAttestChainedService attestChainedService;

    @Resource
    private SolveFileCzFileDos solveFileCzFileDos;

    @Resource
    private IAttestFileService attestFileService;

    @Resource
    private IAttestService attestService;

    @Resource
    private IStatusService statusService;

    @Resource
    private FileCzNotify fileCzNotify;

    @Resource
    private ReaderFile readerFile;

    @Resource
    private FileCzFileBiz fileCzFileBiz;
}
