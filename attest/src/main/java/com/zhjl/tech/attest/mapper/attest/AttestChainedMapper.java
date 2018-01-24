package com.zhjl.tech.attest.mapper.attest;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.attest.model.attest.AttestChained;

import java.util.List;

/**
 *
 * AttestChainedSolve 表数据库控制层接口
 *
 */
public interface AttestChainedMapper extends AutoMapper<AttestChained> {

    AttestChained getAttestChainedByOrdersn(String ordersn);

    AttestChained getAttestChainedByTxId(String txid);

    List<AttestChained> getAttestChainedByNum(int num);

    AttestChained getAttestChainedByMinRefreshTime(String state);

}