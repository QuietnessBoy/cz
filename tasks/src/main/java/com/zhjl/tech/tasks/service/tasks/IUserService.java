package com.zhjl.tech.tasks.service.tasks;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.tasks.model.tasks.User;

/**
 *
 * User 表数据服务层接口
 *
 */
public interface IUserService extends ISuperService<User> {



    User getChannelUserIdByUser(String channelIdUserId);

}