package com.zhjl.tech.tasks.service.tasks.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.tasks.mapper.tasks.AttestChainedMapper;
import com.zhjl.tech.tasks.model.tasks.AttestChained;
import com.zhjl.tech.tasks.service.tasks.IAttestChainedService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * AttestChainedSolve 表数据服务层接口实现类
 *
 */
@Service
public class AttestChainedServiceImpl extends SuperServiceImpl<AttestChainedMapper, AttestChained> implements IAttestChainedService {

    @Resource
    private AttestChainedMapper attestChainedMapper;


    @Override
    public AttestChained getAttestChainedByOrdersn(String ordersn) {
        return attestChainedMapper.getAttestChainedByOrdersn(ordersn);
    }

    @Override
    public AttestChained getAttestChainedByTxId(String txid) {
        return attestChainedMapper.getAttestChainedByTxId(txid);
    }

    @Override
    public List<AttestChained> getAttestChainedByNum(int num) {
        return attestChainedMapper.getAttestChainedByNum(num);
    }

    @Override
    public AttestChained getAttestChainedByMinRefreshTime(String state1, String state2) {
        return attestChainedMapper.getAttestChainedByMinRefreshTime(state1,state2);
    }


}