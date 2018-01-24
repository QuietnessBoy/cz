package com.zhjl.tech.inter.biz.hashcz;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.common.check.CheckRandom;
import com.zhjl.tech.common.constant.JsonConfig;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.utils.ValidatorTool;
import com.zhjl.tech.common.zjlsign.hashcz.HashCzBizSign;
import com.zhjl.tech.common.zjlsign.hashcz.HashCzRequestSign;
import com.zhjl.tech.inter.configs.EnvConfig;
import com.zhjl.tech.inter.model.inter.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

@Slf4j
@Service
public class FileHashCzVerify {


    public JsonResult verify(AttestDto attestDto) {
        //文件Hash存证请求字段合法性判断
        Set<ConstraintViolation<AttestDto>> constraintViolations =
                ValidatorTool.getValidator().validate(attestDto);
        if (constraintViolations.size() > 0) {
            StringBuilder result = new StringBuilder();
            for (ConstraintViolation<AttestDto> c : constraintViolations) {
                result.append(c.getMessage()).append(";");
            }
            log.warn("文件Hash存证:参数合法性判断有误，{}", result.toString());
            return new JsonResult(false, JSONObject.toJSONString(result), JsonConfig.ErrorData,null);
        }

        //校验随机数是否合法
        if (!CheckRandom.judge(attestDto.getRandom())) {
            log.warn("随机数(random)短时间内请求多次。{}",attestDto.getChannelOrdersn());
            return new JsonResult(false,JsonConfig.ErrorRandomDesc,JsonConfig.ErrorRandom,null);
        }

        //校验请求签名类型  默认为 "MD5"
        if (!attestDto.getSignType().equals(SysConfig.SignType)) {
            log.warn("请求签名类型(signType)不匹配.知金链={},渠道方={}",
                    attestDto.getSignType(), SysConfig.SignType);
            return new JsonResult(false, JsonConfig.ErrorSignTypeDesc, JsonConfig.ErrorSignType, null);
        }

        //转日期,异常处理
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(SysConfig.Format, Locale.PRC);
            Date date = sdf.parse(attestDto.getRequestTime());
        } catch (ParseException e) {
            log.error("日期转换失败", e);
            return new JsonResult(false,JsonConfig.ErrorRequestTimeFormatDesc,JsonConfig.ErrorRequestTimeFormat,null);
        }

        //获取缓存channel信息
        Channel channel = EnvConfig.channelMap.get(attestDto.getChannelId());
        //判断channel/accessKey是否合法
        if (channel == null) {
            log.warn("文件Hash存证:渠道号(channelId)不存在.");
            return new JsonResult(false,JsonConfig.ErrorChannelIdDesc,JsonConfig.ErrorChannelId,null);
        } else if (!attestDto.getAccessKey().equals(channel.getAccessKey())) {
            log.warn("文件Hash存证:请求标识(AccessKey)不匹配.AccessKey:{}", channel.getAccessKey());
            return new JsonResult(false,JsonConfig.ErrorAccessKeyDesc,JsonConfig.ErrorAccessKey,null);
        }

        //校验请求sign是否合法
        if (!HashCzRequestSign.verify(attestDto, channel.getChannelPublicKey(),attestDto.getSign())) {
            return new JsonResult(false,JsonConfig.ErrorSignDesc,JsonConfig.ErrorSign,null);
        }

        //校验订单签名是否正确
        log.info("获取待订单签名信息:publicKey={},channelIda={},bizSign={}",
                channel.getChannelPublicKey(), channel.getChannelIda(), attestDto.getBizSign());
        if (!HashCzBizSign.verify(channel.getChannelPublicKey(), channel.getChannelIda(), attestDto)) {
            log.warn("文件Hash存证:订单签名(bizSign)不正确.bizSign:{}", attestDto.getBizSign());
            return new JsonResult(false,JsonConfig.ErrorBizSignDesc,JsonConfig.ErrorBizSign,null);
        }

        return null;
    }


}
