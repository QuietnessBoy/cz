package com.zhjl.tech.attest.mapper.attest;


import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.attest.model.attest.Status;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * Status 表数据库控制层接口
 *
 */
public interface StatusMapper extends AutoMapper<Status> {

    Status getStatusByChannelOrdersn(String channelOrdersn);    //根据channelOrdersn查找对应订单记录

    Status getStatusByOrdersn(String ordersn);  //根据ordersn查找对应订单记录

    List<Status> getStatusByNum(int num);    //根据异常记录次数查找订单记录

    List<Status> getStatusByStateBiz(String state);    //根据订单状态查找处理次数小于10次的

    //更新status表的 num字段：num+1；updatetime：当前时间 remark字段
    void updateStatusByChannelOrdersn(@Param("channelordersn") String channelordersn, @Param("remark") String remark);

    List<Status> getStatusByStateBizInTime(@Param("state") String state, @Param("num") int num);  //根据订单状态查找2分钟内的业务状态订单信息

    List<Status> getStatusByStateNotifyInTime(@Param("state") String state, @Param("num") int num);  //根据订单状态查找2分钟内的回调状态订单信息

    List<Status> getStatusByMoreStateBiz(@Param("state1") String state1, @Param("state2") String state2, @Param("state3") String state3);

}