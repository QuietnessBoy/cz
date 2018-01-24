package com.zhjl.tech.store.service.store.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.store.mapper.store.ChannelMapper;
import com.zhjl.tech.store.model.store.Channel;
import com.zhjl.tech.store.service.store.IChannelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Channel 表数据服务层接口实现类
 *
 */
@Service
public class ChannelServiceImpl extends SuperServiceImpl<ChannelMapper, Channel> implements IChannelService {

    @Resource
    private ChannelMapper channelMapper;

    @Override
    public List<Channel> getAllByChannel() {
        return channelMapper.getAllByChannel();
    }

    @Override
    public Channel getChannelIdByChannel(String channelId) {
        return channelMapper.getChannelIdByChannel(channelId);
    }

}