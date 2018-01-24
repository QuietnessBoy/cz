package com.zhjl.tech.channel.biz;


import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.channel.tool.Tool;
import com.zhjl.tech.channel.biz.request.CzXq;
import com.zhjl.tech.channel.configs.Configs;
import com.zhjl.tech.channel.model.channel.AttestChannelTest;
import com.zhjl.tech.channel.service.channel.IAttestChannelTestService;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.AttestXqDto;
import com.zhjl.tech.common.zjlsign.xqcz.XqCzBizSign;
import com.zhjl.tech.common.zjlsign.xqcz.XqCzRequestSign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class XqCzBiz {

    @Resource
    IAttestChannelTestService attestChannelTestService;

    @Resource
    CzXq czXq;

    @Resource
    Tool tool;

    @Transactional
    public JsonResult xqCz(String testName) throws IOException, URISyntaxException {
        JsonResult jsonResult = new JsonResult();

        List<AttestChannelTest> list= attestChannelTestService.getAttestChannelTestByTestName(testName);
        int s = 0;
        for(AttestChannelTest attestChannelTest : list){
            AttestXqDto attestXqDto = new AttestXqDto();
            BeanUtils.copyProperties(attestChannelTest, attestXqDto);

            //获取originalOrdersn
            attestXqDto.setOrdersn(attestChannelTest.getOrdersn());
            log.info("发送的参数信息{}:{}",s, JSONObject.toJSONString(attestXqDto));

            //channelOrdersn
            String channelOrdersn = "ordersn" + UUID.randomUUID().toString().replaceAll("-","").substring(0,9);
            attestXqDto.setChannelOrdersn(channelOrdersn);

            //生成随机数
            attestXqDto.setRandom((UUID.randomUUID().toString().replaceAll("-","").substring(0,16)));

            //生成BizSign

            String bizSign = XqCzBizSign.gen(attestXqDto,Configs.channelPublickKey,Configs.channelPrivateKey,Configs.ida);
            attestXqDto.setBizSign(bizSign);

            //生成Sign
            String sign = XqCzRequestSign.genSign(attestXqDto,Configs.channelPublickKey);
            attestXqDto.setSign(sign);
            attestXqDto.setRandom( UUID.randomUUID().toString().replaceAll("-",""));
            czXq.xq(attestXqDto);
            attestChannelTest.setUpdateTime(new Date());
            attestChannelTest.setState("开始存证续期");
            attestChannelTestService.insertSelective(attestChannelTest);
            s++;
        }

        jsonResult.setMsg("生成数据完毕,开始存证续期.");
        jsonResult.setSuccess(true);
        return jsonResult;
    }
}
