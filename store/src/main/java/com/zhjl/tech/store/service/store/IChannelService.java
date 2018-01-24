package com.zhjl.tech.store.service.store;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.store.model.store.Channel;

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