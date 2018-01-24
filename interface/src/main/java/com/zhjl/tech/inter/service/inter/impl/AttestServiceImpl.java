package com.zhjl.tech.inter.service.inter.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.inter.mapper.inter.AttestMapper;
import com.zhjl.tech.inter.model.inter.Attest;
import com.zhjl.tech.inter.service.inter.IAttestService;
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
    public Attest getlastestAttestByOrdersn(String ancestors_ordersn, String state1, String state2) {
        return attestMapper.getlastestAttestByOrdersn(ancestors_ordersn,state1,state2);
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
    public Attest getAttestForUpdateByChannelOrdersn(String channelOrdersn) {
        return attestMapper.getAttestForUpdateByChannelOrdersn(channelOrdersn);
    }

    @Override
    public Attest getAttestByFileHashAndOwnerId(String fileHash, String ownerId) {
        return attestMapper.getAttestByFileHashAndOwnerId(fileHash,ownerId);
    }

    @Override
    public List<Attest> getAttestList(int head, int ten) {
        return attestMapper.getAttestList(head,ten);
    }
}