package com.zhjl.tech.common.zjlsign.request;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.common.dto.interfaces.QueryOrderDto;
import com.zhjl.tech.common.encrypt.bytetool.Hex;
import com.zhjl.tech.common.encrypt.digest.Hash;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Slf4j
@Service
public class RequestSign {





    /**
     * 查询订单时，根据请求参数生成的请求sign
     *
     * @param queryOrderDto
     */
    public String genSelectOrderSign(QueryOrderDto queryOrderDto, String publicKey) {
        log.info("收到的存证请求参数:digitalCertificateDemo:{},publick:{}", JSONObject.toJSONString(queryOrderDto),publicKey);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("random", StringUtils.isBlank(queryOrderDto.getRandom())?"": queryOrderDto.getRandom());
        parameters.put("accessKey", StringUtils.isBlank(queryOrderDto.getAccessKey())?"": queryOrderDto.getAccessKey());
        parameters.put("ordersn", StringUtils.isBlank(queryOrderDto.getOrdersn())?"": queryOrderDto.getOrdersn());
        parameters.put("channelId", StringUtils.isBlank(queryOrderDto.getChannelId())?"": queryOrderDto.getChannelId());

        //参数排序
        List<String> arrayList = new ArrayList<>(parameters.keySet());
        Collections.sort( arrayList );

        String toSign = "";
        StringBuilder sb = new StringBuilder();
        for( String s :arrayList){
            sb.append("&")
                    .append(s)
                    .append("=")
                    .append(parameters.get(s));
        }

        //拼接publicKey
        sb = sb.append("&publicKey=").append(publicKey);
        toSign = sb.toString();
        //排序好的参数
        log.info("排序后的存证请求参数:{}", toSign);

        //以utf-8形式获取bytes 数组
        byte[] pbytes = new byte[0];
        try {
            pbytes = toSign.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("获取byte数组失败");
        }
        log.info("存证请求参数的bytes:{}",Hex.encode(pbytes));

        //生成md5摘要
        byte[] rs =Hash.getHashMD5(pbytes);

        //编码
        String sign = Base64.getEncoder().encodeToString(rs);

        //签名字符串
        log.info("存证请求参数sign:{}",sign);
        return sign;
    }


}

