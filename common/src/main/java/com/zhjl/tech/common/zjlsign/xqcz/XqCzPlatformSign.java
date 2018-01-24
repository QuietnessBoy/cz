package com.zhjl.tech.common.zjlsign.xqcz;

import com.zhjl.tech.common.exception.CommonException;
import com.zhjl.tech.common.dto.interfaces.AttestXqDto;
import com.zhjl.tech.common.encrypt.asymmetric.SM2;
import com.zhjl.tech.common.encrypt.asymmetric.SM2Impl;
import com.zhjl.tech.common.encrypt.asymmetric.SM2KeyPair;
import com.zhjl.tech.common.encrypt.bytetool.Hex;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.math.ec.ECPoint;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.*;

@Slf4j
public class XqCzPlatformSign {

    /**
     * 生成文件存证续期platformSign
     *
     * @param attestXqDto
     */
    public static String genSign(AttestXqDto attestXqDto, String sPubKey, String sPriKey,String ida) {
        SM2 sm2 = new SM2Impl();
        //生成加密对象
        ECPoint publicKey = sm2.decodePublicKey(sPubKey);
        BigInteger privateKey = sm2.decodePrivateKey(sPriKey);
        SM2KeyPair sm2KeyPair_load = new SM2KeyPair(publicKey, privateKey);

        String finalParams = encode(attestXqDto);
        //排序好的参数
        log.info("排序后的文件存证platformSign签名参数:{}", finalParams);

        //以utf-8形式获取bytes 数组
        byte[] pbytes = null;
        try {
            pbytes = finalParams.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.warn("文件存证platformSign生成错误.");
            throw new CommonException("生成续期订单的platformsign因UnsupportedEncodingException异常！channelOrdersn="+attestXqDto.getChannelOrdersn());
        }
        log.info("排序后的文件存证platformSign参数的bytes：{}", Hex.encode(pbytes));

        SM2Impl.Signature signature = sm2.sign(
                pbytes,
                //todo
                ida,
                sm2KeyPair_load
        );

        //签名字符串
        String fileCzBizSign = signature.toBase64();
        log.info("生成文件存证platformSign：{}", fileCzBizSign);
        return fileCzBizSign;
    }

    /**
     * 生成存证续期请求platformSignStr
     * @param attestXqDto
     * @return
     */
    public static String encode(AttestXqDto attestXqDto){
        // 参数名：参数值的映射
        Map<String,String> parameters = new HashMap<>();
        ////////////
        parameters.put("requestTime", StringUtils.isBlank(attestXqDto.getRequestTime())?"": attestXqDto.getRequestTime());
        parameters.put("channelId",  StringUtils.isBlank(attestXqDto.getChannelId())?"": attestXqDto.getChannelId());
        parameters.put("channelUserid", StringUtils.isBlank(attestXqDto.getChannelUserid())?"": attestXqDto.getChannelUserid());
        parameters.put("channelOrdersn", StringUtils.isBlank(attestXqDto.getChannelOrdersn())?"": attestXqDto.getChannelOrdersn());
        parameters.put("chained", StringUtils.isBlank(attestXqDto.getChained())?"": attestXqDto.getChained());
        parameters.put("bizType", StringUtils.isBlank(attestXqDto.getBizType())?"": attestXqDto.getBizType());

        parameters.put("fileName", StringUtils.isBlank(attestXqDto.getFileName())?"": attestXqDto.getFileName());
        parameters.put("fileType", StringUtils.isBlank(attestXqDto.getFileType())?"": attestXqDto.getFileType());
        parameters.put("fileSize", StringUtils.isBlank(attestXqDto.getFileSize())?"": attestXqDto.getFileSize());
        parameters.put("fileHash", StringUtils.isBlank(attestXqDto.getFileHash())?"": attestXqDto.getFileHash());

        parameters.put( "ownerType", StringUtils.isBlank(attestXqDto.getOwnerType())?"": attestXqDto.getOwnerType());
        parameters.put( "ownerId", StringUtils.isBlank(attestXqDto.getOwnerId())?"": attestXqDto.getOwnerId());
        parameters.put( "ownerName", StringUtils.isBlank(attestXqDto.getOwnerName())?"": attestXqDto.getOwnerName());

        parameters.put("duration", StringUtils.isBlank(attestXqDto.getDuration())?"": attestXqDto.getDuration());
        parameters.put("startTime", StringUtils.isBlank(attestXqDto.getStartTime())?"": attestXqDto.getStartTime());
        parameters.put("description", StringUtils.isBlank(attestXqDto.getDescription())?"": attestXqDto.getDescription());
        //以上与bizSign相同。
        parameters.put("bizSign", StringUtils.isBlank(attestXqDto.getBizSign())?"": attestXqDto.getBizSign());
        parameters.put("ordersn", StringUtils.isBlank(attestXqDto.getOrdersn())?"": attestXqDto.getOrdersn());

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

        //排序好的参数
        log.info("生成存证xq platformSign签名:{}" , finalParams);
        return finalParams;
    }
}
