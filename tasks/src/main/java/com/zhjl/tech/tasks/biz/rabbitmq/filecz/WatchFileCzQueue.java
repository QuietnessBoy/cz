package com.zhjl.tech.tasks.biz.rabbitmq.filecz;

import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.tasks.model.tasks.RabbitmqMsg;
import com.zhjl.tech.tasks.service.tasks.IRabbitmqMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WatchFileCzQueue {

    /**
     * 监听文件存证rabbitMq队列消息数量
     */
    public JsonResult watch() {
        Map<String, List> map = new HashMap<>();
        List list1 = rabbitmqMsgService.getMsgByRabbitMqQueue(SysConfig.CreateFileCzQueue);
        List list2 = rabbitmqMsgService.getMsgByRabbitMqQueue(SysConfig.CreateFileCzByAddrQueue);
        map.put(SysConfig.CreateFileCzQueue,list1);
        map.put(SysConfig.CreateFileCzByAddrQueue,list2);

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
