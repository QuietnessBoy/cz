package com.zhjl.tech.attest.mapper.attest;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.attest.model.attest.Warning;
import org.apache.ibatis.annotations.Param;

/**
 *
 * Warning 表数据库控制层接口
 *
 */
public interface WarningMapper extends AutoMapper<Warning> {

    Warning getWarningByChannelOrdersn(String channelOrdersn);

    Warning getWarningByChannelOrdersnAndBizType(@Param("channelOrdersn") String channelOrdersn, @Param("bizType") String bizType);

    Warning getWarningByOrdersn(String ordersn);
}