package com.zhjl.tech.common.encrypt.symmetric;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * SM4 使用接口
 */
public interface SM4 {

    /**
     * 流加密。采用PKCS #7进行填充
     *
     * @param inputStream
     * @param outputStream
     * @param key
     */
    void encryptStream(InputStream inputStream, OutputStream outputStream, byte[] key) throws IOException;


    /**
     * 流解密
     *
     * @param inputStream  解密的时候长度必定为16的倍数
     * @param outputStream
     * @param key
     */
    void decryptStream(InputStream inputStream, OutputStream outputStream, byte[] key)throws IOException;

    /**
     * 字节数组加密，没考虑填充，所以输入必须是128bit的整数倍。
     *
     * @param in        明文输入
     * @param inLen     128n
     * @param key       长度16
     * @param out       输出
     * @param CryptFlag 加密还是解密标识,SM4.ENCRYPT, SM4.DECRYPT
     * @return
     */
    int sms4(byte[] in, int inLen, byte[] key, byte[] out, int CryptFlag);


}
