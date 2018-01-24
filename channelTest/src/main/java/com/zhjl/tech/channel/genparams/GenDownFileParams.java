package com.zhjl.tech.channel.genparams;

import com.zhjl.tech.common.dto.interfaces.DownloadFileDto;
import com.zhjl.tech.common.utils.gen.downfile.GenDownLoadData;
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
public class GenDownFileParams {

    public String genPostmanMode(String ordersn, String channelId) throws IOException {
        DownloadFileDto downloadFileDto = genData(ordersn,channelId);  //生成参数信息
        if(downloadFileDto ==null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb = sb.append("</br>")
                .append("signType:").append(downloadFileDto.getSignType()).append("</br>")
                .append("sign:").append(downloadFileDto.getSign()).append("</br>")
                .append("accessKey:").append(downloadFileDto.getAccessKey()).append("</br>")
                .append("random:").append(downloadFileDto.getRandom()).append("</br>")
                .append("channelId:").append(downloadFileDto.getChannelId()).append("</br>")
                .append("ordersn:").append(ordersn)
                .append("</br>");
        log.info("@@@@@@@@@@@@@@{}",sb);
        return sb.toString();
    }


    public String genUrlencodeMode(String ordersn, String channelId) throws IOException {
        DownloadFileDto downloadFileDto = genData(ordersn,channelId);
        if(downloadFileDto ==null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb = sb.append("signType=").append(URLEncoder.encode(downloadFileDto.getSignType(),"UTF-8"))
                .append("&sign=").append(URLEncoder.encode(downloadFileDto.getSign(),"UTF-8"))
                .append("&accessKey=").append(URLEncoder.encode(downloadFileDto.getAccessKey(),"UTF-8"))
                .append("&random=").append(URLEncoder.encode(downloadFileDto.getRandom(),"UTF-8"))
                .append("&channelId=").append(URLEncoder.encode(downloadFileDto.getChannelId(),"UTF-8"))
                .append("&ordersn=").append(URLEncoder.encode(downloadFileDto.getOrdersn(),"UTF-8"));
        log.info("@@@@@@@@@@@@@@{}",sb);
        return sb.toString();
    }

    /**
     * 自动生成数据
     * @return
     * @throws IOException
     */
    public DownloadFileDto genData(String ordersn, String channelId) throws IOException {

        DownloadFileDto downloadFileDto = genDownLoadData.gen(ordersn,channelId);
        return downloadFileDto;
    }

    @Resource
    private GenDownLoadData genDownLoadData;
}







