package com.zhjl.tech.common.utils.gen.downfile;

import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.dto.interfaces.DownloadFileDto;
import com.zhjl.tech.common.zjlsign.query.QueryRequestSign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

@Service
@Slf4j
public class GenDownLoadData {

    public DownloadFileDto gen(String ordersn,String channelId){

        DownloadFileDto downloadFileDto = new DownloadFileDto();
        downloadFileDto.setSignType(comBizConfig.getSignType());
        downloadFileDto.setAccessKey(comBizConfig.getAccessKey());
        downloadFileDto.setRandom(UUID.randomUUID().toString().replaceAll("-","").substring(0,16));
        downloadFileDto.setChannelId(channelId);
        downloadFileDto.setOrdersn(ordersn);

        //生成sign
        String sign = QueryRequestSign.genDownLoadSign(downloadFileDto,comBizConfig.getChannelPublickKey());
        downloadFileDto.setSign(sign);

        return downloadFileDto;
    }

    @Resource
    private ComBizConfig comBizConfig;
}
