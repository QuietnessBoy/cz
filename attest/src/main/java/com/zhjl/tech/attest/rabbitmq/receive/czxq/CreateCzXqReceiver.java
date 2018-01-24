package com.zhjl.tech.attest.rabbitmq.receive.czxq;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.attest.biz.xqcz.CzXqBiz;
import com.zhjl.tech.common.exception.AttestBizException;
import com.zhjl.tech.common.exception.NormalException;
import com.zhjl.tech.common.utils.StringTool;
import com.zhjl.tech.common.message.CreateAttestDetailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 接收业务接口续期请求
 */
@Slf4j
@Component
public class CreateCzXqReceiver implements MessageListener {

    @Resource
    CzXqBiz attestXqBiz;

    @Override
    public void onMessage(Message message) {
        String json = StringTool.getStringFromByte(message.getBody());
        CreateAttestDetailMessage createAttestDetailMessage = JSONObject.parseObject(json,CreateAttestDetailMessage.class);
        try{
            String dummy = attestXqBiz.solveCzXq(createAttestDetailMessage);
        }catch (AttestBizException abe){
            log.error("存证续期业务处理异常.channelOrdersn={}",createAttestDetailMessage.getChannelOrdersn(),abe);
        }catch (NormalException ne){
            log.error("续期订单没要找到原始订单.channelOrdersn={}",createAttestDetailMessage.getChannelOrdersn(),ne);
        }catch (Exception e){
            log.error("系统异常.channelOrdersn={}",createAttestDetailMessage.getChannelOrdersn(),e);
        }
    }
}
