package com.zhjl.tech.common.encrypt.asymmetric;

import com.zhjl.tech.common.encrypt.bytetool.Hex;
import com.zhjl.tech.common.encrypt.digest.SM3;
import com.zhjl.tech.common.encrypt.digest.SM3Impl;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECPoint;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**
 * code from https://github.com/PopezLotado/SM2Java.git, 以bouncycastle代码为基础，进行了改造
 *
 * 素数域上的椭圆曲线。 *
 * 参照标准《GM/T 0003.5-2012 SM2椭圆曲线公钥密码算法》
 *
 * SM2公钥加密算法实现 包括 -签名,验签 -密钥交换 -公钥加密,私钥解密
 */
public class SM2Impl implements SM2{

    private static int PUBLICKEY_X_LENGTH_IN_HEX = 64;

    //保密局推荐的参数
    private static BigInteger p = new BigInteger(
            "FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF" + "FFFFFFFF", 16);
    private static BigInteger a = new BigInteger(
            "FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "00000000" + "FFFFFFFF" + "FFFFFFFC", 16);
    private static BigInteger b = new BigInteger(
            "28E9FA9E" + "9D9F5E34" + "4D5A9E4B" + "CF6509A7" + "F39789F5" + "15AB8F92" + "DDBCBD41" + "4D940E93", 16);
    private static BigInteger gx = new BigInteger(
            "32C4AE2C" + "1F198119" + "5F990446" + "6A39C994" + "8FE30BBF" + "F2660BE1" + "715A4589" + "334C74C7", 16);
    private static BigInteger gy = new BigInteger(
            "BC3736A2" + "F4F6779C" + "59BDCEE3" + "6B692153" + "D0A9877C" + "C62A4740" + "02DF32E5" + "2139F0A0", 16);
    private static BigInteger n = new BigInteger(
            "FFFFFFFE" + "FFFFFFFF" + "FFFFFFFF" + "FFFFFFFF" + "7203DF6B" + "21C6052B" + "53BBF409" + "39D54123", 16);

    // 保密局标准中的示例的参数
//    private static BigInteger p = new BigInteger(
//            "8542D69E4C044F18E8B92435BF6FF7DE457283915C45517D722EDB8B08F1DFC3", 16);
//    private static BigInteger a = new BigInteger(
//            "787968B4FA32C3FD2417842E73BBFEFF2F3C848B6831D7E0EC65228B3937E498", 16);
//    private static BigInteger b = new BigInteger(
//            "63E4C6D3B23B0C849CF84241484BFE48F61D59A5B16BA06E6E12D1DA27C5249A", 16);
//    private static BigInteger gx = new BigInteger(
//            "421DEBD61B62EAB6746434EBC3CC315E32220B3BADD50BDC4C4E6C147FEDD43D", 16);
//    private static BigInteger gy = new BigInteger(
//            "0680512BCBB42C07D47349D2153B70C4E5D7FDFCBFA36EA1A85841B9E46E09A2", 16);
//    private static BigInteger n = new BigInteger(
//            "8542D69E4C044F18E8B92435BF6FF7DD297720630485628D5AE74EE7C32E79B7",16);

    // 编码公钥
    @Override
    public String encodePublicKeyBase64(ECPoint publicKey) {
        if( publicKey == null ){
            return null;
        }

        byte[] pkeyx = publicKey.getXCoord().getEncoded();
        byte[] pkeyy = publicKey.getYCoord().getEncoded();

        byte[] keys = new byte[pkeyx.length+pkeyy.length];

        System.arraycopy(pkeyx,0,keys,0,pkeyx.length);
        System.arraycopy(pkeyy,0,keys,pkeyx.length,pkeyy.length);

        return Hex.encode(keys);
    }

    // 解码公钥
    @Override
    public ECPoint decodePublicKeyBase(String encodeKey) {
        byte[] pks = Hex.decode(encodeKey);

        byte[] pkeyx = new byte[PUBLICKEY_X_LENGTH_IN_HEX/2];
        byte[] pkeyy = new byte[PUBLICKEY_X_LENGTH_IN_HEX/2];

        System.arraycopy(pks,0,pkeyx,0,PUBLICKEY_X_LENGTH_IN_HEX/2);
        System.arraycopy(pks,PUBLICKEY_X_LENGTH_IN_HEX/2,pkeyy,0,PUBLICKEY_X_LENGTH_IN_HEX/2);

        BigInteger px = new BigInteger(pkeyx);
        BigInteger py = new BigInteger(pkeyy);

        return curve.createPoint(px,py);
    }

    @Override
    public String encodePrivateKeyBase64(BigInteger privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.toByteArray());
    }

    @Override
    public BigInteger decodePrivateKeyBase64(String encodeKey) {
        return new BigInteger(Base64.getDecoder().decode(encodeKey));
    }

    private static ECDomainParameters ecc_bc_spec;
    private static int w = (int) Math.ceil(n.bitLength() * 1.0 / 2) - 1;
    private static BigInteger _2w = new BigInteger("2").pow(w);
    private static final int DIGEST_LENGTH = 32;

    private static SecureRandom random = new SecureRandom();
    public static ECCurve.Fp curve;
    private static ECPoint G;
    private boolean debug = false;

    /**
     * 随机数生成器
     *
     * @param max
     * @return
     */
    private static BigInteger random(BigInteger max) {

        BigInteger r = new BigInteger(256, random);
        // int count = 1;

        while (r.compareTo(max) >= 0) {
            r = new BigInteger(128, random);
            // count++;
        }

        // System.out.println("count: " + count);
        return r;
    }

    /**
     * 判断字节数组是否全0
     *
     * @param buffer
     * @return r
     */
    private boolean allZero(byte[] buffer) {
        for (int i = 0; i < buffer.length; i++) {
            if (buffer[i] != 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 公钥加密
     *
     * @param inputBuffer
     *            加密原文
     * @param publicKey
     *            公钥
     * @return
     */
    @Override
    public byte[] encrypt(byte[] inputBuffer, ECPoint publicKey) {

        if (debug) {
            printHexString(inputBuffer);
        }

        byte[] C1Buffer;
        ECPoint kpb;
        byte[] t;
        do {
			/* 1 产生随机数k，k属于[1, n-1] */
            BigInteger k = random(n);
//            标准的测试数据k
//            k=new BigInteger("4C62EEFD6ECFC2B95B92FD6C3D9575148AFA17425546D49018E5388D49DD7B4F",16);
            if (debug) {
                System.out.print("k: ");
                printHexString(k.toByteArray());
            }

			/* 2 计算椭圆曲线点C1 = [k]G = (x1, y1) */
            ECPoint C1 = G.multiply(k);
            C1Buffer = C1.getEncoded(false);
            if (debug) {
                System.out.print("C1: ");
                printHexString(C1Buffer);
            }

			/*
			 * 3 计算椭圆曲线点 S = [h]Pb
			 */
            BigInteger h = ecc_bc_spec.getH();
            if (h != null) {
                ECPoint S = publicKey.multiply(h);
                if (S.isInfinity()) {
                    throw new IllegalStateException();
                }
            }

			/* 4 计算 [k]PB = (x2, y2) */
            kpb = publicKey.multiply(k).normalize();

			/* 5 计算 t = KDF(x2||y2, klen) */
            byte[] kpbBytes = kpb.getEncoded(false);
            t = KDF(kpbBytes, inputBuffer.length);
            // DerivationFunction kdf = new KDF1BytesGenerator(new
            // ShortenedDigest(new SHA256Digest(), DIGEST_LENGTH));
            //
            // t = new byte[inputBuffer.length];
            // kdf.cache(new ISO18033KDFParameters(kpbBytes));
            // kdf.generateBytes(t, 0, t.length);
        } while (allZero(t));

		/* 6 计算C2=M^t */
        byte[] C2 = new byte[inputBuffer.length];
        for (int i = 0; i < inputBuffer.length; i++) {
            C2[i] = (byte) (inputBuffer[i] ^ t[i]);
        }

		/* 7 计算C3 = Hash(x2 || M || y2) */
        byte[] C3 = sm3hash(kpb.getXCoord().toBigInteger().toByteArray(), inputBuffer,
                kpb.getYCoord().toBigInteger().toByteArray());

		/* 8 输出密文 C=C1 || C2 || C3 */

        byte[] encryptResult = new byte[C1Buffer.length + C2.length + C3.length];

        System.arraycopy(C1Buffer, 0, encryptResult, 0, C1Buffer.length);
        System.arraycopy(C2, 0, encryptResult, C1Buffer.length, C2.length);
        System.arraycopy(C3, 0, encryptResult, C1Buffer.length + C2.length, C3.length);

        if (debug) {
            System.out.print("密文: ");
            printHexString(encryptResult);
        }

        return encryptResult;
    }

    /**
     * 私钥解密
     *
     * @param encryptData
     *            密文数据字节数组
     * @param privateKey
     *            解密私钥
     * @return 以utf-8编码的
     */
    @Override
    public String decrypt(byte[] encryptData, BigInteger privateKey) {

        if (debug) {
            System.out.println("encryptData length: " + encryptData.length);
        }

        byte[] C1Byte = new byte[65];
        System.arraycopy(encryptData, 0, C1Byte, 0, C1Byte.length);

        ECPoint C1 = curve.decodePoint(C1Byte).normalize();

		/*
		 * 计算椭圆曲线点 S = [h]C1 是否为无穷点
		 */
        BigInteger h = ecc_bc_spec.getH();
        if (h != null) {
            ECPoint S = C1.multiply(h);
            if (S.isInfinity()) {
                throw new IllegalStateException();
            }
        }
		/* 计算[dB]C1 = (x2, y2) */
        ECPoint dBC1 = C1.multiply(privateKey).normalize();

		/* 计算t = KDF(x2 || y2, klen) */
        byte[] dBC1Bytes = dBC1.getEncoded(false);
        int klen = encryptData.length - 65 - DIGEST_LENGTH;
        byte[] t = KDF(dBC1Bytes, klen);
        // DerivationFunction kdf = new KDF1BytesGenerator(new
        // ShortenedDigest(new SHA256Digest(), DIGEST_LENGTH));
        // if (debug)
        // System.out.println("klen = " + klen);
        // kdf.cache(new ISO18033KDFParameters(dBC1Bytes));
        // kdf.generateBytes(t, 0, t.length);

        if (allZero(t)) {
            System.err.println("all zero");
            throw new IllegalStateException();
        }

		/* 5 计算M'=C2^t */
        byte[] M = new byte[klen];
        for (int i = 0; i < M.length; i++) {
            M[i] = (byte) (encryptData[C1Byte.length + i] ^ t[i]);
        }
        if (debug) {
            printHexString(M);
        }

		/* 6 计算 u = Hash(x2 || M' || y2) 判断 u == C3是否成立 */
        byte[] C3 = new byte[DIGEST_LENGTH];

        if (debug) {
            try {
                System.out.println("M = " + new String(M, "UTF8"));
            } catch (UnsupportedEncodingException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        System.arraycopy(encryptData, encryptData.length - DIGEST_LENGTH, C3, 0, DIGEST_LENGTH);
        byte[] u = sm3hash(dBC1.getXCoord().toBigInteger().toByteArray(), M,
                dBC1.getYCoord().toBigInteger().toByteArray());
        if (Arrays.equals(u, C3)) {
            if (debug) {
                System.out.println("解密成功");
            }
            try {
                return new String(M, "UTF8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return null;
        } else {
            if (debug) {
                System.out.print("u = ");
                printHexString(u);
                System.out.print("C3 = ");
                printHexString(C3);
                System.err.println("解密验证失败");
            }
            return null;
        }

    }


    /**
     * 判断是否在范围内
     *
     * @param param
     * @param min
     * @param max
     * @return
     */
    private boolean between(BigInteger param, BigInteger min, BigInteger max) {
        return param.compareTo(min) >= 0 && param.compareTo(max) < 0;
    }

    /**
     * 判断生成的公钥是否合法
     *
     * @param publicKey
     * @return
     */
    private boolean checkPublicKey(ECPoint publicKey) {

        if (!publicKey.isInfinity()) {

            BigInteger x = publicKey.getXCoord().toBigInteger();
            BigInteger y = publicKey.getYCoord().toBigInteger();

            if (between(x, new BigInteger("0"), p) && between(y, new BigInteger("0"), p)) {

                BigInteger xResult = x.pow(3).add(a.multiply(x)).add(b).mod(p);

                if (debug) {
                    System.out.println("xResult: " + xResult.toString());
                }

                BigInteger yResult = y.pow(2).mod(p);

                if (debug) {
                    System.out.println("yResult: " + yResult.toString());
                }

                if (yResult.equals(xResult) && publicKey.multiply(n).isInfinity()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 生成密钥对
     *
     * @return
     */
    @Override
    public SM2KeyPair generateValidKeyPair() {
        SM2KeyPair keyPair=null;

        while (keyPair == null){
            keyPair = generateKeyPair();
        }

        return keyPair;
    }

    /**
     * 生成密钥对
     *
     * @return
     */
    private SM2KeyPair generateKeyPair() {

        BigInteger d = random(n.subtract(new BigInteger("1")));

        SM2KeyPair keyPair = new SM2KeyPair(G.multiply(d).normalize(), d);

        if (checkPublicKey(keyPair.getPublicKey())) {
            if (debug) {
                System.out.println("generate Key successfully");
            }
            return keyPair;
        } else {
            if (debug) {
                System.err.println("generate Key failed");
            }
            return null;
        }
    }

    public SM2Impl() {
        curve = new ECCurve.Fp(p, // q
                a,
                b);
        G = curve.createPoint(gx, gy);
        ecc_bc_spec = new ECDomainParameters(curve, G, n);
    }

    //是否开启测试模式
    public SM2Impl(boolean debug) {
        this();
        this.debug = debug;
    }

    /**
     * 字节数组拼接
     *
     * @param params
     * @return
     */
    private static byte[] join(byte[]... params) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] res = null;
        try {
            for (int i = 0; i < params.length; i++) {
                baos.write(params[i]);
            }
            res = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * sm3摘要
     *
     * @param params
     * @return
     */
    private static byte[] sm3hash(byte[]... params) {
        byte[] res = null;
        SM3 sm3Impl = new SM3Impl();

        res = sm3Impl.getHash(join(params));

        return res;
    }

    /**
     * 取得用户标识字节数组
     *
     * @param IDA 标识
     * @param aPublicKey 公钥
     * @return byte数组
     */
    private static byte[] ZA(String IDA, ECPoint aPublicKey) {
        byte[] idaBytes = new byte[0];
        try {
            idaBytes = IDA.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // wind 去掉了长度字段。这里只是拼接,可以自定义
        //int entlenA = idaBytes.length * 8;
        //byte[] ENTLA = new byte[] { (byte) (entlenA & 0xFF00), (byte) (entlenA & 0x00FF) };
        return sm3hash(
                //ENTLA,
                idaBytes, a.toByteArray(), b.toByteArray(), gx.toByteArray(), gy.toByteArray(),
                aPublicKey.getXCoord().toBigInteger().toByteArray(),
                aPublicKey.getYCoord().toBigInteger().toByteArray());
    }

    /**
     * 签名
     *
     * @param M
     *            签名信息
     * @param IDA
     *            签名方唯一标识 walletaddr。
     * @param keyPair 签名方密钥对
     * @return 签名
     */
    @Override
    public Signature sign(byte[] M, String IDA, SM2KeyPair keyPair) {
        byte[] ZA = ZA(IDA, keyPair.getPublicKey());
        byte[] M_ = join(ZA, M);
        BigInteger e = new BigInteger(1, sm3hash(M_));
        // BigInteger k = new BigInteger(
        // "6CB28D99 385C175C 94F94E93 4817663F C176D925 DD72B727 260DBAAE
        // 1FB2F96F".replace(" ", ""), 16);
        BigInteger k;
        BigInteger r;
        do {
            k = random(n);
            ECPoint p1 = G.multiply(k).normalize();
            BigInteger x1 = p1.getXCoord().toBigInteger();
            r = e.add(x1);
            r = r.mod(n);
        } while (r.equals(BigInteger.ZERO) || r.add(k).equals(n));

        BigInteger s = ((keyPair.getPrivateKey().add(BigInteger.ONE).modInverse(n))
                .multiply((k.subtract(r.multiply(keyPair.getPrivateKey()))).mod(n))).mod(n);

        return new Signature(r, s);
    }

    /**
     * 验签
     *
     * @param M
     *            签名信息
     * @param signature
     *            签名
     * @param IDA
     *            签名方唯一标识
     * @param aPublicKey
     *            签名方公钥
     * @return true or false
     */
    @Override
    public boolean verify(byte[] M, Signature signature, String IDA, ECPoint aPublicKey) {
        if (!between(signature.r, BigInteger.ONE, n)) {
            return false;
        }
        if (!between(signature.s, BigInteger.ONE, n)) {
            return false;
        }

        byte[] M_ = join(ZA(IDA, aPublicKey), M);
        BigInteger e = new BigInteger(1, sm3hash(M_));
        BigInteger t = signature.r.add(signature.s).mod(n);

        if (t.equals(BigInteger.ZERO)) {
            return false;
        }

        ECPoint p1 = G.multiply(signature.s).normalize();
        ECPoint p2 = aPublicKey.multiply(t).normalize();
        BigInteger x1 = p1.add(p2).normalize().getXCoord().toBigInteger();
        BigInteger R = e.add(x1).mod(n);

        return R.equals(signature.r);
    }



    /**
     * 密钥派生函数
     *
     * @param Z
     * @param klen
     *            生成klen字节数长度的密钥
     * @return
     */
    private static byte[] KDF(byte[] Z, int klen) {
        int ct = 1;
        int end = (int) Math.ceil(klen * 1.0 / 32);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            for (int i = 1; i < end; i++) {
                baos.write(sm3hash(Z, toByteArray(ct)));
                ct++;
            }
            byte[] last = sm3hash(Z, toByteArray(ct));
            if (klen % 32 == 0) {
                baos.write(last);
            } else {
                baos.write(last, 0, klen % 32);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 传输实体类
     *
     */
    public static class TransportEntity implements Serializable {
        final byte[] R; //R点
        final byte[] S; //验证S
        final byte[] Z; //用户标识
        final byte[] K; //公钥

        public TransportEntity(byte[] r, byte[] s,byte[] z,ECPoint pKey) {
            R = r;
            S = s;
            Z=z;
            K=pKey.getEncoded(false);
        }
    }

    /**
     * 密钥协商辅助类
     *
     */
    public static class KeyExchange {
        BigInteger rA;
        ECPoint RA;
        ECPoint V;
        byte[] Z;
        byte[] key;

        String ID;
        SM2KeyPair keyPair;

        public KeyExchange(String ID,SM2KeyPair keyPair) {
            this.ID=ID;
            this.keyPair = keyPair;
            this.Z=ZA(ID, keyPair.getPublicKey());
        }

        /**
         * 密钥协商发起第一步
         *
         * @return
         */
        public TransportEntity keyExchange_1() {
            rA = random(n);
            // rA=new BigInteger("83A2C9C8 B96E5AF7 0BD480B4 72409A9A 327257F1
            // EBB73F5B 073354B2 48668563".replace(" ", ""),16);
            RA = G.multiply(rA).normalize();
            return new TransportEntity(RA.getEncoded(false), null,Z,keyPair.getPublicKey());
        }

        /**
         * 密钥协商响应方
         *
         * @param entity 传输实体
         * @return
         */
        public TransportEntity keyExchange_2(TransportEntity entity) {
            BigInteger rB = random(n);
            // BigInteger rB=new BigInteger("33FE2194 0342161C 55619C4A 0C060293
            // D543C80A F19748CE 176D8347 7DE71C80".replace(" ", ""),16);
            ECPoint RB = G.multiply(rB).normalize();

            this.rA=rB;
            this.RA=RB;

            BigInteger x2 = RB.getXCoord().toBigInteger();
            x2 = _2w.add(x2.and(_2w.subtract(BigInteger.ONE)));

            BigInteger tB = keyPair.getPrivateKey().add(x2.multiply(rB)).mod(n);
            ECPoint RA = curve.decodePoint(entity.R).normalize();

            BigInteger x1 = RA.getXCoord().toBigInteger();
            x1 = _2w.add(x1.and(_2w.subtract(BigInteger.ONE)));

            ECPoint aPublicKey=curve.decodePoint(entity.K).normalize();
            ECPoint temp = aPublicKey.add(RA.multiply(x1).normalize()).normalize();
            ECPoint V = temp.multiply(ecc_bc_spec.getH().multiply(tB)).normalize();
            if (V.isInfinity()) {
                throw new IllegalStateException();
            }
            this.V=V;

            byte[] xV = V.getXCoord().toBigInteger().toByteArray();
            byte[] yV = V.getYCoord().toBigInteger().toByteArray();
            byte[] KB = KDF(join(xV, yV, entity.Z, this.Z), 16);
            key = KB;
            System.out.print("协商得B密钥:");
            printHexString(KB);
            byte[] sB = sm3hash(new byte[] { 0x02 }, yV,
                    sm3hash(xV, entity.Z, this.Z, RA.getXCoord().toBigInteger().toByteArray(),
                            RA.getYCoord().toBigInteger().toByteArray(), RB.getXCoord().toBigInteger().toByteArray(),
                            RB.getYCoord().toBigInteger().toByteArray()));
            return new TransportEntity(RB.getEncoded(false), sB,this.Z,keyPair.getPublicKey());
        }

        /**
         * 密钥协商发起方第二步
         *
         * @param entity 传输实体
         */
        public TransportEntity keyExchange_3(TransportEntity entity) {
            BigInteger x1 = RA.getXCoord().toBigInteger();
            x1 = _2w.add(x1.and(_2w.subtract(BigInteger.ONE)));

            BigInteger tA = keyPair.getPrivateKey().add(x1.multiply(rA)).mod(n);
            ECPoint RB = curve.decodePoint(entity.R).normalize();

            BigInteger x2 = RB.getXCoord().toBigInteger();
            x2 = _2w.add(x2.and(_2w.subtract(BigInteger.ONE)));

            ECPoint bPublicKey=curve.decodePoint(entity.K).normalize();
            ECPoint temp = bPublicKey.add(RB.multiply(x2).normalize()).normalize();
            ECPoint U = temp.multiply(ecc_bc_spec.getH().multiply(tA)).normalize();
            if (U.isInfinity()) {
                throw new IllegalStateException();
            }
            this.V=U;

            byte[] xU = U.getXCoord().toBigInteger().toByteArray();
            byte[] yU = U.getYCoord().toBigInteger().toByteArray();
            byte[] KA = KDF(join(xU, yU,
                    this.Z, entity.Z), 16);
            key = KA;
            System.out.print("协商得A密钥:");
            printHexString(KA);
            byte[] s1= sm3hash(new byte[] { 0x02 }, yU,
                    sm3hash(xU, this.Z, entity.Z, RA.getXCoord().toBigInteger().toByteArray(),
                            RA.getYCoord().toBigInteger().toByteArray(), RB.getXCoord().toBigInteger().toByteArray(),
                            RB.getYCoord().toBigInteger().toByteArray()));
            if(Arrays.equals(entity.S, s1)) {
                System.out.println("B->A 密钥确认成功");
            } else {
                System.out.println("B->A 密钥确认失败");
            }
            byte[] sA= sm3hash(new byte[] { 0x03 }, yU,
                    sm3hash(xU, this.Z, entity.Z, RA.getXCoord().toBigInteger().toByteArray(),
                            RA.getYCoord().toBigInteger().toByteArray(), RB.getXCoord().toBigInteger().toByteArray(),
                            RB.getYCoord().toBigInteger().toByteArray()));

            return new TransportEntity(RA.getEncoded(false), sA,this.Z,keyPair.getPublicKey());
        }

        /**
         * 密钥确认最后一步
         *
         * @param entity 传输实体
         */
        public void keyExchange_4(TransportEntity entity) {
            byte[] xV = V.getXCoord().toBigInteger().toByteArray();
            byte[] yV = V.getYCoord().toBigInteger().toByteArray();
            ECPoint RA = curve.decodePoint(entity.R).normalize();
            byte[] s2= sm3hash(new byte[] { 0x03 }, yV,
                    sm3hash(xV, entity.Z, this.Z, RA.getXCoord().toBigInteger().toByteArray(),
                            RA.getYCoord().toBigInteger().toByteArray(), this.RA.getXCoord().toBigInteger().toByteArray(),
                            this.RA.getYCoord().toBigInteger().toByteArray()));
            if(Arrays.equals(entity.S, s2)) {
                System.out.println("A->B 密钥确认成功");
            } else {
                System.out.println("A->B 密钥确认失败");
            }
        }
    }

    // 以16进制打印字节数组
    public static void printHexString(byte[] b) {
        for (byte bb : b) {
            String hex = Integer.toHexString(bb & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase());
        }
        System.out.println();
    }

    private static byte[] toByteArray(int i) {
        byte[] byteArray = new byte[4];
        byteArray[0] = (byte) (i >>> 24);
        byteArray[1] = (byte) ((i & 0xFFFFFF) >>> 16);
        byteArray[2] = (byte) ((i & 0xFFFF) >>> 8);
        byteArray[3] = (byte) (i & 0xFF);
        return byteArray;
    }

    //公钥的字符串形式。04 || x的hex形式 || y的hex形式
    @Override
    public String encodePublicKey(ECPoint publicKey){
        if( publicKey == null ){
            return null;
        }
        BigInteger x = publicKey.getXCoord().toBigInteger();
        BigInteger y = publicKey.getYCoord().toBigInteger();

        return "04" + StringUtils.leftPad(x.toString(16),64,"0") + StringUtils.leftPad(y.toString(16),64,"0");
    }

    @Override
    public ECPoint decodePublicKey(String encodedKey){
        if(StringUtils.isBlank(encodedKey) || !encodedKey.startsWith("04") ) {
            return null;
        }

        encodedKey = encodedKey.substring(2);
        String xs = encodedKey.substring(0,encodedKey.length()/2);
        String ys = encodedKey.substring(encodedKey.length()/2);

        BigInteger px = new BigInteger(xs,16);
        BigInteger py = new BigInteger(ys,16);

        return curve.createPoint(px,py);
    }

    //私钥的字符串形式，直接hex编码
    @Override
    public String encodePrivateKey(BigInteger privateKey){
        return Hex.encode(privateKey.toByteArray());
    }

    @Override
    public BigInteger decodePrivateKey(String encodedKey){
        return new BigInteger(Hex.decode(encodedKey));
    }

    public static class Signature {
        BigInteger r;
        BigInteger s;

        public Signature(BigInteger r, BigInteger s) {
            this.r = r;
            this.s = s;
        }

        @Override
        public String toString() {
            return StringUtils.leftPad(r.toString(16),64,"0")
                    + StringUtils.leftPad(s.toString(16),64,"0");
        }

        public String toBase64(){
            String hex = StringUtils.leftPad(r.toString(16),64,"0")
                    + StringUtils.leftPad(s.toString(16),64,"0");
            byte[] bytes = Hex.decode(hex);
            return Base64.getEncoder().encodeToString(bytes);
        }

        //根据tostring的结果反向构造Signature
        public static Signature genByHex(String hex) {
            BigInteger  r = new BigInteger(hex.substring(0,64),16);
            BigInteger  s = new BigInteger(hex.substring(64,128),16);
            return new Signature(r,s);
        }

        //根据tostring的结果反向构造Signature
        public static Signature genByBase64(String base) {
            byte[] bs = Base64.getDecoder().decode(base);

            byte[] rs = new byte[bs.length/2];
            byte[] ss = new byte[bs.length/2];

            System.arraycopy(bs,0,rs,0,rs.length);
            System.arraycopy(bs,rs.length,ss,0,ss.length);

            BigInteger  r = new BigInteger(Hex.encode(rs),16);
            BigInteger  s = new BigInteger(Hex.encode(ss),16);
            return new Signature(r,s);
        }
    }
}
