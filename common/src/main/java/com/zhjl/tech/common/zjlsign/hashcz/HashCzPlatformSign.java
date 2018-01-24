package com.zhjl.tech.common.zjlsign.hashcz;

import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.zjlsign.filecz.FileCzPlatformSign;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HashCzPlatformSign {

    /**
     * 生成文件存证platformSign
     *
     * @param attestDto
     */
    public static String gen(AttestDto attestDto, String sPubKey, String sPriKey) {
        return FileCzPlatformSign.gen(attestDto, sPubKey, sPriKey);
    }
}
