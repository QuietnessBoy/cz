package com.zhjl.tech.inter.service.inter;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.inter.model.inter.User;

/**
 *
 * User 表数据服务层接口
 *
 */
public interface IUserService extends ISuperService<User> {



    User getChannelUserIdByUser(String channelIdUserId);

}