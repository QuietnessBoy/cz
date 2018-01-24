package com.zhjl.tech.common.zjlsign.hashcz;

import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.encrypt.asymmetric.SM2KeyPair;
import com.zhjl.tech.common.zjlsign.filecz.FileCzAttestSign;
import lombok.extern.slf4j.Slf4j;


@Slf4j
public class HashCzAttestSign {

    public static String genSign(SM2KeyPair sm2KeyPair, String spublicKey, String channelIDA, AttestDto attestDto){
        return FileCzAttestSign.gen(sm2KeyPair, spublicKey, channelIDA, attestDto);
    }
}
