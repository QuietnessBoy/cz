package com.zhjl.tech.store.service.store;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.store.model.store.User;

/**
 *
 * User 表数据服务层接口
 *
 */
public interface IUserService extends ISuperService<User> {



    User getChannelUserIdByUser(String channelIdUserId);

}