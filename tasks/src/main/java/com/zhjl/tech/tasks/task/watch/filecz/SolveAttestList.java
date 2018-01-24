package com.zhjl.tech.tasks.task.watch.filecz;

import com.zhjl.tech.common.exception.StoreBizException;
import com.zhjl.tech.common.notify.filecz.NotifyFileCz;
import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.message.CreateAttestDetailMessage;
import com.zhjl.tech.common.notify.filecz.NotifyFileCzFailed;
import com.zhjl.tech.common.zjlsign.filecz.FileCzFailedFileRequestSign;
import com.zhjl.tech.common.zjlsign.filecz.FileCzNotifyChanRequestSign;
import com.zhjl.tech.tasks.annotationdemo.atlog.ZhijlLog;
import com.zhjl.tech.tasks.configs.EnvConfig;
import com.zhjl.tech.tasks.model.tasks.*;
import com.zhjl.tech.tasks.service.tasks.IAttestService;
import com.zhjl.tech.tasks.service.tasks.IFileCzFailedRecordService;
import com.zhjl.tech.tasks.service.tasks.IStatusService;
import com.zhjl.tech.tasks.service.tasks.IWarningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class SolveAttestList {

    public void solve(List<Status> list) {
        //解析
        for (Status s : list) {

            //更新status数据
            statusService.updateStatusByChannelOrdersn(s.getChannelOrdersn(),"");

            //根据ordersn查找订单,记录异常状态
            Status status = statusService.getStatusByOrdersn(s.getOrdersn());

            if(status.getNum()>=5){
                Warning warning = warningService.getWarningByChannelOrdersnAndBizType(s.getChannelOrdersn(),SysConfig.BizType);
                if(warning==null){
                    Warning warning1 = new Warning();
                    warning1.setBizType(SysConfig.BizType);
                    warning1.setOrdersn(status.getOrdersn());
                    warning1.setChannelOrdersn(status.getChannelOrdersn());
                    warning1.setNum(status.getNum());
                    warning1.setRemark(status.getRemark());
                    warning1.setUpdateTime(new Date());
                    warningService.insertSelective(warning1);
                    log.warn("业务处理新增报警订单记录.ordersn={}",status.getOrdersn());
                }
            }

            //通过ordersn查找订单信息
            Attest attest = attestService.getAttestByOrdersn(status.getOrdersn());
            //开始回调
            fileCzSuccessNotify(attest);
        }
    }

    public void solveFiled(List<Status> list) {
        //解析
        for (Status s : list) {

            //更新status数据
            statusService.updateStatusByChannelOrdersn(s.getChannelOrdersn(),"");

            //根据ordersn查找订单,记录异常状态
            Status status = statusService.getStatusByOrdersn(s.getOrdersn());

            if(status.getNum()>=5){
                Warning warning = warningService.getWarningByChannelOrdersnAndBizType(s.getChannelOrdersn(),SysConfig.BizType);
                if(warning==null){
                    Warning warning1 = new Warning();
                    warning1.setBizType(SysConfig.BizType);
                    warning1.setOrdersn(status.getOrdersn());
                    warning1.setChannelOrdersn(status.getChannelOrdersn());
                    warning1.setNum(status.getNum());
                    warning1.setRemark(status.getRemark());
                    warning1.setUpdateTime(new Date());
                    warningService.insertSelective(warning1);
                    log.warn("业务处理新增报警订单记录.ordersn={}",status.getOrdersn());
                }
            }

            //通过ordersn查找订单信息
            Attest attest = attestService.getAttestByOrdersn(status.getOrdersn());
            FileCzFailedRecord fileCzFailedRecord = fileCzFailedRecordService.getFileCzFailedRecordByOrdersn(attest.getOrdersn());
            //开始回调
            fileCzFiledNotify(attest,fileCzFailedRecord.getState(),fileCzFailedRecord.getMsg());
        }
    }

    /**
     * 文件存证成功异步回调函数
     * @param attest
     */
    @Async
    @ZhijlLog(rquestMethod="Normal",ChannelorderSn= "p[0].getChannelOrdersn",orderSn="p[0].getOrdersn")
    public void fileCzSuccessNotify (Attest attest){
        ///// 开始正确完成工作的回调
        // 复制对象
        CreateAttestDetailMessage createAttestDetailMessage = new CreateAttestDetailMessage();
        BeanUtils.copyProperties(attest, createAttestDetailMessage);

        // 通过缓存获取所有渠道信息
        Channel channel = EnvConfig.channelMap.get(createAttestDetailMessage.getChannelId());

        // 生成文件存证完成请求sign
        String random = UUID.randomUUID().toString().replaceAll("-","");
        String sign = FileCzNotifyChanRequestSign.gen(random, createAttestDetailMessage, channel.getChannelPublicKey());

        // 获取对应渠道地址信息Config信息
        String callBackUrl = EnvConfig.configChannnelIdMap.get(channel.getChannelId()).get(SysConfig.cz_ok_url);
        Status status = statusService.getStatusByOrdersn(attest.getOrdersn());
        // 回调渠道方
        try{
            notifyFileCz.notifyChannel(createAttestDetailMessage, callBackUrl, sign, 3,random);
            //回调成功
            status.setStateNotify(State.CallBack_File_Success);
            status.setRemark("存证成功.");
            status.setUpdateTime(new Date());
            statusService.updateSelectiveById(status);

            //删除warning表对应记录
            Warning warning = warningService.getWarningByChannelOrdersnAndBizType(status.getChannelOrdersn(),SysConfig.BizType);
            if(warning != null){
                warningService.deleteById((long)warning.getId());
            }
        }catch (StoreBizException sb){
            //更改状态表回调状态 为文件存证回调失败
            status.setStateNotify(State.CallBack_File_Failed);
            status.setRemark("文件存证完成通知回调失败");
            status.setUpdateTime(new Date());
            statusService.updateSelectiveById(status);
            throw sb;
        }
    }

    /**
     * 文件存证失败异步回调函数
     * @param attest
     */
    @Async
    @ZhijlLog(rquestMethod="Normal",ChannelorderSn = "p[0].getChannelOrdersn",orderSn="p[0].getOrdersn")
    public void fileCzFiledNotify (Attest attest,String state,String msg){
        ///// 开始正确完成工作的回调
        // 复制对象
        CreateAttestDetailMessage createAttestDetailMessage = new CreateAttestDetailMessage();
        BeanUtils.copyProperties(attest, createAttestDetailMessage);

        // 通过缓存获取所有渠道信息
        Channel channel = EnvConfig.channelMap.get(createAttestDetailMessage.getChannelId());

        // 生成文件存证完成请求sign
        String random = UUID.randomUUID().toString().replaceAll("-","");
        String sign = FileCzFailedFileRequestSign.gen(random, createAttestDetailMessage, channel.getChannelPublicKey(),SysConfig.FileCzsuccess, state, msg);

        // 获取对应渠道地址信息Config信息
        String callBackUrl = EnvConfig.configChannnelIdMap.get(channel.getChannelId()).get(SysConfig.cz_ok_url);
        Status status = statusService.getStatusByOrdersn(attest.getOrdersn());
        // 回调渠道方
        try{
            notifyFileCzFailed.notifyChannel(createAttestDetailMessage, callBackUrl, sign, 3, SysConfig.FileCzsuccess, state, msg);
            //回调成功
            status.setStateNotify(State.FailedAndCallBackSuccess);
            status.setRemark(State.FailedAndCallBackSuccess);
            status.setUpdateTime(new Date());
            statusService.updateSelectiveById(status);

            //扫描回调，回调成功后删除回调失败状态记录
            FileCzFailedRecord fileCzFailedRecord = fileCzFailedRecordService.getFileCzFailedRecordByOrdersn(attest.getOrdersn());
            if (fileCzFailedRecord != null){
                fileCzFailedRecordService.deleteById((long)fileCzFailedRecord.getId());
            }

            //删除warning表对应记录
            Warning warning = warningService.getWarningByChannelOrdersnAndBizType(status.getChannelOrdersn(),SysConfig.BizType);
            if(warning != null){
                warningService.deleteById((long)warning.getId());
            }
            log.info("文件存证失败结果回调成功.ordersn={}",createAttestDetailMessage.getOrdersn());
        }catch (StoreBizException sb){
            //更改状态表回调状态 为文件存证回调失败
            status.setStateNotify(State.FailedAndCallBackFailed);
            status.setRemark("处理结果回调失败");
            status.setUpdateTime(new Date());
            statusService.updateSelectiveById(status);
            FileCzFailedRecord fileCzFailedRecord = fileCzFailedRecordService.getFileCzFailedRecordByOrdersn(attest.getOrdersn());
            if(fileCzFailedRecord == null){
                FileCzFailedRecord fileCzFailedRecord1 = new FileCzFailedRecord();
                fileCzFailedRecord1.setChannelOrdersn(attest.getChannelOrdersn());
                fileCzFailedRecord1.setOrdersn(attest.getOrdersn());
                fileCzFailedRecord1.setState(state);
                fileCzFailedRecord1.setMsg(msg);
                fileCzFailedRecordService.insertSelective(fileCzFailedRecord1);
            }
            log.warn("文件存证失败结果回调失败.ordersn={}",createAttestDetailMessage.getOrdersn());
            throw sb;
        }
    }

    @Resource
    private IAttestService attestService;

    @Resource
    private IStatusService statusService;

    @Resource
    private IWarningService warningService;

    @Resource
    private NotifyFileCzFailed notifyFileCzFailed;

    @Resource
    private NotifyFileCz notifyFileCz;

    @Resource
    private IFileCzFailedRecordService fileCzFailedRecordService;

}
