package com.zhjl.tech.channel.mvc;

import com.zhjl.tech.channel.controller.BaseController;
import com.zhjl.tech.channel.model.channel.AttestChannelTest;
import com.zhjl.tech.channel.service.channel.IAttestChannelTestService;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.encrypt.asymmetric.SM2;
import com.zhjl.tech.common.encrypt.asymmetric.SM2Impl;
import com.zhjl.tech.common.encrypt.bytetool.Hex;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.math.ec.ECPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Component
public class HashCzOk  extends BaseController {

    @Resource
    IAttestChannelTestService attestChannelTestService;
    /**
     * 接收存证平台fileToken,并上传存证文件
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/hashCzOk")
    public JsonResult selectAttestFile(HttpServletRequest request) throws UnsupportedEncodingException, ParseException {
        JsonResult jsonResult = new JsonResult();
        String signType = request.getParameter("signType");
        String sign = request.getParameter("sign");
        String random = request.getParameter("random");
        String ordersn = request.getParameter("ordersn");
        String channelOrdersn = request.getParameter("channelOrdersn");
        String startTime = request.getParameter("startTime");
        String duration = request.getParameter("duration");
        String platformSign = request.getParameter("platformSign");
        String success = request.getParameter("success");
        String state = request.getParameter("state");
        String msg = request.getParameter("msg");

        log.info("Hash存证成功.收到的参数信息:" +
                "signType="+signType+
                "sign="+sign+
                "random="+random+
                "startTime="+startTime+
                "ordersn="+ordersn+
                "channelOrdersn="+channelOrdersn+
                "duration="+duration +
                "platformSign=" + platformSign+
                "success=" + success+
                "state=" + state+
                "msg=" + msg
        );
        AttestChannelTest attestChannelTest = attestChannelTestService.getAttestChannelTestByChannelOrdersn(channelOrdersn);

        String PlatformPublicKey="04329b15fef0019bfefb95747dbe5e74fa0b9114dfe084baa68fe4b137206872757405a64150490e33158f91ae6ba76553b4f11d5b5b1520c8038f2b779fd79548";
        String platformIDA="zhijinlian_blockchain_cz2017";

        boolean verifySign = verifyPlatformSign(PlatformPublicKey,platformIDA,platformSign, attestChannelTest,ordersn,startTime);
        if(!verifySign){
            log.info("platformSign校验不正确.{}:{}",verifySign,platformSign);
            attestChannelTest.setState("file存证完成,platformSign不正确.{}");
        }

        log.info("确认为知金链平台标识信息.Hash存证platformSign校验成功.{}",verifySign);

        attestChannelTest.setState("Hash存证完成");
        attestChannelTest.setOrdersn(ordersn);
        attestChannelTest.setStartTime(new SimpleDateFormat(SysConfig.Format).parse(startTime));
        attestChannelTestService.updateSelectiveById(attestChannelTest);


        jsonResult.setSuccess(true);
        jsonResult.setMsg("接收文件Hash存证通知成功.");
        return jsonResult;
    }


    /**
     *  检验platformsign
     * @param platformPublicKey
     * @param platfomrIDA
     * @param platformSign
     * @param attestChannelTest
     * @return
     * @throws UnsupportedEncodingException
     */
    public boolean verifyPlatformSign(String platformPublicKey, String platfomrIDA, String platformSign, AttestChannelTest attestChannelTest, String ordersn, String startTime) throws UnsupportedEncodingException {
        SM2 sm2 = new SM2Impl();
        //生成加密对象
        ECPoint publicKey = sm2.decodePublicKey(platformPublicKey);

        //签名对象
        SM2Impl.Signature signature = SM2Impl.Signature.genByBase64(platformSign);


        Map<String,String> parameters = new HashMap<>();
        parameters.put("requestTime", StringUtils.isBlank(attestChannelTest.getRequestTime())?"": attestChannelTest.getRequestTime());
        parameters.put("channelId", StringUtils.isBlank(attestChannelTest.getChannelId())?"": attestChannelTest.getChannelId());
        parameters.put("channelUserid", StringUtils.isBlank(attestChannelTest.getChannelUserid())?"": attestChannelTest.getChannelUserid());
        parameters.put("channelOrdersn", StringUtils.isBlank(attestChannelTest.getChannelOrdersn())?"": attestChannelTest.getChannelOrdersn());
        parameters.put("ordersn", StringUtils.isBlank(ordersn)?"":ordersn);
        parameters.put("chained", StringUtils.isBlank(attestChannelTest.getChained())?"": attestChannelTest.getChained());
        parameters.put("bizType", StringUtils.isBlank(attestChannelTest.getBizType())?"": attestChannelTest.getBizType());
        parameters.put("bizSign", StringUtils.isBlank(attestChannelTest.getBizSign())?"": attestChannelTest.getBizSign());

        parameters.put("fileName", StringUtils.isBlank(attestChannelTest.getFileName())?"": attestChannelTest.getFileName());
        parameters.put("fileType", StringUtils.isBlank(attestChannelTest.getFileType())?"": attestChannelTest.getFileType());
        parameters.put("fileSize", StringUtils.isBlank(attestChannelTest.getFileSize())?"": attestChannelTest.getFileSize());
        parameters.put("fileHash", StringUtils.isBlank(attestChannelTest.getFileHash())?"": attestChannelTest.getFileHash());

        parameters.put( "ownerType", StringUtils.isBlank(attestChannelTest.getOwnerType())?"": attestChannelTest.getOwnerType());
        parameters.put( "ownerId", StringUtils.isBlank(attestChannelTest.getOwnerId())?"": attestChannelTest.getOwnerId());
        parameters.put( "ownerName", StringUtils.isBlank(attestChannelTest.getOwnerName())?"": attestChannelTest.getOwnerName());

        parameters.put("duration", StringUtils.isBlank(attestChannelTest.getDuration())?"": attestChannelTest.getDuration());
        parameters.put("startTime", StringUtils.isBlank(startTime)?"":startTime);
        parameters.put("description", StringUtils.isBlank(attestChannelTest.getDescription())?"": attestChannelTest.getDescription());


        //参数排序
        List<String> arrayList = new ArrayList<>(parameters.keySet());
        Collections.sort( arrayList );

        String finalParams="";
        StringBuilder sb = new StringBuilder();
        for( String s :arrayList){
            sb.append("&")
                    .append(s)
                    .append("=")
                    .append(parameters.get(s));
        }
        finalParams = sb.toString();
        log.info("排序后的platformSign:{}",finalParams);

        //以utf-8形式获取bytes 数组
        byte[] pbytes = finalParams.getBytes("UTF-8");
        log.info("platformSign  bytes:{}", Hex.encode(pbytes));

        //开始校验
        log.info("存证校验订单签名==========================pbyte={},sign={},platfomrIDA={},publicKey={}",Hex.encode(pbytes),
                signature.toBase64(),
                platfomrIDA,
                platformPublicKey);
        return  sm2.verify(pbytes,
                signature,
                platfomrIDA,
                publicKey);
    }
}
