package com.zjl.tech.rabbitmq.receive;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

@Component
public class Receiver implements MessageListener {

    @Override
    public void onMessage(Message message) {
//        String rs = null;
//        try {
//            rs = new String(message.getBody(),"UTF-8");
//            System.out.println("receiver:"+rs);
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
        Map<String,Object> args = new HashMap<String,Object>();
        args.put("x-message-ttl",2000);
        ConnectionFactory factory=new ConnectionFactory();
        Connection connection= null;
        try {
            connection = factory.newConnection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        Channel channel = null;
        try {
            channel = connection.createChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            channel.queueDeclare("receive",true,false,false,args);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[]args){
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("rabbitmq_receiver.xml");
    }

}
