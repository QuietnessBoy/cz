package com.zhjl.tech.channel.tool.oss;

import lombok.extern.slf4j.Slf4j;

import java.io.File;

/**
 * @author wind
 */
@Slf4j
public class FileTool {

    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     */
    public static long getFileLength(String fileName) {
        File file = new File(fileName);
        long length = file.length();
        return length;
    }
}
