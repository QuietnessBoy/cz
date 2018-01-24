package com.zhjl.tech.store.biz.downLoad;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.common.check.CheckRandom;
import com.zhjl.tech.common.constant.JsonConfig;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.DownloadFileDto;
import com.zhjl.tech.common.oss.OssOpt;
import com.zhjl.tech.common.utils.ValidatorTool;
import com.zhjl.tech.common.zjlsign.query.QueryRequestSign;
import com.zhjl.tech.store.configs.EnvConfig;
import com.zhjl.tech.store.model.store.Attest;
import com.zhjl.tech.store.model.store.AttestFile;
import com.zhjl.tech.store.model.store.Channel;
import com.zhjl.tech.store.service.store.IAttestFileService;
import com.zhjl.tech.store.service.store.IAttestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import java.util.*;

/**
 * 下载文件流信息
 */
@Slf4j
@Service
public class DownLoadFile {

    @Resource
    private IAttestFileService attestFileService;

    @Resource
    private DownLoadFileVerify downLoadFileVerify;

    @Resource
    private OssOpt ossOpt;

    /**
     * 下载源文件，返回用户下载地址
     *
     * @param downloadFileDto
     * @param response
     */
    public JsonResult download(DownloadFileDto downloadFileDto, HttpServletResponse response){
        JsonResult jsonResult = downLoadFileVerify.verify(downloadFileDto);
        if (jsonResult!= null) {
            log.warn("订单不合法,终止!ordersn={}",downloadFileDto.getOrdersn());
            return jsonResult;
        }

        AttestFile attestFile = attestFileService.getAttestFileByOrdersn(downloadFileDto.getOrdersn());
        //校验订单是否存在
        if (attestFile == null) {
            log.warn("订单不存在！ordersn={}",downloadFileDto.getOrdersn());
            return new JsonResult(true,JsonConfig.SearchNoDesc, JsonConfig.SearchNo,null);
        }

        return ossOpt.downLoadFile(attestFile.getFileAddr(),attestFile.getFileName(),response);
    }
}