package com.zhjl.tech.common.utils;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.zhjl.tech.common.configs.ComBizConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;
import java.net.URL;
import java.util.Date;


@Slf4j
@Service
public class OssTool {

    @Resource
    private ComBizConfig comBizConfig;

    // Object是OSS存储数据的基本单元，称为OSS的对象，也被称为OSS的文件。详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
    // Object命名规范如下：使用UTF-8编码，长度必须在1-1023字节之间，不能以“/”或者“\”字符开头。
    //为下载时的文件名，需带文件后缀

    /**
     *  上传OSS,获取下载文件地址
     * @param fileName  文件名，相当于fileKey
     */
    public String upload(String fileName,String sourceFilePath) {

        // endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。
        String endpoint = comBizConfig.getOss_endpoint();

        // accessKeyId和accessKeySecret是OSS的访问密钥，您可以在控制台上创建和查看，
        String accessKeyId =comBizConfig.getOss_accessKeyId();
        String accessKeySecret = comBizConfig.getOss_accessKeySecret();

        // Bucket用来管理所存储Object的存储空间，详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
        // Bucket命名规范如下：只能包括小写字母，数字和短横线（-），必须以小写字母或者数字开头，长度必须在3-63字节之间。
        String bucketName = comBizConfig.getBucketName();

        URL url = null;

        // 生成OSSClient，您可以指定一些参数，详见“SDK手册 > Java-SDK > 初始化”，
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
        try {

            // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
            if (!ossClient.doesBucketExist(bucketName)) {
                log.info("您的Bucket不存在，创建Bucket：" + bucketName );
                // 创建Bucket。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
                ossClient.createBucket(bucketName);
            }

            // 把字符串存入OSS，Object的名称为firstKey。详细请参看“SDK手册 > Java-SDK > 上传文件”。
            String fileKey = fileName;
            ossClient.putObject(bucketName, fileKey, new File(sourceFilePath));
            log.info("Object：" + fileKey + "存入OSS成功。");

            // 下载地址>>>设置URL过期时间为半小时
            Date expiration = new Date(System.currentTimeMillis() + 1000 * 3600 * 24*20);

            // 生成URL
            url = ossClient.generatePresignedUrl(bucketName, fileKey, expiration);
            log.info("{}的URL是：{}",fileName,url.toString());
        }catch (OSSException oe) {
            oe.printStackTrace();
            log.error("获取OSS数据失败");
        } finally {
            ossClient.shutdown();
        }
        return url.toString();
    }
}
