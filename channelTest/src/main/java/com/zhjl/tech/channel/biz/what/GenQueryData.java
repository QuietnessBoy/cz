package com.zhjl.tech.channel.biz.what;

import com.zhjl.tech.channel.tool.Tool;
import com.zhjl.tech.common.dto.interfaces.QueryOrderDto;
import com.zhjl.tech.common.utils.gen.query.GenQueryDatas;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * ps:创建表的时候,可以考虑加上测试名称
 *
 * 根据本次测试的名称[testName]
 * 1 生成n条测试
 * 2 生成对应的文件
 * 3 将对应的数据插入到tables
 */

/**
 * 自动生成文件Hash存证请求参数
 */
@Slf4j
@Service
public class GenQueryData {

    @Resource
    Tool tool;

    public String genPostmanMode(String ordersn){
        QueryOrderDto queryOrderDto = genData(ordersn);  //生成参数信息
        if(queryOrderDto ==null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb = sb.append("</br>")
                .append("signType:").append(queryOrderDto.getSignType()).append("</br>")
                .append("sign:").append(queryOrderDto.getSign()).append("</br>")
                .append("accessKey:").append(queryOrderDto.getAccessKey()).append("</br>")
                .append("random:").append(queryOrderDto.getRandom()).append("</br>")
                .append("channelId:").append(queryOrderDto.getChannelId()).append("</br>")
                .append("ordersn:").append(ordersn)
                .append("</br>");
        log.info("@@@@@@@@@@@@@@{}",sb);
        return sb.toString();
    }


    public String genUrlencodeMode(String ordersn){
        QueryOrderDto queryOrderDto = genData(ordersn);
        if(queryOrderDto ==null){
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try {
            sb = sb.append("signType=").append(URLEncoder.encode(queryOrderDto.getSignType(),"UTF-8"))
                    .append("&sign=").append(URLEncoder.encode(queryOrderDto.getSign(),"UTF-8"))
                    .append("&accessKey=").append(URLEncoder.encode(queryOrderDto.getAccessKey(),"UTF-8"))
                    .append("&random=").append(URLEncoder.encode(queryOrderDto.getRandom(),"UTF-8"))
                    .append("&channelId=").append(URLEncoder.encode(queryOrderDto.getChannelId(),"UTF-8"))
                    .append("&ordersn=").append(URLEncoder.encode(queryOrderDto.getOrdersn(),"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            log.error("生成查询订单请求参数错误.",e);
        }
        log.info("@@@@@@@@@@@@@@{}",sb);
        return sb.toString();
    }

    /**
     * 自动生成数据
     * @return
     * @throws IOException
     */
    public QueryOrderDto genData(String ordersn){
        QueryOrderDto queryOrderDto = genQueryDatas.gen(ordersn);
        return queryOrderDto;
    }

    @Resource
    private GenQueryDatas genQueryDatas;
}
