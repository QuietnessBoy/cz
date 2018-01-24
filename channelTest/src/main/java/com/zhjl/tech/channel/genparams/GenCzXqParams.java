package com.zhjl.tech.channel.genparams;

import com.zhjl.tech.channel.model.channel.AttestChannelTest;
import com.zhjl.tech.channel.service.channel.IAttestChannelTestService;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.interfaces.AttestXqDto;
import com.zhjl.tech.common.utils.gen.xqcz.GenXqData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
@Slf4j
public class GenCzXqParams {

    @Transactional
    public String genUrlencodeMode(String path){
        AttestXqDto attestXqDto = genData(path);  //生成参数信息

        StringBuilder sb = new StringBuilder();
        try {
            sb = sb.append("signType=").append(URLEncoder.encode(attestXqDto.getSignType(), "UTF-8"))
                    .append("&sign=").append(URLEncoder.encode(attestXqDto.getSign(), "UTF-8"))
                    .append("&accessKey=").append(URLEncoder.encode(attestXqDto.getAccessKey(), "UTF-8"))
                    .append("&random=").append(URLEncoder.encode(attestXqDto.getRandom(), "UTF-8"))
                    .append("&bizSign=").append(URLEncoder.encode(attestXqDto.getBizSign(), "UTF-8"))
                    .append("&requestTime=").append(URLEncoder.encode(attestXqDto.getRequestTime(), "UTF-8"))
                    .append("&channelId=").append(URLEncoder.encode(attestXqDto.getChannelId(), "UTF-8"))
                    .append("&channelUserid=").append(URLEncoder.encode(attestXqDto.getChannelUserid(), "UTF-8"))
                    .append("&channelOrdersn=").append(URLEncoder.encode(attestXqDto.getChannelOrdersn(), "UTF-8"))
                    .append("&ordersn=").append(URLEncoder.encode(attestXqDto.getOrdersn(), "UTF-8"))
                    .append("&chained=").append(URLEncoder.encode(attestXqDto.getChained(), "UTF-8"))
                    .append("&bizType=").append(URLEncoder.encode(attestXqDto.getBizType(), "UTF-8"))
                    .append("&fileName=").append(URLEncoder.encode(attestXqDto.getFileName(), "UTF-8"))
                    .append("&fileType=").append(URLEncoder.encode(attestXqDto.getFileType(), "UTF-8"))
                    .append("&fileSize=").append(URLEncoder.encode(attestXqDto.getFileSize(), "UTF-8"))
                    .append("&fileHash=").append(URLEncoder.encode(attestXqDto.getFileHash(), "UTF-8"))
                    .append("&ownerType=").append(URLEncoder.encode(attestXqDto.getOwnerType(), "UTF-8"))
                    .append("&ownerId=").append(URLEncoder.encode(attestXqDto.getOwnerId(), "UTF-8"))
                    .append("&ownerName=").append(URLEncoder.encode(attestXqDto.getOwnerName(), "UTF-8"))
                    .append("&agentName=").append(URLEncoder.encode(attestXqDto.getAgentName(), "UTF-8"))
                    .append("&agentPhone=").append(URLEncoder.encode(attestXqDto.getAgentPhone(), "UTF-8"))
                    .append("&agentEmail=").append(URLEncoder.encode(attestXqDto.getAgentEmail(), "UTF-8"))
                    .append("&duration=").append(URLEncoder.encode(attestXqDto.getDuration(), "UTF-8"))
                    .append("&description=").append(URLEncoder.encode(attestXqDto.getDescription(), "UTF-8"))
                    .append("&price=").append(URLEncoder.encode(attestXqDto.getPrice(), "UTF-8"))
                    .append("&startTime=").append(URLEncoder.encode(attestXqDto.getStartTime(), "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("生成存证续期请求参数失败.",e);
        }
        log.info("@@@@@@@@@@@@@@{}", sb);
        return sb.toString();
    }


    @Transactional
    public String genPostmanMode(String ordersn){

        AttestXqDto attestXqDto = genData(ordersn);  //生成参数信息
        if (attestXqDto == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb = sb.append("</br>")
                .append("signType:").append(attestXqDto.getSignType()).append("</br>")
                .append("sign:").append(attestXqDto.getSign()).append("</br>")
                .append("accessKey:").append(attestXqDto.getAccessKey()).append("</br>")
                .append("random:").append(attestXqDto.getRandom()).append("</br>")
                .append("bizSign:").append(attestXqDto.getBizSign()).append("</br>")
                .append("requestTime:").append(attestXqDto.getRequestTime()).append("</br>")
                .append("channelId:").append(attestXqDto.getChannelId()).append("</br>")
                .append("channelUserid:").append(attestXqDto.getChannelUserid()).append("</br>")
                .append("channelOrdersn:").append(attestXqDto.getChannelOrdersn()).append("</br>")
                .append("ordersn:").append(ordersn).append("</br>")
                .append("chained:").append(attestXqDto.getChained()).append("</br>")
                .append("bizType:").append(attestXqDto.getBizType()).append("</br>")
                .append("fileName:").append(attestXqDto.getFileName()).append("</br>")
                .append("fileType:").append(attestXqDto.getFileType()).append("</br>")
                .append("fileSize:").append(attestXqDto.getFileSize()).append("</br>")
                .append("fileHash:").append(attestXqDto.getFileHash()).append("</br>")
                .append("ownerType:").append(attestXqDto.getOwnerType()).append("</br>")
                .append("ownerId:").append(attestXqDto.getOwnerId()).append("</br>")
                .append("ownerName:").append(attestXqDto.getOwnerName()).append("</br>")
                .append("agentName:").append(attestXqDto.getAgentName()).append("</br>")
                .append("agentPhone:").append(attestXqDto.getAgentPhone()).append("</br>")
                .append("agentEmail:").append(attestXqDto.getAgentEmail()).append("</br>")
                .append("duration:").append(attestXqDto.getDuration()).append("</br>")
                .append("description:").append(attestXqDto.getDescription()).append("</br>")
                .append("price:").append(attestXqDto.getPrice()).append("</br>")
                .append("startTime:").append(attestXqDto.getStartTime()).append("</br>");
        log.info("@@@@@@@@@@@@@@{}", sb);
        return sb.toString();
    }


    /**
     * 自动生成数据
     *
     * @return
     * @throws IOException
     */
    public AttestXqDto genData(String ordersn){
        AttestChannelTest attestChannelTest = attestChannelTestService.getAttestChannelTestByOrdersn(ordersn);
        if (attestChannelTest == null) {
            return null;
        }
        AttestXqDto attestXqDto = new AttestXqDto();
        BeanUtils.copyProperties(attestChannelTest, attestXqDto);

        String startTime =new SimpleDateFormat(SysConfig.Format).format(attestChannelTest.getStartTime());
        attestXqDto = genXqData.gen(attestXqDto,ordersn,startTime);

        AttestChannelTest attestChannelTest1 = new AttestChannelTest();
        BeanUtils.copyProperties(attestXqDto, attestChannelTest1);
        attestChannelTestService.insertSelective(attestChannelTest1);
        return attestXqDto;
    }


    @Resource
    private IAttestChannelTestService attestChannelTestService;

    @Resource
    private GenXqData genXqData;

}
