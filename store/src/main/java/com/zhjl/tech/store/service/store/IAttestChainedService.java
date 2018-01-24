package com.zhjl.tech.store.service.store;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.store.model.store.AttestChained;

/**
 *
 * AttestChainedSolve 表数据服务层接口
 *
 */
public interface IAttestChainedService extends ISuperService<AttestChained> {

    AttestChained getAttestChainedByOrdersn(String ordersn);

    AttestChained getAttestChainedByTxId(String txid);

    AttestChained getAttestChainedByMinRefreshTime(String state);

}