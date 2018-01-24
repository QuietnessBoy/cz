package test.com.zhjl.tech.channel.test.query;

import com.zhjl.tech.channel.model.channel.AttestChannelTest;
import com.zhjl.tech.channel.service.channel.IAttestChannelTestService;
import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.dto.interfaces.DigitalCertificateDto;
import com.zhjl.tech.common.dto.interfaces.QueryOrderDto;
import com.zhjl.tech.common.utils.gen.query.GenQueryDatas;
import com.zhjl.tech.common.utils.gen.query.GenVerifyOrderData;
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
public class QueryChained extends BaseTest{

    @Test
    public void a() throws URISyntaxException, IOException {
        String ordersn="005151180109000064";
        AttestChannelTest attestChannelTest = attestChannelTestService.getAttestChannelTestByOrdersn(ordersn);
        DigitalCertificateDto digitalCertificateDto = genVerifyOrderData.gen(attestChannelTest.getOwnerId(),attestChannelTest.getFileHash());

        URI url;

        url = new URIBuilder(comBizConfig.getVerifyUrl()).
                setParameter("signType", digitalCertificateDto.getSignType()).
                setParameter("sign", digitalCertificateDto.getSign()).
                setParameter("accessKey", digitalCertificateDto.getAccessKey()).
                setParameter("random", digitalCertificateDto.getRandom()).
                setParameter("channelId", digitalCertificateDto.getChannelId()).
                setParameter("ownerId", digitalCertificateDto.getOwnerId()).
                setParameter("fileHash", digitalCertificateDto.getFileHash()).
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
            log.info("区块链存证订单校验请求响应成功.{}",comBizConfig.getFileCzUrl());
        } else {
            log.error("区块链存证订单校验请求失败.{},{}",response.getStatusLine().getStatusCode(),comBizConfig.getVerifyUrl());
        }
    }




    @Resource
    private GenVerifyOrderData genVerifyOrderData;

    @Resource
    private IAttestChannelTestService attestChannelTestService;

    @Resource
    private ComBizConfig comBizConfig;

}
