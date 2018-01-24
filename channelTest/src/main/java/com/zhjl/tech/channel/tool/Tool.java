package com.zhjl.tech.channel.tool;

import com.zhjl.tech.common.encrypt.bytetool.Hex;
import com.zhjl.tech.common.encrypt.digest.Hash;
import com.zhjl.tech.common.zjlsign.request.GenRequestSignStr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

@SuppressWarnings("AliAccessStaticViaInstance")
@Slf4j
@Service
public class Tool {

    /**
     * 生成请求sign
     */
    public String createUploadFileSign(String accessKey,String random,String ordersn,String fileToken ,String publicKey){
        String finalParams = GenRequestSignStr.encodeUploadParams(accessKey,random,ordersn,fileToken);
        //拼接publicKey
        String toSign = finalParams + "&publicKey=" + publicKey;

        //排序好的参数
        log.info("排序后的上传文件请求参数:{}", toSign);

        //以utf-8形式获取bytes 数组
        byte[] pbytes = new byte[0];
        try {
            pbytes = toSign.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info("上传文件请求请求参数的bytes:{}", Hex.encode(pbytes));

        //生成md5摘要
        byte[] rs = Hash.getHashMD5(pbytes);

        //编码
        String sign = Base64.getEncoder().encodeToString(rs);

        //签名字符串
        log.info("上传文件请求请求参数sign:{}",sign);
        return sign;
    }











}
