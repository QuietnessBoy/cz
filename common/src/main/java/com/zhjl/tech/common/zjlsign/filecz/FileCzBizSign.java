package com.zhjl.tech.common.zjlsign.filecz;

import com.zhjl.tech.common.channeldemo.ChannelConfig;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.encrypt.asymmetric.SM2;
import com.zhjl.tech.common.encrypt.asymmetric.SM2Impl;
import com.zhjl.tech.common.encrypt.asymmetric.SM2KeyPair;
import com.zhjl.tech.common.encrypt.bytetool.Hex;
import com.zhjl.tech.common.zjlsign.hashcz.HashCzBizSign;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.math.ec.ECPoint;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

@Slf4j
public class FileCzBizSign {

    /**
     *  检验文件存证订单签名sign
     * @param spublicKey
     * @param channelIDA
     * @param attestDto
     * @return
     * @throws UnsupportedEncodingException
     */
    public static boolean verify(String spublicKey, String channelIDA, AttestDto attestDto){
        return HashCzBizSign.verify(spublicKey,channelIDA,attestDto);
    }

    /**
     * 生成文件存证订单签名
     * @param attestDto
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String gen(AttestDto attestDto, String sPubKey, String sPriKey ) throws UnsupportedEncodingException {
        SM2 sm2 = new SM2Impl();

        //生成加密对象
        ECPoint publicKey = sm2.decodePublicKey(sPubKey);
        BigInteger privateKey = sm2.decodePrivateKey(sPriKey);
        SM2KeyPair sm2KeyPair_load = new SM2KeyPair(publicKey,privateKey);

        String finalParams = HashCzBizSign.encode(attestDto);

        //以utf-8形式获取bytes 数组
        byte[] pbytes = finalParams.getBytes("UTF-8");
        log.info("排序后的订单签名参数的bytes：{}", Hex.encode(pbytes));

        //开始签名
        SM2Impl.Signature signature = sm2.sign(
                pbytes,
                ChannelConfig.channelIDA,
                sm2KeyPair_load
        );


        String fileCzBizSign = signature.toBase64();
        log.info("生成存证订单签名：{}", fileCzBizSign);
        return fileCzBizSign;
    }
}
