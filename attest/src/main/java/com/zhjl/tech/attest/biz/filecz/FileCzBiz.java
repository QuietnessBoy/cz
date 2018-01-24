package com.zhjl.tech.attest.biz.filecz;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.attest.annotationdemo.atlog.ZhijlLog;
import com.zhjl.tech.attest.biz.common.CommonBiz;
import com.zhjl.tech.attest.configs.EnvConfig;
import com.zhjl.tech.attest.model.attest.*;
import com.zhjl.tech.attest.rabbitmq.send.CreateFileCzSender;
import com.zhjl.tech.attest.service.attest.*;
import com.zhjl.tech.attest.util.OrdersnGeneration;
import com.zhjl.tech.common.exception.AttestBizException;
import com.zhjl.tech.common.exception.NormalException;
import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.encrypt.asymmetric.SM2;
import com.zhjl.tech.common.encrypt.asymmetric.SM2Impl;
import com.zhjl.tech.common.encrypt.asymmetric.SM2KeyPair;
import com.zhjl.tech.common.message.CreateFileDataMessage;
import com.zhjl.tech.common.utils.TimeTool;
import com.zhjl.tech.common.zjlsign.filecz.FileCzAttestSign;
import com.zhjl.tech.common.zjlsign.filecz.FileCzPlatformSign;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class FileCzBiz {


    /**
     * 开始文件存证
     * @param channelOrdersn
     */
    @ZhijlLog(rquestMethod="rabbitMQ",orderSn="r",ChannelorderSn = "p[0]")
    public String solveFileCz(String channelOrdersn) {
            //判断正式表（attest）是否存在该订单记录
            Attest attest = attestService.getAttestByChannelOrdersn(channelOrdersn);
            Attest attest1 = null;
            //正式表不存在订单，正常处理
            if (attest == null) {
                attest1 = solveNormal(channelOrdersn);
                // rabbitMq推送消息至文件存储系统
                CreateFileDataMessage createFileDataMessage = new CreateFileDataMessage();
                createFileDataMessage.setOrdersn(attest1.getOrdersn());
                createFileCzSender.send(createFileDataMessage);
                return attest1.getOrdersn();
            } else if (StringUtils.isNotBlank(attest.getOrdersn())
                    && attest.getState().equals(SysConfig.SolveFaild)) {
                //当订单存在的时候，肯定有订单号
                //当订单存在的时候，只有state是失败的时候才需要进一步处理，其他情况忽略。
                solveFaild(attest);

                // rabbitMq推送消息至文件存储系统
                CreateFileDataMessage createFileDataMessage = new CreateFileDataMessage();
                createFileDataMessage.setOrdersn(attest.getOrdersn());
                createFileCzSender.send(createFileDataMessage);
                return attest.getOrdersn();
            } else {
                // 三种情况。到此为止！！
                // 1 attest存在，但是状态不为“失败”
                // 2 此时attest表订单存在，ordersn为空；这种情况不可能发生，所以不用处理。
                // 3.通过rabbitmq扫描进入队列的已处理成功的信息，有ordersn，状态为“[File]文件存证业务处理完成2”，是正常现象。
                log.warn("收到消息，不予处理.channelOrdersn={}", channelOrdersn);
                return attest.getOrdersn();
            }
    }

    /**
     * 文件存证,正常处理流程。
     *  正常处理完毕会返回业务对象
     *  异常处理，会返回业务异常
     *
     * @param channelOrdersn
     * @return
     * @throws AttestBizException
     */
    @Transactional(rollbackFor = Exception.class)
    public Attest solveNormal(String channelOrdersn) {
        TempOrder tempOrder = fileCzBizDos.getLockAndCheckStatus(channelOrdersn);

        //复制对象
        Attest attest = new Attest();
        BeanUtils.copyProperties(tempOrder, attest);

        /////////////////////// temp operation start 这部分操作主要是替终端用户生成私钥、生成attestSign
        Channel channel = EnvConfig.channelMap.get(tempOrder.getChannelId());
        fileCzBizDos.thingsUserShouldDo(channel, attest);
        /////////////////////// temp operation end

        //// 生成订单号
        String ordersn = ordersnGeneration.gen(attest);
        fileCzBizDos.checkDumplicateOrdersn(ordersn) ;
        attest.setAncestorsOrdersn(ordersn);
        attest.setOrdersn(ordersn);

        //todo importan time。should be remember it!
        Date nowDate = new Date();
        attest.setStartTime(nowDate);
        attest.setOriginTime(nowDate);

        // 计算expireTime
        attest.setExpiredTime(TimeTool.getDateAfterDays(attest.getStartTime(),Integer.parseInt(attest.getDuration())));

        //生成平台签名 platformSign
        AttestDto attestDto = new AttestDto();
        BeanUtils.copyProperties(attest, attestDto);
        attestDto.setRequestTime( TimeTool.date2Str(attest.getRequestTime()));
        attestDto.setStartTime(TimeTool.date2Str(attest.getStartTime()));
        String platformSign = FileCzPlatformSign.gen(attestDto,channel.getPlatformPublickKey(),channel.getPlatformPrivateKey());
        attest.setPlatformSign(platformSign);
        attest.setUpdateTime(null);
        attest.setCreateTime(null);
        attestService.insertSelective(attest);
        log.info("添加正式订单订单记录.ordersn={}", ordersn);

        //更新AttestFile表
        AttestFile attestFile = new AttestFile();
        BeanUtils.copyProperties(attest, attestFile);
        attestFile.setFileAddr(tempOrder.getFileAddr());
        attestFile.setUpdateTime(new Date());
        attestFileService.insertSelective(attestFile);

        //删除临时表存证记录
        tempOrderService.deleteById((long)tempOrder.getId());

        //业务处理完成
        Status status = statusService.getStatusByChannelOrdersn(channelOrdersn);
        status.setStateBiz(State.Solve_FileCz_Ok);
        status.setOrdersn(ordersn);
        status.setUpdateTime(new Date());
        statusService.updateSelectiveById(status);

        log.info("[业务处理系统工作结束].ordersn={}",ordersn);
        return attest;
    }

    // 接收处理失败订单消息
    @Transactional(rollbackFor = Exception.class)
    public void solveFaild(Attest attest) {
        TempOrder tempOrder = fileCzBizDos.getLockAndCheckStatus(attest.getChannelOrdersn());

        SM2 sm2 = new SM2Impl();
        // 获取缓存的channel信息
        String ordersn = attest.getOrdersn();
        Attest attest_new = new Attest();
        BeanUtils.copyProperties(tempOrder, attest_new);
        attest_new.setId(attest.getId());

        //////////////// temp operation start 这部分操作主要是替终端用户生成私钥
        //根据attest对象获取对应用户的keypair，同时设置attest对象的钱包地址等
        SM2KeyPair sm2KeyPair = commonBiz.genUserKeyPair(attest_new);

        // 获取缓存的channel信息
        Channel channel = EnvConfig.channelMap.get(attest_new.getChannelId());

        // 根据用户公钥生成订单的用户签名attestSign信息
        AttestDto attestDto_t = new AttestDto();
        BeanUtils.copyProperties(attest_new,attestDto_t);
        String attestSignStr = FileCzAttestSign.gen(sm2KeyPair, sm2.encodePublicKey(sm2KeyPair.getPublicKey()), channel.getChannelIda(),attestDto_t );
        //Base64转换格式
        attest_new.setAttestSign(attestSignStr);
        //////////////// temp operation end

        //set requestTime
        attestDto_t.setRequestTime(TimeTool.date2Str(attest_new.getRequestTime()));
        attestDto_t.setStartTime(TimeTool.date2Str(new Date()));

        Date nowDate = new Date();
        attest_new.setStartTime(nowDate);
        attest_new.setOriginTime(nowDate);

        //计算expireTime  之前必须先setStartTime
        attest_new.setExpiredTime(TimeTool.getDateAfterDays(attest_new.getStartTime(),Integer.valueOf(attest_new.getDuration())));

        // 根据生成平台签名 platformSign
        String platformSign = FileCzPlatformSign.gen(attestDto_t,channel.getPlatformPublickKey(),channel.getChannelPrivateKey());
        attest_new.setPlatformSign(platformSign);

        attest_new.setAncestorsOrdersn(ordersn);
        attest_new.setOrdersn(ordersn);
        attest_new.setFileHash(tempOrder.getFileHash());
        attest_new.setUpdateTime(new Date());
        attestService.updateSelectiveById(attest_new);
        log.info("更新正式订单订单记录.ordersn={}", ordersn);

        //更新AttestFile表
        AttestFile attestFile = attestFileService.getAttestFileByOrdersn(ordersn);
        int attestFileID = attestFile.getId();
        BeanUtils.copyProperties(attest_new, attestFile);
        attestFile.setId(attestFileID);
        attestFile.setFileAddr(tempOrder.getFileAddr());
        attestFile.setFileSize(tempOrder.getFileSize());
        attestFile.setUpdateTime(new Date());
        attestFileService.updateSelectiveById(attestFile);

        //删除临时表存证记录
        tempOrderService.deleteById((long)tempOrder.getId());

        Status status = statusService.getStatusByChannelOrdersn(attest_new.getChannelOrdersn());
        // 判断订单状态是否为空。空的话insert,不为空的话update
        status.setStateBiz(State.Solve_FileCz_Ok);
        status.setOrdersn(ordersn);
        status.setUpdateTime(new Date());
        statusService.updateSelectiveById(status);

        log.info("[业务处理系统重新处理失败订单结束],ordersn={}",attest.getOrdersn());
    }
    @Resource
    private OrdersnGeneration ordersnGeneration;

    @Resource
    private CreateFileCzSender createFileCzSender;

    @Resource
    private IAttestFileService attestFileService;

    @Resource
    private ITempOrderService tempOrderService;

    @Resource
    private IAttestService attestService;

    @Resource
    private IStatusService statusService;

    @Resource
    private CommonBiz commonBiz;

    @Resource
    private FileCzBizDos fileCzBizDos;
}
