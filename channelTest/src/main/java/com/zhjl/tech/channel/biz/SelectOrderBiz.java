package com.zhjl.tech.channel.biz;


import com.zhjl.tech.channel.configs.Configs;
import com.zhjl.tech.channel.model.channel.AttestChannelTest;
import com.zhjl.tech.channel.service.channel.IAttestChannelTestService;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.encrypt.bytetool.Hex;
import com.zhjl.tech.common.encrypt.digest.Hash;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.*;

/**
 * 测试查询订单
 */
@Service
@Slf4j
public class SelectOrderBiz {

    @Resource
    com.zhjl.tech.channel.biz.request.Select select;

    @Resource
    IAttestChannelTestService attestChannelTestService;

    @Transactional
    public JsonResult select(String ordersn) throws IOException, URISyntaxException {
        JsonResult jsonResult = new JsonResult();
        String signType="MD5";
        String random = (UUID.randomUUID().toString()).replaceAll("-","");
        AttestChannelTest attestChannelTest = attestChannelTestService.getAttestChannelTestByOrdersn(ordersn);
        String sign = createSign(Configs.accessKey,random, attestChannelTest.getChannelId(),ordersn,Configs.channelPublickKey);
        log.info("获取到的sign:{}",sign);
        select.select(sign,signType, Configs.accessKey,random, attestChannelTest.getChannelId(),ordersn);

        return jsonResult;
    }


    /**
     * 生成订单查询签名信息
     * @param publicKey
     * @return
     */
    public String createSign(String accessKey,String random,String channelId,String ordersn, String publicKey){

        log.info("收到的存证请求参数:accessKey:{},channelID:{},ordersn:{},publicKey:{}",accessKey,channelId,ordersn,publicKey);

        Map<String,String> parameters = new HashMap<>();
        parameters.put( "accessKey", accessKey);
        parameters.put( "random", random);
        parameters.put( "channelId", channelId);
        parameters.put( "ordersn",ordersn);
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
        finalParams= sb.toString();
        log.info("生成请求签名:{}",finalParams);

        //拼接publicKey
        String toSign = finalParams + "&publicKey=" + publicKey;

        //排序好的参数
        log.info("排序后的存证请求参数:{}", toSign);

        //以utf-8形式获取bytes 数组
        byte[] pbytes = new byte[0];
        try {
            pbytes = toSign.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info("存证请求参数的bytes:{}", Hex.encode(pbytes));

        //生成md5摘要
        byte[] rs = Hash.getHashMD5(pbytes);

        //编码
        String sign = Base64.getEncoder().encodeToString(rs);

        //签名字符串
        log.info("存证请求参数sign:{}",sign);
        return sign;
    }
}
