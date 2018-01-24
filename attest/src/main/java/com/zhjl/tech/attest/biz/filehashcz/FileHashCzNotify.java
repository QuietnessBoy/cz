package com.zhjl.tech.attest.biz.filehashcz;

import com.zhjl.tech.attest.annotationdemo.atlog.ZhijlLog;
import com.zhjl.tech.attest.configs.EnvConfig;
import com.zhjl.tech.attest.model.attest.Attest;
import com.zhjl.tech.attest.model.attest.Channel;
import com.zhjl.tech.attest.model.attest.Status;
import com.zhjl.tech.attest.model.attest.Warning;
import com.zhjl.tech.attest.service.attest.IStatusService;
import com.zhjl.tech.attest.service.attest.IWarningService;
import com.zhjl.tech.common.exception.AttestBizException;
import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.message.CreateAttestDetailMessage;
import com.zhjl.tech.common.notify.filehashcz.NotifyFileHashCz;
import com.zhjl.tech.common.zjlsign.hashcz.HashCzNotifySign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.UUID;

@Service
@Slf4j
public class FileHashCzNotify {
    /**
     * Hash存证完成回调异步函数
     * @param attest
     */
    @Async
    @ZhijlLog(rquestMethod="Normal",ChannelorderSn = "p[0].getChannelOrdersn",orderSn="p[0].getOrdersn")
    public void HashCzNotify(Attest attest){

        //复制对象
        CreateAttestDetailMessage createAttestDetailMessage = new CreateAttestDetailMessage();
        BeanUtils.copyProperties(attest,createAttestDetailMessage);

        // 通过缓存获取所有渠道信息
        Channel channel = EnvConfig.channelMap.get(createAttestDetailMessage.getChannelId());

        // 生成文件存证完成请求sign
        String random = UUID.randomUUID().toString().replaceAll("-","");
        String sign = HashCzNotifySign.gen(random, createAttestDetailMessage, channel.getChannelPublicKey());

        // 获取对应渠道地址信息Config信息
        String callBackUrl = EnvConfig.configChannnelIdMap.get(channel.getChannelId()).get(SysConfig.hash_cz_ok_url);

        // 根据channelOrdersn查找status表中对应订单记录
        Status status = statusService.getStatusByChannelOrdersn(attest.getChannelOrdersn());
        // 回调渠道方
        try{
            // 回调文件Hash存证完成通知
            notifyFileHashCz.notifyChannel(createAttestDetailMessage, sign,callBackUrl,3);
            //回调成功
            status.setStateNotify(State.CallBack_Hash_Success);
            status.setRemark("");
            status.setUpdateTime(new Date());
            statusService.updateSelectiveById(status);

            //删除warning表对应记录
            Warning warning = warningService.getWarningByChannelOrdersnAndBizType(status.getChannelOrdersn(),SysConfig.BizType);
            if(warning != null){
                warningService.deleteById((long)warning.getId());
            }
            log.info("文件Hash存证回调成功.ordersn={}",attest.getOrdersn());
        }catch (AttestBizException sb){
            //更改状态表回调状态 为文件存证回调失败
            status.setStateNotify(State.CallBack_Hash_Failed);
            status.setRemark("回调文件Hash存证通知结果失败.");
            status.setUpdateTime(new Date());
            statusService.updateSelectiveById(status);
            log.info("文件Hash存证回调失败.ordersn={}",attest.getOrdersn());
            throw sb;
        }
    }

    @Resource
    private NotifyFileHashCz notifyFileHashCz;

    @Resource
    private IStatusService statusService;

    @Resource
    private IWarningService warningService;
}
