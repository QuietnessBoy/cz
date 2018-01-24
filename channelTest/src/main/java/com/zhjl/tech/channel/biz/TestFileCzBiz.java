package com.zhjl.tech.channel.biz;


import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.channel.biz.request.FileCz;
import com.zhjl.tech.channel.biz.what.GenFileCzData;
import com.zhjl.tech.channel.model.channel.AttestChannelTest;

import com.zhjl.tech.channel.service.channel.IAttestChannelTestService;
import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.utils.CommonTool;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.net.URISyntaxException;
import java.util.List;

@Service
@Slf4j
public class TestFileCzBiz {




    @Transactional
    public JsonResult mock(String testName, int a) throws IOException, URISyntaxException {
        JsonResult jsonResult = new JsonResult();
        String userid = CommonTool.genRandomString(3);

        for(int i=0;i< a;i++) {
            genFileCzData.gen(userid,testName);  //生成参数信息
        }
        log.info("数据生成完毕!开始存证请求.");

        List<AttestChannelTest> list= attestChannelTestService.getAttestChannelTestByTestName(testName);
        int s = 0;
        for(AttestChannelTest attestChannelTest : list){
            AttestDto attestDto = new AttestDto();
            BeanUtils.copyProperties(attestChannelTest, attestDto);
            log.info("发送的参数信息{}:{}",s, JSONObject.toJSONString(attestChannelTest));
            fileCz.fileCz(attestDto);
            attestChannelTest.setState("开始文件存证");
            attestChannelTestService.updateSelectiveById(attestChannelTest);
            s++;
        }

        jsonResult.setMsg("生成数据完毕,开始文件存证.");
        jsonResult.setSuccess(true);
        return jsonResult;
    }

    /**
     * 文件存证数据生成
     * @param testName
     * @param a
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    @Transactional
    public JsonResult genDatas(String testName, int a) throws IOException, URISyntaxException {
        JsonResult jsonResult = new JsonResult();
        String userid = CommonTool.genRandomString(3);

        for(int i=0;i< a;i++) {
            AttestDto attestDto = genFileCzData.gen(userid,testName);  //生成参数信息
            genFile(attestDto);
        }
        log.info("数据生成完毕!");

        jsonResult.setMsg("生成数据完毕.");
        jsonResult.setSuccess(true);
        return jsonResult;
    }

    /**
     * 生成数据保存在txt文件中
     * @param attestDto
     * @throws IOException
     */
    public void genFile(AttestDto attestDto) throws IOException {
        log.info("生成文件:{}",JSONObject.toJSONString(attestDto));
        FileOutputStream fop = null;

        byte[] contentInBytes;
        byte[] filecontent;
        String attestDtoString = null;

        attestDtoString = attestDto.getSignType()+"&zhijl&"+attestDto.getSign()+"&zhijl&"+attestDto.getAccessKey()+"&zhijl&"+attestDto.getRandom()+"&zhijl&"+attestDto.getBizSign()+"&zhijl&"
                +attestDto.getRequestTime()+"&zhijl&"+attestDto.getChannelId()+"&zhijl&"+attestDto.getChannelUserid()+"&zhijl&"+attestDto.getChannelOrdersn()+"&zhijl&"
                +attestDto.getChained()+"&zhijl&"+attestDto.getBizType()+"&zhijl&"+attestDto.getFileName()+"&zhijl&"+attestDto.getFileType()+"&zhijl&"+attestDto.getFileSize()+"&zhijl&"
                +attestDto.getFileHash()+"&zhijl&"+attestDto.getFileAddr()+"&zhijl&"+attestDto.getOwnerType()+"&zhijl&"+attestDto.getOwnerId()+"&zhijl&"+attestDto.getOwnerName()+"&zhijl&"
                +attestDto.getAgentName()+"&zhijl&"+attestDto.getAgentPhone()+"&zhijl&"+attestDto.getAgentEmail()+"&zhijl&"+attestDto.getDuration()+"&zhijl&"+attestDto.getDescription()+"&zhijl&"
                +attestDto.getPrice()+"\r\n";
        try {
            File file = new File(comBizConfig.getDataPath(),comBizConfig.getFileData());

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
    FileCz fileCz;

    @Resource
    GenFileCzData genFileCzData;

    @Resource
    private ComBizConfig comBizConfig;

    @Resource
    IAttestChannelTestService attestChannelTestService;


}
