package com.zhjl.tech.tasks.task.watch.hashcz;

import com.zhjl.tech.common.exception.AttestBizException;
import com.zhjl.tech.common.notify.filehashcz.NotifyFileHashCz;
import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.message.CreateAttestDetailMessage;
import com.zhjl.tech.common.zjlsign.hashcz.HashCzNotifySign;
import com.zhjl.tech.tasks.annotationdemo.atlog.ZhijlLog;
import com.zhjl.tech.tasks.configs.EnvConfig;
import com.zhjl.tech.tasks.model.tasks.Attest;
import com.zhjl.tech.tasks.model.tasks.Channel;
import com.zhjl.tech.tasks.model.tasks.Status;
import com.zhjl.tech.tasks.model.tasks.Warning;
import com.zhjl.tech.tasks.service.tasks.IAttestService;
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
public class WatchAttestFileHash {


    /**
     * 监听订单状态为文件Hash存证回调失败的订单信息
     */
    public void watchFileHashCzCallBackOk() {
        log.info("[开始扫描文件Hash存证回调失败订单.]");

        //查找订单状态为"文件Hash存证回调失败"状态的订单集合
        List<Status> list = statusService.getStatusByStateNotifyInTime(State.CallBack_Hash_Failed, SysConfig.Num);

        if(list == null){
            log.warn("未找到近期文件Hash存证回调失败的订单.");
            return;
        }

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
                    log.warn("业务处理新增报警订单记录.{}",status.getOrdersn());
                }
            }

            //通过ordersn查找订单信息
            Attest attest = attestService.getAttestByOrdersn(status.getOrdersn());
            //异步回调
            HashCzNotify(attest);
        }
    }

    /**
     * 续期完成回调异步函数
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
        String random = UUID.randomUUID().toString();
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
        }catch (AttestBizException sb){
            //更改状态表回调状态 为文件存证回调失败
            status.setStateNotify(State.CallBack_Hash_Failed);
            status.setRemark("回调文件Hash存证通知结果失败");
            status.setUpdateTime(new Date());
            statusService.updateSelectiveById(status);
            throw sb;
        }
    }

    @Resource
    private IStatusService statusService;

    @Resource
    private IAttestService attestService;

    @Resource
    private NotifyFileHashCz notifyFileHashCz;

    @Resource
    private IWarningService warningService;
}
