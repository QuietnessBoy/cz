package com.zhjl.tech.store.rabbitmq.receive;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.exception.NormalException;
import com.zhjl.tech.common.exception.StoreBizException;
import com.zhjl.tech.common.utils.StringTool;
import com.zhjl.tech.common.message.CreateFileDataMessage;
import com.zhjl.tech.store.biz.filecz.FileCzFileBiz;
import com.zhjl.tech.store.model.store.Attest;
import com.zhjl.tech.store.model.store.Status;
import com.zhjl.tech.store.service.store.IAttestService;
import com.zhjl.tech.store.service.store.IStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Date;

/**
 * 9.1版本
 * 接收业务处理子系统（attest）的文件存证订单请求.
 */
@Slf4j
@Component
public class ReceiveFileCzFileMessage implements MessageListener {

    @Override
    public void onMessage(Message message) {
        String json = StringTool.getStringFromByte(message.getBody());
        CreateFileDataMessage createFileDataMessage = JSONObject.parseObject(json, CreateFileDataMessage.class);
        
        log.info("[CreateFileCzByAddr]:start={}", JSONObject.toJSONString(createFileDataMessage));
        try {
            String channelOrdersn = fileCzFileBiz.solveFileCzFile(createFileDataMessage);
        } catch (NormalException ne) {
            log.error("该条数据正在处理,稍后..ordersn={}",createFileDataMessage.getOrdersn(), ne);
        } catch (StoreBizException sb) {
            log.error("没有找到指定的订单而中断.ordersn={}",createFileDataMessage.getOrdersn(), sb);
            Attest attest = attestService.getAttestByOrdersn(createFileDataMessage.getOrdersn());
            Status status = statusService.getStatusByOrdersn(createFileDataMessage.getOrdersn());
            if (sb.getMessage().indexOf(SysConfig.HashException) != -1
                    || sb.getMessage().indexOf(SysConfig.FileSizeException) != -1) {
                //更新attest表
                attest.setState(SysConfig.SolveFaild);
                attest.setStateTime(new Date());
                attest.setUpdateTime(new Date());
                attestService.updateSelectiveById(attest);
                //更改状态表业务状态 为文件存证处理失败
                status.setStateNotify(null);
                status.setRemark(null);
                status.setStateBiz(State.File_Failed);
                status.setUpdateTime(new Date());
                statusService.updateSelectiveById(status);
            }
        }catch (Exception e){
            log.error("系统异常.ordersn={}",createFileDataMessage.getOrdersn(),e);
        }
    }



    @Resource
    private FileCzFileBiz fileCzFileBiz;

    @Resource
    private IAttestService attestService;

    @Resource
    private IStatusService statusService;

}
