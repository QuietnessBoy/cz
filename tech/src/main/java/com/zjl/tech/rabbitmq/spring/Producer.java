package com.zjl.tech.rabbitmq.spring;

import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Producer {

    @Resource
    AmqpTemplate amqpTemplate;

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
//                new String[]{"rabbitmq_send.xml"});
                new String[]{"rabbitmq_producer.xml"});
        context.start();

        AmqpTemplate amqpTemplate = (AmqpTemplate)context.getBean("myAmqpTemplate");

        String a = "aaaaaaaa";
        byte[] b ;
        try {
            b = a.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        amqpTemplate.convertAndSend("hello5", b);
    }
}
