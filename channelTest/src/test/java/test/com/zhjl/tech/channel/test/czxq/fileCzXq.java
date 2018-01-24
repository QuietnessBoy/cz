package test.com.zhjl.tech.channel.test.czxq;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.channel.model.channel.AttestChannelTest;
import com.zhjl.tech.channel.service.channel.IAttestChannelTestService;
import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.dto.interfaces.AttestXqDto;
import com.zhjl.tech.common.utils.gen.filehashcz.GenFileHashCzDatas;
import com.zhjl.tech.common.utils.gen.xqcz.GenXqData;
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
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class fileCzXq extends BaseTest{

    @Resource
    IAttestChannelTestService attestChannelTestService;

    @Resource
    private GenXqData genXqData;

    @Resource
    private ComBizConfig comBizConfig;

    @Test
    public void a() throws IOException, URISyntaxException {
        String ordersn = "005351180117000103";
        AttestChannelTest attestChannelTest1 = attestChannelTestService.getAttestChannelTestByOrdersn(ordersn);
        AttestXqDto attestXqDto = new AttestXqDto();
        BeanUtils.copyProperties(attestChannelTest1,attestXqDto);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SysConfig.Format);
        attestXqDto = genXqData.gen(attestXqDto,ordersn,simpleDateFormat.format(attestChannelTest1.getStartTime()));

        AttestChannelTest attestChannelTest = new AttestChannelTest();
        BeanUtils.copyProperties(attestXqDto, attestChannelTest);
        attestChannelTest.setState("创建文件存证订单");
        attestChannelTestService.insertSelective(attestChannelTest);
        log.info("保存临时文件存证订单数据.{}", JSONObject.toJSONString(attestChannelTest));

        URI url;
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
                setParameter("startTime", attestXqDto.getStartTime()).

                setParameter("duration", attestXqDto.getDuration()).
                setParameter("description", attestXqDto.getDescription()).
                setParameter("price", attestXqDto.getPrice()).
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
            log.info("文件Hash存证请求响应成功.{}",comBizConfig.getFileCzUrl());
        } else {
            log.error("文件Hash存证请求失败.{},{}",response.getStatusLine().getStatusCode(),comBizConfig.getFileCzUrl());
        }
    }


}
