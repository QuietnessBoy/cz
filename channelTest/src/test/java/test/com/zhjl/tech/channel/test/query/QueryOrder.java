package test.com.zhjl.tech.channel.test.query;

import com.zhjl.tech.channel.biz.what.GenQueryData;
import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.dto.interfaces.QueryOrderDto;
import com.zhjl.tech.common.utils.gen.query.GenQueryDatas;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import test.com.zhjl.tech.channel.base.BaseTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
public class QueryOrder extends BaseTest{

    @Test
    public void a() throws URISyntaxException, IOException {
        String ordersn="005331180110000022";
        QueryOrderDto queryOrderDto = genQueryDatas.gen(ordersn);

        URI url;

        url = new URIBuilder(comBizConfig.getSelectUrl()).
                setParameter("signType", queryOrderDto.getSignType()).
                setParameter("sign", queryOrderDto.getSign()).
                setParameter("accessKey", queryOrderDto.getAccessKey()).
                setParameter("random", queryOrderDto.getRandom()).
                setParameter("channelId", queryOrderDto.getChannelId()).
                setParameter("ordersn", queryOrderDto.getOrdersn()).
                build();

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost request = new HttpPost(url);
        HttpResponse response = httpclient.execute(request);

        /**请求发送成功，并得到响应**/
        System.out.println("状态码：" + response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
            /**读取服务器返回数据**/
            String strResult = EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println(" 响应的内容 : " + strResult);
            log.info("查询存证订单请求响应成功.{}",comBizConfig.getFileCzUrl());
        } else {
            log.error("查询存证订单请求失败.{},{}",response.getStatusLine().getStatusCode(),comBizConfig.getSelectUrl());
        }
    }




    @Resource
    private GenQueryDatas genQueryDatas;

    @Resource
    ComBizConfig comBizConfig;

}
