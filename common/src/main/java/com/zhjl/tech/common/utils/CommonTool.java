package com.zhjl.tech.common.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class CommonTool {

    /**
     * 根据 request获取调用端ip
     * @param request
     * @return
     */
    public static String getIp(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        String forwarded = request.getHeader("X-Forwarded-For");
        String realIp = request.getHeader("X-Real-IP");

        String ip = null;
        if (realIp == null) {
            if (forwarded == null) {
                ip = remoteAddr;
            } else {
                ip = remoteAddr + "/" + forwarded;
            }
        } else {
            if (realIp.equals(forwarded)) {
                ip = realIp;
            } else {
                ip = realIp + "/" + forwarded.replaceAll(", " + realIp, "");
            }
        }
        return ip;
    }

    /**
     * 生成指定长度的随机字符串
     * @param length  1-32
     * @return
     */
    public static String genRandomString(int length){
        return (UUID.randomUUID().toString()).replaceAll("-","").substring(0,length);
    }
}
