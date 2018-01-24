package com.zhjl.tech.common.configs;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Random;

/**
 * 业务数据变量
 */
@Data
@Component
public class ComBizConfig {

    /**
     * OSS相关数据
     */
    @Value("${oss_endpoint}")
    private String oss_endpoint;
    @Value("${bucketName}")
    private String bucketName;
    @Value("${oss_accessKeyId}")
    private String oss_accessKeyId;
    @Value("${oss_accessKeySecret}")
    private String oss_accessKeySecret;

    /**
     * 请求接口地址
     */
    // 文件存证地址
    @Value("${fileCzUrl}")
    private String fileCzUrl;

    // 文件Hash存证地址
    @Value("${fileHashCzUrl}")
    private String fileHashCzUrl;

    // 存证续期地址
    @Value("${xqCzUrl}")
    private String xqCzUrl;

    // 存证查询地址
    @Value("${selectUrl}")
    private String selectUrl;

    // 存证校验地址
    @Value("${verifyUrl}")
    private String verifyUrl;

    // 下载源文件地址
    @Value("${downFileUrl}")
    private String downFileUrl;

    /****************data*****************/
    @Value("${channelId}")
    private String channelId;

    @Value("${channelPublickKey}")
    private String channelPublickKey;

    @Value("${channelPrivateKey}")
    private String channelPrivateKey;

    @Value("${accessKey}")
    private String accessKey;

    @Value("${ida}")
    private String ida;

    @Value("${signType}")
    private String signType;

    @Value("${price}")
    private String price;

    @Value("${dataPath}")
    private String dataPath;

    @Value("${fileData}")
    private String fileData;

    @Value("${hashData}")
    private String hashData;

    @Value("${testFile}")
    private String testFile;
}
