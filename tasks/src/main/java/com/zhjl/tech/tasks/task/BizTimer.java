package com.zhjl.tech.tasks.task;

import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.tasks.service.tasks.IAttestService;
import com.zhjl.tech.tasks.task.rabbitmq.WatchMsg;
import com.zhjl.tech.tasks.task.watch.czxq.WatchExpireCzXq;
import com.zhjl.tech.tasks.task.watch.czxq.WatchInterFormXqCz;
import com.zhjl.tech.tasks.task.watch.filecz.WatchAttestFileCz;
import com.zhjl.tech.tasks.task.watch.filecz.WatchInterFileCz;
import com.zhjl.tech.tasks.task.watch.filecz.WatchStoreFileCz;
import com.zhjl.tech.tasks.task.watch.hashcz.WatchAttestFileHash;
import com.zhjl.tech.tasks.task.watch.czxq.WatchAttestFromXqCz;
import com.zhjl.tech.tasks.task.watch.hashcz.WatchInterFileHash;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 业务相关的作业调度
 * <p>
 * 字段               允许值                           允许的特殊字符
 * 秒	 	0-59	 	, - * /
 * 分	 	0-59	 	, - * /
 * 小时	 	0-23	 	, - * /
 * 日期	 	1-31	 	, - * ? / L W C
 * 月份	 	1-12 或者 JAN-DEC	 	, - * /
 * 星期	 	1-7 或者 SUN-SAT	 	, - * ? / L C #
 * 年（可选）	 	留空, 1970-2099	 	, - * /
 * <p>
 * 字符代表所有可能的值
 * /  字符用来指定数值的增量
 * L  字符仅被用于天（月）和天（星期）两个子表达式，表示一个月的最后一天或者一个星期的最后一天
 * 6L 可以表示倒数第６天
 */
@Slf4j
@Component
public class BizTimer {

    @Resource
    private WatchAttestFileCz watchAttestFileCz;

    @Resource
    private WatchAttestFileHash watchAttestFileHash;

    @Resource
    private WatchInterFileHash watchInterFileHash;

    @Resource
    private WatchInterFileCz watchInterFileCz;

    @Resource
    private WatchStoreFileCz watchStoreFileCz;

    @Resource
    private WatchAttestFromXqCz watchAttestFromXqCz;

    @Resource
    private WatchInterFormXqCz watchInterFormXqCz;

    @Resource
    private WatchExpireCzXq watchExpireCzXq;

    @Resource
    private IAttestService attestService;

    @Resource
    private WatchMsg watchMsg;


    /**
     * 扫描正常业务处理订单
     * 每5分钟执行一次
     */
    @Scheduled(cron = "0 0/5 * * * ?")
//    @Scheduled(cron="0/15 * * * * ?")
    public void listen(){
        /*******************inter*******************/
        watchInterFileCz.watchRequestFileCzOk();
        watchInterFileHash.watchRequestFileHashCzOk();
        watchInterFormXqCz.watchRequestXqCzOk();

        /*******************attest*******************/
        watchAttestFileCz.watchFileCzRequestSolveOk();
        watchAttestFileHash.watchFileHashCzCallBackOk();
        watchAttestFromXqCz.watchXqCzCallBackOk();

        /*******************store*******************/
        watchStoreFileCz.watchFileCzCallBackOk();
        watchStoreFileCz.watchSolveFaild();

        /*******************warning*******************/
//        watchWarningOrder.listen();
    }

    /**
     *  检测续期订单是否过时
     * 每天2点执行一次
     */
    @Scheduled(cron="0 0 2 * * ? ")
    public void listenOutTime(){
        // 1. 获取总数
        int count = attestService.getAllExpired(SysConfig.BizSolveOk, SysConfig.GoChained);
        int a = 0;
        if(count > 0){
            if(count% SysConfig.Count>0){
                a=count/ SysConfig.Count+1;
            }
            // 2 按照总数确定执行的次数
            for(int i=0;i<a ;i++){
                watchExpireCzXq.watchCzIsExpire();
            }
        }
    }

    /**
     * 每隔5s，监控rabbitMq队列消息数量，并入库
     */
    @Scheduled(cron="0/5 * * * * ?")
    public void listenMqQueue(){
        watchMsg.watch(SysConfig.CreateFileCzQueue);
        watchMsg.watch(SysConfig.CreateFileCzByAddrQueue);
        watchMsg.watch(SysConfig.CreateHashCzQueue);
        watchMsg.watch(SysConfig.CreateCzXqQueue);
    }
}
