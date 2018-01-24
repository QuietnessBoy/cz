package com.zhjl.tech.channel.biz.request;

import com.zhjl.tech.channel.configs.Configs;
import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.dto.interfaces.AttestXqDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
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
public class CzXq {
    @Resource
    private ComBizConfig comBizConfig;

    public void xq(AttestXqDto attestXqDto){
        URI url = null;
        try {
            url = new URIBuilder(comBizConfig.getXqCzUrl()).
                    setParameter("signType", attestXqDto.getSignType()).
                    setParameter("sign", attestXqDto.getSign()).
                    setParameter("accessKey", attestXqDto.getAccessKey()).
                    setParameter("random", attestXqDto.getRandom()).

                    setParameter("requestTime", attestXqDto.getRequestTime()).
                    setParameter("channelId", attestXqDto.getChannelId()).
                    setParameter("channelOrdersn", attestXqDto.getChannelOrdersn()).
                    setParameter("channelUserid", attestXqDto.getChannelUserid()).
                    setParameter("ordersn", attestXqDto.getOrdersn()).

                    setParameter("chained", attestXqDto.getChained()).
                    setParameter("chainedCallBack", attestXqDto.getChained()).
                    setParameter("bizType", attestXqDto.getBizType()).
                    setParameter("bizSign", attestXqDto.getBizSign()).

                    setParameter("fileName", attestXqDto.getFileName()).
                    setParameter("fileType", attestXqDto.getFileType()).
                    setParameter("fileSize", attestXqDto.getFileSize()).
                    setParameter("fileHash", attestXqDto.getFileHash()).

                    setParameter("ownerType", attestXqDto.getOwnerType()).
                    setParameter("ownerId", attestXqDto.getOwnerId()).
                    setParameter("ownerName", attestXqDto.getOwnerName()).

                    setParameter("agentName", attestXqDto.getAgentName()).
                    setParameter("agentPhone", attestXqDto.getAgentPhone()).
                    setParameter("agentEmail", attestXqDto.getAgentEmail()).

                    setParameter("duration", attestXqDto.getDuration()).
                    setParameter("description", attestXqDto.getDescription()).
                    setParameter("startTime", attestXqDto.getStartTime()).
                    setParameter("price", attestXqDto.getPrice()).
                    build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        try {
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost request = new HttpPost(url);
            HttpResponse response = httpclient.execute(request);
            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回数据**/
                String strResult = EntityUtils.toString(response.getEntity(), "utf-8");

                log.info("存证续期请求内容 : " +strResult);
            } else {
                log.info("续期失败");
                return;
            }
                httpclient.close();
            } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
