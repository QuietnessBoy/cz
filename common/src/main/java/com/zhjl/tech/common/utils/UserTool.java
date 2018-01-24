package com.zhjl.tech.common.utils;

import com.zhjl.tech.common.encrypt.bytetool.Hex;
import com.zhjl.tech.common.encrypt.digest.SM3;
import com.zhjl.tech.common.encrypt.bytetool.Base58;
import com.zhjl.tech.common.encrypt.digest.SM3Impl;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 终端用户相关工具.根
 */
public class UserTool {
    /**
     * 根据publickey计算用户钱包地址。
     * 将公钥直接sm3，然后base58编码。
     * @param publickey。形如 dO6GCEbBYyCFmu3QdLMFexxy0dybSjH2Gq9IqB57xrs=@hOYbt3GbR0fGSWxxH65FCDcAAF2hIpmY1dfqRw2tj0s=
     *  @ 前为公钥的px的base64形式，后为py
     * @return
     */
    public static String getAddress(String publickey){
        byte[] pub = Hex.decode(publickey);
        SM3 sm3Impl = new SM3Impl();
        byte[] input1 = sm3Impl.getHash(sm3Impl.getHash(pub));
        return Base58.encodeCheck(input1);
    }
}
