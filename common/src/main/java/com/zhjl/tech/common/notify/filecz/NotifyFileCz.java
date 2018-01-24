package com.zhjl.tech.common.notify.filecz;

import com.zhjl.tech.common.exception.StoreBizException;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.message.CreateAttestDetailMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.UUID;

@Slf4j
@Service
public class NotifyFileCz {

    /**
     * 文件存证回调函数
     *
     * @param createAttestDetailMessage 用于消息传输对象
     * @param callBackUrl               渠道对应回调地址
     * @param sign                      回调函数的请求签名
     * @param count                     设置回调失败后的重试次数
     */
    public void notifyChannel(CreateAttestDetailMessage createAttestDetailMessage, String callBackUrl, String sign, int count,String random) {
        if (count <= 0) {
            throw new StoreBizException("文件存证完成通知回调失败，ordersn=" + createAttestDetailMessage.getOrdersn());
        }
        int a;
        //设置时间转换格式
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SysConfig.Format);
        try {
            URI url = new URIBuilder(callBackUrl)
                    .setParameter("signType", SysConfig.SignType)
                    .setParameter("sign", sign)
                    .setParameter("random", random)
                    .setParameter("ordersn", createAttestDetailMessage.getOrdersn())
                    .setParameter("channelOrdersn", createAttestDetailMessage.getChannelOrdersn())
                    .setParameter("startTime", simpleDateFormat.format(createAttestDetailMessage.getStartTime()))
                    .setParameter("platformSign", createAttestDetailMessage.getPlatformSign())
                    .setParameter("duration", String.valueOf(createAttestDetailMessage.getDuration()))
                    .setParameter("success", "true")
                    .setParameter("state", "200")
                    .setParameter("msg", "文件存证完成")
                    .build();
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost request = new HttpPost(url);
            HttpResponse response = httpclient.execute(request);
            response.setHeader("Content-type", "text/html;charset=UTF-8");
            //判断文件存证回调是否成功
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                log.info("文件存证处理结果回调成功.ordersn={}",createAttestDetailMessage.getOrdersn());
            } else {
                //不管何种原因，均需要重新开始回调
                log.warn("文件存证回调失败.ordersn={}", createAttestDetailMessage.getOrdersn());
                count--;
                notifyChannel(createAttestDetailMessage, callBackUrl, sign, count,random);
            }
        } catch (URISyntaxException e) {
            log.error("URL不能存在特殊字符.ordersn={}",createAttestDetailMessage.getOrdersn(), e);
            throw new StoreBizException("文件存证因URISyntaxException回调通知失败，ordersn=" + createAttestDetailMessage.getOrdersn());
        } catch (IOException e) {
            log.error("获取服务器数据失败.ordersn={}",createAttestDetailMessage.getOrdersn(), e);
            throw new StoreBizException("文件存证因IOException回调通知失败，ordersn=" + createAttestDetailMessage.getOrdersn());
        }
    }
}
