package com.zhjl.tech.attest.service.attest.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.attest.mapper.attest.AttestHistoryMapper;
import com.zhjl.tech.attest.model.attest.AttestHistory;
import com.zhjl.tech.attest.service.attest.IAttestHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * AttestHistory 表数据服务层接口实现类
 *
 */
@Service
public class AttestHistoryServiceImpl extends SuperServiceImpl<AttestHistoryMapper, AttestHistory> implements IAttestHistoryService {

    @Resource
    private  AttestHistoryMapper attestHistoryMapper;

    @Override
    public AttestHistory getAttestHistoryByOrdersn(String ordersn) {
        return attestHistoryMapper.getAttestHistoryByOrdersn(ordersn);
    }

    @Override
    public AttestHistory getAttestHistoryByChannelOrdersn(String channelOrdersn) {
        return attestHistoryMapper.getAttestHistoryByChannelOrdersn(channelOrdersn);
    }
}