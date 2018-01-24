package com.zhjl.tech.tasks.task.warning;

import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.tasks.model.tasks.AttestChained;
import com.zhjl.tech.tasks.model.tasks.Status;
import com.zhjl.tech.tasks.model.tasks.Warning;
import com.zhjl.tech.tasks.service.tasks.IAttestChainedService;
import com.zhjl.tech.tasks.service.tasks.IStatusService;
import com.zhjl.tech.tasks.service.tasks.IWarningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class WatchWarningOrder {

    @Resource
    private IAttestChainedService attestChainedService;

    @Resource
    private IStatusService statusService;

    @Resource
    private IWarningService warningService;


    /**
     * 监听订单状态
     */
    public void listen(){
        log.info("[开始查询异常订单.]");
        //查找异常记录大于5次的订单
        List<Status> list = statusService.getStatusByNum(SysConfig.Num);
        if(list.isEmpty()){
            log.info("未发现处理次数大于5次的订单.");
            return;
        }

        for (Status status : list) {
            Warning warning = warningService.getWarningByChannelOrdersn(status.getChannelOrdersn());
            if(warning==null){
                //记录到报警表
                Warning warning1 = new Warning();
                warning1.setBizType(SysConfig.BizType);
                warning1.setOrdersn(status.getOrdersn());
                warning1.setChannelOrdersn(status.getChannelOrdersn());
                warning1.setNum(status.getNum());
                warning1.setRemark(status.getRemark());
                warning1.setUpdateTime(new Date());
                warningService.insertSelective(warning1);
                log.warn("新增报警订单记录.ordersn={}",status.getOrdersn());
            }
        }

        //查找处理次数大于5的上链订单集合
        List<AttestChained> list1 = attestChainedService.getAttestChainedByNum(SysConfig.Num);
        if(list1.isEmpty()){
            log.info("未发现处理次数大于5次的订单.");
            return;
        }
        for (AttestChained attestChained : list1) {
            Warning warning = warningService.getWarningByOrdersn(attestChained.getOrdersn());
            if(warning==null){
                //记录到报警表
                Warning warning1 = new Warning();
                warning1.setBizType(SysConfig.ChainedType);
                warning1.setOrdersn(attestChained.getOrdersn());
                warning1.setChannelOrdersn(attestChained.getChannelOrdersn());
                warning1.setNum(attestChained.getNum());
                warning1.setRemark(attestChained.getRemark());
                warning1.setUpdateTime(new Date());
                warningService.insertSelective(warning1);
                log.warn("新增上链异常订单至报警表.ordersn={}",attestChained.getOrdersn());
            }
        }
        log.info("[查询异常订单结束.]");
    }
}
