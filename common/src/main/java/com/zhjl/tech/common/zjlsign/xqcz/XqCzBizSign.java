package com.zhjl.tech.common.zjlsign.xqcz;

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
public class XqCzBizSign {

    /**
     *  校验文件存证请求sign
     * @param spublicKey
     * @param channelIDA
     * @param bizSign
     * @param attestXqDto
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean verify(String spublicKey, String channelIDA, String bizSign, AttestXqDto attestXqDto){
        SM2 sm2 = new SM2Impl();
        //生成加密对象
        ECPoint publicKey = sm2.decodePublicKey(spublicKey);

        //签名对象
        SM2Impl.Signature signature = SM2Impl.Signature.genByBase64(bizSign);

        String finalParams = encode(attestXqDto);

        //排序好的参数
        log.info("排序后的订单签名:{}",finalParams);

        //以utf-8形式获取bytes 数组
        byte[] pbytes;
        try {
            pbytes = finalParams.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("获取byte数组失败");
            return false;
        }
        log.info("订单签名bytes:{}", Hex.encode(pbytes));

        //开始校验
        log.info("校验订单签名，pbyte={},sign={},channelIDA={},publicKey={}",
                Hex.encode(pbytes),
                signature.toBase64(),
                channelIDA,
                spublicKey );
        return  sm2.verify(pbytes,
                signature,
                channelIDA,
                publicKey);
    }

    /**
     * 生成文件存证续期订单签名
     * @param attestXqDto
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String gen(AttestXqDto attestXqDto, String sPubKey, String sPriKey,String ida) throws UnsupportedEncodingException {
        SM2 sm2 = new SM2Impl();
        //生成加密对象
        ECPoint publicKey = sm2.decodePublicKey(sPubKey);
        BigInteger privateKey = sm2.decodePrivateKey(sPriKey);
        SM2KeyPair sm2KeyPair_load = new SM2KeyPair(publicKey,privateKey);

        String finalParams = XqCzBizSign.encode(attestXqDto);
        //排序好的参数
        log.info("排序后的续期订单签名参数:{}", finalParams);

        //以utf-8形式获取bytes 数组
        byte[] pbytes = finalParams.getBytes("UTF-8");
        log.info("续期订单签名参数的bytes：{}",Hex.encode(pbytes));

        SM2Impl.Signature signature = sm2.sign(
                pbytes,
                ida,
                sm2KeyPair_load
        );

        //签名字符串
        String fileHashCzBizSign = signature.toBase64();
        log.info("生成续期订单签名:pubKey:{},ida:{},bizsign:{}",sPubKey,ida,fileHashCzBizSign);

        return fileHashCzBizSign;
    }

    /**
     * 续期订单签名str
     * @param attestXqDto
     * @return
     */
    public static String encode(AttestXqDto attestXqDto){
        // 参数名：参数值的映射
        Map<String,String> parameters = new HashMap<>();
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

        parameters.put("duration", StringUtils.isBlank(attestXqDto.getDuration())?"": attestXqDto.getDuration());
        parameters.put("startTime", StringUtils.isBlank(attestXqDto.getStartTime())?"": attestXqDto.getStartTime());
        parameters.put("description", StringUtils.isBlank(attestXqDto.getDescription())?"": attestXqDto.getDescription());

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
        log.info("生成续期订单签名:{}",finalParams);
        return finalParams;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        AttestXqDto attestXqDto = new AttestXqDto();
        attestXqDto.setOrdersn("11");

        String s = gen(attestXqDto, "04f244fc01a3c1f5e0a7c1fc45ae7d7a19fd6cee70dbbabbf5f49fb17211c278459dd9e97d5d466a283b81c477b709cd763968dce5bc0b81bed234d8cab226ab45",
                "2e6c7975dcac8040768550a36f347648f2cb029cc8b0aba41461d3af1e029707",
                "super_channel_01");
        System.out.println(verify("04f244fc01a3c1f5e0a7c1fc45ae7d7a19fd6cee70dbbabbf5f49fb17211c278459dd9e97d5d466a283b81c477b709cd763968dce5bc0b81bed234d8cab226ab45", "super_channel_01",s,attestXqDto));

    }

}
