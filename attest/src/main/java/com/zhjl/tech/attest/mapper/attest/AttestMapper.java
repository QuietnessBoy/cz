package com.zhjl.tech.attest.mapper.attest;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.attest.model.attest.Attest;
import com.zhjl.tech.attest.model.attest.TempOrder;
import org.apache.ibatis.annotations.Param;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


/**
 *
 * Attest 表数据库控制层接口
 *
 */
public interface AttestMapper extends AutoMapper<Attest> {

    Attest getAttestByOrdersn(String ordersn);  //根据订单号查询存证信息

    Attest getlastestAttestByOrdersn(@Param("ancestors_ordersn") String ancestors_ordersn,@Param("state1") String state1,@Param("state2") String state2);  //根据订单号查询最新存证信息

    List<Attest> getExpiredAttests(String state);   //根据订单状态查询存证信息

    List<Attest> getExpiredByAttests(@Param("state1") String state1,@Param("state2") String state2);   // 查询过期订单

    void  deleteAttest(String ancestorsOrdersn);   //根据祖先订单号批量删除订单

    Attest getAttestByParentOrdersn(String parentOrdersn);  // 根据父订单号查询存证信息

    Attest getAttestByChannelOrdersn(String channelOrdersn);    //根据渠道订单号查询存证信息

    List<Attest> getAttestByAncestorsOrdersn(String ancestorsOrdersn); //根据祖先订单号查询存证信息

    Attest getAttestForUpdateByChannelOrdersn(String channelOrdersn); //根据渠道订单号查询存证信息并加锁

    Attest getAttestByFileHashAndOwnerId(@Param("fileHash") String fileHash, @Param("ownerId") String ownerId); //根据数字指纹和身份验证查询存证信息

}
