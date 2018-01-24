package com.zhjl.tech.common.zjlsign.hashcz;

import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.encrypt.asymmetric.SM2;
import com.zhjl.tech.common.encrypt.asymmetric.SM2Impl;
import com.zhjl.tech.common.encrypt.bytetool.Hex;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.math.ec.ECPoint;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Slf4j
public class HashCzBizSign {
    /**
     *  检验文件存证订单签名sign
     * @param spublicKey
     * @param channelIDA
     * @param attestDto
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean verify(String spublicKey, String channelIDA, AttestDto attestDto){
        SM2 sm2 = new SM2Impl();

        String bizSign = attestDto.getBizSign();

        //生成公钥
        ECPoint publicKey = sm2.decodePublicKey(spublicKey);

        //签名对象
        SM2Impl.Signature signature = SM2Impl.Signature.genByBase64(bizSign);

        String finalParams = encode(attestDto);
        //以utf-8形式获取bytes 数组
        byte[] pbytes;
        try {
            pbytes = finalParams.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("检验文件存证订单签名sign时，生成byte数组异常",e);
            return false;
        }

        //开始校验
        log.debug("存证校验订单签名==========================pbyte={},sign={},channelIDA={},publicKey={}",
                Hex.encode(pbytes),
                signature.toBase64(),
                channelIDA,
                spublicKey);

        return  sm2.verify(pbytes,
                signature,
                channelIDA,
                publicKey);
    }

    /**
     * 文件存证业务 checked
     * @param attestDto
     * @return
     */
    public static String encode(AttestDto attestDto){
        // 参数名：参数值的映射
        Map<String,String> parameters = new HashMap<>();
        parameters.put("requestTime", StringUtils.isBlank(attestDto.getRequestTime())?"": attestDto.getRequestTime());
        parameters.put("channelId", StringUtils.isBlank(attestDto.getChannelId())?"": attestDto.getChannelId());
        parameters.put("channelUserid", StringUtils.isBlank(attestDto.getChannelUserid())?"": attestDto.getChannelUserid());
        parameters.put("channelOrdersn", StringUtils.isBlank(attestDto.getChannelOrdersn())?"": attestDto.getChannelOrdersn());
        parameters.put("chained", StringUtils.isBlank(attestDto.getChained())?"": attestDto.getChained());
        parameters.put("bizType", StringUtils.isBlank(attestDto.getBizType())?"": attestDto.getBizType());

        parameters.put("fileName", StringUtils.isBlank(attestDto.getFileName())?"": attestDto.getFileName());
        parameters.put("fileType", StringUtils.isBlank(attestDto.getFileType())?"": attestDto.getFileType());
        parameters.put("fileSize", StringUtils.isBlank(attestDto.getFileSize())?"": attestDto.getFileSize());
        parameters.put("fileHash", StringUtils.isBlank(attestDto.getFileHash())?"": attestDto.getFileHash());

        parameters.put( "ownerType", StringUtils.isBlank(attestDto.getOwnerType())?"": attestDto.getOwnerType());
        parameters.put( "ownerId", StringUtils.isBlank(attestDto.getOwnerId())?"": attestDto.getOwnerId());
        parameters.put( "ownerName", StringUtils.isBlank(attestDto.getOwnerName())?"": attestDto.getOwnerName());

        parameters.put("duration", StringUtils.isBlank(attestDto.getDuration())?"": attestDto.getDuration());
        parameters.put("description", StringUtils.isBlank(attestDto.getDescription())?"": attestDto.getDescription());

        //参数排序
        List<String> arrayList = new ArrayList<>(parameters.keySet());
        Collections.sort( arrayList );


        StringBuilder sb = new StringBuilder();
        for( String s :arrayList){
            sb.append("&")
                    .append(s)
                    .append("=")
                    .append(parameters.get(s));
        }
        String finalParams = sb.toString();

        //排序好的参数
        log.info("生成file存证订单bizSign签名的前提字符串:{}" , finalParams);
        return finalParams;
    }
}
