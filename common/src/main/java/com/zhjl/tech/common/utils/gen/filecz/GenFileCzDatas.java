package com.zhjl.tech.common.utils.gen.filecz;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.encrypt.digest.SM3;
import com.zhjl.tech.common.encrypt.digest.SM3Impl;
import com.zhjl.tech.common.utils.OssTool;
import com.zhjl.tech.common.utils.gen.FileTool;
import com.zhjl.tech.common.zjlsign.filecz.FileCzBizSign;
import com.zhjl.tech.common.zjlsign.filecz.FileCzRequestSign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

@Service
@Slf4j
public class GenFileCzDatas {

    public AttestDto gen(String path,String userid,String testName){

        AttestDto attestDto = new AttestDto();

        attestDto.setAccessKey(comBizConfig.getAccessKey());
        attestDto.setRandom( UUID.randomUUID().toString().replaceAll("-",""));
        attestDto.setChained("1");

        //requestTime
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(SysConfig.Format);
        String s = sdf.format(date);
        attestDto.setRequestTime(s);

        //channelId
        attestDto.setChannelId(comBizConfig.getChannelId());

        //channelUserid
        attestDto.setChannelUserid(userid);

        //channelOrdersn
        //String channelOrdersn = "ordersn" + UUID.randomUUID().toString().replaceAll("-","").substring(0,9);
        String channelOrdersn = testName + UUID.randomUUID().toString().replaceAll("-","").substring(0,9);
        attestDto.setChannelOrdersn(channelOrdersn);

        //BizType
        attestDto.setBizType(String.valueOf(rd.nextInt(6) + 1));

        //fileName,此处与ordersn一样
        attestDto.setFileName("测试-" + channelOrdersn);
        //fileType
        attestDto.setFileType("."+UUID.randomUUID().toString().replaceAll("-","").substring(0,3));

        // fileSize
        long a = FileTool.getFileLength(path);
        attestDto.setFileSize(String.valueOf(a));

        SM3 sm3 = new SM3Impl();
        try {
            attestDto.setFileHash(Base64.getEncoder().encodeToString(sm3.getFileHash(path)));
        } catch (IOException e) {
            log.error("获取文件Hash值失败.",e);
        }

        //ownerType
        attestDto.setOwnerType(String.valueOf(rd.nextInt(2) + 1));

        //ownerId
        attestDto.setOwnerId( "ownerid"+ String.valueOf(rd.nextInt(11) + 1));

        //ownerName
        attestDto.setOwnerName("存证人"+ (UUID.randomUUID().toString()).replaceAll("-","").substring(0,4));

        //agentName
        attestDto.setAgentName("agent"+(UUID.randomUUID().toString()).replaceAll("-", "").substring(0, 5));

        //agentPhone
        attestDto.setAgentPhone(String.valueOf(rd.nextInt(9999999) + 1));

        //agentEmail
        String n = (UUID.randomUUID().toString()).replaceAll("-", "").substring(0, 7);
        String addr = (UUID.randomUUID().toString()).replaceAll("-", "").substring(0, 4);
        attestDto.setAgentEmail(n.concat(addr).concat("@").concat(".com"));

        //duration
        attestDto.setDuration(String.valueOf(rd.nextInt(5) + 1));

        //description

        StringBuilder sb = new StringBuilder();
        attestDto.setDescription(sb.append("描述:").append(JSONObject.toJSONString(attestDto)).substring(0,150));

        //price
        attestDto.setPrice(comBizConfig.getPrice());

        //上传OSS获取下载地址
        String ossFileAddr = ossTool.upload(attestDto.getFileName(),path);
        attestDto.setFileAddr(ossFileAddr);
//        attestDto.setFileAddr("987654");

        //生成BizSign
        String bizSign = null;
        try {
            bizSign = FileCzBizSign.gen(attestDto,comBizConfig.getChannelPublickKey(),comBizConfig.getChannelPrivateKey());
        } catch (UnsupportedEncodingException e) {
            log.error("生成bizSign错误.",e);
        }
        attestDto.setBizSign(bizSign);

        //生成sign/signType
        attestDto.setSignType(comBizConfig.getSignType());
        String sign = FileCzRequestSign.gen(attestDto,comBizConfig.getChannelPublickKey());
        attestDto.setSign(sign);

        return attestDto;
    }

    @Resource
    private ComBizConfig comBizConfig;

    @Resource
    private OssTool ossTool;

    static Random rd = new Random();
}
