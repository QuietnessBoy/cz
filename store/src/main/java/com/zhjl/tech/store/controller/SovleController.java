package com.zhjl.tech.store.controller;


import com.zhjl.tech.common.annotation.controllers.ZhijlCtrl;
import com.zhjl.tech.common.constant.JsonConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.DownloadFileDto;
import com.zhjl.tech.common.utils.CommonTool;
import com.zhjl.tech.store.annotationdemo.atlog.ZhijlLog;
import com.zhjl.tech.store.biz.downLoad.DownLoadFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Controller
public class SovleController extends BaseController {

    @Resource
    private DownLoadFile downLoadFile;

    /**
     * 下载源文件接口
     * URL=http://localhost:8082/store/fetchAttestFile?sign=jcz/gHNIdVCoj4oWjSU2Zg==&signType=MD5&channelId=005&random=1&accessKey=29DQsxdm481LQjnfVMigS2BaDuDzDXaPKQBMzTm4WFwfwQc4L7&ownerId=ownerid1&fileHash=uVuMlepyMpFxhc5RXzTk+6Qx/Lqi6peRRQRJYaWX3yU=&ordersn=005131171103000002
     *
     * @return
     */
    @ZhijlCtrl
    @ResponseBody
    @RequestMapping("fetchAttestFile")
    @ZhijlLog(rquestMethod="Http",orderSn = "p[2].getOrdersn")
    public JsonResult downLoadFile(HttpServletRequest request, HttpServletResponse response, DownloadFileDto downloadFileDto){
        return downLoadFile.download(downloadFileDto, response);
    }
}
