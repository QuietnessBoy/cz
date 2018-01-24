package com.zhjl.tech.tasks.task.watch.filecz;

import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.message.CreateFileDataMessage;
import com.zhjl.tech.tasks.model.tasks.Status;
import com.zhjl.tech.tasks.model.tasks.Warning;
import com.zhjl.tech.tasks.rabbitmq.sender.AttestFileCzSender;
import com.zhjl.tech.tasks.service.tasks.IStatusService;
import com.zhjl.tech.tasks.service.tasks.IWarningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 监听订单状态未文件存证请求接收完成订单
 */

@Service
@Slf4j
public class WatchAttestFileCz {

    /**
     * 监听订单状态为文件存证请求接收完成订单
     */
    public void watchFileCzRequestSolveOk() {
        log.info("开始扫描文件存证订单.");
        //查找订单状态为"文件存证接收处理完成"状态的订单集合
        List<Status> list = statusService.getStatusByStateBizInTime(State.Solve_FileCz_Ok, SysConfig.Num);

        if(list.isEmpty()){
            log.warn("未找到近期文件存证处理错误的订单.");
            return;
        }

        //解析
        for (Status s : list) {
            //更新status数据
            statusService.updateStatusByChannelOrdersn(s.getChannelOrdersn(),"推送至文件存储系统失败");

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

            //复制对象，推送消息至文件存储系统（store）
            CreateFileDataMessage createFileDataMessage = new CreateFileDataMessage();
            createFileDataMessage.setOrdersn(status.getOrdersn());
            attestFileCzSender.send(createFileDataMessage);
        }
    }
    @Resource
    private IStatusService statusService;

    @Resource
    private AttestFileCzSender attestFileCzSender;

    @Resource
    private IWarningService warningService;
}
