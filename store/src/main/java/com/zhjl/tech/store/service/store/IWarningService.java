package com.zhjl.tech.store.service.store;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.store.model.store.Warning;
import org.apache.ibatis.annotations.Param;

/**
 *
 * Warning 表数据服务层接口
 *
 */
public interface IWarningService extends ISuperService<Warning> {

    Warning getWarningByChannelOrdersn(String channelOrdersn);

    Warning getWarningByChannelOrdersnAndBizType(@Param("channelOrdersn") String channelOrdersn, @Param("bizType") String bizType);

    Warning getWarningByOrdersn(String ordersn);

}