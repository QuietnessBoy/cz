package com.zhjl.tech.inter.service.inter.impl;

import com.zhjl.tech.inter.mapper.inter.TempOrderMapper;
import com.zhjl.tech.inter.model.inter.TempOrder;
import com.zhjl.tech.inter.service.inter.ITempOrderService;
import org.springframework.stereotype.Service;

import com.baomidou.framework.service.impl.SuperServiceImpl;

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
}