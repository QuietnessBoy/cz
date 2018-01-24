package com.zhjl.tech.tasks.task.rabbitmq;

import com.rabbitmq.client.Channel;
import com.zhjl.tech.tasks.model.tasks.RabbitmqMsg;
import com.zhjl.tech.tasks.service.tasks.IRabbitmqMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class WatchMsg {

    /**
     * 查询对应rabbitMq数量
     * @param queue  队列名称
     */
    public long watch(String queue){
        Connection conn = connectionFactory.createConnection();
        Channel channel = conn.createChannel(false);
        long num = 0;
        try {
            num = channel.messageCount(queue);
            // 存入数据库
            RabbitmqMsg rabbitmqMsg = new RabbitmqMsg();
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-HH:mm:ss");
            rabbitmqMsg.setCreateTime(date);
            rabbitmqMsg.setQueueName(queue);
            rabbitmqMsg.setNum(num);
            rabbitmqMsgService.insert(rabbitmqMsg);
        } catch (IOException e) {
            log.error("获取队列消息失败.queue={}",queue,e);
        }
        return num;
    }



    @Resource
    private ConnectionFactory connectionFactory;

    @Resource
    private IRabbitmqMsgService rabbitmqMsgService;


}
