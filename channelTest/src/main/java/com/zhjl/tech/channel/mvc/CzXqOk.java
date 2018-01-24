package com.zhjl.tech.channel.mvc;

import com.zhjl.tech.channel.configs.Configs;
import com.zhjl.tech.channel.controller.BaseController;
import com.zhjl.tech.channel.model.channel.AttestChannelTest;
import com.zhjl.tech.channel.service.channel.IAttestChannelTestService;
import com.zhjl.tech.common.dto.interfaces.AttestXqDto;
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
public class CzXqOk extends BaseController {

    @Resource
    IAttestChannelTestService attestChannelTestService;

    /**
     * 接收存证平台fileToken,并上传存证文件
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping("/CzXqOk")
    public void xq(HttpServletRequest request) throws UnsupportedEncodingException, ParseException {
        JsonResult jsonResult = new JsonResult();
        String signType = request.getParameter("signType");
        String sign = request.getParameter("sign");
        String random = request.getParameter("random");
        String ordersn = request.getParameter("ordersn");
        String startTime = request.getParameter("startTime");
        String duration = request.getParameter("duration");
        String platformSign = request.getParameter("platformSign");
        String channelOrdersn = request.getParameter("channelOrdersn");
        String success = request.getParameter("success");
        String state = request.getParameter("state");
        String msg = request.getParameter("msg");

        String PlatFromIda = "zhijinlian_blockchain_cz2017";
        String PlatformPublicKey="04329b15fef0019bfefb95747dbe5e74fa0b9114dfe084baa68fe4b137206872757405a64150490e33158f91ae6ba76553b4f11d5b5b1520c8038f2b779fd79548";

        log.info("收到的续期回到参数信息:signType="+signType+
                "sign="+sign+
                "random="+random+
                "ordersn="+ordersn+
                "startTimeDate="+startTime+
                "duration="+duration +
                "platformSign=" + platformSign,
                "channelOrdersn=" + channelOrdersn+
                "success=" + success +
                "state=" + state+
                "msg=" + msg
        );

        AttestChannelTest attestChannelTest = attestChannelTestService.getAttestChannelTestByChannelOrdersn(channelOrdersn);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd-HHmmss");
        attestChannelTest.setStartTime(simpleDateFormat.parse(startTime));
        attestChannelTest.setOrdersn(ordersn);
        boolean verifySign = verifyPlatformSign(PlatformPublicKey,PlatFromIda,platformSign,attestChannelTest,ordersn,startTime);
        if(!verifySign){
            log.info("platformSign校验不正确.{}",verifySign);
            attestChannelTest.setState("file存证完成,platformSign不正确.");
            attestChannelTestService.updateSelectiveById(attestChannelTest);
        }
        log.info("platFormSign:{}",verifySign);
        // 计算expireTime
        Calendar time = Calendar.getInstance();
        time.setTime(simpleDateFormat.parse(startTime));
        time.add(Calendar.DAY_OF_YEAR, Integer.parseInt(attestChannelTest.getDuration()));
        attestChannelTest.setExpiredTime(time.getTime());
        attestChannelTest.setState("platformSign正确.");
        attestChannelTestService.updateSelectiveById(attestChannelTest);
    }



    /**
     *  检验platformsign
     * @param platformPublicKey
     * @param platfomrIDA
     * @param platformSign
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
        parameters.put("channelId",  StringUtils.isBlank(attestChannelTest.getChannelId())?"": attestChannelTest.getChannelId());
        parameters.put("channelUserid", StringUtils.isBlank(attestChannelTest.getChannelUserid())?"": attestChannelTest.getChannelUserid());
        parameters.put("channelOrdersn", StringUtils.isBlank(attestChannelTest.getChannelOrdersn())?"": attestChannelTest.getChannelOrdersn());
        parameters.put("chained", StringUtils.isBlank(attestChannelTest.getChained())?"": attestChannelTest.getChained());
        parameters.put("bizType", StringUtils.isBlank(attestChannelTest.getBizType())?"": attestChannelTest.getBizType());

        parameters.put("fileName", StringUtils.isBlank(attestChannelTest.getFileName())?"": attestChannelTest.getFileName());
        parameters.put("fileType", StringUtils.isBlank(attestChannelTest.getFileType())?"": attestChannelTest.getFileType());
        parameters.put("fileSize", StringUtils.isBlank(attestChannelTest.getFileSize())?"": attestChannelTest.getFileSize());
        parameters.put("fileHash", StringUtils.isBlank(attestChannelTest.getFileHash())?"": attestChannelTest.getFileHash());

        parameters.put( "ownerType", StringUtils.isBlank(attestChannelTest.getOwnerType())?"": attestChannelTest.getOwnerType());
        parameters.put( "ownerId", StringUtils.isBlank(attestChannelTest.getOwnerId())?"": attestChannelTest.getOwnerId());
        parameters.put( "ownerName", StringUtils.isBlank(attestChannelTest.getOwnerName())?"": attestChannelTest.getOwnerName());

        parameters.put("duration", StringUtils.isBlank(attestChannelTest.getDuration())?"": attestChannelTest.getDuration());
        parameters.put("startTime", StringUtils.isBlank(startTime)?"": startTime);
        parameters.put("description", StringUtils.isBlank(attestChannelTest.getDescription())?"": attestChannelTest.getDescription());

        parameters.put("bizSign", StringUtils.isBlank(attestChannelTest.getBizSign())?"": attestChannelTest.getBizSign());
        parameters.put("ordersn", StringUtils.isBlank(ordersn)?"": ordersn);


        //参数排序
        List<String> arrayList = new ArrayList<>(parameters.keySet());
        Collections.sort( arrayList );


        StringBuilder sb = new StringBuilder();
        for( String s :arrayList){
            sb.append("&")
                    .append(s)
                    .append("=")
                    .append(parameters.get(s));
        }
        String finalParams = sb.toString();
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
