package com.zhjl.tech.inter.rabbitmq.send;

import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.message.CreateCzMessage;
import com.zhjl.tech.common.utils.StringTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
/**
 * 发送Hash存证请求至业务处理子系统
 */
@Service
@Slf4j
public class CreateHashCzSender {

    @Resource
    private AmqpTemplate amqpTemplate;

    public void send(CreateCzMessage createCzMessage) {
        log.info("[推送文件Hash存证请求至文件处理子系统.]channelOrdersn={}",createCzMessage.getChannelOrdersn());
        try {
            byte[] content = StringTool.encodeObject(createCzMessage);
            amqpTemplate.convertAndSend(SysConfig.CreateHashCz, content);
        } catch (Exception e) {
            log.warn("消息发送失败.channelOrdersn={}",createCzMessage.getChannelOrdersn(), e);
        }
        log.info("[推送消息完成.channelOrdersn={}]",createCzMessage.getChannelOrdersn());

    }
}
