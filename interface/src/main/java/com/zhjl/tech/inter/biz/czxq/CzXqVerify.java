package com.zhjl.tech.inter.biz.czxq;

import com.zhjl.tech.common.check.CheckRandom;
import com.zhjl.tech.common.constant.JsonConfig;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.AttestXqDto;
import com.zhjl.tech.common.utils.ValidatorTool;
import com.zhjl.tech.common.zjlsign.request.RequestSign;
import com.zhjl.tech.common.zjlsign.xqcz.XqCzBizSign;
import com.zhjl.tech.common.zjlsign.xqcz.XqCzRequestSign;
import com.zhjl.tech.inter.configs.EnvConfig;
import com.zhjl.tech.inter.model.inter.Attest;
import com.zhjl.tech.inter.model.inter.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

/**
 * @author wind
 *
 * 文件续期相关的校验
 */
@Slf4j
@Service
public class CzXqVerify {
    public JsonResult verify(AttestXqDto attestXqDto) {
        //存证续期请求参数合法性判断
        Set<ConstraintViolation<AttestXqDto>> constraintViolations =
                ValidatorTool.getValidator().validate(attestXqDto);
        if (constraintViolations.size() > 0) {
            StringBuilder result = new StringBuilder();
            for (ConstraintViolation<AttestXqDto> c : constraintViolations) {
                result.append(c.getMessage()).append(";");
            }
            log.warn("存证续期:请求参数不合法,{},{}", attestXqDto.getChannelOrdersn(),result.toString());
            return new JsonResult(false,result.toString(), JsonConfig.ErrorData,null);
        }

        //校验随机数是否合法
        if (!CheckRandom.judge(attestXqDto.getRandom())) {
            log.warn("存证续期:随机数请求不合法,{}", attestXqDto.getChannelOrdersn());
            return new JsonResult(false,JsonConfig.ErrorRandomDesc,JsonConfig.ErrorRandom,null);
        }

        //校验请求签名类型  默认为 "MD5"
        if (!attestXqDto.getSignType().equals(SysConfig.SignType)) {
            log.warn("请求签名类型(signType)不匹配.cordersn={},知金链={},渠道方={}",attestXqDto.getChannelOrdersn(),
                    attestXqDto.getSignType(), SysConfig.SignType);
            return new JsonResult(false, JsonConfig.ErrorSignTypeDesc, JsonConfig.ErrorSignType, null);
        }

        try {
            SimpleDateFormat sdf = new SimpleDateFormat(SysConfig.Format, Locale.PRC);
            Date date = sdf.parse(attestXqDto.getRequestTime());
        } catch (ParseException e) {
            log.error("日期转换失败,channelOrdersn={}", attestXqDto.getChannelOrdersn());
            return new JsonResult(false,JsonConfig.ErrorRequestTimeFormatDesc,JsonConfig.ErrorRequestTimeFormat,null);
        }

        //校验channel/accesskey是否合法
        Channel channel = EnvConfig.channelMap.get(attestXqDto.getChannelId());
        if (channel == null) {
            log.warn("存证续期:渠道ID(channelId)为空,{}", attestXqDto.getChannelOrdersn());
            return new JsonResult(false,JsonConfig.ErrorChannelIdDesc,JsonConfig.ErrorChannelId,null);
        } else if (!attestXqDto.getAccessKey().equals(channel.getAccessKey())) {
            log.warn("存证续期:请求标识(accessKey)不匹配.知金链:{},渠道方:{}",
                    channel.getAccessKey(), attestXqDto.getAccessKey());
            return new JsonResult(false,JsonConfig.ErrorAccessKeyDesc,JsonConfig.ErrorAccessKey,null);
        }

        //校验请求sign是否匹配
        if (!XqCzRequestSign.verify(attestXqDto, channel.getChannelPublicKey(),attestXqDto.getSign())) {
            return new JsonResult(false,JsonConfig.ErrorSignDesc,JsonConfig.ErrorSign,null);
        }

        log.info("@@@@@@@@@@@@@pubKey:{},ida:{},bizsign:{}",channel.getChannelPublicKey(),channel.getChannelIda(),attestXqDto.getBizSign());
        //校验bizSign是否匹配
        if (!XqCzBizSign.verify(channel.getChannelPublicKey(), channel.getChannelIda(),
                attestXqDto.getBizSign(), attestXqDto)) {
            log.warn("订单签名(bizSign)不正确.cordersn={},attestXqDemo:{}", attestXqDto.getChannelOrdersn(), attestXqDto.getBizSign());
            return new JsonResult(false,JsonConfig.ErrorBizSignDesc,JsonConfig.ErrorBizSign,null);
        }
        return null;
    }

    public JsonResult checklastestAttest(AttestXqDto attestXqDto, Attest lastestAttest, Attest attest) {
        if(lastestAttest == null){
            log.warn("续期订单不存在,{}", attestXqDto.getChannelOrdersn());
            return new JsonResult(false,
                    new StringBuilder().append(JsonConfig.NoExistOrdersnDesc)
                            .append(attestXqDto.getOrdersn()).toString(),
                    JsonConfig.NoExistOrdersn,attestXqDto);
        }
        if (attest == null) {
            log.warn("续期订单异常,原始订单不存在,{}", attestXqDto.getChannelOrdersn());
            return new JsonResult(false,JsonConfig.SystemExceptionDesc,JsonConfig.SystemException,null);
        }
        return null;
    }

    /**
     * 校验两个对象的字段是否一致
     * @param attestXqDto
     * @param attest
     * @return
     */
    public JsonResult checkContent(AttestXqDto attestXqDto, Attest attest) {
        //校验请求参数字段与订单字段是否匹配
        if (!attest.getChannelId().equals(attestXqDto.getChannelId())) {
            log.warn("channelId不匹配.channelOrdersn={},知金链:{},渠道方:{}", attestXqDto.getChannelOrdersn(),attest.getChannelId(), attestXqDto.getChannelId());
            return new JsonResult(false, JsonConfig.MatchChannelIdDesc,JsonConfig.MatchChannelId,null);
        }
        if (!attest.getChannelUserid().equals(attestXqDto.getChannelUserid())) {
            log.warn("channelUserId不匹配.channelOrdersn={},知金链:{},渠道方:{}", attestXqDto.getChannelOrdersn(), attest.getChannelUserid(), attestXqDto.getChannelUserid());
            return new JsonResult(false,JsonConfig.MatchChannelUserIdDesc,JsonConfig.MatchChannelUserId,null);
        }
        if (!attest.getBizType().equals(attestXqDto.getBizType())) {
            log.warn("biyType不匹配.channelOrdersn={},知金链:{},渠道方:{}", attestXqDto.getChannelOrdersn(), attest.getBizType(), attestXqDto.getBizType());
            return new JsonResult(false,JsonConfig.ErrorBizTypeDesc,JsonConfig.ErrorBizType,null);
        }
        if (!attest.getFileName().equals(attestXqDto.getFileName())) {
            log.warn("fileName不匹配.channelOrdersn={},知金链:{},渠道方:{}", attestXqDto.getChannelOrdersn(), attest.getFileName(), attestXqDto.getFileName());
            return new JsonResult(false,JsonConfig.ErrorFileNameDesc,JsonConfig.ErrorFileName,null);
        }
        if (!attest.getFileType().equals(attestXqDto.getFileType())) {
            log.warn("fileType不匹配.channelOrdersn={},知金链:{},渠道方:{}", attestXqDto.getChannelOrdersn(), attest.getFileType(), attestXqDto.getFileType());
            return new JsonResult(false,JsonConfig.ErrorFileTypeDesc,JsonConfig.ErrorFileType,null);
        }
        if (!attest.getFileSize().equals(attestXqDto.getFileSize())) {
            log.warn("FileSize不匹配.channelOrdersn={},知金链:{},渠道方:{}", attestXqDto.getChannelOrdersn(), attest.getFileSize(), attestXqDto.getFileSize());
            return new JsonResult(false,JsonConfig.ErrorFileSizeDesc,JsonConfig.ErrorFileSize,null);
        }
        if (!attest.getFileHash().equals(attestXqDto.getFileHash())) {
            log.warn("fileHash不匹配.channelOrdersn={},知金链:{},渠道方:{}", attestXqDto.getChannelOrdersn(), attest.getFileHash(), attestXqDto.getFileHash());
            return new JsonResult(false,JsonConfig.ErrorFileHashDesc,JsonConfig.ErrorFileHash,null);
        }
        if (!attest.getOwnerType().equals(attestXqDto.getOwnerType())) {
            log.warn("ownerType不匹配.channelOrdersn={},知金链:{},渠道方:{}", attestXqDto.getChannelOrdersn(), attest.getOwnerType(), attestXqDto.getOwnerType());
            return new JsonResult(false,JsonConfig.ErrorOwnerTypeDesc,JsonConfig.ErrorOwnerType,null);
        }
        if (!attest.getOwnerName().equals(attestXqDto.getOwnerName())) {
            log.warn("ownerName不匹配.channelOrdersn={},知金链:{},渠道方:{}", attestXqDto.getChannelOrdersn(), attest.getOwnerName(), attestXqDto.getOwnerName());
            return new JsonResult(false,JsonConfig.ErrorOwnerNameDesc,JsonConfig.ErrorOwnerName,null);
        }
        if (!attest.getOwnerId().equals(attestXqDto.getOwnerId())) {
            log.warn("ownerID不匹配.channelOrdersn={},store:{},attestXqDTO:{}", attestXqDto.getChannelOrdersn(), attest.getOwnerId(), attestXqDto.getOwnerId());
            return new JsonResult(false,JsonConfig.ErrorOwnerIdDesc,JsonConfig.ErrorOwnerId,null);
        }
        return null;
    }


}
