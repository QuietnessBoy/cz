package com.zhjl.tech.attest.service.attest.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.attest.mapper.attest.WarningMapper;
import com.zhjl.tech.attest.model.attest.Warning;
import com.zhjl.tech.attest.service.attest.IWarningService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * Warning 表数据服务层接口实现类
 *
 */
@Service
public class WarningServiceImpl extends SuperServiceImpl<WarningMapper, Warning> implements IWarningService {

    @Resource
    private WarningMapper warningMapper;

    @Override
    public Warning getWarningByChannelOrdersn(String channelOrdersn) {
        return warningMapper.getWarningByChannelOrdersn(channelOrdersn);
    }

    @Override
    public Warning getWarningByChannelOrdersnAndBizType(String channelOrdersn, String bizType) {
        return warningMapper.getWarningByChannelOrdersnAndBizType(channelOrdersn,bizType);
    }

    @Override
    public Warning getWarningByOrdersn(String ordersn) {
        return warningMapper.getWarningByOrdersn(ordersn);
    }
}