package com.zhjl.tech.common.notify.filehashcz;

import com.zhjl.tech.common.exception.AttestBizException;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.message.CreateAttestDetailMessage;
import com.zhjl.tech.common.utils.TimeTool;
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
import java.util.UUID;

@Slf4j
@Service
public class NotifyFileHashCz {

    /**
     * 文件Hash存证请求通知
     *
     * @param createAttestDetailMessage
     */
    public void notifyChannel(CreateAttestDetailMessage createAttestDetailMessage,String sign,String callBackUrl, int count ) {
        if( count <= 0){
            //重试多次而不成功
            throw new AttestBizException("文件Hash存证完成通知回调失败，ordersn="+createAttestDetailMessage.getOrdersn());
        }

        // 生成请求随机数
        String random = UUID.randomUUID().toString().replaceAll("-", "");
        int a;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()){
            URI url = new URIBuilder(callBackUrl)
                    .setParameter("signType", SysConfig.SignType)
                    .setParameter("sign", sign)
                    .setParameter("random", random)
                    .setParameter("channelOrdersn", createAttestDetailMessage.getChannelOrdersn())
                    .setParameter("ordersn", createAttestDetailMessage.getOrdersn())
                    .setParameter("startTime", TimeTool.date2Str(createAttestDetailMessage.getStartTime()))
                    .setParameter("platformSign", createAttestDetailMessage.getPlatformSign())
                    .setParameter("duration", String.valueOf(createAttestDetailMessage.getDuration()))
                    .build();
            HttpPost request = new HttpPost(url);
            HttpResponse response = httpclient.execute(request);
            response.setHeader("Content-type", "text/html;charset=UTF-8");

            //判断请求是否成功
            if(response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                log.info("文件Hash存证回调成功.ordersn={}",createAttestDetailMessage.getOrdersn());
            }else{
                //不管何种原因，均需要重新开始回调
                log.warn("文件Hash存证回调失败,ordersn={}",createAttestDetailMessage.getOrdersn());
                count--;
                notifyChannel(createAttestDetailMessage,sign,callBackUrl,count);
            }
        } catch (URISyntaxException e) {
            log.error("URL不能存在特殊字符.ordersn={}",createAttestDetailMessage.getOrdersn(), e);
            throw new AttestBizException("文件Hash存证完成因URISyntaxException回调失败，ordersn="+createAttestDetailMessage.getOrdersn());
        } catch (IOException e) {
            log.error("获取服务器数据失败.ordersn={}",createAttestDetailMessage.getOrdersn(), e);
            throw new AttestBizException("文件Hash存证完成因IOException回调失败，ordersn="+createAttestDetailMessage.getOrdersn());
        }
    }
}
