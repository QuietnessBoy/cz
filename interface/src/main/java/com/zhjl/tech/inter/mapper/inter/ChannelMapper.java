package com.zhjl.tech.inter.mapper.inter;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.inter.model.inter.Channel;

import java.util.List;

/**
 *
 * Channel 表数据库控制层接口
 *
 */
public interface ChannelMapper extends AutoMapper<Channel> {
    List<Channel> getAllByChannel();

    Channel getChannelIdByChannel(String channelId);
}