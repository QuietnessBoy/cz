package com.zhjl.tech.tasks.mapper.tasks;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.tasks.model.tasks.AttestChained;
import org.apache.ibatis.annotations.Param;

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

    AttestChained getAttestChainedByMinRefreshTime(@Param("state1")String state1, @Param("state2") String state2);

}