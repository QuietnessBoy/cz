package com.zhjl.tech.attest.util;

import com.zhjl.tech.attest.annotationdemo.atlog.ZhijlLog;
import com.zhjl.tech.attest.model.attest.Attest;
import com.zhjl.tech.attest.model.attest.AttestChained;
import com.zhjl.tech.attest.service.attest.IAttestChainedService;
import com.zhjl.tech.attest.service.attest.IAttestService;
import com.zhjl.tech.common.constant.SysConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;

@Service
@Slf4j
public class ChainedTool {

    @Resource
    private IAttestService attestService;

    @Resource
    private IAttestChainedService attestChainedService;

    /**
     * 处理完成的数据保存在待上链表中
     *
     * @param ordersn
     */
    @Transactional(rollbackFor = Exception.class)
    @ZhijlLog(rquestMethod="Normal",orderSn = "p[0]")
    public void saveDataToBlockChain(String ordersn) {
        //根据订单号查找对应订单记录
        Attest attest = attestService.getAttestByOrdersn(ordersn);
        if (attest == null) {
            log.warn("没有找到指定的订单.Ordersn={}", ordersn);
            return;
        }
        AttestChained attestChained1 = attestChainedService.getAttestChainedByOrdersn(ordersn);
        if (attestChained1 != null) {
            log.warn("该订单已存在于上链表中.ordersn:{}", ordersn);
            return;
        }
        //待上链的数据保存起来
        AttestChained attestChained = new AttestChained();
        attestChained.setOrdersn(ordersn);
        attestChained.setChannelOrdersn(attest.getChannelOrdersn());
        attestChained.setRefreshTime(new Date());
        attestChained.setState(SysConfig.UnFinishChainedAddCz);
        attestChained.setNum(0);
        attestChainedService.insertSelective(attestChained);
        log.info("已保存待上链信息.ordersn={}",ordersn);
    }
}
