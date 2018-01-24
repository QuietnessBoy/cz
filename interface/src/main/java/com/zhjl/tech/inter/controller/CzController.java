package com.zhjl.tech.inter.controller;

import com.zhjl.tech.common.annotation.controllers.ZhijlCtrl;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.dto.interfaces.AttestXqDto;
import com.zhjl.tech.inter.annotationdemo.atlog.ZhijlLog;
import com.zhjl.tech.inter.biz.czxq.CzXqBiz;
import com.zhjl.tech.inter.biz.filecz.FileCzBiz;
import com.zhjl.tech.inter.biz.hashcz.FileHashCzBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@Slf4j
public class CzController extends BaseController {

    /**
     * 文件存证
     */
    @ZhijlCtrl
    @ResponseBody
    @RequestMapping("createFileAttest")
    @ZhijlLog(rquestMethod="Http",ChannelorderSn = "p[2].getChannelOrdersn",orderSn="p[2].getOrdersn")
    public JsonResult fileAttest( HttpServletRequest request,
                                  HttpServletResponse response,
                                  AttestDto attestDto) {
        return fileCzBiz.solve(attestDto);
    }

    /**
     * 文件Hash存证
     *
     * @param attestDto
     * @return
     */
    @ZhijlCtrl
    @ResponseBody
    @RequestMapping("createHastAttest")
    @ZhijlLog(rquestMethod="Http",ChannelorderSn = "p[2].getChannelOrdersn",orderSn="p[2].getOrdersn")
    public JsonResult HashAttest( HttpServletRequest request,
                                  HttpServletResponse response,
                                  AttestDto attestDto) {
        return fileHashCzBiz.solve(attestDto);
    }

    /**
     * 文件续期存证
     *
     * @return
     */
    @ZhijlCtrl
    @ResponseBody
    @RequestMapping("attestContinue")
    @ZhijlLog(rquestMethod="Http",ChannelorderSn = "p[2].getChannelOrdersn")
    public JsonResult attestContinue( HttpServletRequest request,HttpServletResponse response,AttestXqDto attestXqDto) {
        return czXqBiz.solve(attestXqDto);
    }

    @Resource
    private FileCzBiz fileCzBiz;

    @Resource
    private FileHashCzBiz fileHashCzBiz;

    @Resource
    private CzXqBiz czXqBiz;
}