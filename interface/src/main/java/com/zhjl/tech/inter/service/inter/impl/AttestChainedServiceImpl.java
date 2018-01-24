package com.zhjl.tech.inter.service.inter.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.inter.mapper.inter.AttestChainedMapper;
import com.zhjl.tech.inter.model.inter.AttestChained;
import com.zhjl.tech.inter.service.inter.IAttestChainedService;
import org.springframework.stereotype.Service;

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
}