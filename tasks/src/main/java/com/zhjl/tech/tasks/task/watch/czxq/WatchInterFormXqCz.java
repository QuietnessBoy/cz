package com.zhjl.tech.tasks.task.watch.czxq;

import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.message.CreateAttestDetailMessage;
import com.zhjl.tech.tasks.model.tasks.Status;
import com.zhjl.tech.tasks.model.tasks.TempOrder;
import com.zhjl.tech.tasks.model.tasks.Warning;
import com.zhjl.tech.tasks.rabbitmq.sender.InterCzXqSender;
import com.zhjl.tech.tasks.service.tasks.IStatusService;
import com.zhjl.tech.tasks.service.tasks.ITempOrderService;
import com.zhjl.tech.tasks.service.tasks.IWarningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/*
 * 监听存证续期未完成的订单
 */
@Service
@Slf4j
public class WatchInterFormXqCz {

    /**
     * 监听订单状态为续期存证接收处理完成订单
     */
    public void watchRequestXqCzOk() {
        List<Status> list = statusService.getStatusByStateBizInTime(State.Solve_Xq_Ok, SysConfig.Num);
        if(list.isEmpty()){
            log.warn("未找到近期续期存证处理错误的订单.");
            return;
        }
        //解析
        for (Status s : list) {
            //更新status数据
            statusService.updateStatusByChannelOrdersn(s.getChannelOrdersn(),"推送至业务处理子系统失败");

            //根据channelOrdersn查找订单,记录异常状态
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

            //通过ordersn查找订单信息
            TempOrder tempOrder = tempOrderService.getTempOrderByChannelOrdersn(s.getChannelOrdersn());
            if(tempOrder == null){
                log.warn("未找到近期续期存证处理错误的订单.");
                return;
            }

            //推送消息至业务处理子系统
            CreateAttestDetailMessage createAttestDetailMessage = new CreateAttestDetailMessage();
            BeanUtils.copyProperties(tempOrder, createAttestDetailMessage);
            interCzXqSender.send(createAttestDetailMessage);
            log.info("重新推送请求.channelOrdersn={}",createAttestDetailMessage.getChannelOrdersn());
        }
//        log.info("[扫描续期存证订单结束.]");
    }


    @Resource
    private InterCzXqSender interCzXqSender;

    @Resource
    private ITempOrderService tempOrderService;

    @Resource
    private IStatusService statusService;

    @Resource
    private IWarningService warningService;

}
