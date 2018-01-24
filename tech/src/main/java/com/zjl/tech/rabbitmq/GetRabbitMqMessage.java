package com.zjl.tech.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by wind on 2017/7/29.
 */
public class GetRabbitMqMessage {
    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        // amqp://userName:password@hostName:portNumber/virtualHost
        factory.setUri("amqp://guest:guest@127.0.0.1:5672");
        Connection conn = factory.newConnection();
//         System.out.println("@@@@@@@@@@@@@"+conn.getChannelMax());
        Channel channel = conn.createChannel();
        // System.out.println(channel.getChannelNumber());
        System.out.println(channel.messageCount("workqueue"));
        channel.close();
        conn.close();
    }
}
