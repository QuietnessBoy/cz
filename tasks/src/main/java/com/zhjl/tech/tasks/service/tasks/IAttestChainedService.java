package com.zhjl.tech.tasks.service.tasks;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.tasks.model.tasks.AttestChained;
import org.apache.ibatis.annotations.Param;

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

    AttestChained getAttestChainedByMinRefreshTime(@Param("state1") String state1, @Param("state2") String state2);

}