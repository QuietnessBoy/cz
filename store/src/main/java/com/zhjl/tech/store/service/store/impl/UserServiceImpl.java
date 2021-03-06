package com.zhjl.tech.store.service.store.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.store.mapper.store.UserMapper;
import com.zhjl.tech.store.model.store.User;
import com.zhjl.tech.store.service.store.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 * User 表数据服务层接口实现类
 *
 */
@Service
public class UserServiceImpl extends SuperServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public User getChannelUserIdByUser(String channelIdUserId) {
        return userMapper.getChannelUserIdByUser(channelIdUserId);
    }
}