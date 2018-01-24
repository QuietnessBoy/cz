package com.zhjl.tech.channel.biz.what;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.channel.tool.Tool;
import com.zhjl.tech.channel.configs.Configs;
import com.zhjl.tech.channel.model.channel.AttestChannelTest;
import com.zhjl.tech.channel.service.channel.IAttestChannelTestService;
import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.zjlsign.filecz.FileCzBizSign;
import com.zhjl.tech.common.zjlsign.filecz.FileCzRequestSign;
import com.zhjl.tech.common.zjlsign.hashcz.HashCzRequestSign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

/**
 * ps:创建表的时候,可以考虑加上测试名称
 *
 * 根据本次测试的名称[testName]
 * 1 生成n条测试
 * 2 生成对应的文件
 * 3 将对应的数据插入到tables
 */

/**
 * 自动生成文件Hash存证请求参数
 */
@Slf4j
@Service
public class GenFileHashCzData {

    @Resource
    private IAttestChannelTestService attestChannelTestService;


    @Resource
    private ComBizConfig comBizConfig;

    public static void main(String[] args) {

        //生成请求时间
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(Configs.data_url);
        String s = sdf.format(date);
        System.out.append(s);
    }
    /**
     * 自动生成数据
     * @param userid 临时文件保存目录
     * @return
     * @throws IOException
     */
    public AttestDto gen(String userid,String testName) throws IOException {
        AttestDto attestDto = new AttestDto();
        Random rd = new Random();

        attestDto.setAccessKey(Configs.accessKey);
        attestDto.setRandom( UUID.randomUUID().toString().replaceAll("-",""));

        //requestTime
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(Configs.data_url);
        String s = sdf.format(date);
        attestDto.setRequestTime(s);

        //channelId
        attestDto.setChannelId(Configs.channelId);

        //channelUserid
        attestDto.setChannelUserid(userid);

        String channelOrdersn = testName + UUID.randomUUID().toString().replaceAll("-","").substring(0,9);
        attestDto.setChannelOrdersn(channelOrdersn);

        //chainedCallBack
        attestDto.setChained(String.valueOf(rd.nextInt(2) + 1));
        //BizType
        attestDto.setBizType(String.valueOf(rd.nextInt(6) + 1));

//        //fileName,此处与ordersn一样
        attestDto.setFileName("捡到一分钱" + channelOrdersn);
//        //fileType
        attestDto.setFileType("."+UUID.randomUUID().toString().replaceAll("-","").substring(0,3));
//        //filesize
        attestDto.setFileSize(""+JSON.toJSONString(attestDto).getBytes("utf-8").length);
        //fileHash
        String random = UUID.randomUUID().toString().replaceAll("-","");
        attestDto.setFileHash(random + UUID.randomUUID().toString().replaceAll("-","").substring(0,12));
        //ownerType
        attestDto.setOwnerType(String.valueOf(rd.nextInt(2) + 1));
        //ownerId
        attestDto.setOwnerId( "ownerid"+ String.valueOf(rd.nextInt(11) + 1));
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
        attestDto.setDescription("描述:"+JSON.toJSONString(attestDto).substring(0,150));

        //price
        attestDto.setPrice(Configs.price);

        //生成BizSign
        String bizSign = FileCzBizSign.gen(attestDto,Configs.channelPublickKey,Configs.channelPrivateKey);
        attestDto.setBizSign(bizSign);

        //生成sign/signType
        attestDto.setSignType(Configs.signType);
        String sign = HashCzRequestSign.gen(attestDto,Configs.channelPublickKey);
        attestDto.setSign(sign);

        AttestChannelTest attestChannelTest = new AttestChannelTest();
        BeanUtils.copyProperties(attestDto, attestChannelTest);
        attestChannelTest.setState("创建Hash存证订单");
        attestChannelTest.setTestName(testName);
        attestChannelTest.setUpdateTime(new Date());
        attestChannelTestService.insertSelective(attestChannelTest);
        log.info("保存临时Hash文件存证订单数据.{}", JSONObject.toJSONString(attestChannelTest));
        return attestDto;
    }
}
