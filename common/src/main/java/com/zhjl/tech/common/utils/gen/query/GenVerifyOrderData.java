package com.zhjl.tech.common.utils.gen.query;

import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.dto.interfaces.DigitalCertificateDto;
import com.zhjl.tech.common.zjlsign.query.QueryRequestSign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
@Slf4j
public class GenVerifyOrderData {

    public DigitalCertificateDto gen(String ownerId,String fileHash){
        DigitalCertificateDto digitalCertificateDto = new DigitalCertificateDto();
        digitalCertificateDto.setSignType(comBizConfig.getSignType());
        digitalCertificateDto.setAccessKey(comBizConfig.getAccessKey());
        digitalCertificateDto.setRandom(UUID.randomUUID().toString().replaceAll("-","").substring(0,16));
        digitalCertificateDto.setChannelId(comBizConfig.getChannelId());
        digitalCertificateDto.setOwnerId(ownerId);
        digitalCertificateDto.setFileHash(fileHash);
        //生成sign
        String sign = QueryRequestSign.genDigitalCertificateSign(digitalCertificateDto,comBizConfig.getChannelPublickKey());
        digitalCertificateDto.setSign(sign);

        return digitalCertificateDto;
    }


    @Resource
    private ComBizConfig comBizConfig;
}
