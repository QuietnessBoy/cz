package com.zjl.tech.rabbitmq.spring;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MessageReceiver implements MessageListener {

    @Override
    public void onMessage(Message message) {
        String rs =  new String(message.getBody());
        System.out.println("渠道6得到的消息:"+rs);
    }

    public static void main(String[]args){
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("rabbitmq_consumer.xml");

    }



}
