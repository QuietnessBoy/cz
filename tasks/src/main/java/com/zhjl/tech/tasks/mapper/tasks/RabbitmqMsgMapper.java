package com.zhjl.tech.tasks.mapper.tasks;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.tasks.model.tasks.RabbitmqMsg;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *
 * RabbitmqMsg 表数据库控制层接口
 *
 */
public interface RabbitmqMsgMapper extends AutoMapper<RabbitmqMsg> {

    List<RabbitmqMsg> getMsgByRabbitMqQueue(String queueName);
    List<RabbitmqMsg> getMsgByRabbitMqQueue1(@Param("queueName") String queueName, @Param("date") Date date);

}