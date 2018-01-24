package com.zjl.tech.rabbitmq.spring;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class MessageReceiver5 implements MessageListener {

    @Override
    public void onMessage(Message message) {
        String rs = null;
        try {
            rs = new String(message.getBody(),"UTF-8");
            System.out.println("渠道5555555得到的消息:"+rs);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[]args){
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("rabbitmq_consumer.xml");

    }

}
