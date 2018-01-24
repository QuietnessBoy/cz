package com.zhjl.tech.tasks.service.tasks.impl;

import com.zhjl.tech.tasks.mapper.tasks.RabbitmqMsgMapper;
import com.zhjl.tech.tasks.model.tasks.RabbitmqMsg;
import com.zhjl.tech.tasks.service.tasks.IRabbitmqMsgService;
import org.springframework.stereotype.Service;

import com.baomidou.framework.service.impl.SuperServiceImpl;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 *
 * RabbitmqMsg 表数据服务层接口实现类
 *
 */
@Service
public class RabbitmqMsgServiceImpl extends SuperServiceImpl<RabbitmqMsgMapper, RabbitmqMsg> implements IRabbitmqMsgService {

    @Resource
    private RabbitmqMsgMapper rabbitmqMsgMapper;

    @Override
    public List<RabbitmqMsg> getMsgByRabbitMqQueue(String queueName) {
        return rabbitmqMsgMapper.getMsgByRabbitMqQueue(queueName);
    }

    @Override
    public List<RabbitmqMsg> getMsgByRabbitMqQueue1(String queueName, Date date) {
        return rabbitmqMsgMapper.getMsgByRabbitMqQueue1(queueName,date);
    }
}