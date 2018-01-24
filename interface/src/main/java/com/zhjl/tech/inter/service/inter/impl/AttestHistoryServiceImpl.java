package com.zhjl.tech.inter.service.inter.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.inter.mapper.inter.AttestHistoryMapper;
import com.zhjl.tech.inter.model.inter.AttestHistory;
import com.zhjl.tech.inter.service.inter.IAttestHistoryService;
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
    private AttestHistoryMapper attestHistoryMapper;

    @Override
    public List<AttestHistory> getAllByAttestHistory() {
        return attestHistoryMapper.getAllByAttestHistory();
    }

    @Override
    public AttestHistory getOrdersnByAttestHistory(String ordersn) {
        return attestHistoryMapper.getOrdersnByAttestHistory(ordersn);
    }

    @Override
    public AttestHistory getChannelOrdersnByAttestHistory(String channelOrdersn) {
        return attestHistoryMapper.getOrdersnByAttestHistory(channelOrdersn);
    }
}