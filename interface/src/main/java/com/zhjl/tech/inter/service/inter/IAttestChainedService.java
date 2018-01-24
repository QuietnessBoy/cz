package com.zhjl.tech.inter.service.inter;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.inter.model.inter.AttestChained;

/**
 *
 * AttestChainedSolve 表数据服务层接口
 *
 */
public interface IAttestChainedService extends ISuperService<AttestChained> {

    AttestChained getAttestChainedByOrdersn(String ordersn);

    AttestChained getAttestChainedByTxId(String txid);

}