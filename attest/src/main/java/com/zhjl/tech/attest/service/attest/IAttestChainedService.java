package com.zhjl.tech.attest.service.attest;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.attest.model.attest.AttestChained;

import java.util.List;

/**
 *
 * AttestChainedSolve 表数据服务层接口
 *
 */
public interface IAttestChainedService extends ISuperService<AttestChained> {

    AttestChained getAttestChainedByOrdersn(String ordersn);

    AttestChained getAttestChainedByTxId(String txid);

    List<AttestChained> getAttestChainedByNum(int num);

    AttestChained getAttestChainedByMinRefreshTime(String state);

}