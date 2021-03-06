package com.zhjl.tech.tasks.service.tasks.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.tasks.mapper.tasks.TempOrderMapper;
import com.zhjl.tech.tasks.model.tasks.TempOrder;
import com.zhjl.tech.tasks.service.tasks.ITempOrderService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * TempOrder 表数据服务层接口实现类
 *
 */
@Service
public class TempOrderServiceImpl extends SuperServiceImpl<TempOrderMapper, TempOrder> implements ITempOrderService {

    @Resource
    private TempOrderMapper tempOrderMapper;

    @Override
    public TempOrder getTempOrderByChannelOrdersn(String channelOrdersn) {
        return tempOrderMapper.getTempOrderByChannelOrdersn(channelOrdersn);
    }

    @Override
    public TempOrder getTempOrderForUpdateByChannelOrdersn(String channelOrdersn) {
        return tempOrderMapper.getTempOrderForUpdateByChannelOrdersn(channelOrdersn);
    }

}