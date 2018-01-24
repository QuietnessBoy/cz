package com.zhjl.tech.attest.mapper.attest;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.attest.model.attest.Attest;
import com.zhjl.tech.attest.model.attest.TempOrder;

/**
 *
 * TempOrder 表数据库控制层接口
 *
 */
public interface TempOrderMapper extends AutoMapper<TempOrder> {

    TempOrder getTempOrderByChannelOrdersn(String channelOrdersn);

    TempOrder getTempOrderForUpdateByChannelOrdersn(String channelOrdersn);
}