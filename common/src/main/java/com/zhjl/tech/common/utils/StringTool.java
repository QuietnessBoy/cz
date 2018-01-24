package com.zhjl.tech.common.utils;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class StringTool {

    /**
     * 接收消息对象
     * @param object
     * @return
     */
    public static byte[] encodeObject(Object object){
       if( object == null ) {
           return null;
       }

       String json = JSON.toJSONString(object);
       byte[] rs = null;
        try {
            rs = json.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.warn("接收消息对象错误.{}",object);
        }
        return rs;
    }

    /**
     * 解析消息对象
     * @param bytes
     * @return
     */
    public static String getStringFromByte(byte[] bytes){

        String rs = null;
        try {
            rs = new String( bytes,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.warn("解析消息对象错误.",e);
        }
        return rs;
    }
}
