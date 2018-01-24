package com.zhjl.tech.ordersn.service.ordersn;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.ordersn.model.ordersn.Attest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * Attest 表数据服务层接口
 *
 */
public interface IAttestService extends ISuperService<Attest> {

    Attest getAttestByOrdersn(String ordersn);  //根据订单号查询存证信息
}