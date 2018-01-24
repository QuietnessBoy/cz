package com.zhjl.tech.inter.biz.filecz;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.common.check.CheckRandom;
import com.zhjl.tech.common.constant.JsonConfig;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.utils.ValidatorTool;
import com.zhjl.tech.common.zjlsign.filecz.FileCzBizSign;
import com.zhjl.tech.common.zjlsign.filecz.FileCzRequestSign;
import com.zhjl.tech.common.zjlsign.hashcz.HashCzBizSign;
import com.zhjl.tech.inter.configs.EnvConfig;
import com.zhjl.tech.inter.model.inter.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

/**
 * @author wind
 *
 * 文件存证相关的校验
 */
@Slf4j
@Service
public class FileCzVerify {

    public JsonResult verify(AttestDto attestDto) {

        //请求字段合法性判断
        Set<ConstraintViolation<AttestDto>> constraintViolations =
                ValidatorTool.getValidator().validate(attestDto);
        if (constraintViolations.size() > 0) {
            StringBuilder result = new StringBuilder();
            for (ConstraintViolation<AttestDto> c : constraintViolations) {
                result.append(c.getMessage()).append(";");
            }
            log.warn("请求数据不合法,{}", result.toString());
            return new JsonResult(false, JSONObject.toJSONString(result), JsonConfig.ErrorData, null);
        }

        //文件存证请求的fileaddr必须存在
        if( StringUtils.isBlank(attestDto.getFileAddr())){
            log.warn("文件存证的fileadd不存在,{}", attestDto.getChannelOrdersn());
            return new JsonResult(false, "fileAddr不可为空！", JsonConfig.ErrorData, null);
        }

        //校验请求随机数是否合法
        if (!CheckRandom.judge(attestDto.getRandom())) {
            log.warn("随机数(random)短时间内请求多次:{}", attestDto.getRandom());
            return new JsonResult(false, JsonConfig.ErrorRandomDesc, JsonConfig.ErrorRandom, null);
        }

        //校验请求签名类型  默认为 "MD5"
        if (!SysConfig.SignType.equals(attestDto.getSignType())) {
            log.warn("{}请求签名类型(signType)不匹配.知金链={},渠道方={}",attestDto.getChannelOrdersn(),
                    attestDto.getSignType(), SysConfig.SignType);
            return new JsonResult(false, JsonConfig.ErrorSignTypeDesc, JsonConfig.ErrorSignType, null);
        }

        //日期格式校验
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(SysConfig.Format, Locale.PRC);
            Date date = sdf.parse(attestDto.getRequestTime());
        } catch (ParseException e) {
            log.error("日期转换失败.", e);
            return new JsonResult(false, JsonConfig.ErrorRequestTimeFormatDesc, JsonConfig.ErrorRequestTimeFormat, null);
        }

        //找到缓存中channel表信息   核对channelId是否为空/accessKey是否为空/accessKey是否匹配该渠道
        Channel channel = EnvConfig.channelMap.get(attestDto.getChannelId());
        if (channel == null) {
            log.warn("渠道ID(channelId){}未找到!", attestDto.getChannelId());
            return new JsonResult(false, JsonConfig.ErrorChannelIdDesc, JsonConfig.ErrorChannelId, null);
        } else if (!attestDto.getAccessKey().equals(channel.getAccessKey())) {
            log.warn("该请求标识(AccessKey)不匹配.渠道方:{},知金链存储的:{}", attestDto.getAccessKey(), channel.getAccessKey());
            return new JsonResult(false, JsonConfig.ErrorAccessKeyDesc, JsonConfig.ErrorAccessKey, null);
        }

        //校验请求参数sign是否匹配
        if (!FileCzRequestSign.verify(attestDto, channel.getChannelPublicKey(),attestDto.getSign())) {
            return new JsonResult(false, JsonConfig.ErrorSignDesc, JsonConfig.ErrorSign, null);
        }

        //校验bizSign是否正确,通过channel_publicKey/channel_ida
        log.debug("打印渠道待签名信息:publicKey={},channelIda={},bizSign={}", channel.getChannelPublicKey(),
                channel.getChannelIda(), attestDto.getBizSign());
        if (!FileCzBizSign.verify(channel.getChannelPublicKey(), channel.getChannelIda(),
                attestDto)) {
            log.warn("该订单签名(bizSign)不匹配.渠道方={}", attestDto.getBizSign());
            return new JsonResult(false, JsonConfig.ErrorBizSignDesc, JsonConfig.ErrorBizSign, null);
        }

        return null;
    }


}
