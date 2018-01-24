package com.zhjl.tech.attest.rabbitmq.send;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.exception.AttestBizException;
import com.zhjl.tech.common.message.CreateFileDataMessage;
import com.zhjl.tech.common.utils.StringTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 推送文件存证至文件存储系统
 */
@Slf4j
@Service
public class CreateFileCzSender {

    @Resource
    private AmqpTemplate amqpTemplate;

    public void send(CreateFileDataMessage createFileDataMessage) {
        log.info("[开始推送消息至文件存储系统(store)].{}", JSONObject.toJSONString(createFileDataMessage));
        try {
            byte[] content = StringTool.encodeObject(createFileDataMessage);
            amqpTemplate.convertAndSend(SysConfig.CreateFileCzByAddr, content);
        } catch (Exception e) {
            log.error("消息发送失败.ordersn={}",createFileDataMessage.getOrdersn(), e);
            throw new AttestBizException("CreateFileCzSender消息发送发生异常,"+JSONObject.toJSONString(createFileDataMessage) + ", " + e.getMessage());
        }
        log.info("[推送消息至文件存储系统完成].ordresn={}",createFileDataMessage.getOrdersn());
    }
}
