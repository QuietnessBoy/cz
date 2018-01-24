package com.zjl.tech.rabbitmq.send;

import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class Send {

    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(
                new String[]{"rabbitmq_send.xml"});
        context.start();

        AmqpTemplate amqpTemplate = (AmqpTemplate)context.getBean("myAmqpTemplate");

        String a = "aaaaaaaaaaaa";
        byte[] b ;
        try {
            b = a.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }
        amqpTemplate.convertAndSend("receive", b);
    }
}
