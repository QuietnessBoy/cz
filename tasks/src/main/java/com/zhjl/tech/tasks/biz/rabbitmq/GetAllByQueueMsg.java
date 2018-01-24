package com.zhjl.tech.tasks.biz.rabbitmq;

import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.tasks.service.tasks.IRabbitmqMsgService;
import lombok.extern.slf4j.Slf4j;
import org.beetl.ext.fn.Json;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class GetAllByQueueMsg {

    /**
     * 以当前时间为目的获取各个通道60条数据
     */
    public JsonResult watch(){
        Map<String, List> map = new HashMap<>();
        List list1 = rabbitmqMsgService.getMsgByRabbitMqQueue(SysConfig.CreateFileCzQueue);
        List list2 = rabbitmqMsgService.getMsgByRabbitMqQueue(SysConfig.CreateFileCzByAddrQueue);
        List list3 = rabbitmqMsgService.getMsgByRabbitMqQueue(SysConfig.CreateHashCzQueue);
        List list4 = rabbitmqMsgService.getMsgByRabbitMqQueue(SysConfig.CreateCzXqQueue);
        map.put(SysConfig.CreateFileCzQueue,list1);
        map.put(SysConfig.CreateFileCzByAddrQueue,list2);
        map.put(SysConfig.CreateHashCzQueue,list3);
        map.put(SysConfig.CreateCzXqQueue,list4);

        JsonResult jsonResult = new JsonResult();
        jsonResult.setSuccess(true);
        jsonResult.setMsg("查询成功.");
        jsonResult.setStatus("200");
        jsonResult.setObj(map);
        log.info("查询OK！！！");
        return jsonResult;
    }

    @Resource
    private IRabbitmqMsgService rabbitmqMsgService;
}
