package com.zhjl.tech.common.zjlsign.query;

import com.alibaba.fastjson.JSONObject;
import com.zhjl.tech.common.exception.CommonException;
import com.zhjl.tech.common.dto.interfaces.DigitalCertificateDto;
import com.zhjl.tech.common.dto.interfaces.DownloadFileDto;
import com.zhjl.tech.common.dto.interfaces.QueryOrderDto;
import com.zhjl.tech.common.encrypt.bytetool.Hex;
import com.zhjl.tech.common.encrypt.digest.Hash;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author wind
 *
 */
@Slf4j
public class QueryRequestSign {

    /**
     * 查询订单请求sign
     *
     * @param queryOrderDto
     */
    public static String genQueryDataSign(QueryOrderDto queryOrderDto, String publicKey) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("accessKey", StringUtils.isBlank(queryOrderDto.getAccessKey())?"": queryOrderDto.getAccessKey());
        parameters.put("random",StringUtils.isBlank(queryOrderDto.getRandom())?"": queryOrderDto.getRandom());
        parameters.put("channelId", StringUtils.isBlank(queryOrderDto.getChannelId())?"": queryOrderDto.getChannelId());
        parameters.put("ordersn", StringUtils.isBlank(queryOrderDto.getOrdersn())?"": queryOrderDto.getOrdersn());

        List<String> arrayList = new ArrayList<>(parameters.keySet());
        Collections.sort(arrayList);

        String fianlStr = "";
        StringBuilder sb = new StringBuilder();
        for (String s : arrayList) {
            sb.append("&").append(s).append("=").append(parameters.get(s));
        }
        sb.append("&publicKey=").append(publicKey);
        fianlStr = sb.toString();

        log.info("finalstr = {}", fianlStr);

        String sign = null;
        try {
            byte[] bs = fianlStr.getBytes("utf-8");
            sign = Base64.getEncoder().encodeToString(Hash.getHashMD5(bs));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        log.info("sign = {}", sign);

        return sign;
    }

    /**
     * 根据数字证书/存证人ID 生成的查询订单时的请求sign
     *
     * @param digitalCertificateDto
     */
    public static String genDigitalCertificateSign(DigitalCertificateDto digitalCertificateDto, String publicKey) {
        log.info("请求参数:digitalCertificateDemo:{},publick:{}", JSONObject.toJSONString(digitalCertificateDto),publicKey);
        Map<String, String> parameters = new HashMap<>();
        parameters.put("accessKey", StringUtils.isBlank(digitalCertificateDto.getAccessKey())?"": digitalCertificateDto.getAccessKey());
        parameters.put("random", StringUtils.isBlank(digitalCertificateDto.getRandom())?"": digitalCertificateDto.getRandom());
        parameters.put("channelId", StringUtils.isBlank(digitalCertificateDto.getChannelId())?"": digitalCertificateDto.getChannelId());
        parameters.put("ownerId", StringUtils.isBlank(digitalCertificateDto.getOwnerId())?"": digitalCertificateDto.getOwnerId());
        parameters.put("fileHash", StringUtils.isBlank(digitalCertificateDto.getFileHash())?"": digitalCertificateDto.getFileHash());

        //参数排序
        List<String> arrayList = new ArrayList<>(parameters.keySet());
        Collections.sort( arrayList );

        String toSign;
        StringBuilder sb = new StringBuilder();
        for( String s :arrayList){
            sb.append("&")
                    .append(s)
                    .append("=")
                    .append(parameters.get(s));
        }

        //拼接publicKey
        sb = sb.append("&publicKey=").append(publicKey);
        toSign = sb.toString();

        //排序好的参数
        log.info("排序后的查询请求订单参数:{}", toSign);

        //以utf-8形式获取bytes 数组
        byte[] pbytes = new byte[0];
        try {
            pbytes = toSign.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("获取byte数组失败");
        }
        log.info("查询请求参数的bytes:{}", Hex.encode(pbytes));

        //生成md5摘要
        byte[] rs = Hash.getHashMD5(pbytes);

        //编码
        String sign = Base64.getEncoder().encodeToString(rs);

        //签名字符串
        log.info("存证请求参数sign:{}",sign);
        return sign;
    }

    /**
     * 校验下载源文件请求sign
     */
    public static String genDownLoadSign(DownloadFileDto downloadFileDto, String channelPublicKey) {

        Map<String, String> parameters = new HashMap<>();
        parameters.put("random", StringUtils.isBlank(downloadFileDto.getRandom()) ? "" : downloadFileDto.getRandom());
        parameters.put("accessKey", StringUtils.isBlank(downloadFileDto.getAccessKey()) ? "" : downloadFileDto.getAccessKey());
        parameters.put("ordersn", StringUtils.isBlank(downloadFileDto.getOrdersn()) ? "" : downloadFileDto.getOrdersn());
        parameters.put("channelId", StringUtils.isBlank(downloadFileDto.getChannelId()) ? "" : downloadFileDto.getChannelId());

        //参数排序
        List<String> arrayList = new ArrayList<>(parameters.keySet());
        Collections.sort(arrayList);

        String toSign;
        StringBuilder sb = new StringBuilder();
        for (String s : arrayList) {
            sb.append("&")
                    .append(s)
                    .append("=")
                    .append(parameters.get(s));
        }
        //拼接publicKey
        sb = sb.append("&publicKey=").append(channelPublicKey);
        toSign = sb.toString();
        //排序好的参数
        log.info("排序后的存证请求参数:{}", toSign);

        //以utf-8形式获取bytes 数组
        byte[] pbytes;
        try {
            pbytes = toSign.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("获取byte数组失败");
            throw new CommonException("genDownLoadSign的时候出现异常UnsupportedEncodingException，ordersn=" + downloadFileDto.getOrdersn());
        }
        log.info("存证请求参数的bytes:{}", Hex.encode(pbytes));

        //生成md5摘要
        byte[] rs = Hash.getHashMD5(pbytes);

        //编码
        String sign = Base64.getEncoder().encodeToString(rs);

        return sign;
    }
}
