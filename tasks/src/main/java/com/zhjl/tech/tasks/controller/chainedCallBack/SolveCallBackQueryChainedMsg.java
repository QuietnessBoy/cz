package com.zhjl.tech.tasks.controller.chainedCallBack;

import com.zhjl.tech.tasks.controller.BaseController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 接收链上查询回调数据
 */
@Service
@Slf4j
@Controller
public class SolveCallBackQueryChainedMsg extends BaseController {

    //todo 本方法无实际效果

    @ResponseBody
    @RequestMapping("/queryAttestChained")
    public String addCzCallBack(HttpServletRequest request) {

        String a = "error";
        String txid = request.getParameter("txid");
        String type = request.getParameter("type");
        String success = request.getParameter("success");
        String datas = request.getParameter("datas");
        log.info("接收区块链接口回调数据.]:Txid={},type={},success={},datas={}", txid, type, success, datas);

        if (!"queryCzByOrdersn".equalsIgnoreCase(type)) {
            log.error("回调类型不匹配.type={}",type);
            return a;
        }

        if ("true".equalsIgnoreCase(success)) {
            log.info("[接收区块链回调信息成功.]链上存在该数据信息.txid={}",txid);
            return "ok";
        } else {
            log.warn("[接收区块链回调信息失败.]链上不存在该数据信息.txid={}",txid);
            return a;
        }
    }
}
