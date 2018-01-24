package com.zhjl.tech.common.encrypt.symmetric;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SM4Test {

    public static void main(String[] args) throws IOException {
        /**
         *
         * 1 整数倍的byte数组
         *      sm4.sms4(in, inLen, Key, out, ENCRYPT);
         * 2 流式加解密
         *      sm4.encryptStream(inputStream,outputStream,Key);
         *      sm4.decryptStream(inputStream,outputStream,Key);
         */

        //获取加密对象
        SM4 sm4Impl = new SM4Impl();

        byte[] in={
                0x01,0x23,0x45,0x67,(byte) 0x89,(byte) 0xab,(byte) 0xcd,(byte) 0xef,
                (byte) 0xfe,(byte) 0xdc,(byte) 0xba,(byte) 0x98,0x76,0x54,0x32,0x10
        };
        byte[] in1={
                0x01,0x23,0x45,0x67,(byte) 0x89,(byte) 0xab,(byte) 0xcd,(byte) 0xef,
                (byte) 0xfe,(byte) 0xdc,(byte) 0xba,(byte) 0x98,0x76,0x54,0x32,0x10,
                0x01,0x23,0x45,0x67,(byte) 0x89,(byte) 0xab,(byte) 0xcd,(byte) 0xef,
                (byte) 0xfe,(byte) 0xdc,(byte) 0xba,(byte) 0x98,0x76,0x54,0x32,0x10
        };

        byte[] key={
                0x01,0x23,0x45,0x67,(byte) 0x89,(byte) 0xab,(byte) 0xcd,(byte) 0xef,
                (byte) 0xfe,(byte) 0xdc,(byte) 0xba,(byte) 0x98,0x76,0x54,0x32,0x10
        };

        int inLen=16,ENCRYPT=1,DECRYPT=0,inlen1=32;
        byte[] out=new byte[16];
        byte[] out1=new byte[32];
        long starttime;

        //加密 128bit
        starttime=System.nanoTime();
        sm4Impl.sms4(in, inLen, key, out, ENCRYPT);
        pl("加密1个分组执行时间： "+(System.nanoTime()-starttime)+"ns");
        pl("明文：");
        printBytesToInt(in);
        pl("加密结果：");
        printBytesToInt(out);

        //解密 128bit
        sm4Impl.sms4(out, inLen, key, in, DECRYPT);
        pl("解密结果：");
        printBytesToInt(in);

        pl("=======================================");

        //加密多个分组
        System.out.println();
        sm4Impl.sms4(in1, inlen1, key, out1, ENCRYPT);
        System.out.println("\r多分组加密结果：");
        printBytesToInt(out1);
        //解密多个分组
        System.out.println();
        sm4Impl.sms4(out1, inlen1, key, in1, DECRYPT);
        System.out.println("多分组解密结果：");
        printBytesToInt(in1);

        //1,000,000次加密
        System.out.println();
        starttime=System.currentTimeMillis();
        for(int i=1;i<1000000;i++)
        {
            sm4Impl.sms4(in, inLen, key, out, ENCRYPT);
            in=out;
        }
        sm4Impl.sms4(in, inLen, key, out, ENCRYPT);
        System.out.println("\r1000000次加密执行时间： "+(System.currentTimeMillis()-starttime)+"ms");
        System.out.println("加密结果：");
        for(int i=0;i<16;i++) {
            System.out.print(Integer.toHexString(out[i] & 0xff) + "\t");
        }

        pl("\n======================================流式加密，整数倍的情况");
        in=new byte[]{
                0x01,0x23,0x45,0x67,(byte) 0x89,(byte) 0xab,(byte) 0xcd,(byte) 0xef,
                (byte) 0xfe,(byte) 0xdc,(byte) 0xba,(byte) 0x98,0x76,0x54,0x32,0x10
        };

        pl("明文：");
        printBytesToInt(in);
        InputStream inputStream = new ByteArrayInputStream(in);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(200);

        sm4Impl.encryptStream(inputStream,outputStream,key);
        pl("密文：");
        byte[] b_encrypt = outputStream.toByteArray();
        printBytesToInt(b_encrypt);

        //解密
        inputStream = new ByteArrayInputStream(b_encrypt);
        outputStream = new ByteArrayOutputStream(200);
        sm4Impl.decryptStream(inputStream,outputStream,key);
        pl("解密后的明文：");
        printBytesToInt(outputStream.toByteArray());

        pl("\n======================================流式加密，非整数倍的情况");
        in=new byte[]{
                0x01,0x23,0x45,0x67,(byte) 0x89,(byte) 0xab,(byte) 0xcd,(byte) 0xef,
                (byte) 0xfe,(byte) 0xdc,(byte) 0xba,(byte) 0x98,0x76,0x54,0x32,0x10,
                0x58,0x45,0x45,0x45,0x45,0x45,0x45
        };

        pl("明文：");
        printBytesToInt(in);
        inputStream = new ByteArrayInputStream(in);
        outputStream = new ByteArrayOutputStream(200);

        sm4Impl.encryptStream(inputStream,outputStream,key);
        pl("密文：");
        b_encrypt = outputStream.toByteArray();
        printBytesToInt(b_encrypt);

        //解密
        inputStream = new ByteArrayInputStream(b_encrypt);
        outputStream = new ByteArrayOutputStream(200);
        sm4Impl.decryptStream(inputStream,outputStream,key);
        pl("解密后的明文：");
        printBytesToInt(outputStream.toByteArray());
    }

    private static void printBytesToInt(byte[] in){
        for(int i =0; i < in.length;i++){
            System.out.print(Integer.toHexString(in[i]&0xff)+"\t");
            if( (i+1) %8 == 0 ){
                System.out.println();
            }
        }
    }

    private static void p(String s){
        System.out.print(s);
    }

    private static void pl(String s){
        System.out.println(s);
    }
}
