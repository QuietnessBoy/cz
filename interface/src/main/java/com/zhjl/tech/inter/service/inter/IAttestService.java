package com.zhjl.tech.inter.service.inter;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.inter.model.inter.Attest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * Attest 表数据服务层接口
 *
 */
public interface IAttestService extends ISuperService<Attest> {

    Attest getAttestByOrdersn(String ordersn);  //根据订单号查询存证信息

    Attest getlastestAttestByOrdersn(@Param("ancestors_ordersn") String ancestors_ordersn,@Param("state1") String state1,@Param("state2") String state2);  //根据订单号查询最新存证信息

    List<Attest> getExpiredAttests(String state);   //根据订单状态查询存证信息

    Attest getAttestByChannelOrdersn(String channelOrdersn);    //根据渠道订单号查询存证信息

    Attest getAttestByParentOrdersn(String parentOrdersn);  // 根据父订单号查询存证信息

    List<Attest> getAttestByAncestorsOrdersn(String ancestorsOrdersn); //根据祖先订单号查询存证信息

    Attest getAttestForUpdateByChannelOrdersn(String channelOrdersn); //根据渠道订单号查询存证信息并加锁

    Attest getAttestByFileHashAndOwnerId(@Param("fileHash") String fileHash, @Param("ownerId") String ownerId); //根据数字指纹和身份验证查询存证信息

    List<Attest> getAttestList(@Param("head") int head,@Param("ten") int ten);//分页查询   根据所需的第一条记录数和一共查询的条数  查询
}