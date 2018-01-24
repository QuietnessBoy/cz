package com.zhjl.tech.common.zjlsign.xqcz;

import com.zhjl.tech.common.exception.AttestBizException;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.interfaces.AttestXqDto;
import com.zhjl.tech.common.encrypt.asymmetric.SM2;
import com.zhjl.tech.common.encrypt.asymmetric.SM2Impl;
import com.zhjl.tech.common.encrypt.asymmetric.SM2KeyPair;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Slf4j
public class XqCzAttestSign {

    public static String genSign(SM2KeyPair sm2KeyPair, String spublicKey, String channelIDA, AttestXqDto attestXqDto){

        SM2Impl.Signature attestSign = null;
        SM2 sm2 = new SM2Impl();
        String encodestr = encode(attestXqDto,spublicKey);
        try {
            //按照UTF-8形式生成存证签名信息
            attestSign = sm2.sign(encodestr.getBytes(SysConfig.CharsetName), channelIDA, sm2KeyPair);
            return attestSign.toBase64();
        } catch (UnsupportedEncodingException e) {
            log.error("生成attestSign失败.", e);
            throw new AttestBizException("生成attestSign失败,原因：" + e.getMessage());
        }
    }


    /**
     * 生成存证业务用户签名信息
     *
     * @param attestXqDto
     * @param publicKey
     * @return
     */
    public static String encode(AttestXqDto attestXqDto, String publicKey) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("requestTime", attestXqDto.getRequestTime());
        parameters.put("channelUserid", attestXqDto.getChannelUserid());
        parameters.put("channelOrdersn", attestXqDto.getChannelOrdersn());
        parameters.put("channelId", attestXqDto.getChannelId());
        parameters.put("bizType", attestXqDto.getBizType());

        parameters.put("fileName", attestXqDto.getFileName());
        parameters.put("fileSize", attestXqDto.getFileSize());
        parameters.put("fileType", attestXqDto.getFileType());
        parameters.put("fileHash", attestXqDto.getFileHash());

        parameters.put("ownerType", attestXqDto.getOwnerType());
        parameters.put("ownerId", attestXqDto.getOwnerId());
        parameters.put("ownerName", attestXqDto.getOwnerName());

        parameters.put("duration", attestXqDto.getDuration());
        parameters.put("description", attestXqDto.getDescription());
        parameters.put("price", attestXqDto.getPrice());
        parameters.put("ordersn", attestXqDto.getPrice());

        List<String> arrayList = new ArrayList<>(parameters.keySet());
        Collections.sort(arrayList);

        StringBuilder sb = new StringBuilder();
        for (String s : arrayList) {
            sb.append("&").append(s).append("=").append(parameters.get(s));
        }
        sb.append("&publicKey=").append(publicKey);
        return sb.toString();
    }
}