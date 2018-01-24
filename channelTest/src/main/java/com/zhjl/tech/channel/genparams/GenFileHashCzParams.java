package com.zhjl.tech.channel.genparams;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.channel.model.channel.AttestChannelTest;
import com.zhjl.tech.channel.service.channel.IAttestChannelTestService;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.utils.gen.filehashcz.GenFileHashCzDatas;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.UUID;

/**
 * @author wind
 */
@Service
@Slf4j
public class GenFileHashCzParams {


    @Transactional
    public String genUrlencodeMode(String path) throws IOException {
        String userid = UUID.randomUUID().toString().replaceAll("-","").substring(0,3);
        AttestDto attestDto = genData(path,userid);  //生成参数信息
        if(attestDto ==null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb = sb.append("signType=").append(URLEncoder.encode(attestDto.getSignType(),"UTF-8"))
               .append("&sign=").append(URLEncoder.encode(attestDto.getSign(),"UTF-8"))
               .append("&accessKey=").append(URLEncoder.encode(attestDto.getAccessKey(),"UTF-8"))
               .append("&random=").append(URLEncoder.encode(attestDto.getRandom(),"UTF-8"))
               .append("&bizSign=").append(URLEncoder.encode(attestDto.getBizSign(),"UTF-8"))
               .append("&requestTime=").append(URLEncoder.encode(attestDto.getRequestTime(),"UTF-8"))
               .append("&channelId=").append(URLEncoder.encode(attestDto.getChannelId(),"UTF-8"))
               .append("&channelUserid=").append(URLEncoder.encode(attestDto.getChannelUserid(),"UTF-8"))
               .append("&channelOrdersn=").append(URLEncoder.encode(attestDto.getChannelOrdersn(),"UTF-8"))
               .append("&chained=").append(URLEncoder.encode(attestDto.getChained(),"UTF-8"))
               .append("&bizType=").append(URLEncoder.encode(attestDto.getBizType(),"UTF-8"))
               .append("&fileName=").append(URLEncoder.encode(attestDto.getFileName(),"UTF-8"))
               .append("&fileType=").append(URLEncoder.encode(attestDto.getFileType(),"UTF-8"))
               .append("&fileSize=").append(URLEncoder.encode(attestDto.getFileSize(),"UTF-8"))
               .append("&fileHash=").append(URLEncoder.encode(attestDto.getFileHash(),"UTF-8"))
               .append("&ownerType=").append(URLEncoder.encode(attestDto.getOwnerType(),"UTF-8"))
               .append("&ownerId=").append(URLEncoder.encode(attestDto.getOwnerId(),"UTF-8"))
               .append("&ownerName=").append(URLEncoder.encode(attestDto.getOwnerName(),"UTF-8"))
               .append("&agentName=").append(URLEncoder.encode(attestDto.getAgentName(),"UTF-8"))
               .append("&agentPhone=").append(URLEncoder.encode(attestDto.getAgentPhone(),"UTF-8"))
               .append("&agentEmail=").append(URLEncoder.encode(attestDto.getAgentEmail(),"UTF-8"))
               .append("&duration=").append(URLEncoder.encode(attestDto.getDuration(),"UTF-8"))
               .append("&description=").append(URLEncoder.encode(attestDto.getDescription(),"UTF-8"))
               .append("&price=").append(URLEncoder.encode(attestDto.getPrice(),"UTF-8"));
        log.info("@@@@@@@@@@@@@@{}",sb);
        return sb.toString();
    }


    @Transactional
    public String genPostmanMode(String path) throws IOException {
        String userid = UUID.randomUUID().toString().replaceAll("-","").substring(0,3);
        AttestDto attestDto = genData(path,userid);  //生成参数信息
        if(attestDto ==null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb = sb.append("</br>")
                .append("signType:").append(attestDto.getSignType()).append("</br>")
                .append("sign:").append(attestDto.getSign()).append("</br>")
                .append("accessKey:").append(attestDto.getAccessKey()).append("</br>")
                .append("random:").append(attestDto.getRandom()).append("</br>")
                .append("bizSign:").append(attestDto.getBizSign()).append("</br>")
                .append("requestTime:").append(attestDto.getRequestTime()).append("</br>")
                .append("channelId:").append(attestDto.getChannelId()).append("</br>")
                .append("channelUserid:").append(attestDto.getChannelUserid()).append("</br>")
                .append("channelOrdersn:").append(attestDto.getChannelOrdersn()).append("</br>")
                .append("chained:").append(attestDto.getChained()).append("</br>")
                .append("bizType:").append(attestDto.getBizType()).append("</br>")
                .append("fileName:").append(attestDto.getFileName()).append("</br>")
                .append("fileType:").append(attestDto.getFileType()).append("</br>")
                .append("fileSize:").append(attestDto.getFileSize()).append("</br>")
                .append("fileHash:").append(attestDto.getFileHash()).append("</br>")
                .append("ownerType:").append(attestDto.getOwnerType()).append("</br>")
                .append("ownerId:").append(attestDto.getOwnerId()).append("</br>")
                .append("ownerName:").append(attestDto.getOwnerName()).append("</br>")
                .append("agentName:").append(attestDto.getAgentName()).append("</br>")
                .append("agentPhone:").append(attestDto.getAgentPhone()).append("</br>")
                .append("agentEmail:").append(attestDto.getAgentEmail()).append("</br>")
                .append("duration:").append(attestDto.getDuration()).append("</br>")
                .append("description:").append(attestDto.getDescription()).append("</br>")
                .append("price:").append(attestDto.getPrice())
                .append("</br>");
        return sb.toString();
    }

    /**
     * 自动生成数据
     * @param path 临时文件保存目录
     * @param userid userId,用户标识
     * @return
     * @throws IOException
     */
    public AttestDto genData(String path,String userid) throws IOException {
        AttestDto attestDto = genFileHashCzDatas.gen(path,userid);

        AttestChannelTest attestChannelTest = new AttestChannelTest();
        BeanUtils.copyProperties(attestDto, attestChannelTest);
        attestChannelTest.setState("创建文件Hash存证订单");
        attestChannelTestService.insertSelective(attestChannelTest);
        log.info("保存临时文件存证订单数据.{}", JSONObject.toJSONString(attestChannelTest));

        return attestDto;
    }

    @Resource
    private IAttestChannelTestService attestChannelTestService;

    @Resource
    private GenFileHashCzDatas genFileHashCzDatas;

}
