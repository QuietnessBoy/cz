package com.zhjl.tech.store.biz.downLoad;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.common.check.CheckRandom;
import com.zhjl.tech.common.constant.JsonConfig;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.DownloadFileDto;
import com.zhjl.tech.common.utils.ValidatorTool;
import com.zhjl.tech.common.zjlsign.query.QueryRequestSign;
import com.zhjl.tech.store.configs.EnvConfig;
import com.zhjl.tech.store.model.store.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * 下载源文件检验部分
 */
@Slf4j
@Service
public class DownLoadFileVerify {

    /**
     * 校验上传对象是否合法
     * @param downloadFileDto
     * @return
     */
    public JsonResult verify(DownloadFileDto downloadFileDto) {
        //文件存证请求字段合法性判断
        Set<ConstraintViolation<DownloadFileDto>> constraintViolations =
                ValidatorTool.getValidator().validate(downloadFileDto);
        if (constraintViolations.size() > 0) {
            StringBuilder result = new StringBuilder();
            for (ConstraintViolation<DownloadFileDto> c : constraintViolations) {
                result.append(c.getMessage()).append(";");
            }
            log.warn("[download]参数合法性判断有误，ordersn={},{}",downloadFileDto.getOrdersn(), result.toString());
            return new JsonResult(false, JSONObject.toJSONString(result), JsonConfig.ErrorData,null);
        }

        // 校验随机数是否合法
        if (!CheckRandom.judge(downloadFileDto.getRandom())) {
            log.warn("[download]随机数请求不合法.ordersn={}",downloadFileDto.getOrdersn());
            return new JsonResult(false,JsonConfig.ErrorRandomDesc,JsonConfig.ErrorRandom,null);
        }

        // 获取缓存中的channel信息
        Channel channel = EnvConfig.channelMap.get(downloadFileDto.getChannelId());
        log.info("获取渠道信息:channel={}", JSONObject.toJSONString(channel));
        //校验channelID
        if (channel == null) {
            log.warn("找不到对应渠道信息.ordersn={}",downloadFileDto.getOrdersn());
            return new JsonResult(false,JsonConfig.ErrorChannelIdDesc,JsonConfig.ErrorChannelId,null);
        }

        //校验signType是否匹配
        if (!downloadFileDto.getSignType().equalsIgnoreCase(SysConfig.SignType)) {
            log.warn("signType不匹配.ordersn={},知金链:{},渠道方:{}",downloadFileDto.getOrdersn(),downloadFileDto.getSignType(),SysConfig.SignType);
            return new JsonResult(false,JsonConfig.ErrorSignTypeDesc, JsonConfig.ErrorSignType,null);
        }

        //校验sign是否匹配
        String requestSign = QueryRequestSign.genDownLoadSign(downloadFileDto, channel.getChannelPublicKey());
        if (!requestSign.equalsIgnoreCase(downloadFileDto.getSign())) {
            log.warn("sign不匹配.ordersn={},平台生成:{},用户:{}",downloadFileDto.getOrdersn(), requestSign, downloadFileDto.getSign());
            return new JsonResult(false,JsonConfig.ErrorSignDesc, JsonConfig.ErrorSign,null);
        }

        //校验accessKey
        if (!downloadFileDto.getAccessKey().equals(channel.getAccessKey())) {
            log.warn("渠道accessKey不匹配.ordersn={},渠道accessKey:{},请求accessKey:{}",downloadFileDto.getOrdersn(), downloadFileDto.getAccessKey(), channel.getAccessKey());
            return new JsonResult(false,JsonConfig.ErrorAccessKeyDesc,JsonConfig.ErrorAccessKey,null);
        }
        return null;
    }
}
