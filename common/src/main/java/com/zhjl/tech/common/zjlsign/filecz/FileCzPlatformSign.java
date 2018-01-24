package com.zhjl.tech.common.zjlsign.filecz;

import com.zhjl.tech.common.exception.AttestBizException;
import com.zhjl.tech.common.channeldemo.ChannelConfig;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.encrypt.asymmetric.SM2;
import com.zhjl.tech.common.encrypt.asymmetric.SM2Impl;
import com.zhjl.tech.common.encrypt.asymmetric.SM2KeyPair;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.math.ec.ECPoint;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.*;

@Slf4j
public class FileCzPlatformSign {

    /**
     * 生成文件存证platformSign
     *
     * @param attestDto
     */
    public static String gen(AttestDto attestDto, String sPubKey, String sPriKey) {
        SM2 sm2 = new SM2Impl();
        //生成加密对象
        ECPoint publicKey = sm2.decodePublicKey(sPubKey);
        BigInteger privateKey = sm2.decodePrivateKey(sPriKey);
        SM2KeyPair sm2KeyPair_load = new SM2KeyPair(publicKey, privateKey);

        String finalParams = FileCzPlatformSign.encode(attestDto);

        //以utf-8形式获取bytes 数组
        byte[] pbytes = null;
        try {
            pbytes = finalParams.getBytes(SysConfig.CharsetName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.warn("文件存证platformSign生成错误.",e);
            throw new AttestBizException("生成platformsign的时候因UnsupportedEncodingException而异常！");
        }

        SM2Impl.Signature signature = sm2.sign(
                pbytes,
                ChannelConfig.platformIDA,
                sm2KeyPair_load
        );

        //签名字符串
        String fileCzBizSign = signature.toBase64();
        log.info("生成文件存证platformSign：{}", fileCzBizSign);
        return fileCzBizSign;
    }
    /**
     * 生成文件存证请求platformSign
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
        //以上字段与bizSign相同。下面三个为新增的
        parameters.put("bizSign", StringUtils.isBlank(attestDto.getBizSign())?"": attestDto.getBizSign());
        parameters.put("ordersn", StringUtils.isBlank(attestDto.getOrdersn() )?"":attestDto.getOrdersn());
        parameters.put("startTime", StringUtils.isBlank(attestDto.getStartTime())?"":attestDto.getStartTime());

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
        log.info("生成存证platformSign签名的准备字符串:{}" , finalParams);
        return finalParams;
    }
}
