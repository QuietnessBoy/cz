package com.zhjl.tech.ordersn.mapper.ordersn;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.ordersn.model.ordersn.Attest;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 *
 * Attest 表数据库控制层接口
 *
 */
public interface AttestMapper extends AutoMapper<Attest> {

    Attest getAttestByOrdersn(String ordersn);  //根据订单号查询存证信息
}
