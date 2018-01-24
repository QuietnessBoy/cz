package com.zhjl.tech.attest.util;

import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.encrypt.digest.Hash;
import com.zhjl.tech.common.message.CreateAttestDetailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 说明渠道请求参数的签名方式
 */
@Slf4j
@Service
public class Tool {



    /**
     * 生成文件存证续期完成请求sign
     *
     * @return
     */
    public String createXqSign(String random, CreateAttestDetailMessage createAttestDetailMessage, String publicKey) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SysConfig.Format);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("random", random);
        parameters.put("duration", createAttestDetailMessage.getDuration());
        parameters.put("ordersn", createAttestDetailMessage.getAncestorsOrdersn());
        parameters.put("channelOrdersn", createAttestDetailMessage.getChannelOrdersn());
        parameters.put("startTime", simpleDateFormat.format(createAttestDetailMessage.getStartTime()));
        parameters.put("platformSign", createAttestDetailMessage.getPlatformSign());

        List<String> arrayList = new ArrayList<>(parameters.keySet());
        Collections.sort(arrayList);

        String fianlStr = "";
        StringBuilder sb = new StringBuilder();
        for (String s : arrayList) {
            sb.append(fianlStr).append("&").append(s).append(parameters.get(s));
        }
        sb = sb.append("&publicKey=").append(publicKey);
        fianlStr = sb.toString();
        log.info("finalstr = {}", fianlStr);

        String sign = null;
        try {
            sign = Base64.getEncoder().encodeToString(Hash.getHashMD5(fianlStr.getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info("sign = {}", sign);

        return sign;
    }







}
