package com.zhjl.tech.tasks.mapper.tasks;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.tasks.model.tasks.User;

/**
 *
 * User 表数据库控制层接口
 *
 */
public interface UserMapper extends AutoMapper<User> {

    User getChannelUserIdByUser(String channelUserId);
}