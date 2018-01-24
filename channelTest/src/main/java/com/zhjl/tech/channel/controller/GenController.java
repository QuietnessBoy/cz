package com.zhjl.tech.channel.controller;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.channel.biz.what.GenQueryData;
import com.zhjl.tech.channel.biz.what.GenVerifyData;
import com.zhjl.tech.channel.genparams.*;
import com.zhjl.tech.common.dto.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@Controller
public class GenController {

    @Resource
    GenFileHashCzParams genFileHashCzParams;

    @Resource
    GenVerifyData genVerifyData;

    @Resource
    GenDownFileParams genDownFileParams;

    @Resource
    GenFileCzParams genFileCzParams;

    @Resource
    GenQueryData genQueryData;

    @Resource
    GenCzXqParams genCzXqParams;

    //***********************************文件存证参数生成************************************/
    /**
     * postMan其他格式文件存证请求参数
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("gen/fileCzMsg1")
    public ModelAndView genPostmanModeFileCz(String path) throws IOException, URISyntaxException {
        String s = genFileCzParams.genPostmanMode(path);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/genHash1.btl");
        modelAndView.addObject("result",s);

        return modelAndView;
    }

    /**
     * postMan工具urlencode格式请求参数
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("gen/fileCzMsg2")
    public ModelAndView genUrlencodeModeFileCz(String path) throws IOException, URISyntaxException {
        String s = genFileCzParams.genUrlencodeMode(path);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/genHash1.btl");
        modelAndView.addObject("result",s);

        return modelAndView;
    }



    //***********************************文件Hash存证************************************/
    /**
     * 使用postMan工具请求文件Hash存证请求参数
     * @return
     * @throws IOException
     */
    @RequestMapping("gen/fileHashCzMsg1")
    public ModelAndView postManGenfileHashCz1(String path) throws IOException, URISyntaxException {
        String s = genFileHashCzParams.genPostmanMode(path);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/genHash1.btl");
        modelAndView.addObject("result",s);

        return modelAndView;
    }

    /**
     * postMan工具urlencode格式请求参数
     * @return
     * @throws IOException
     */
    @RequestMapping("gen/fileHashCzMsg2")
    public ModelAndView postManGenfileHashCz2(String path) throws IOException, URISyntaxException {
        String s = genFileHashCzParams.genUrlencodeMode(path);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/genHash1.btl");
        modelAndView.addObject("result",s);

        return modelAndView;
    }

    //***********************************存证续期************************************/
    /**
     * postMan工具urlencode格式请求参数
     * @return
     * @throws IOException
     */
    @RequestMapping("gen/CzXqMsg1")
    public ModelAndView postManGenCzXq1(String ordersn) throws IOException, URISyntaxException {
        String s = genCzXqParams.genPostmanMode(ordersn);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/genHash1.btl");
        modelAndView.addObject("result",s);

        return modelAndView;
    }

    /**
     * 使用postMan工具请求存证续期请求参数
     * @return
     * @throws IOException
     */
    @RequestMapping("gen/CzXqMsg2")
    public ModelAndView postManGenCzXq2(String ordersn) throws IOException, URISyntaxException {
        String s = genCzXqParams.genUrlencodeMode(ordersn);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/genHash1.btl");
        modelAndView.addObject("result",s);

        return modelAndView;
    }

    //***********************************订单查询************************************/
    /**
     * postMan工具urlencode格式请求参数
     * @return
     * @throws IOException
     */
    @RequestMapping("gen/genQueryData1")
    public ModelAndView genQueryData1(String ordersn) throws IOException, URISyntaxException {
        String s = genQueryData.genPostmanMode(ordersn);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/genHash1.btl");
        modelAndView.addObject("result",s);

        return modelAndView;
    }

    /**
     * 使用postMan工具请求存证续期请求参数
     * @return
     * @throws IOException
     */
    @RequestMapping("gen/genQueryData2")
    public ModelAndView genQueryData2(String ordersn) throws IOException, URISyntaxException {
        String s = genQueryData.genUrlencodeMode(ordersn);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/genHash1.btl");
        modelAndView.addObject("result",s);

        return modelAndView;
    }


    /************************************订单校验***************************************/
    /**
     * postMan工具urlencode格式请求参数
     * @return
     * @throws IOException
     */
    @RequestMapping("gen/genVerifyData1")
    public ModelAndView genVerifyData1(String ownerId,String fileHash) throws IOException, URISyntaxException {
        String s = genVerifyData.genPostmanMode(ownerId,fileHash);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/genHash1.btl");
        modelAndView.addObject("result",s);

        return modelAndView;
    }

    /**
     * 使用postMan工具请求存证续期请求参数
     * @return
     * @throws IOException
     */
    @RequestMapping("gen/genVerifyData2")
    public ModelAndView genVerifyData2(String ownerId,String fileHash) throws IOException, URISyntaxException {
        String s = genVerifyData.genUrlencodeMode(ownerId,fileHash);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/genHash1.btl");
        modelAndView.addObject("result",s);

        return modelAndView;
    }

    /************************************下载源文件***************************************/
    /**
     * postMan工具urlencode格式请求参数
     * @return
     * @throws IOException
     */
    @RequestMapping("gen/downFile1")
    public ModelAndView downFile1(String ordersn,String channelId) throws IOException, URISyntaxException {
        String s = genDownFileParams.genPostmanMode(ordersn,channelId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/genHash1.btl");
        modelAndView.addObject("result",s);

        return modelAndView;
    }

    /**
     * 使用postMan工具请求存证续期请求参数
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("gen/downFile2")
    public ModelAndView downFile2(String ordersn,String channelId) throws IOException, URISyntaxException {
        String s = genDownFileParams.genUrlencodeMode(ordersn,channelId);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/genHash1.btl");
        modelAndView.addObject("result",s);

        return modelAndView;
    }
}
