package com.zhjl.tech.inter.mapper.inter;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.inter.model.inter.User;

/**
 *
 * User 表数据库控制层接口
 *
 */
public interface UserMapper extends AutoMapper<User> {

    User getChannelUserIdByUser(String channelUserId);
}