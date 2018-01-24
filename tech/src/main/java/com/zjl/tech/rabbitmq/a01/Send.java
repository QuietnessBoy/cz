package com.zjl.tech.rabbitmq.a01;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.util.concurrent.TimeoutException;

public class Send {

    private final static String QUEUE_NAME = "hello";

    public static void main(String[] argv)
            throws java.io.IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        boolean durable = true;//持久化
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
//        String rabbitmq = "Hello World! 1 ww";
//        channel.basicPublish("", QUEUE_NAME, null, rabbitmq.getBytes());
//        System.out.println(" [x] Sent '" + rabbitmq + "'");

        String message = getMessage(argv);
        channel.basicPublish("", "hello", MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }

    private static String getMessage(String[] strings){
        if (strings.length < 1) {
            return "Hello World! .....";
        }
        return joinStrings(strings, " ");
    }

    private static String joinStrings(String[] strings, String delimiter) {
        int length = strings.length;
        if (length == 0) {
            return "";
        }
        StringBuilder words = new StringBuilder(strings[0]);
        for (int i = 1; i < length; i++) {
            words.append(delimiter).append(strings[i]);
        }
        return words.toString();
    }
}

