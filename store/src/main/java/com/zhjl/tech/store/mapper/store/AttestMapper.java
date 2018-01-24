package com.zhjl.tech.store.mapper.store;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.store.model.store.Attest;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 *
 * Attest 表数据库控制层接口
 *
 */
public interface AttestMapper extends AutoMapper<Attest> {

    Attest getAttestByOrdersn(String ordersn);  //根据订单号查询存证信息

    List<Attest> getExpiredAttests(String state);   //根据订单状态查询存证信息

    Attest getAttestByChannelOrdersn(String channelOrdersn);    //根据渠道订单号查询存证信息

    Attest getAttestByParentOrdersn(String parentOrdersn);  // 根据父订单号查询存证信息

    List<Attest> getAttestByAncestorsOrdersn(String ancestorsOrdersn); //根据祖先订单号查询存证信息

    Attest getAttestForUpdateByOrdersn(String ordersn); //根据订单号查询存证信息并加锁

    Attest getAttestByFileHashAndOwnerId(@Param("fileHash") String fileHash, @Param("ownerId") String ownerId); //根据数字指纹和身份验证查询存证信息



}
