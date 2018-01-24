package com.zhjl.tech.common.channeldemo;

import com.zhjl.tech.common.encrypt.asymmetric.SM2;
import com.zhjl.tech.common.encrypt.asymmetric.SM2Impl;
import com.zhjl.tech.common.encrypt.asymmetric.SM2KeyPair;
import com.zhjl.tech.common.encrypt.bytetool.Base58;
import com.zhjl.tech.common.encrypt.digest.SM3;
import com.zhjl.tech.common.encrypt.digest.SM3Impl;
import org.bouncycastle.math.ec.ECPoint;

import java.math.BigInteger;
import java.util.Base64;

/**
 * 生成渠道自己的SM2密钥对，将公钥提交给存证平台，私钥用来对交易数据签名。
 */
public class GenChannelKeypair {

    public void gen(){

        SM2 sm2 = new SM2Impl();
        //生成密钥
        SM2KeyPair sm2KeyPair = sm2.generateValidKeyPair();

        //编码密钥,hex形式。将此处的spublicKey提交给渠道，sprivateKey保管好用于后续操作。
        String spublicKey = sm2.encodePublicKey(sm2KeyPair.getPublicKey());
        String sprivateKey = sm2.encodePrivateKey(sm2KeyPair.getPrivateKey());

        System.out.println("渠道生成的公钥:\n"+spublicKey);
        System.out.println("渠道生成的私钥:\n" +sprivateKey);

        //根据保存的编码后的密钥串，重建加密对象
        ECPoint publicKey = sm2.decodePublicKey(spublicKey);
        BigInteger privateKey = sm2.decodePrivateKey(sprivateKey);
        SM2KeyPair sm2KeyPair_load = new SM2KeyPair(publicKey,privateKey);
        System.out.println("重建后的加密对象的公私钥:\n"
                + sm2.encodePublicKey(sm2KeyPair_load.getPublicKey())
                + "\n"
                + sm2.encodePrivateKey(sm2KeyPair_load.getPrivateKey()));

    }

    /**
     * 根据publickey计算accessKey. 对公钥二次sm3摘要，然后base58
     * @param publickey。publickey是直接拼接x y字符数组，然后进行base64编码的
     * @return
     */
    public String getAccessKey2(String publickey){
        byte[] xys = Base64.getDecoder().decode(publickey);

        SM3 sm3Impl = new SM3Impl();
        byte[] input1 = sm3Impl.getHash(xys);
        byte[] input2 = sm3Impl.getHash(input1);
        return Base58.encodeCheck(input2);
    }

    public static void main(String[] args) {
        new GenChannelKeypair().gen();
    }
}
