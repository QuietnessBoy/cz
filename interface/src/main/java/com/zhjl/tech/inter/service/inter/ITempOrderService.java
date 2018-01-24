package com.zhjl.tech.inter.service.inter;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.inter.model.inter.TempOrder;

/**
 *
 * TempOrder 表数据服务层接口
 *
 */
public interface ITempOrderService extends ISuperService<TempOrder> {

    TempOrder getTempOrderByChannelOrdersn(String channelOrdersn);

}