package test.com.zhjl.tech.channel.test.filecz;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.channel.model.channel.AttestChannelTest;
import com.zhjl.tech.channel.service.channel.IAttestChannelTestService;
import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.utils.gen.filecz.GenFileCzDatas;
import com.zhjl.tech.common.zjlsign.filecz.FileCzBizSign;
import com.zhjl.tech.common.zjlsign.filecz.FileCzRequestSign;
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
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * 处理失败订单进行重新发送
 */
@Slf4j
public class fileCzFailed extends BaseTest{

    @Resource
    IAttestChannelTestService attestChannelTestService;

    @Resource
    private GenFileCzDatas genFileCzDatas;

    @Resource
    private ComBizConfig comBizConfig;

    @Test
    public void a() throws IOException, URISyntaxException {
        String channelOrdersn = "ordersn3b685859d";
        AttestChannelTest attestChannelTest = attestChannelTestService.getAttestChannelTestByChannelOrdersn(channelOrdersn);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SysConfig.Format);

        // set new data
        attestChannelTest.setRequestTime(simpleDateFormat.format(new Date()));
        String size = "9";
        attestChannelTest.setFileSize(size);
//        attestChannelTest.setFileHash("");
//        attestChannelTest.setFileAddr("");

        AttestDto attestDto = new AttestDto();
        BeanUtils.copyProperties(attestChannelTest,attestDto);
        attestDto.setRandom( UUID.randomUUID().toString().replaceAll("-",""));

        //生成BizSign
        String bizSign = null;
        try {
            bizSign = FileCzBizSign.gen(attestDto,comBizConfig.getChannelPublickKey(),comBizConfig.getChannelPrivateKey());
        } catch (UnsupportedEncodingException e) {
            log.error("生成bizSign错误.",e);
        }
        attestDto.setBizSign(bizSign);

        //生成sign/signType
        String sign = FileCzRequestSign.gen(attestDto,comBizConfig.getChannelPublickKey());
        attestDto.setSign(sign);

        AttestChannelTest attestChannelTest1 = new AttestChannelTest();
        BeanUtils.copyProperties(attestChannelTest, attestChannelTest1);
        BeanUtils.copyProperties(attestDto, attestChannelTest1);
        attestChannelTest1.setState("创建文件存证订单");
        attestChannelTest1.setFileSize(size);
        attestChannelTestService.updateSelectiveById(attestChannelTest1);
        log.info("保存临时文件存证订单数据.{}", JSONObject.toJSONString(attestChannelTest1));

        URI url;

        url = new URIBuilder("http://47.95.241.154/interface/createFileAttest").
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
                setParameter("fileAddr", attestDto.getFileAddr()).

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
            log.info("文件存证请求响应成功.{}",comBizConfig.getFileCzUrl());
        } else {
            log.error("文件存证请求失败.{},{}",response.getStatusLine().getStatusCode(),comBizConfig.getFileCzUrl());
        }
    }


}
