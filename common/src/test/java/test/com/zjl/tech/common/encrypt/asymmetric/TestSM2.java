package test.com.zjl.tech.common.encrypt.asymmetric;


import com.zhjl.tech.common.encrypt.asymmetric.SM2;
import com.zhjl.tech.common.encrypt.asymmetric.SM2Impl;
import com.zhjl.tech.common.encrypt.asymmetric.SM2KeyPair;
import org.bouncycastle.math.ec.ECPoint;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Base64;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TestSM2 {

    SM2 sm2 = new SM2Impl();

    /**
     * 测试生成密钥对后的编码、解码
     */
    @Test
    public void testGenAndEncode(){
        for( int i = 0;i<10;i++){
            SM2KeyPair sm2KeyPair = sm2.generateValidKeyPair();

            String spub = sm2.encodePublicKey(sm2KeyPair.getPublicKey());
            String spri = sm2.encodePrivateKey(sm2KeyPair.getPrivateKey());

            System.out.println("生成的公钥，"+spub.length() + "," +spub);
            System.out.println("生成的私钥，"+spri.length() + "," +spri);

            assertEquals(130,spub.length());
            SM2KeyPair sm2KeyPair_gen = SM2KeyPair.genByHexString(spub,spri);

            System.out.println("regen的公钥，"+sm2.encodePublicKey(sm2KeyPair_gen.getPublicKey()));
            System.out.println("regen的私钥，"+sm2.encodePrivateKey(sm2KeyPair_gen.getPrivateKey()));

            // 解析，然后比对
            assertEquals(sm2.encodePrivateKey(sm2KeyPair_gen.getPrivateKey()),spri);
            assertEquals(sm2.encodePublicKey(sm2KeyPair_gen.getPublicKey()),spub);
        }
    }

    /**
     * 测试签名
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testSign() throws UnsupportedEncodingException {
        //初始化算法和密钥
//        SM2KeyPair sm2KeyPair = sm2.generateValidKeyPair();

        String pu="04c0d9e8e4e607aaf4181ffa763aedb484226ff284ab0dcc2c5100a66cab83e0ea28dcd079d53049aca0cd8a50080c387c6245568815d78ab269cfa75efa6cbe5";
        String pri="00fbc8a528392577088c03b9ea6d3165f9bfc08ebcb61be0c868566c04ad44a0ac";
        SM2KeyPair sm2KeyPair1 = SM2KeyPair.genByHexString(pu,pri);

        System.out.println("-----------------签名与验签-----------------");
        String IDA = "Heartbeats";
        String M = "要签名的信息";
        SM2Impl.Signature signature = sm2.sign(M.getBytes("UTF-8"), IDA, sm2KeyPair1);

        System.out.println("用户标识:" + IDA);
        System.out.println("签名信息:" + M);
        System.out.println("数字签名hex   :" + signature);
        String base64 = signature.toBase64();
        System.out.println("数字签名base64:" + base64);

        assertTrue(
                sm2.verify(
                        M.getBytes("UTF-8"),
                        SM2Impl.Signature.genByBase64(base64),
                        IDA,
                        sm2KeyPair1.getPublicKey()
                )
        );

        assertTrue(
                sm2.verify(
                        M.getBytes("UTF-8"),
                        SM2Impl.Signature.genByBase64(base64),
                        IDA,
                        sm2KeyPair1.getPublicKey()
                )
        );


        System.out.println("数字签名测试完成！" );

    }

    /**
     * 测试公钥加密
     */
    @Test
    public void testEncrypt() throws UnsupportedEncodingException {

        System.out.println("-------------开始测试sm2公私钥编解码、公钥加密");

        SM2KeyPair keyPair = sm2.generateValidKeyPair();
        ECPoint publicKey= keyPair.getPublicKey();
        BigInteger privateKey= keyPair.getPrivateKey();

        //公钥
        String encode_publickey = sm2.encodePublicKey(publicKey);
        System.out.println("公钥编码:" + encode_publickey);

        //测试公钥的编解码
        assertEquals(encode_publickey,
                sm2.encodePublicKey(sm2.decodePublicKey(encode_publickey)));

        //私钥
        String encode_privatekey = sm2.encodePrivateKey(privateKey);
        System.out.println("私钥编码:" + encode_privatekey);

        //测试私钥的编解码
        assertEquals(encode_privatekey,
                sm2.encodePrivateKey(sm2.decodePrivateKey(encode_privatekey)));

        byte[] in = new byte[]{
                0x65,0x6E,0x63,0x72,0x79,0x70,0x74,0x69,
                0x6F,0x6E,0x20,0x73,0x74,0x61,0x6E,0x64,
                0x61,0x72,0x64};
        byte[] data = sm2.encrypt(in, publicKey);
        System.out.println("密文base64:" + Base64.getEncoder().encodeToString(data));

        //解密
        System.out.println("解密后明文:" + sm2.decrypt(data, privateKey));
        assertEquals(sm2.decrypt(data, privateKey),new String(in,"utf-8"));
    }

    /**
     * 测试sm2密钥交换
     */
    @Test
    public void testExchange(){
        System.out.println("-------------开始测试sm2密钥协商");

        SM2 sm02 = new SM2Impl();

        //手动指定私钥，公钥。用于测试效果
//        BigInteger px = new BigInteger(
//                "435B39CC A8F3B508 C1488AFC 67BE491A 0F7BA07E 581A0E48 49A5CF70 628A7E0A".replace(" ", ""), 16);
//        BigInteger py = new BigInteger(
//                "75DDBA78 F15FEECB 4C7895E2 C1CDF5FE 01DEBB2C DBADF453 99CCF77B BA076A42".replace(" ", ""), 16);
//        publicKey = sm02.curve.createPoint(px, py);
//        privateKey = new BigInteger(
//                "1649AB77 A00637BD 5E2EFE28 3FBF3535 34AA7F7C B89463F2 08DDBC29 20BB0DA0".replace(" ", ""), 16);

        String aID = "AAAAAAAAAAAAA";
        SM2KeyPair aKeyPair = sm02.generateValidKeyPair();
        SM2Impl.KeyExchange aKeyExchange = new SM2Impl.KeyExchange(aID,aKeyPair);

        String bID = "BBBBBBBBBBBBB";
        SM2KeyPair bKeyPair = sm02.generateValidKeyPair();
        SM2Impl.KeyExchange bKeyExchange = new SM2Impl.KeyExchange(bID,bKeyPair);

        SM2Impl.TransportEntity entity1 = aKeyExchange.keyExchange_1();
        SM2Impl.TransportEntity entity2 = bKeyExchange.keyExchange_2(entity1);
        SM2Impl.TransportEntity entity3 = aKeyExchange.keyExchange_3(entity2);
        bKeyExchange.keyExchange_4(entity3);
    }

}
