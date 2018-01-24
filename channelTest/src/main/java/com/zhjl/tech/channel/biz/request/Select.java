package com.zhjl.tech.channel.biz.request;

import com.zhjl.tech.channel.configs.Configs;
import com.zhjl.tech.common.configs.ComBizConfig;
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
public class Select {

    @Resource
    private ComBizConfig comBizConfig;

    public void select(String sign,String signType,String accessKey,String random,String channelId,String ordersn){
        URI url = null;
        try {
            url = new URIBuilder(comBizConfig.getSelectUrl()).
                    setParameter("sign",sign).
                    setParameter("signType",signType).
                    setParameter("accessKey",accessKey).
                    setParameter("random",random).
                    setParameter("channelId", channelId).
                    setParameter("ordersn", ordersn).
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
                log.info("查询订单请求成功。");
            } else {
                log.info("查询订单请求失败。"+comBizConfig.getSelectUrl());
                return;
            }
                httpclient.close();
            } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
