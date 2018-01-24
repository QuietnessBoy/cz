package com.zhjl.tech.common.zjlsign.hashcz;

import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.encrypt.digest.Hash;
import com.zhjl.tech.common.message.CreateAttestDetailMessage;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class HashCzNotifySign {
    /**
     * 生成文件Hash存证完成通知请求sign
     *
     * @param random    随机数
     * @param createAttestDetailMessage    参数对象
     * @param publicKey 渠道公钥
     * @return
     */
    public static String gen(String random, CreateAttestDetailMessage createAttestDetailMessage, String publicKey) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("duration", createAttestDetailMessage.getDuration());
        parameters.put("ordersn", createAttestDetailMessage.getOrdersn());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SysConfig.Format);
        parameters.put("startTime", simpleDateFormat.format(createAttestDetailMessage.getStartTime()));
        parameters.put("channelOrdersn", createAttestDetailMessage.getChannelOrdersn());
        parameters.put("platformSign", createAttestDetailMessage.getPlatformSign());
        parameters.put("random", random);

        List<String> arrayList = new ArrayList<>(parameters.keySet());
        Collections.sort(arrayList);

        String fianlStr = "";
        StringBuilder sb = new StringBuilder();
        for (String s : arrayList) {
            sb.append(fianlStr).append("&").append("=").append(parameters.get(s));
        }
        sb = sb.append("&publicKey=").append(publicKey);
        fianlStr = sb.toString();
        log.info("[createFileHashCzOkSign]finalstr = {}", fianlStr);

        String sign = null;
        try {
            sign = Base64.getEncoder().encodeToString(Hash.getHashMD5(fianlStr.getBytes("utf-8")));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        log.info("[createFileHashCzOkSign]sign = {}", sign);

        return sign;
    }
}
