package com.zhjl.tech.tasks.service.tasks;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.tasks.model.tasks.RabbitmqMsg;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 *
 * RabbitmqMsg 表数据服务层接口
 *
 */
public interface IRabbitmqMsgService extends ISuperService<RabbitmqMsg> {

    List<RabbitmqMsg> getMsgByRabbitMqQueue(String queueName);
    List<RabbitmqMsg> getMsgByRabbitMqQueue1(@Param("queueName") String queueName, @Param("date")Date date);
}