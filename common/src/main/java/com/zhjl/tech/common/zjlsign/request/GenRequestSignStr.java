package com.zhjl.tech.common.zjlsign.request;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class GenRequestSignStr {

    /**
     * 上传文件所需请求签名参数 checked
     * @param accessKey     渠道accessKey
     * @param random        随机数
     * @param ordersn       订单号
     * @param fileToken     文件上传标识
     * @return
     */
    public static String encodeUploadParams(String accessKey, String random, String ordersn, String fileToken){
        Map<String,String> parameters = new HashMap<>();

        parameters.put("accessKey",accessKey);
        parameters.put("random",random);
        parameters.put("ordersn",ordersn);
        parameters.put("fileToken",fileToken);

        //参数排序
        List<String> arrayList = new ArrayList<>(parameters.keySet());
        Collections.sort( arrayList );

        String finalParams="";
        StringBuilder sb = new StringBuilder();
        for( String s :arrayList){
            sb.append("&")
                    .append(s)
                    .append("=")
                    .append(parameters.get(s));
        }
        finalParams = sb.toString();
        log.info("生成上传文件请求签名的前提字符串:{}",finalParams);
        return finalParams;
    }




}
