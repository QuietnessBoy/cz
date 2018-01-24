package com.zhjl.tech.store.mapper.store;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.store.model.store.AttestChained;

/**
 *
 * AttestChainedSolve 表数据库控制层接口
 *
 */
public interface AttestChainedMapper extends AutoMapper<AttestChained> {

    AttestChained getAttestChainedByOrdersn(String ordersn);

    AttestChained getAttestChainedByTxId(String txid);

    AttestChained getAttestChainedByMinRefreshTime(String state);

}