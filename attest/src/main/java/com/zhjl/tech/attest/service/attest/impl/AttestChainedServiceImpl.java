package com.zhjl.tech.attest.service.attest.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.attest.mapper.attest.AttestChainedMapper;
import com.zhjl.tech.attest.model.attest.AttestChained;
import com.zhjl.tech.attest.service.attest.IAttestChainedService;
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
        return attestChainedMapper.getAttestChainedByOrdersn(txid);
    }

    @Override
    public List<AttestChained> getAttestChainedByNum(int num) {
        return attestChainedMapper.getAttestChainedByNum(num);
    }

    @Override
    public AttestChained getAttestChainedByMinRefreshTime(String state) {
        return attestChainedMapper.getAttestChainedByMinRefreshTime(state);
    }
}