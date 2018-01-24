package com.zhjl.tech.attest.service.attest;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.attest.model.attest.Channel;

import java.util.List;

/**
 *
 * Channel 表数据服务层接口
 *
 */
public interface IChannelService extends ISuperService<Channel> {
    List<Channel> getAllByChannel();

    Channel getChannelIdByChannel(String channelId);
}