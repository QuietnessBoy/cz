package com.zhjl.tech.common.encrypt.digest;

import lombok.extern.log4j.Log4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wind on 2016/12/28.
 * md5和sha256常用接口。
 */
@Log4j
public class Hash {

    final private static char[] hexArray = "0123456789ABCDEF".toCharArray();
    private static String md5 = "MD5";
    private static String sha256 = "SHA-256";

    //bytetool to hex
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    //对字节进行md5摘要
    public static byte[] getHashMD5(byte[] strDate) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(md5);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            log.error("exception ", e);
        }
        md.update(strDate, 0, strDate.length);
        return md.digest();
    }


    //对字节进行md5摘要
    public static byte[] getHashSHA256(byte[] strDate){
        MessageDigest md;
        try {
            md = MessageDigest.getInstance(sha256);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("cn.zjl.tech.encrypt.digest.Hash , 找不到sha256摘要算法。");
        }
        md.update(strDate, 0, strDate.length);
        return md.digest();
    }

    //对文件实现摘要
    public static byte[] getFileHash(String path) throws OutOfMemoryError, IOException, NoSuchAlgorithmException {
        long start = System.currentTimeMillis();
        File file = new File(path);
        FileInputStream in = new FileInputStream(file);
        MessageDigest messagedigest;
        try {
            messagedigest = MessageDigest.getInstance(sha256);

            //分配10m内存
            byte[] buffer = new byte[1024 * 1024 * 10];
            int len;

            while ((len = in.read(buffer)) > 0) {
                messagedigest.update(buffer, 0, len);
            }

            long total = System.currentTimeMillis() - start;
            log.info("time ellapse : " + total);
            return messagedigest.digest();
        } finally {
            in.close();
        }
    }
}
