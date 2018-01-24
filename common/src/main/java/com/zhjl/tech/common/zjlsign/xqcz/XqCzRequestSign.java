package com.zhjl.tech.common.zjlsign.xqcz;

import com.zhjl.tech.common.dto.interfaces.AttestXqDto;
import com.zhjl.tech.common.encrypt.digest.Hash;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author wind
 */
@Slf4j
public class XqCzRequestSign {

    public static boolean verify(AttestXqDto attestXqDto, String publicKey, String verifySign){
        String sign = genSign(attestXqDto,publicKey);
        log.info("校验xqcz的请求签名,get={},local gen={}",verifySign,sign);
        return sign.equals(verifySign);
    }

    /**
     * 生成存证续期请求sign
     *
     * @param attestXqDto
     */
    public static String genSign(AttestXqDto attestXqDto, String publicKey) {

        String finalParams = encode(attestXqDto);
        //拼接publicKey
        StringBuilder sb = new StringBuilder();
        sb = sb.append(finalParams).append("&publicKey=").append(publicKey);
        String toSign = sb.toString();

        //排序好的参数
        log.info("续期存证请求Sign待摘要的str:\n {}",toSign);

        //以utf-8形式获取bytes 数组
        byte[] pbytes = new byte[0];
        try {
            pbytes = toSign.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("获取byte数组失败");
        }

        byte[] rs = Hash.getHashMD5(pbytes);
        String sign = Base64.getEncoder().encodeToString(rs);

        //签名字符串
        return sign;
    }

    /**
     * 存证续期所需请求签名参数
     * @param attestXqDto
     * @return
     */
    public static String encode(AttestXqDto attestXqDto){
        Map<String,String> parameters = new HashMap<>();
        parameters.put("accessKey", StringUtils.isBlank(attestXqDto.getAccessKey())?"": attestXqDto.getAccessKey());
        parameters.put("random",StringUtils.isBlank(attestXqDto.getRandom())?"": attestXqDto.getRandom());

        parameters.put("bizSign", StringUtils.isBlank(attestXqDto.getBizSign())?"": attestXqDto.getBizSign());
        parameters.put("requestTime", StringUtils.isBlank(attestXqDto.getRequestTime())?"": attestXqDto.getRequestTime());

        parameters.put("channelId", StringUtils.isBlank(attestXqDto.getChannelId())?"": attestXqDto.getChannelId());
        parameters.put("channelUserid", StringUtils.isBlank(attestXqDto.getChannelUserid())?"": attestXqDto.getChannelUserid());
        parameters.put("channelOrdersn", StringUtils.isBlank(attestXqDto.getChannelOrdersn())?"": attestXqDto.getChannelOrdersn());
        parameters.put("ordersn", StringUtils.isBlank(attestXqDto.getOrdersn())?"": attestXqDto.getOrdersn());

        parameters.put("chained", StringUtils.isBlank(attestXqDto.getChained())?"": attestXqDto.getChained());
        parameters.put("bizType", StringUtils.isBlank(attestXqDto.getBizType())?"": attestXqDto.getBizType());

        parameters.put("fileName", StringUtils.isBlank(attestXqDto.getFileName())?"": attestXqDto.getFileName());
        parameters.put("fileType", StringUtils.isBlank(attestXqDto.getFileType())?"": attestXqDto.getFileType());
        parameters.put("fileSize", StringUtils.isBlank(attestXqDto.getFileSize())?"": attestXqDto.getFileSize());
        parameters.put("fileHash", StringUtils.isBlank(attestXqDto.getFileHash())?"": attestXqDto.getFileHash());

        parameters.put( "ownerType", StringUtils.isBlank(attestXqDto.getOwnerType())?"": attestXqDto.getOwnerType());
        parameters.put( "ownerId", StringUtils.isBlank(attestXqDto.getOwnerId())?"": attestXqDto.getOwnerId());
        parameters.put( "ownerName", StringUtils.isBlank(attestXqDto.getOwnerName())?"": attestXqDto.getOwnerName());
        parameters.put( "agentName", StringUtils.isBlank(attestXqDto.getAgentName())?"": attestXqDto.getAgentName());
        parameters.put( "agentPhone", StringUtils.isBlank(attestXqDto.getAgentPhone())?"": attestXqDto.getAgentPhone());
        parameters.put( "agentEmail", StringUtils.isBlank(attestXqDto.getAgentEmail())?"": attestXqDto.getAgentEmail());

        parameters.put("duration", StringUtils.isBlank(attestXqDto.getDuration())?"": attestXqDto.getDuration());
        parameters.put("startTime", StringUtils.isBlank(attestXqDto.getStartTime())?"": attestXqDto.getStartTime());
        parameters.put("description", StringUtils.isBlank(attestXqDto.getDescription())?"": attestXqDto.getDescription());
        parameters.put("price", StringUtils.isBlank(attestXqDto.getPrice())?"": attestXqDto.getPrice());

        //参数排序
        List<String> arrayList = new ArrayList<>(parameters.keySet());
        Collections.sort( arrayList );

        String finalParams="";
        StringBuilder sb = new StringBuilder();
        for( String s :arrayList){
            sb.append("&")
                    .append(s)
                    .append("=")
                    .append(parameters.get(s));
        }
        finalParams = sb.toString();
        return finalParams;
    }
}
