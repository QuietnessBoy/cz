package com.zhjl.tech.common.zjlsign.filecz;

import com.zhjl.tech.common.exception.AttestBizException;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.encrypt.asymmetric.SM2;
import com.zhjl.tech.common.encrypt.asymmetric.SM2Impl;
import com.zhjl.tech.common.encrypt.asymmetric.SM2KeyPair;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Slf4j
public class FileCzAttestSign {

    /**
     *
     * @param sm2KeyPair
     * @param spublicKey
     * @param channelIDA
     * @param attestDto
     * @return
     */
    public static String gen(SM2KeyPair sm2KeyPair, String spublicKey, String channelIDA, AttestDto attestDto){
        SM2Impl.Signature attestSign = null;
        SM2 sm2 = new SM2Impl();
        String encodestr = encode(attestDto,spublicKey);
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
     * @param attestDto
     * @param publicKey
     * @return
     */
    public static String encode(AttestDto attestDto, String publicKey) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("requestTime", attestDto.getRequestTime());
        parameters.put("channelUserid", attestDto.getChannelUserid());
        parameters.put("channelOrdersn", attestDto.getChannelOrdersn());
        parameters.put("channelId", attestDto.getChannelId());
        parameters.put("bizType", attestDto.getBizType());

        parameters.put("fileName", attestDto.getFileName());
        parameters.put("fileSize", attestDto.getFileSize());
        parameters.put("fileType", attestDto.getFileType());
        parameters.put("fileHash", attestDto.getFileHash());

        parameters.put("ownerType", attestDto.getOwnerType());
        parameters.put("ownerId", attestDto.getOwnerId());
        parameters.put("ownerName", attestDto.getOwnerName());

        parameters.put("duration", attestDto.getDuration());
        parameters.put("description", attestDto.getDescription());
        parameters.put("price", attestDto.getPrice());

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
