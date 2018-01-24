package com.zjl.tech.rabbitmq.emit;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.concurrent.TimeoutException;

public class emit {

    private static final String EXCHANGE_NAME = "logs1";

    public static void main(String[] argv)
            throws java.io.IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "direct");

        String message = getMessage(argv);

        channel.basicPublish(EXCHANGE_NAME, "warn", null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

        channel.close();
        connection.close();
    }


    private static String getMessage(String[] strings){
        if (strings.length < 1) {
            return "Hello World! ->........";
        }
        return joinStrings(strings, " ");
    }

    private static String joinStrings(String[] strings, String delimiter) {
        int length1 = strings.length;
        if (length1 == 0) {
            return "";
        }

        int di=0;

        StringBuilder words1 = new StringBuilder(strings[0]);
        for (int i = 1; i < length1; i++) {
            words1.append(delimiter).append(strings[i]);
        }
        return words1.toString();
    }
}
