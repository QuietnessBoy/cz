package com.zhjl.tech.common.zjlsign.hashcz;

import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.encrypt.bytetool.Hex;
import com.zhjl.tech.common.encrypt.digest.Hash;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.*;

@Slf4j
public class HashCzRequestSign {

    public static boolean verify(AttestDto attestDto, String publicKey, String verifySign){
        String sign = gen(attestDto,publicKey);
        log.info("校验filecz的请求签名,get={},local gen={}",verifySign,sign);
        return sign.equals(verifySign);
    }

    /**
     * 生成文件Hash存证请求sign
     *
     * @param attestDto
     */
    public static String gen(AttestDto attestDto, String publicKey) {
        String finalParams = encode(attestDto);

        //拼接publicKey
        StringBuilder sb = new StringBuilder();
        sb = sb.append(finalParams).append("&publicKey=").append(publicKey);
        String toSign = sb.toString();

        //排序好的参数
        log.info("排序后的存证请求参数:{}=>{}",attestDto.getChannelOrdersn(), toSign);

        //以utf-8形式获取bytes 数组
        byte[] pbytes = null;
        try {
            pbytes = toSign.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("获取byte数组失败");
        }
        log.debug("存证请求参数的bytes:{}=>{}",attestDto.getChannelOrdersn(), Hex.encode(pbytes));

        //生成md5摘要
        byte[] rs = Hash.getHashMD5(pbytes);

        //编码
        return Base64.getEncoder().encodeToString(rs);
    }

    /**
     * 文件Hash存证所需请求签名参数
     * @param attestDto
     * @return
     */
    public static String encode(AttestDto attestDto){
        Map<String,String> parameters = new HashMap<>();

        parameters.put("accessKey", StringUtils.isBlank(attestDto.getAccessKey())?"": attestDto.getAccessKey());
        parameters.put("random",StringUtils.isBlank(attestDto.getRandom())?"": attestDto.getRandom());

        parameters.put("requestTime", StringUtils.isBlank(attestDto.getRequestTime())?"": attestDto.getRequestTime());
        parameters.put("channelId", StringUtils.isBlank(attestDto.getChannelId())?"": attestDto.getChannelId());
        parameters.put("channelUserid", StringUtils.isBlank(attestDto.getChannelUserid())?"": attestDto.getChannelUserid());
        parameters.put("channelOrdersn", StringUtils.isBlank(attestDto.getChannelOrdersn())?"": attestDto.getChannelOrdersn());
        parameters.put("chained", StringUtils.isBlank(attestDto.getChained())?"": attestDto.getChained());
        parameters.put("bizType", StringUtils.isBlank(attestDto.getBizType())?"": attestDto.getBizType());
        parameters.put("bizSign", StringUtils.isBlank(attestDto.getBizSign())?"": attestDto.getBizSign());

        parameters.put("fileHash", StringUtils.isBlank(attestDto.getFileHash())?"": attestDto.getFileHash());
        parameters.put("fileName", StringUtils.isBlank(attestDto.getFileName())?"": attestDto.getFileName());
        parameters.put("fileType", StringUtils.isBlank(attestDto.getFileType())?"": attestDto.getFileType());
        parameters.put("fileSize", StringUtils.isBlank(attestDto.getFileSize())?"": attestDto.getFileSize());

        parameters.put( "ownerType", StringUtils.isBlank(attestDto.getOwnerType())?"": attestDto.getOwnerType());
        parameters.put( "ownerId", StringUtils.isBlank(attestDto.getOwnerId())?"": attestDto.getOwnerId());
        parameters.put( "ownerName", StringUtils.isBlank(attestDto.getOwnerName())?"": attestDto.getOwnerName());
        parameters.put( "agentName", StringUtils.isBlank(attestDto.getAgentName())?"": attestDto.getAgentName());
        parameters.put( "agentPhone", StringUtils.isBlank(attestDto.getAgentPhone())?"": attestDto.getAgentPhone());
        parameters.put( "agentEmail", StringUtils.isBlank(attestDto.getAgentEmail())?"": attestDto.getAgentEmail());

        parameters.put("duration", StringUtils.isBlank(attestDto.getDuration())?"": attestDto.getDuration());
        parameters.put("description", StringUtils.isBlank(attestDto.getDescription())?"": attestDto.getDescription());
        parameters.put("price", StringUtils.isBlank(attestDto.getPrice())?"": attestDto.getPrice());

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
        log.info("生成请求签名:{}",finalParams);
        return finalParams;
    }
}
