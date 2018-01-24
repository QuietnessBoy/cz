package com.zhjl.tech.common.dto;

import lombok.Data;

/**
 * 用于监听rabbitMq队列时返回的消息数量。
 */
@Data
public class QueryRabbitMqDTO {

    private String time;

    private long num1;

    private String queueName1;

    private long num2;

    private String queueName2;

}
