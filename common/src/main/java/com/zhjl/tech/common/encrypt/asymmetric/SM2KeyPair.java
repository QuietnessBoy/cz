package com.zhjl.tech.common.encrypt.asymmetric;

import lombok.Data;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;

/**
 * sm2密钥对 对象
 */
@Data
public class SM2KeyPair {

    private final ECPoint publicKey;
    private final BigInteger privateKey;

    public SM2KeyPair(ECPoint publicKey, BigInteger privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    /**
     *
     * @param publicKeyStr 编码格2式Base64。
     * @param privateKeyStr 编码格式Base64。
     * @return
     */
    @Deprecated
    public static SM2KeyPair genKeyPair(String publicKeyStr,String privateKeyStr){
        SM2 sm2 = new SM2Impl();

        ECPoint publicKey = sm2.decodePublicKey(publicKeyStr);
        BigInteger privateKey = sm2.decodePrivateKey(privateKeyStr);

        return new SM2KeyPair(publicKey,privateKey);
    }

    /**
     *
     * @param publicKeyStr hex
     * @param privateKeyStr hex
     * @return
     */
    public static SM2KeyPair genByHexString(String publicKeyStr, String privateKeyStr){
        SM2 sm2 = new SM2Impl();

        ECPoint publicKey = sm2.decodePublicKey(publicKeyStr);
        BigInteger privateKey = sm2.decodePrivateKey(privateKeyStr);

        return new SM2KeyPair(publicKey,privateKey);
    }
}