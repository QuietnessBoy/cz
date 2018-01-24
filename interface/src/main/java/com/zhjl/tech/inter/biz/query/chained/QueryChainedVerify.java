package com.zhjl.tech.inter.biz.query.chained;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.common.check.CheckRandom;
import com.zhjl.tech.common.constant.JsonConfig;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.DigitalCertificateDto;
import com.zhjl.tech.common.utils.ValidatorTool;
import com.zhjl.tech.common.zjlsign.query.QueryRequestSign;
import com.zhjl.tech.inter.configs.EnvConfig;
import com.zhjl.tech.inter.model.inter.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * 校验上链数据检验部分
 */
@Service
@Slf4j
public class QueryChainedVerify {

    public JsonResult verify(DigitalCertificateDto digitalCertificateDto){

        log.info("[开始查询订单]:收到请求参数:{}", JSONObject.toJSONString(digitalCertificateDto));

        //文件存证请求字段合法性判断
        Set<ConstraintViolation<DigitalCertificateDto>> constraintViolations =
                ValidatorTool.getValidator().validate(digitalCertificateDto);
        if (constraintViolations.size() > 0) {
            StringBuilder result = new StringBuilder();
            for (ConstraintViolation<DigitalCertificateDto> c : constraintViolations) {
                result.append(c.getMessage()).append(";");
            }
            String rs = result.toString();
            log.warn("数字指纹查询订单:参数合法性判断有误，{}", rs);
            return new JsonResult(false,rs, JsonConfig.ErrorData,null);
        }

        // 校验随机数是否合法
        if (!CheckRandom.judge(digitalCertificateDto.getRandom())) {
            log.warn("存证续期:随机数请求不合法.query filehash={}", digitalCertificateDto.getFileHash());
            return new JsonResult(false,JsonConfig.ErrorRandomDesc,JsonConfig.ErrorRandom,null);
        }

        // 校验signType是否匹配  默认为"MD5"
        if (!digitalCertificateDto.getSignType().equals(SysConfig.SignType)) {
            log.warn("存证续期:请求签名类型(signType)不正确.query filehash={}", digitalCertificateDto.getFileHash());
            return new JsonResult(false,JsonConfig.ErrorSignTypeDesc,JsonConfig.ErrorSignType,null);
        }

        // 获取缓存中的channel信息
        Channel channel = EnvConfig.channelMap.get(digitalCertificateDto.getChannelId());
        log.info("获取渠道信息:{}", JSONObject.toJSONString(channel));
        //校验channelID
        if (channel == null) {
            log.warn("找不到对应渠道信息.query filehash={}", digitalCertificateDto.getFileHash());
            return new JsonResult(false,JsonConfig.ErrorChannelIdDesc,JsonConfig.ErrorChannelId,null);
        }

        //校验请求sign是否匹配
        String sign = QueryRequestSign.genDigitalCertificateSign(digitalCertificateDto, channel.getChannelPublicKey());
        if (!sign.equals(digitalCertificateDto.getSign())) {
            log.warn("查询存证请求sign值不匹配.sign={},attestDTO_sign={}", sign, digitalCertificateDto.getSign());
            return new JsonResult(false,JsonConfig.ErrorSignDesc,JsonConfig.ErrorSign,null);
        }

        //校验accessKey
        if (!digitalCertificateDto.getAccessKey().equals(channel.getAccessKey())) {
            log.warn("渠道accessKey不匹配.渠道accessKey:{},请求accessKey:{}", digitalCertificateDto.getAccessKey(), channel.getAccessKey());
            return new JsonResult(false,JsonConfig.ErrorAccessKeyDesc,JsonConfig.ErrorAccessKey,null);
        }

        return null;
    }

}
