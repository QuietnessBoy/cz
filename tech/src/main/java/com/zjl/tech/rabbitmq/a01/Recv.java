package com.zjl.tech.rabbitmq.a01;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv {
    private final static String QUEUE_NAME = "hello11111111111";

    public static void main(String[] argv)
            throws IOException,
            InterruptedException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        boolean durable = true;//持久化
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

//        Consumer consumer = new DefaultConsumer(channel) {
//            @Override
//            public void handleDelivery(String consumerTag, Envelope envelope,
//                                       AMQP.BasicProperties properties, byte[] body)
//                    throws IOException {
//                String rabbitmq = new String(body, "UTF-8");
//                System.out.println(" [x] Received '" + rabbitmq + "'");
//            }
//        };
//        channel.basicConsume(QUEUE_NAME, true, consumer);

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                System.out.println(" [x] Received '" + message + "'");
                try {
                    try {
                        doWork(message);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } finally {
                    System.out.println(" [x] Done");
                }
            }
        };
        boolean autoAck = false; // acknowledgment is covered below
        channel.basicConsume(QUEUE_NAME, autoAck, consumer);
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch: task.toCharArray()) {
            if (ch == '.') {
                Thread.sleep(1000);
            }
            System.out.print(ch);
        }
    }
}
