package com.zhjl.tech.common.zjlsign.filecz;

import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.encrypt.digest.Hash;
import com.zhjl.tech.common.message.CreateAttestDetailMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class FileCzFailedFileRequestSign {

    /**
     * 生成文件存证处理失败回调请求sign
     *
     * @return
     */
    public static String gen(String random, CreateAttestDetailMessage createAttestDetailMessage, String publicKey,String success,String state,String msg) {
        Map<String, String> parameters = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SysConfig.Format);
        parameters.put("random", StringUtils.isBlank(random)?"": random);
        parameters.put("channelOrdersn", StringUtils.isBlank(createAttestDetailMessage.getChannelOrdersn())?"": createAttestDetailMessage.getChannelOrdersn());
        parameters.put("ordersn","");
        parameters.put("startTime","");
        parameters.put("platformSign", "");
        parameters.put("duration", "");
        parameters.put("success", StringUtils.isBlank(success)?"": success);
        parameters.put("state", StringUtils.isBlank(state)?"": state);
        parameters.put("msg", StringUtils.isBlank(msg)?"": msg);

        List<String> arrayList = new ArrayList<>(parameters.keySet());
        Collections.sort(arrayList);

        String fianlStr;
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
