package com.zhjl.tech.tasks.service.tasks;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.tasks.model.tasks.Warning;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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