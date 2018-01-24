package com.zhjl.tech.channel.biz.what;

import com.zhjl.tech.channel.tool.Tool;
import com.zhjl.tech.common.dto.interfaces.DigitalCertificateDto;
import com.zhjl.tech.common.utils.gen.query.GenVerifyOrderData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URLEncoder;

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
public class GenVerifyData {

    @Resource
    Tool tool;

    public String genPostmanMode(String ownerId, String fileHash) throws IOException {
        DigitalCertificateDto digitalCertificateDto = genData(ownerId,fileHash);  //生成参数信息
        if(digitalCertificateDto ==null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb = sb.append("</br>")
                .append("signType:").append(digitalCertificateDto.getSignType()).append("</br>")
                .append("sign:").append(digitalCertificateDto.getSign()).append("</br>")
                .append("accessKey:").append(digitalCertificateDto.getAccessKey()).append("</br>")
                .append("random:").append(digitalCertificateDto.getRandom()).append("</br>")
                .append("channelId:").append(digitalCertificateDto.getChannelId()).append("</br>")
                .append("ownerId:").append(ownerId).append("</br>")
                .append("fileHash:").append(fileHash)
                .append("</br>");
        log.info("@@@@@@@@@@@@@@{}",sb);
        return sb.toString();
    }


    public String genUrlencodeMode(String ownerId, String fileHash) throws IOException {
        DigitalCertificateDto digitalCertificateDto = genData(ownerId,fileHash);  //生成参数信息
        if(digitalCertificateDto ==null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb = sb.append("signType=").append(URLEncoder.encode(digitalCertificateDto.getSignType(),"UTF-8"))
                .append("&sign=").append(URLEncoder.encode(digitalCertificateDto.getSign(),"UTF-8"))
                .append("&accessKey=").append(URLEncoder.encode(digitalCertificateDto.getAccessKey(),"UTF-8"))
                .append("&random=").append(URLEncoder.encode(digitalCertificateDto.getRandom(),"UTF-8"))
                .append("&channelId=").append(URLEncoder.encode(digitalCertificateDto.getChannelId(),"UTF-8"))
                .append("&ownerId=").append(URLEncoder.encode(digitalCertificateDto.getOwnerId(),"UTF-8"))
                .append("&fileHash=").append(URLEncoder.encode(digitalCertificateDto.getFileHash(),"UTF-8"));
        log.info("@@@@@@@@@@@@@@{}",sb);
        return sb.toString();
    }

    /**
     * 自动生成数据
     * @return
     * @throws IOException
     */
    public DigitalCertificateDto genData(String ownerId,String fileHash) throws IOException {

        DigitalCertificateDto digitalCertificateDto =genVerifyOrderData.gen(ownerId,fileHash);
        return digitalCertificateDto;
    }

    @Resource
    private GenVerifyOrderData genVerifyOrderData;
}
