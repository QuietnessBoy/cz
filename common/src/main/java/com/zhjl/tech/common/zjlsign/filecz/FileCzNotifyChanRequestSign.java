package com.zhjl.tech.common.zjlsign.filecz;

import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.encrypt.digest.Hash;
import com.zhjl.tech.common.message.CreateAttestDetailMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class FileCzNotifyChanRequestSign {

    /**
     * 生成文件存证完成通知请求sign
     *
     * @return
     */
    public static String gen(String random, CreateAttestDetailMessage createAttestDetailMessage, String publicKey) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SysConfig.Format);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("random", random);
        parameters.put("ordersn", createAttestDetailMessage.getOrdersn());
        parameters.put("channelOrdersn", createAttestDetailMessage.getChannelOrdersn());
        parameters.put("startTime",simpleDateFormat.format(createAttestDetailMessage.getStartTime()));
        parameters.put("duration", createAttestDetailMessage.getDuration());
        parameters.put("platformSign", createAttestDetailMessage.getPlatformSign());
        parameters.put("success","true" );
        parameters.put("state","200");
        parameters.put("msg", "文件存证完成");

        List<String> arrayList = new ArrayList<>(parameters.keySet());
        Collections.sort(arrayList);

        String fianlStr = "";
        StringBuilder sb= new StringBuilder();
        for (String s : arrayList) {
            sb.append("&")
                    .append(s)
                    .append("=")
                    .append(parameters.get(s));
        }
        sb = sb.append("&publicKey=").append(publicKey);
        fianlStr = sb.toString();
        log.info("finalstr = {}",fianlStr);

        String sign = null;
        try {
            sign = Base64.getEncoder().encodeToString(Hash.getHashMD5(fianlStr.getBytes(SysConfig.CharsetName)));
        } catch (UnsupportedEncodingException e) {
            log.error("生成请求sign失败");
        }
        log.info("sign = {}", sign);

        return sign;
    }
}
