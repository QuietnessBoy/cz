package com.zhjl.tech.store.service.store.impl;

import com.zhjl.tech.store.mapper.store.AttestChainedMapper;
import com.zhjl.tech.store.model.store.AttestChained;
import com.zhjl.tech.store.service.store.IAttestChainedService;
import org.springframework.stereotype.Service;

import com.baomidou.framework.service.impl.SuperServiceImpl;

import javax.annotation.Resource;

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
    public AttestChained getAttestChainedByMinRefreshTime(String state) {
        return attestChainedMapper.getAttestChainedByMinRefreshTime(state);
    }
}