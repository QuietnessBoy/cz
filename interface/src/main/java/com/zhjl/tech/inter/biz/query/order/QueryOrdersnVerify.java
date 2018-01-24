package com.zhjl.tech.inter.biz.query.order;

import com.zhjl.tech.common.check.CheckRandom;
import com.zhjl.tech.common.constant.JsonConfig;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.QueryOrderDto;
import com.zhjl.tech.common.utils.ValidatorTool;
import com.zhjl.tech.common.zjlsign.query.QueryRequestSign;
import com.zhjl.tech.inter.configs.EnvConfig;
import com.zhjl.tech.inter.model.inter.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import java.util.Set;

/**
 * 查询订单校验部分
 */
@Service
@Slf4j
public class QueryOrdersnVerify {

    public JsonResult verify(QueryOrderDto queryOrderDto){
        //请求字段合法性判断
        Set<ConstraintViolation<QueryOrderDto>> constraintViolations =
                ValidatorTool.getValidator().validate(queryOrderDto);
        if (constraintViolations.size() > 0) {
            StringBuilder result = new StringBuilder();
            for (ConstraintViolation<QueryOrderDto> c : constraintViolations) {
                result.append(c.getMessage()).append(";");
            }
            String rs = result.toString();
            log.warn("请求数据不合法,{}", rs);
            return new JsonResult(false,rs, JsonConfig.ErrorData,null);
        }

        // 校验随机数是否合法
        if (!CheckRandom.judge(queryOrderDto.getRandom())) {
            log.warn("存证续期:随机数请求不合法，query odersn={}",queryOrderDto.getOrdersn());
            return new JsonResult(false,JsonConfig.ErrorRandomDesc,JsonConfig.ErrorRandom,null);
        }

        // 校验signType是否为空  默认为"MD5"
        if (!queryOrderDto.getSignType().equals(SysConfig.SignType)) {
            log.warn("存证续期:请求签名类型(signType)不正确，query odersn={}",queryOrderDto.getOrdersn());
            return new JsonResult(false, JsonConfig.ErrorSignTypeDesc,JsonConfig.ErrorSignType,null);
        }

        // 校验渠道相关信息
        Channel channel = EnvConfig.channelMap.get(queryOrderDto.getChannelId());
        //校验channelID
        if (channel == null) {
            log.warn("找不到对应渠道信息.query odersn={}",queryOrderDto.getOrdersn());
            return new JsonResult(false,JsonConfig.ErrorChannelIdDesc,JsonConfig.ErrorChannelId,null);
        }

        //校验请求sign是否匹配
        String sign = QueryRequestSign.genQueryDataSign(queryOrderDto,channel.getChannelPublicKey());
        if (!sign.equals(queryOrderDto.getSign())) {
            log.warn("查询订单请求sign值不匹配.ordersn={},sign={},attestDTO_sign={}", queryOrderDto.getOrdersn(),sign, queryOrderDto.getSign());
            return new JsonResult(false,JsonConfig.ErrorSignDesc,JsonConfig.ErrorSign,null);
        }

        //校验accessKey是否匹配
        if (!queryOrderDto.getAccessKey().equals(channel.getAccessKey())) {
            log.warn("请求的accessKey不匹配.ordersn={},渠道accessKey:{},请求accessKey:{}", queryOrderDto.getOrdersn(),queryOrderDto.getAccessKey(), channel.getAccessKey());
            return new JsonResult(false,JsonConfig.ErrorAccessKeyDesc,JsonConfig.ErrorAccessKey,null);
        }

        return null;
    }
}
