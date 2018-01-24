package com.zhjl.tech.inter.service.inter.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.inter.mapper.inter.UserMapper;
import com.zhjl.tech.inter.model.inter.User;
import com.zhjl.tech.inter.service.inter.IUserService;
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
    private  UserMapper userMapper;

    @Override
    public User getChannelUserIdByUser(String channelIdUserId) {
        return userMapper.getChannelUserIdByUser(channelIdUserId);
    }
}