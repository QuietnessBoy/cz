package com.zhjl.tech.channel.controller;

import com.zhjl.tech.channel.biz.TestFileCzBiz;
import com.zhjl.tech.channel.biz.HashCzBiz;
import com.zhjl.tech.channel.biz.SelectOrderBiz;
import com.zhjl.tech.channel.biz.XqCzBiz;
import com.zhjl.tech.common.dto.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@Controller
public class TestController extends BaseController {
    @Resource
    HashCzBiz hashCzBiz;

    @Resource
    XqCzBiz xqCzBiz;

    @Resource
    TestFileCzBiz testFileCzBiz;

    @Resource
    SelectOrderBiz selectOrderBiz;

    /**
     * 文件存证测试
     * @param testName   测试数据标识
     * @param a     生成数据次数
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("channelTest/fileCz")
    public JsonResult fileCzMock(String testName, int a) throws IOException, URISyntaxException {
        return testFileCzBiz.mock(testName,a);
    }

    /**
     * 文件存证生成数据
     * @param testName   测试数据标识
     * @param a     生成数据次数
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("channelTest/fileCzGenDatas")
    public JsonResult fileCzGenDatas(String testName, int a) throws IOException, URISyntaxException {
        return  testFileCzBiz.genDatas(testName,a);
    }

    /**
     * 文件Hash存证
     * @param testName   测试数据标识
     * @param a     生成数据次数
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("channelTest/fileHashCz")
    public JsonResult Hashcz(String testName,int a) throws IOException, URISyntaxException {
        return  hashCzBiz.fileHashCz(testName,a);
    }

    /**
     * hash存证生成数据
     * @param testName   测试数据标识
     * @param a     生成数据次数
     * @return
     * @throws IOException
     */
    @ResponseBody
    @RequestMapping("channelTest/hashCzGenDatas")
    public JsonResult hashCzGenDatas(String testName, int a) throws IOException, URISyntaxException {
        return  hashCzBiz.genDatas(testName,a);
    }

    /**
     * 续期存证
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    @ResponseBody
    @RequestMapping("channelTest/XqCz")
    public JsonResult XqCz(String dev) throws IOException, URISyntaxException {
        return xqCzBiz.xqCz(dev);
    }

    /**
     * 查询订单
     * @return
     */
    @ResponseBody
    @RequestMapping("channelTest/selectOrder")
    public JsonResult selectOrder(String ordersn) throws IOException, URISyntaxException {
        return selectOrderBiz.select(ordersn);
    }
}
