package com.zhjl.tech.channel.biz;


import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.channel.biz.request.HashCz;
import com.zhjl.tech.channel.biz.what.GenFileHashCzData;
import com.zhjl.tech.channel.model.channel.AttestChannelTest;
import com.zhjl.tech.channel.service.channel.IAttestChannelTestService;
import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class HashCzBiz {




    @Transactional
    public JsonResult fileHashCz(String testName, int a) throws IOException, URISyntaxException {
        JsonResult jsonResult = new JsonResult();
        String userid = UUID.randomUUID().toString().replaceAll("-","").substring(0,3);
        for(int i=0;i< a;i++) {
            genFileHashCzData.gen(userid, testName);  //生成参数信息
        }
        log.info("数据生成完毕!开始存证请求.");

        List<AttestChannelTest> list = attestChannelTestService.getAttestChannelTestByTestName(testName);
        for (AttestChannelTest attestChannelTest :list){
            AttestDto attestDto = new AttestDto();
            BeanUtils.copyProperties(attestChannelTest, attestDto);
            log.info("发送的参数信息:{}", JSONObject.toJSONString(attestChannelTest));
            hashCz.hash(attestDto);
            attestChannelTest.setState("开始Hash存证");
            attestChannelTestService.updateSelectiveById(attestChannelTest);
        }
        jsonResult.setSuccess(true);
        jsonResult.setMsg("数据生成完毕,开始文件Hash存证.");
        return jsonResult;
    }

    /**
     * hash存证数据生成
     * @param testName
     * @param a
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    @Transactional
    public JsonResult genDatas(String testName, int a) throws IOException, URISyntaxException {
        JsonResult jsonResult = new JsonResult();
        String userid = UUID.randomUUID().toString().replaceAll("-","").substring(0,3);
        for(int i=0;i< a;i++) {
            AttestDto attestDto = genFileHashCzData.gen(userid, testName);  //生成参数信息
            genFile(attestDto);
        }

        log.info("数据生成完毕.");

        jsonResult.setSuccess(true);
        jsonResult.setMsg("数据生成完毕.");
        return jsonResult;
    }


    /**
     * 生成数据保存在txt文件中
     * @param attestDto
     * @throws IOException
     */
    public void genFile(AttestDto attestDto) throws IOException {
        log.info("生成文件:{}", JSONObject.toJSONString(attestDto));
        FileOutputStream fop = null;

        byte[] contentInBytes;
        byte[] filecontent;
        String attestDtoString = null;

        attestDtoString = attestDto.getSignType() + "&zhijl&" + attestDto.getSign() + "&zhijl&" + attestDto.getAccessKey() + "&zhijl&" + attestDto.getRandom() + "&zhijl&" + attestDto.getBizSign() + "&zhijl&"
                + attestDto.getRequestTime() + "&zhijl&" + attestDto.getChannelId() + "&zhijl&" + attestDto.getChannelUserid() + "&zhijl&" + attestDto.getChannelOrdersn() + "&zhijl&"
                + attestDto.getChained() + "&zhijl&" + attestDto.getBizType() + "&zhijl&" + attestDto.getFileName() + "&zhijl&" + attestDto.getFileType() + "&zhijl&" + attestDto.getFileSize() + "&zhijl&"
                + attestDto.getFileHash() + "&zhijl&" + attestDto.getOwnerType() + "&zhijl&" + attestDto.getOwnerId() + "&zhijl&" + attestDto.getOwnerName() + "&zhijl&"
                + attestDto.getAgentName() + "&zhijl&" + attestDto.getAgentPhone() + "&zhijl&" + attestDto.getAgentEmail() + "&zhijl&" + attestDto.getDuration() + "&zhijl&" + attestDto.getDescription() + "&zhijl&"
                + attestDto.getPrice() + "\r\n";
        try {
            File file = new File(comBizConfig.getDataPath(), comBizConfig.getHashData());

            if (!file.exists()) {
                file.createNewFile();
                contentInBytes = attestDtoString.getBytes();
                fop = new FileOutputStream(file);
                fop.write(contentInBytes);
                fop.flush();
                fop.close();
            }else{
                InputStream in = new FileInputStream(file);
                Long filelength = file.length();
                filecontent = new byte[filelength.intValue()];
                in.read(filecontent);
                in.close();
                contentInBytes = ArrayUtils.addAll(filecontent,attestDtoString.getBytes());
                fop = new FileOutputStream(file);
                fop.write(contentInBytes);
                fop.flush();
                fop.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fop != null) {
                    fop.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Resource
    HashCz hashCz;

    @Resource
    private ComBizConfig comBizConfig;

    @Resource
    GenFileHashCzData genFileHashCzData;

    @Resource
    IAttestChannelTestService attestChannelTestService;
}
