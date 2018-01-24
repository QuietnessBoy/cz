package com.zhjl.tech.common.utils.gen.filehashcz;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.encrypt.digest.SM3;
import com.zhjl.tech.common.encrypt.digest.SM3Impl;
import com.zhjl.tech.common.utils.gen.FileTool;
import com.zhjl.tech.common.zjlsign.filecz.FileCzBizSign;
import com.zhjl.tech.common.zjlsign.hashcz.HashCzRequestSign;
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
public class GenFileHashCzDatas {

    public AttestDto gen(String path,String userid){

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
        String channelOrdersn = "ordersn" + UUID.randomUUID().toString().replaceAll("-","").substring(0,9);
        attestDto.setChannelOrdersn(channelOrdersn);

        //BizType
        attestDto.setBizType(String.valueOf(rd.nextInt(6) + 1));

        //fileName,此处与ordersn一样
        attestDto.setFileName("测试-" + channelOrdersn);
        //fileType
        attestDto.setFileType("."+UUID.randomUUID().toString().replaceAll("-","").substring(0,3));

        long a = FileTool.getFileLength(path);
        attestDto.setFileSize(String.valueOf(a));

        //fileHash
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
        attestDto.setDescription(
                sb.append("描述:").
                        append(JSONObject.toJSONString(attestDto))
                        .substring(0,150)
        );

        //price
        attestDto.setPrice(comBizConfig.getPrice());

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
        String sign = HashCzRequestSign.gen(attestDto,comBizConfig.getChannelPublickKey());
        attestDto.setSign(sign);

        return attestDto;
    }


    public static void main(String[] args) {

        String s="\\\"";
        log.info("s={}",s);
        JsonResult jsonResult = new JsonResult(false, "生成urlencode格式参数完毕", "200", s);
        log.info("json.gets={}",jsonResult.getObj());
        log.info("last return={}", JSONObject.toJSONString(jsonResult));

        AttestDto attestDto = new AttestDto();
        attestDto.setAgentName("\\\"");

        String json=(JSONObject.toJSONString(attestDto));
        attestDto.setAgentPhone(json);


        System.out.println( "sbtest=" + new StringBuilder().append("\\").toString());


        System.out.println( "true=" + attestDto.getAgentName());
        System.out.println( json);
        System.out.println(JSON.parseObject(json,AttestDto.class).getAgentName());
        System.out.println(JSONObject.toJSONString(attestDto));
        System.out.println((JSONObject.toJSONString(attestDto)).toString());
    }

    @Resource
    private ComBizConfig comBizConfig;

    static Random rd = new Random();
}
