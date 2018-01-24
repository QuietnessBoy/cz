package com.zhjl.tech.attest.util;

import com.zhjl.tech.attest.configs.EnvConfig;
import com.zhjl.tech.attest.model.attest.Attest;
import com.zhjl.tech.common.exception.AttestBizException;
import com.zhjl.tech.common.constant.SysConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


@Slf4j
@Service
public class OrdersnGeneration {


    /**
     * 根据attest对象，请求获取订单号
     * @param attest
     * @return
     */
    public String gen(Attest attest) {
        // 获取缓存中的ordersn请求地址，config表中ordersn对应的channelId为002
        String ordersnUrl = EnvConfig.configChannnelIdMap.get(SysConfig.Config_type_zjl).get(SysConfig.ordersnUrl);

        log.info("[开始请求订单号],channelordersn={}",attest.getChannelOrdersn());
        //订单号处理
        /*三位业务编号采用BDY格式，B为业务类型，D为存储的数据文件类型， Y时间期限。具体为：
                                B业务类型\1存储存证\2存储出证\3区块链存证\4区块链出证；
                                D:数据类型\1文字\2图片\3视频\4音频\5压缩；
                                Y:时间期限\1一年\2二年\3五年\4十年\5长期。*/
        //B业务类型
        String buninessType;
        if (attest.getChained().equals(SysConfig.ChainedOk)) {
            buninessType = SysConfig.BlockChainAttest;;
        } else {
            buninessType = SysConfig.SaveAttest;
        }

        //Y:时间期限
        String duration = ComputeTime.convertDaysToYear(Integer.parseInt(attest.getDuration())); //对应日期类别

        URI url;
        String strResult = null;
        try (CloseableHttpClient httpclient = HttpClients.createDefault()){
            url = new URIBuilder(ordersnUrl)
                    .setParameter("channelOrdersn", attest.getChannelOrdersn())
                    .setParameter("buninessType", buninessType)
                    .setParameter("channelId", attest.getChannelId())
                    .setParameter("bizType", attest.getBizType())
                    .setParameter("duration", duration).build();
            HttpPost request = new HttpPost(url);
            HttpResponse response = httpclient.execute(request);
            response.setHeader("Content-type", "text/html;charset=UTF-8");

            //判断请求是否超时m
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_GATEWAY_TIMEOUT) {
                log.warn("订单号请求超时.channelOrdersn={}",attest.getChannelOrdersn());
                throw new AttestBizException("订单号请求超时！channelOrdersn="+attest.getChannelOrdersn());
            }

            //请求发送成功，并得到响应
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                //读取服务器返回数据
                strResult = EntityUtils.toString(response.getEntity()).replaceAll("\"", "");
                log.info("ordersn请求成功.ordersn={}", strResult);
                //这里是正常的业务流程
            } else {
                throw new AttestBizException("请求订单号时，服务器响应结果状态码不正确！");
            }
        } catch (URISyntaxException e) {
            log.error("URL不能存在特殊字符.channelOrdersn={}",attest.getChannelOrdersn(), e);
            throw new AttestBizException("请求订单号时，URL不能存在特殊字符! " + e.getMessage());
        } catch (IOException e) {
            log.error("获取服务器数据失败.channelOrdersn={}",attest.getChannelOrdersn(), e);
            throw new AttestBizException("请求订单号时，发生异常： " + e.getMessage());
        }
        return strResult;
    }
}
