package com.zhjl.tech.tasks.service.tasks;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.tasks.model.tasks.TempOrder;

/**
 *
 * TempOrder 表数据服务层接口
 *
 */
public interface ITempOrderService extends ISuperService<TempOrder> {
    TempOrder getTempOrderByChannelOrdersn(String channelOrdersn);

    TempOrder getTempOrderForUpdateByChannelOrdersn(String channelOrdersn);


}