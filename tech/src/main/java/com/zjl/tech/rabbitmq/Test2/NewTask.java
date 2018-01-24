package com.zjl.tech.rabbitmq.Test2;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class NewTask {

    //队列名称
//    private final static String QUEUE_NAME = "workqueue";
    private final static String QUEUE_NAME = "com.zhjl.tech.attest.CreateFileCz";
    private final static String QUEUE_NAME1 = "com.zhjl.tech.attest.CreateHashCz";
    private final static String QUEUE_NAME2 = "com.zhjl.tech.attest.CreateCzXq";
    private final static String QUEUE_NAME3 = "com.zhjl.tech.store.CreateFileCzByAddr";

    public static void main(String[] args) throws IOException, TimeoutException {


        //创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        //声明队列
        boolean durable = true;
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

        channel.exchangeDeclare("myChange3", "direct");
        Map<String, Object> a = new HashMap<String, Object>();
        a.put("x-dead-letter-exchange", "myChange");
        a.put("x-dead-letter-routing-key", "test5");
        channel.queueDeclare("myqueue", false, false, false, a);

        //发送10条消息，依次在消息后面附加1-10个点
        for (int i = 3300; i > 0; i--)
        {
            String dots = "";
            for (int j = 0; j <= i; j++)
            {
                dots += ".";
            }
            String message = "helloworld" + dots + dots.length();
            // MessageProperties 2、设置消息持久化
            channel.basicPublish("", QUEUE_NAME,
                    MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
            System.out.println(" [x] Sent '" + message + "'");
        }
        // 关闭频道和资源
        channel.close();
        connection.close();

    }

}
