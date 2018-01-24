package com.zhjl.tech.attest.service.attest;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.attest.model.attest.User;

import java.util.List;

/**
 *
 * User 表数据服务层接口
 *
 */
public interface IUserService extends ISuperService<User> {



    User getChannelUserIdByUser(String channelIdUserId);

}