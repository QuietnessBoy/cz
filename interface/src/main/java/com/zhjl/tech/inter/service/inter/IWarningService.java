package com.zhjl.tech.inter.service.inter;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.inter.model.inter.Warning;

/**
 *
 * Warning 表数据服务层接口
 *
 */
public interface IWarningService extends ISuperService<Warning> {

    Warning getWarningByChannelOrdersn(String channelOrdersn);

}