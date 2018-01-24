package com.zhjl.tech.channel.biz.request;

import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Service
@Slf4j
public class HashCz {
    @Resource
    private ComBizConfig comBizConfig;

    /**
     * Hash存证HTTP请求
     * @param attestDto
     * @return
     */
    public AttestDto hash(AttestDto attestDto){
        URI url = null;
        try {
            url = new URIBuilder(comBizConfig.getFileHashCzUrl()).
                    setParameter("signType", attestDto.getSignType()).
                    setParameter("sign", attestDto.getSign()).
                    setParameter("accessKey", attestDto.getAccessKey()).
                    setParameter("random", attestDto.getRandom()).

                    setParameter("requestTime", attestDto.getRequestTime()).
                    setParameter("channelId", attestDto.getChannelId()).
                    setParameter("channelOrdersn", attestDto.getChannelOrdersn()).
                    setParameter("channelUserid", attestDto.getChannelUserid()).

                    setParameter("chained", attestDto.getChained()).
                    setParameter("bizType", attestDto.getBizType()).
                    setParameter("bizSign", attestDto.getBizSign()).

                    setParameter("fileName", attestDto.getFileName()).
                    setParameter("fileType", attestDto.getFileType()).
                    setParameter("fileSize", attestDto.getFileSize()).
                    setParameter("fileHash", attestDto.getFileHash()).

                    setParameter("ownerType", attestDto.getOwnerType()).
                    setParameter("ownerId", attestDto.getOwnerId()).
                    setParameter("ownerName", attestDto.getOwnerName()).

                    setParameter("agentName", attestDto.getAgentName()).
                    setParameter("agentPhone", attestDto.getAgentPhone()).
                    setParameter("agentEmail", attestDto.getAgentEmail()).

                    setParameter("duration", attestDto.getDuration()).
                    setParameter("description", attestDto.getDescription()).
                    setParameter("price", attestDto.getPrice()).
                    build();

            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost request = new HttpPost(url);
            HttpResponse response = httpclient.execute(request);
            /**请求发送成功，并得到响应**/
            System.out.println("状态码："+response.getStatusLine().getStatusCode());
            if (response.getStatusLine().getStatusCode() == 200) {
                /**读取服务器返回数据**/
                String strResult = EntityUtils.toString(response.getEntity(),"utf-8");
                System.out.println(" 响应的内容 : "+ strResult);
                log.info("文件Hash存证成功.{}", comBizConfig.getFileHashCzUrl());
            } else {
                log.info("文件Hash存证失败.{},{}",response.getStatusLine().getStatusCode(),comBizConfig.getFileHashCzUrl());
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
            log.warn("Hash文件数据传输失败.",e);
        } catch (IOException e) {
            e.printStackTrace();
            log.warn("Hash文件数据接收失败.",e);
        }
        return attestDto;
    }
}
