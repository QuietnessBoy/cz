package com.zhjl.tech.store.mapper.store;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.store.model.store.User;

/**
 *
 * User 表数据库控制层接口
 *
 */
public interface UserMapper extends AutoMapper<User> {

    User getChannelUserIdByUser(String channelUserId);
}