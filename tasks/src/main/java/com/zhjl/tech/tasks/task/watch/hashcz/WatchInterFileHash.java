package com.zhjl.tech.tasks.task.watch.hashcz;

import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.message.CreateCzMessage;
import com.zhjl.tech.tasks.model.tasks.Status;
import com.zhjl.tech.tasks.model.tasks.Warning;
import com.zhjl.tech.tasks.rabbitmq.sender.InterHashCzSender;
import com.zhjl.tech.tasks.service.tasks.IStatusService;
import com.zhjl.tech.tasks.service.tasks.IWarningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 监听文件Hash存证未完成的订单
 */
@Service
@Slf4j
public class WatchInterFileHash {

    /**
     * 监听订单状态为文件Hash存证请求处理完成订单
     */
    public void watchRequestFileHashCzOk() {
        log.info("[开始扫描文件Hash存证订单]");
        //查找订单状态为"文件存证接收处理完成"状态的订单集合
        List<Status> list = statusService.getStatusByStateBizInTime(State.Solve_FileHashCz_Ok, SysConfig.Num);

        if(list.isEmpty()){
            log.warn("未找到近期文件Hash存证处理错误的订单.");
            return;
        }

        for (Status s : list) {
            //更新status数据
            statusService.updateStatusByChannelOrdersn(s.getChannelOrdersn(),"推送至业务处理子系统失败");

            Status status = statusService.getStatusByChannelOrdersn(s.getChannelOrdersn());

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

            //推送消息至业务处理子系统
            CreateCzMessage createCzMessage = new CreateCzMessage();
            createCzMessage.setChannelOrdersn(status.getChannelOrdersn());
            interHashCzSender.send(createCzMessage);
        }
        log.info("[扫描文件Hash存证订单结束]");
    }

    @Resource
    private InterHashCzSender interHashCzSender;

    @Resource
    private IStatusService statusService;

    @Resource
    private IWarningService warningService;
}
