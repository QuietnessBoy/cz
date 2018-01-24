package com.zhjl.tech.inter.rabbitmq.send;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.message.CreateAttestDetailMessage;
import com.zhjl.tech.common.utils.StringTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 发送续期存证请求至业务处理子系统
 */
@Service
@Slf4j
public class CreateCzXqSender {

    @Resource
    private AmqpTemplate amqpTemplate;

    public void send(CreateAttestDetailMessage createAttestDetailMessage) {
        log.info("[推送存证续期请求至文件处理子系统.]channelOrdersn={}",createAttestDetailMessage.getChannelOrdersn());
        try {
            byte[] content = StringTool.encodeObject(createAttestDetailMessage);
            amqpTemplate.convertAndSend(SysConfig.CreateCzXq, content);
        } catch (Exception e) {
            log.warn("消息发送失败.channelOrdersn={}",createAttestDetailMessage.getChannelOrdersn(), e);
        }
        log.info("[推送消息完成.channelOrdersn={}]",createAttestDetailMessage.getChannelOrdersn());
    }
}
