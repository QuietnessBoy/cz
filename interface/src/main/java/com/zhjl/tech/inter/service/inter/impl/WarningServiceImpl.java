package com.zhjl.tech.inter.service.inter.impl;

import com.zhjl.tech.inter.mapper.inter.WarningMapper;
import com.zhjl.tech.inter.model.inter.Warning;
import com.zhjl.tech.inter.service.inter.IWarningService;
import org.springframework.stereotype.Service;

import com.baomidou.framework.service.impl.SuperServiceImpl;

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
}