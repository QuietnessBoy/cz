package com.zhjl.tech.attest.rabbitmq.receive.hashcz;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.attest.biz.filehashcz.FileHashCzBiz;
import com.zhjl.tech.common.exception.AttestBizException;
import com.zhjl.tech.common.exception.NormalException;
import com.zhjl.tech.common.utils.StringTool;
import com.zhjl.tech.common.message.CreateCzMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 接收业务接口存证请求,返回filetoken
 */
@Slf4j
@Component
public class CreateHashCzReceiver implements MessageListener {

    @Resource
    FileHashCzBiz fileHashCzBiz;

    @Override
    public void onMessage(Message message) {
        String json = StringTool.getStringFromByte(message.getBody());
        CreateCzMessage createCzMessage = JSONObject.parseObject(json, CreateCzMessage.class);
        log.info("业务处理子系统收到hashcz消息:{}", JSONObject.toJSONString(createCzMessage));
        try{
            String dummy = fileHashCzBiz.sloveHashAttest(createCzMessage.getChannelOrdersn());
        }catch (NormalException ne){
            log.error("该条数据正在处理,稍后...channelOrdersn={}",createCzMessage.getChannelOrdersn(),ne);
        }catch (AttestBizException ab){
            log.error("文件Hash存证失败,回调失败.channelOrdersn={}",createCzMessage.getChannelOrdersn(),ab);
        }catch (Exception e){
            log.error("系统异常.channelOrdersn={}",createCzMessage.getChannelOrdersn(),e);
        }
    }
}
