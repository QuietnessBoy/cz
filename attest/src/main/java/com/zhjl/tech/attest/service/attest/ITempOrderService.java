package com.zhjl.tech.attest.service.attest;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.attest.model.attest.Attest;
import com.zhjl.tech.attest.model.attest.TempOrder;

/**
 *
 * TempOrder 表数据服务层接口
 *
 */
public interface ITempOrderService extends ISuperService<TempOrder> {
    TempOrder getTempOrderByChannelOrdersn(String channelOrdersn);

    TempOrder getTempOrderForUpdateByChannelOrdersn(String channelOrdersn);


}