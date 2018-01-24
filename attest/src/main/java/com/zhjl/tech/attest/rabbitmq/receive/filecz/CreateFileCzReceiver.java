package com.zhjl.tech.attest.rabbitmq.receive.filecz;

import com.alibaba.fastjson.JSONObject;
import com.rabbitmq.client.Channel;
import com.zhjl.tech.attest.biz.filecz.FileCzBiz;
import com.zhjl.tech.common.exception.AttestBizException;
import com.zhjl.tech.common.exception.NormalException;
import com.zhjl.tech.common.utils.StringTool;
import com.zhjl.tech.common.message.CreateCzMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author zhijl
 * <p>
 * 接收业务接口存证请求,返回filetoken
 */
@Slf4j
@Component
public class CreateFileCzReceiver implements MessageListener {

    @Resource
    private FileCzBiz fileCzBiz;

    @Override
    public void onMessage(Message message) {
        String json = StringTool.getStringFromByte(message.getBody());
        CreateCzMessage createCzMessage = JSONObject.parseObject(json, CreateCzMessage.class);
        try{
            String dummy = fileCzBiz.solveFileCz(createCzMessage.getChannelOrdersn());
        }catch (NormalException ne){
            log.error("该条数据正在处理,稍后...{}",createCzMessage.getChannelOrdersn(),ne);
        }catch (AttestBizException ab){
            log.error("attest系统处理filecz订单的时候,发生了严重状态问题channelOrdersn={}",createCzMessage.getChannelOrdersn(),ab);
        }catch (Exception e){
            log.error("系统异常，请联系管理员.{}",createCzMessage.getChannelOrdersn(),e);
        }
    }
}
