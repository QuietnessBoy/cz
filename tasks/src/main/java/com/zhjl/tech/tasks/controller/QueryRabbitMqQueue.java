package com.zhjl.tech.tasks.controller;

import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.tasks.biz.rabbitmq.GetAllByQueueMsg;
import com.zhjl.tech.tasks.biz.rabbitmq.czxq.WatchCzXqQueue;
import com.zhjl.tech.tasks.biz.rabbitmq.filecz.WatchFileCzQueue;
import com.zhjl.tech.tasks.biz.rabbitmq.filehashcz.WatchFileHashQueue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class QueryRabbitMqQueue extends BaseController {

    /**
     * 查询所有队列消息数量
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("queryQueueMsg")
    public JsonResult watchQueueMsg(HttpServletRequest request,
                                       HttpServletResponse response) {
        return getAllByQueueMsg.watch();
    }

    /**
     * 监听文件存证所有队列存储消息情况
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("queryFileCzQueue")
    public JsonResult watchFileCzQueue(HttpServletRequest request,
                                 HttpServletResponse response) {
        return watchFileCzQueue.watch();
    }

    /**
     * 监听文件Hash存证所有队列存储消息情况
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("queryFileHashCzQueue")
    public JsonResult watchFileHashCzQueue(HttpServletRequest request,
                                       HttpServletResponse response) {
        return getAllByQueueMsg.watch();
    }

    /**
     * 监听存证续期所有队列存储消息情况
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping("queryCzXqQueue")
    public JsonResult watchCzXqQueue(HttpServletRequest request,
                                           HttpServletResponse response) {
        return watchCzXqQueue.watch();
    }


    @Resource
    private WatchFileCzQueue watchFileCzQueue;

    @Resource
    private WatchFileHashQueue watchFileHashQueue;

    @Resource
    private WatchCzXqQueue watchCzXqQueue;

    @Resource
    private GetAllByQueueMsg getAllByQueueMsg;
}
