package com.zhjl.tech.store.service.store.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.store.mapper.store.AttestMapper;
import com.zhjl.tech.store.model.store.Attest;
import com.zhjl.tech.store.service.store.IAttestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 *
 * Attest 表数据服务层接口实现类
 *
 */
@Service
public class AttestServiceImpl extends SuperServiceImpl<AttestMapper, Attest> implements IAttestService {

    @Resource
    private AttestMapper attestMapper;


    @Override
    public Attest getAttestByOrdersn(String ordersn) {
        return attestMapper.getAttestByOrdersn(ordersn);
    }

    @Override
    public List<Attest> getExpiredAttests(String state) {
        return attestMapper.getExpiredAttests(state);
    }

    @Override
    public Attest getAttestByChannelOrdersn(String channelOrdersn) {
        return attestMapper.getAttestByChannelOrdersn(channelOrdersn);
    }

    @Override
    public Attest getAttestByParentOrdersn(String parentOrdersn) {
        return attestMapper.getAttestByParentOrdersn(parentOrdersn);
    }

    @Override
    public List<Attest> getAttestByAncestorsOrdersn(String ancestorsOrdersn) {
        return attestMapper.getAttestByAncestorsOrdersn(ancestorsOrdersn);
    }

    @Override
    public Attest getAttestForUpdateByOrdersn(String ordersn) {
        return attestMapper.getAttestForUpdateByOrdersn(ordersn);
    }

    @Override
    public Attest getAttestByFileHashAndOwnerId(String fileHash, String ownerId) {
        return attestMapper.getAttestByFileHashAndOwnerId(fileHash,ownerId);
    }
}