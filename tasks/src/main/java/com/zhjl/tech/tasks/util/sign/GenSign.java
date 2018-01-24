package com.zhjl.tech.tasks.util.sign;

import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.tasks.model.tasks.Attest;
import com.zhjl.tech.common.encrypt.digest.Hash;
import com.zhjl.tech.common.message.CreateAttestDetailMessage;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.math.ec.ECPoint;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 说明渠道请求参数的签名方式
 */
@Slf4j
@Service
public class GenSign {

    /**
     * 生成文件Hash存证完成通知请求sign
     *
     * @param random    随机数
     * @param attest    存证对象
     * @param publicKey 渠道公钥
     * @return
     */
    public String createFileHashCzOkSign(String random, Attest attest, String publicKey) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SysConfig.Format);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("duration", attest.getDuration());
        parameters.put("ordersn", attest.getOrdersn());
        parameters.put("startTime", simpleDateFormat.format(attest.getStartTime()));
        parameters.put("channelOrdersn", attest.getChannelOrdersn());
        parameters.put("platformSign", attest.getPlatformSign());
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
        parameters.put("ordersn", createAttestDetailMessage.getOrdersn());
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

    /**
     * 生成存证业务用户签名信息
     *
     * @param attest
     * @param publicKey
     * @return
     */
    public String createAttestSignStr(Attest attest, String publicKey) {
        Map<String, String> parameters = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
        parameters.put("requestTime", simpleDateFormat.format(attest.getRequestTime()));
        parameters.put("channelUserid", attest.getChannelUserid());
        parameters.put("channelOrdersn", attest.getChannelOrdersn());
        parameters.put("channelId", attest.getChannelId());
        parameters.put("bizType", attest.getBizType());

        parameters.put("fileName", attest.getFileName());
        parameters.put("fileSize", attest.getFileSize());
        parameters.put("fileType", attest.getFileType());
        parameters.put("fileHash", attest.getFileHash());

        parameters.put("ownerType", attest.getOwnerType());
        parameters.put("ownerId", attest.getOwnerId());
        parameters.put("ownerName", attest.getOwnerName());

        parameters.put("duration", attest.getDuration());
        parameters.put("description", attest.getDescription());
        parameters.put("price", attest.getPrice());

        List<String> arrayList = new ArrayList<>(parameters.keySet());
        Collections.sort(arrayList);

        StringBuilder sb = new StringBuilder();
        for (String s : arrayList) {
            sb.append("&").append(s).append("=").append(parameters.get(s));
        }
        sb.append("&publicKey=").append(publicKey);
        return sb.toString();
    }


    /**
     * 生成文件存证完成通知请求sign
     *
     * @return
     */
    public String createFileCzSign(String random, CreateAttestDetailMessage createAttestDetailMessage, String publicKey) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SysConfig.Format);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("random", random);
        parameters.put("ordersn", createAttestDetailMessage.getOrdersn());
        parameters.put("channelOrdersn", createAttestDetailMessage.getChannelOrdersn());
        parameters.put("startTime",simpleDateFormat.format(createAttestDetailMessage.getStartTime()));
        parameters.put("duration", createAttestDetailMessage.getDuration());
        parameters.put("platformSign", createAttestDetailMessage.getPlatformSign());

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

    /**
     * 生成文件存证处理失败回调请求sign
     *
     * @return
     */
    public String createSolveFaildRequestSign(String random, Attest attest,String success,String state,String msg, String publicKey) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SysConfig.Format);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("random", random);
        parameters.put("ordersn", attest.getOrdersn());
        parameters.put("channelOrdersn",attest.getChannelOrdersn());
        parameters.put("startTime",simpleDateFormat.format(attest.getStartTime()));
        parameters.put("platformSign",attest.getPlatformSign());
        parameters.put("duration",attest.getDuration());
        parameters.put("success",success);
        parameters.put("state",state);
        parameters.put("msg",msg);

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
