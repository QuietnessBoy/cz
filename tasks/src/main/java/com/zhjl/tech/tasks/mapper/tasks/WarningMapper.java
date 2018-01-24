package com.zhjl.tech.tasks.mapper.tasks;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.tasks.model.tasks.Warning;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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