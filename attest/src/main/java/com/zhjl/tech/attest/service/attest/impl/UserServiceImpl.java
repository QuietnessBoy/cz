package com.zhjl.tech.attest.service.attest.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.attest.mapper.attest.UserMapper;
import com.zhjl.tech.attest.model.attest.User;
import com.zhjl.tech.attest.service.attest.IUserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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