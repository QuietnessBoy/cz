package com.zhjl.tech.tasks.task.block;

import com.google.common.util.concurrent.RateLimiter;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.tasks.biz.chained.DataToBlockChain;
import com.zhjl.tech.tasks.model.tasks.AttestChained;
import com.zhjl.tech.tasks.model.tasks.Warning;
import com.zhjl.tech.tasks.service.tasks.IAttestChainedService;
import com.zhjl.tech.tasks.service.tasks.IWarningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Slf4j
@Service
public class SendToBlockTask {

    /**
     * 限流,每4秒进行一次
     */
    @Async
    public void limit() {
        //四秒一个令牌 每秒不超过limiterNum个任务被提交
        RateLimiter rateLimiter = RateLimiter.create(SysConfig.limiterNum);
        while (true) {
            //尝试获取令牌
            if (rateLimiter.tryAcquire()) {
                //读最早数据  上链  删除上链成功的数据, get RefreshTime最小的待上链数据
                AttestChained attestChained = attestChainedService.getAttestChainedByMinRefreshTime(SysConfig.BizSolveOk, SysConfig.SolveFaild);
                if (attestChained != null) {
                    //当前次数加一
                    attestChained.setNum(attestChained.getNum() + 1);
                    //更新下次提交时间   为当前时间加两分钟
                    attestChained.setRefreshTime(new Date(System.currentTimeMillis() + SysConfig.limiterNextTime));
                    //更新发送时间
                    attestChained.setSendTime(new Date());
                    attestChainedService.updateSelectiveById(attestChained);

                    if(attestChained.getNum()>=10 && attestChained.getNum()<13){
                        Warning warning = warningService.getWarningByChannelOrdersnAndBizType(attestChained.getChannelOrdersn(),SysConfig.ChainedType);
                        if(warning==null){
                            Warning warning1 = new Warning();
                            warning1.setBizType(SysConfig.ChainedType);
                            warning1.setOrdersn(attestChained.getOrdersn());
                            warning1.setChannelOrdersn(attestChained.getChannelOrdersn());
                            warning1.setNum(attestChained.getNum());
                            warning1.setRemark(attestChained.getRemark());
                            warning1.setUpdateTime(new Date());
                            warningService.insertSelective(warning1);
                            log.warn("区块链处理新增报警订单记录.ordersn={}",attestChained.getOrdersn());
                        }
                    }
                    //准备请求上链
                    dataToBlockChain.send(attestChained);
                }
            }
            //执行2秒
            try {
                Thread.sleep(SysConfig.limiterSleepNum);
            } catch (InterruptedException e) {
                log.error("延迟机制错误.");
            }
        }
    }
    @Resource
    private IAttestChainedService attestChainedService;

    @Resource
    private DataToBlockChain dataToBlockChain;

    @Resource
    private IWarningService warningService;
}
