package com.zhjl.tech.inter.mapper.inter;

import com.zhjl.tech.inter.model.inter.TempOrder;
import com.baomidou.mybatisplus.mapper.AutoMapper;

/**
 *
 * TempOrder 表数据库控制层接口
 *
 */
public interface TempOrderMapper extends AutoMapper<TempOrder> {

    TempOrder getTempOrderByChannelOrdersn(String channelOrdersn);

}