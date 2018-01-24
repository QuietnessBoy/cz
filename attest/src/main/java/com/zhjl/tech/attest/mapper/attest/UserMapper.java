package com.zhjl.tech.attest.mapper.attest;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.attest.model.attest.User;

import java.util.List;

/**
 *
 * User 表数据库控制层接口
 *
 */
public interface UserMapper extends AutoMapper<User> {

    User getChannelUserIdByUser(String channelUserId);
}