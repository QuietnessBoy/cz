package com.zhjl.tech.tasks.service.tasks.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.tasks.mapper.tasks.AttestHistoryMapper;
import com.zhjl.tech.tasks.model.tasks.AttestHistory;
import com.zhjl.tech.tasks.service.tasks.IAttestHistoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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