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
public class NotifyFileCzFailed {

    /**
     * 文件存证处理失败回调函数
     *
     * @param createAttestDetailMessage 用于消息传输对象
     * @param sign 回调函数的请求签名
     * @param count 设置回调失败后的重试次数
     * @param callBackUrl 渠道回调接收地址
     * @param success 回调的json数据处理成功与否标识信息
     * @param state 该订单处理状态
     * @param msg 该订单处理结果描述
     */
    public void notifyChannel(CreateAttestDetailMessage createAttestDetailMessage,String callBackUrl,String sign,int count, String success,String state,String msg) {
        if (count <= 0) {
            //重试多次而不成功
            throw new StoreBizException("文件存证处理失败回调失败，ordersn=" + createAttestDetailMessage.getOrdersn());
        }

        // 生成请求随机数
        String random = UUID.randomUUID().toString().replaceAll("-","");

        try {

            URI url = new URIBuilder(callBackUrl)
                    .setParameter("signType", SysConfig.SignType)
                    .setParameter("sign", sign)
                    .setParameter("random", random)
                    .setParameter("ordersn", createAttestDetailMessage.getOrdersn())
                    .setParameter("channelOrdersn", createAttestDetailMessage.getChannelOrdersn())
                    .setParameter("startTime", "")
                    .setParameter("platformSign", createAttestDetailMessage.getPlatformSign())
                    .setParameter("duration", String.valueOf(createAttestDetailMessage.getDuration()))
                    .setParameter("success", success)
                    .setParameter("state", state)
                    .setParameter("msg", msg)
                    .build();
            CloseableHttpClient httpclient = HttpClients.createDefault();
            HttpPost request = new HttpPost(url);
            HttpResponse response = httpclient.execute(request);
            response.setHeader("Content-type", "text/html;charset=UTF-8");

            //判断文件存证回调是否成功
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                log.info("文件存证失败结果回调成功.ordersn={}",createAttestDetailMessage.getOrdersn());
            }else{
                //不管何种原因，均需要重新开始回调
                log.warn("文件存证失败结果回调失败.ordersn={}",createAttestDetailMessage.getOrdersn());
                count--;
                notifyChannel(createAttestDetailMessage, callBackUrl,sign, count,success,state,msg);

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
