package com.zhjl.tech.inter.mapper.inter;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.inter.model.inter.Warning;

/**
 *
 * Warning 表数据库控制层接口
 *
 */
public interface WarningMapper extends AutoMapper<Warning> {

    Warning getWarningByChannelOrdersn(String channelOrdersn);

}