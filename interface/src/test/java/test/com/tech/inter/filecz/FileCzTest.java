package test.com.tech.inter.filecz;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.constant.JsonConfig;
import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.utils.gen.filecz.GenFileCzDatas;
import com.zhjl.tech.inter.biz.filecz.FileCzBiz;
import com.zhjl.tech.inter.controller.CzController;
import com.zhjl.tech.inter.model.inter.Status;
import com.zhjl.tech.inter.model.inter.TempOrder;
import com.zhjl.tech.inter.service.inter.IStatusService;
import com.zhjl.tech.inter.service.inter.ITempOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.test.web.servlet.MockMvc;
import test.com.tech.inter.base.BaseTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.UUID;

@Slf4j
public class FileCzTest extends BaseTest {



    @Test
    public void normal() throws IOException, URISyntaxException {

        AttestDto attestDto = genFileCzDatas.gen("D://11.txt","mrWW","ordersn");

        JsonResult jsonResult = fileCzBiz.solve(attestDto);

        Assert.assertEquals(true,jsonResult.isSuccess());
    }

    @Test
    public void signVerifyFailed() throws IOException, URISyntaxException {
        AttestDto attestDto = genFileCzDatas.gen("D://11.txt","mrWW22","ordersn");

        attestDto.setSign("aaaa");

        JsonResult jsonResult = fileCzBiz.solve(attestDto);

        Assert.assertEquals(jsonResult.isSuccess(),false);
    }

    @Test
    public void channelIdFailed() throws IOException, URISyntaxException {
        AttestDto attestDto = genFileCzDatas.gen("D://11.txt","mrWW22","ordersn");

        attestDto.setChannelId("not_exists");

        JsonResult jsonResult = fileCzBiz.solve(attestDto);

        Assert.assertEquals(false,jsonResult.isSuccess());
        Assert.assertEquals(JsonConfig.ErrorChannelId, jsonResult.getStatus());
    }


    /**
     * 文件存证正常流程。  不发送给rabbitmq
     * 测试思路
     * 1. 测试文件存证interface的controller方法
     * 2. 查看TempOrder表是否存在该记录
     * 3. 查看status表是否存在该记录,并且status表state字段是否为“文件存证完成1
     */
    @Test
    public void fileCz(){
        String userid = UUID.randomUUID().toString().replaceAll("-","").substring(0,3);
        AttestDto attestDto = genFileCzDatas.gen("D://11.txt",userid,"ordersn");
        JsonResult jsonResult = fileCzBiz.solveInteral(attestDto);
        //返回jsonResult为true   参数校验通过
        Assert.assertEquals(true,jsonResult.isSuccess());

        //temporder表中有对应channelordersn的数据
        TempOrder tempOrder = tempOrderService.getTempOrderByChannelOrdersn(attestDto.getChannelOrdersn());
        Assert.assertNotNull(tempOrder);
        Assert.assertEquals(attestDto.getBizSign(),tempOrder.getBizSign());

        //status表state字段为文件存证完成1
        Status status = statusService.getStatusByChannelOrdersn(attestDto.getChannelOrdersn());
        Assert.assertTrue(State.Request_File_Ok.equals(status.getStateBiz()));

        System.out.println("-------------------ChannelOrdersn:"+attestDto.getChannelOrdersn());

    }

    @Resource
    private ITempOrderService tempOrderService;

    @Resource
    private IStatusService statusService;

    @Resource
    private GenFileCzDatas genFileCzDatas;

    @Resource
    private FileCzBiz fileCzBiz;

}

