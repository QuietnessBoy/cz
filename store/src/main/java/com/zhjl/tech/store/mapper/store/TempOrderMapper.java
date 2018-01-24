package com.zhjl.tech.store.mapper.store;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.store.model.store.TempOrder;

/**
 *
 * TempOrder 表数据库控制层接口
 *
 */
public interface TempOrderMapper extends AutoMapper<TempOrder> {

    TempOrder getTempOrderByChannelOrdersn(String channelOrdersn);
}