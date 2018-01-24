package com.zhjl.tech.channel.biz.what;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.channel.model.channel.AttestChannelTest;
import com.zhjl.tech.channel.service.channel.IAttestChannelTestService;
import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.utils.gen.filecz.GenFileCzDatas;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * ps:创建表的时候,可以考虑加上测试名称
 *
 * 根据本次测试的名称[testName]
 * 1 生成n条测试
 * 2 生成对应的文件
 * 3 将对应的数据插入到tables
 */


/**
 * 自动生成文件存证请求参数
 */
@Slf4j
@Service
public class GenFileCzData {

    @Resource
    GenFileCzDatas genFileCzDatas;

    /**
     * 自动生成数据
     * @param userid 临时文件保存目录
     * @return
     * @throws IOException
     */
    public AttestDto gen(String userid, String testName) throws IOException {

        AttestDto attestDto = genFileCzDatas.gen(comBizConfig.getTestFile(),userid,testName);

        AttestChannelTest attestChannelTest = new AttestChannelTest();
        BeanUtils.copyProperties(attestDto, attestChannelTest);
        attestChannelTest.setState("创建文件存证订单");
        attestChannelTest.setTestName(testName);
        attestChannelTest.setUpdateTime(new Date());
        attestChannelTestService.insertSelective(attestChannelTest);
        log.info("保存临时文件存证订单数据.{}", JSONObject.toJSONString(attestChannelTest));

        return attestDto;
    }


    @Resource
    private IAttestChannelTestService attestChannelTestService;

    @Resource
    private ComBizConfig comBizConfig;

    static Random rd = new Random();
}
