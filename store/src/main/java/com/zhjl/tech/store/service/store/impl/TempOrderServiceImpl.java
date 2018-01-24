package com.zhjl.tech.store.service.store.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.store.mapper.store.TempOrderMapper;
import com.zhjl.tech.store.model.store.TempOrder;
import com.zhjl.tech.store.service.store.ITempOrderService;
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
}