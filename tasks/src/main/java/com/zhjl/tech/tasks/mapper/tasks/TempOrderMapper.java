package com.zhjl.tech.tasks.mapper.tasks;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.tasks.model.tasks.TempOrder;

/**
 *
 * TempOrder 表数据库控制层接口
 *
 */
public interface TempOrderMapper extends AutoMapper<TempOrder> {

    TempOrder getTempOrderByChannelOrdersn(String channelOrdersn);

    TempOrder getTempOrderForUpdateByChannelOrdersn(String channelOrdersn);
}