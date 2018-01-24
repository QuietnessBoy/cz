package com.zhjl.tech.tasks.service.tasks.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.tasks.mapper.tasks.WarningMapper;
import com.zhjl.tech.tasks.model.tasks.Warning;
import com.zhjl.tech.tasks.service.tasks.IWarningService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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