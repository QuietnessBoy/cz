package com.zhjl.tech.common.encrypt.digest;

import java.io.IOException;

/**
 * sm3相关接口
 */
public interface SM3 {

    //直接获取此byte数组的摘要
    byte[] getHash(byte[] input);

    //对文件实现摘要
    byte[] getFileHash(String path) throws IOException ;

    //依次更新byte，然后采用digest方法获得最终摘要
    void update(byte b[]);
    void update(byte b[], int off, int len);
    void update(byte b);
    byte[] digest();
}
