package com.zhjl.tech.common.encrypt.bytetool;

import java.io.UnsupportedEncodingException;

public class Hex {
    final private static char[] hexArray = "0123456789abcdef".toCharArray();
    final private static String hexString = "0123456789abcdef";

    /**
     * byte数组转字符串
     * @param bytes
     * @return 转换后的hex字符串，均是小写的
     */
    public static String encode(byte[] bytes) {
        if (bytes.length == 0) {
            return "";
        }
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars).toLowerCase();
    }

    /**
     * hex字符串转换为Byte值。长度必须偶数。
     * @param src String Byte字符串，每个Byte之间没有分隔符(字符范围:0-9 A-F)
     * @return byte[]
     */
    public static byte[] decode(String src){
        /*对输入值进行规范化整理*/
        src = src.trim().replace(" ", "").toLowerCase();
        byte[] rs = new byte[src.length()/2]; //分配存储空间

        for (int i = 0; i < src.length()/2; i++){
            rs[i] = (byte)(hexString.indexOf(src.substring(i*2,i*2+1))*16
                    + hexString.indexOf(src.substring(i*2+1,i*2+2)) );
        }
        return rs;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        String src1 = "0ABD234BDAFD".toLowerCase();
        byte[] rs = decode(src1);
        String src2 = encode(rs);
        System.out.println( src2 );
        System.out.println( src2.equals(src1) );
        System.out.println( new String(decode("697420697320612066696e652064617920746f64792e20776861742073686f756c6420796f7520646f206e6f773f2068692e206765742061776179"),"ascii"));
    }
}
