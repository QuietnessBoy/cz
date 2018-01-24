package com.zhjl.tech.store.service.store;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.store.model.store.TempOrder;

/**
 *
 * TempOrder 表数据服务层接口
 *
 */
public interface ITempOrderService extends ISuperService<TempOrder> {

    TempOrder getTempOrderByChannelOrdersn(String channelOrdersn);

}