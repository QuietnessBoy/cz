package com.zhjl.tech.common.encrypt.asymmetric;

import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

/**
 * Created by wind on 2017/7/29.
 * sm2相关算法
 */
public interface SM2 {

    /**
     * 生成sm2密钥对
     * @return
     */
    SM2KeyPair generateValidKeyPair();

    /**
     * 签名
     *
     * @param M
     *            待签名信息的byte数组。 一般建议采用utf-8获取
     * @param IDA
     *            签名方唯一标识。
     * @param keyPair
     *            签名方密钥对
     * @return 签名
     */
    SM2Impl.Signature sign(byte[] M, String IDA, SM2KeyPair keyPair);

    /**
     * 验签
     *
     * @param M 签名信息的bytes数组. 一般建议采用utf-8获取
     * @param signature 签名
     * @param IDA 签名方唯一标识
     * @param aPublicKey 签名方公钥
     * @return true or false
     */
    boolean verify(byte[] M, SM2Impl.Signature signature, String IDA, ECPoint aPublicKey);

    /**
     * 采用公钥进行加密
     * @param inputBuffer
     * @param publicKey
     * @return
     */
    byte[] encrypt(byte[] inputBuffer, ECPoint publicKey);

    /**
     * sm2解密
     * @param encryptData
     * @param privateKey
     * @return
     */
    String decrypt(byte[] encryptData, BigInteger privateKey);

    // 以下为进行byte数字的格式化

    /**
     *
     * @param publicKey
     * @return 公钥的字符串形式。04 || x的hex形式 || y的hex形式
     */
    String encodePublicKey(ECPoint publicKey);
    ECPoint decodePublicKey(String encodedKey);

    //私钥的字符串形式，直接hex编码
    String encodePrivateKey(BigInteger privateKey);
    BigInteger decodePrivateKey(String encodedKey);


    //采用 格式说明 base64(x,y坐标数组拼接)
    @Deprecated
    String encodePublicKeyBase64(ECPoint publicKey);
    @Deprecated
    ECPoint decodePublicKeyBase(String encodedKey);

    // 格式 直接base64
    @Deprecated
    String encodePrivateKeyBase64(BigInteger privateKey);
    @Deprecated
    BigInteger decodePrivateKeyBase64(String encodedKey);

}
