package com.zhjl.tech.inter.mapper.inter;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.inter.model.inter.AttestChained;

/**
 *
 * AttestChainedSolve 表数据库控制层接口
 *
 */
public interface AttestChainedMapper extends AutoMapper<AttestChained> {

    AttestChained getAttestChainedByOrdersn(String ordersn);

    AttestChained getAttestChainedByTxId(String txid);
}