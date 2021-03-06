package com.zhjl.tech.tasks.biz.rabbitmq.filehashcz;

import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.tasks.service.tasks.IRabbitmqMsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WatchFileHashQueue {

    /**
     * 监听文件Hash存证rabbitMq队列消息数量
     */
    public JsonResult watch(){
        Map<String, List> map = new HashMap<>();
        List list1 = rabbitmqMsgService.getMsgByRabbitMqQueue(SysConfig.CreateHashCzQueue);
        map.put(SysConfig.CreateHashCzQueue,list1);

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
