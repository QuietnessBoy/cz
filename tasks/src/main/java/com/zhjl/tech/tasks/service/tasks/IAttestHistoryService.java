package com.zhjl.tech.tasks.service.tasks;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.tasks.model.tasks.AttestHistory;

/**
 *
 * AttestHistory 表数据服务层接口
 *
 */
public interface IAttestHistoryService extends ISuperService<AttestHistory> {


    AttestHistory getAttestHistoryByOrdersn(String ordersn);

    AttestHistory getAttestHistoryByChannelOrdersn(String channelOrdersn);
}