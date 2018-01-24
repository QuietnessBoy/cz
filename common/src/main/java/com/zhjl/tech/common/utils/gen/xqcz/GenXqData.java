package com.zhjl.tech.common.utils.gen.xqcz;

import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.dto.interfaces.AttestXqDto;
import com.zhjl.tech.common.zjlsign.xqcz.XqCzBizSign;
import com.zhjl.tech.common.zjlsign.xqcz.XqCzRequestSign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Slf4j
@Service
public class GenXqData {

    public AttestXqDto gen(AttestXqDto attestXqDto,String ordersn,String startTime){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        attestXqDto.setRequestTime(simpleDateFormat.format(new Date()));
        attestXqDto.setOrdersn(ordersn);
        attestXqDto.setRandom(UUID.randomUUID().toString().replaceAll("-", ""));

        //channelOrdersn
        String channelOrdersn = "ordersn" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 9);
        attestXqDto.setChannelOrdersn(channelOrdersn);

        attestXqDto.setStartTime(startTime);
        //duration
        attestXqDto.setDuration(String.valueOf(rd.nextInt(5) + 1));

        //price
        attestXqDto.setPrice(comBizConfig.getPrice());

        //生成BizSign
        try {
            String bizSign = XqCzBizSign.gen(attestXqDto, comBizConfig.getChannelPublickKey(),comBizConfig.getChannelPrivateKey(), comBizConfig.getIda());
            attestXqDto.setBizSign(bizSign);
        } catch (UnsupportedEncodingException e) {
            log.error("生成续期请求订单签名bizSign错误.",e);
        }

        //生成sign/signType
        attestXqDto.setSignType(comBizConfig.getSignType());
        String sign = XqCzRequestSign.genSign(attestXqDto, comBizConfig.getChannelPublickKey());
        attestXqDto.setSign(sign);

        return attestXqDto;
    }


    private static Random rd = new Random();

    @Resource
    private ComBizConfig comBizConfig;
}
