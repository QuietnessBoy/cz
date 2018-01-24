package com.zhjl.tech.ordersn.controller;

import com.zhjl.tech.ordersn.annotationdemo.atlog.ZhijlLog;
import com.zhjl.tech.ordersn.biz.GenOrdersn;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@Slf4j
public class CreateOrdersn extends BaseController {

    @Resource
    GenOrdersn genOrdersn;

    /**
     * 请求ordersn接口
     * URL:http://localhost:8084/ordersn/createOrdersn?channelId=1&bizType=2&duration=3&buninessType=4
     */
    @ResponseBody
    @RequestMapping("/createOrdersn")
    @ZhijlLog(rquestMethod="Normal",orderSn = "r",ChannelorderSn="p[0]")
    public String requestOrdersn(@Param("channelOrdersn") String channelOrdersn,
                                 @Param("channelId") String channelId,
                                 @Param("bizType") String bizType,
                                 @Param("duration") String duration,
                                 @Param("buninessType") String buninessType) {
        log.info("收到的参数信息:channelId:{},buninessType:{},bizType:{},duration:{}", channelOrdersn,channelId, buninessType, bizType, duration);

        //todo 所以的参数不可为空。当为空的时候。。。。，客户端要对应
        assert channelId!=null && !channelId.trim().equals("") ;

        String ordersn = genOrdersn.genOrdersn(channelId, buninessType, bizType, duration);
        return ordersn;
    }

}


