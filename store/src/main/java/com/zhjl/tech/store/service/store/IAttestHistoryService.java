package com.zhjl.tech.store.service.store;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.store.model.store.AttestHistory;

import java.util.List;

/**
 *
 * AttestHistory 表数据服务层接口
 *
 */
public interface IAttestHistoryService extends ISuperService<AttestHistory> {

    List<AttestHistory> getAllByAttestHistory();

    AttestHistory getOrdersnByAttestHistory(String ordersn);

    AttestHistory getChannelOrdersnByAttestHistory(String channelOrdersn);
}