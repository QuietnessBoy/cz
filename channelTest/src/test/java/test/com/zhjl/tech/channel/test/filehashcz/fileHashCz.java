package test.com.zhjl.tech.channel.test.filehashcz;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.channel.model.channel.AttestChannelTest;
import com.zhjl.tech.channel.service.channel.IAttestChannelTestService;
import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.utils.gen.filehashcz.GenFileHashCzDatas;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import test.com.zhjl.tech.channel.base.BaseTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

@Slf4j
public class fileHashCz extends BaseTest{

    @Resource
    IAttestChannelTestService attestChannelTestService;

    @Resource
    private GenFileHashCzDatas genFileHashCzDatas;

    @Resource
    private ComBizConfig comBizConfig;

    @Test
    public void a() throws IOException, URISyntaxException {
        AttestDto attestDto = genFileHashCzDatas.gen("D://11.txt","userid_07");

        AttestChannelTest attestChannelTest = new AttestChannelTest();
        BeanUtils.copyProperties(attestDto, attestChannelTest);
        attestChannelTest.setState("创建文件存证订单");
        attestChannelTestService.insertSelective(attestChannelTest);
        log.info("保存临时文件存证订单数据.{}", JSONObject.toJSONString(attestChannelTest));

        URI url;

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
        System.out.println("状态码：" + response.getStatusLine().getStatusCode());
        if (response.getStatusLine().getStatusCode() == 200) {
            /**读取服务器返回数据**/
            String strResult = EntityUtils.toString(response.getEntity(), "utf-8");
            System.out.println(" 响应的内容 : " + strResult);
            log.info("文件Hash存证请求响应成功.{}");
        } else {
            log.error("文件Hash存证请求失败.{},{}",response.getStatusLine().getStatusCode(),comBizConfig.getFileCzUrl());
        }
    }


}
