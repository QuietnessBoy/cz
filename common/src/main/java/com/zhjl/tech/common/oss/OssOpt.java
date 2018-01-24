package com.zhjl.tech.common.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.constant.JsonConfig;
import com.zhjl.tech.common.dto.JsonResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;


@Slf4j
@Component
public class OssOpt {

    @Resource
    private ComBizConfig comBizConfig;

    private int tempByteSize = 1024 * 2;

    /**
     * 从阿里云下载文件 （以附件形式下载）
     * @param fileKey
     * @param filename
     * @param response 获取文件成功时，直接将流写入此处
     * @return JsonResult 获取文件异常时，将异常写入此处并返回
     */
    public JsonResult downLoadFile(String fileKey, String filename, HttpServletResponse response) {
        try {
            // endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。
            String endpoint = comBizConfig.getOss_endpoint();

            // accessKeyId和accessKeySecret是OSS的访问密钥，您可以在控制台上创建和查看，
            String accessKeyId = comBizConfig.getOss_accessKeyId();
            String accessKeySecret = comBizConfig.getOss_accessKeySecret();
            OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);
            String bucketName = comBizConfig.getBucketName();
            //获取fileid对应的阿里云上的文件对象
            OSSObject ossObject = ossClient.getObject(bucketName, fileKey);//bucketName需要自己设置

            // 读去Object内容  返回
            BufferedInputStream in = new BufferedInputStream(ossObject.getObjectContent());

            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            //通知浏览器以附件形式下载
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "utf-8"));

            byte[] car = new byte[tempByteSize];
            int L;
            while ((L = in.read(car)) != -1) {
                out.write(car, 0, L);
            }
            if (out != null) {
                out.flush();
                out.close();
            }
            if (in != null) {
                in.close();
            }
            ossClient.shutdown();
            log.info("下载完毕.");
            //用户看不到这个返回结果
            JsonResult jsonResult = new JsonResult();
            jsonResult.setSuccess(true);
            return jsonResult;
        } catch (IOException e) {
            log.error("下载源文件异常.",e);
            return new JsonResult(false,JsonConfig.SystemExceptionDesc, JsonConfig.SystemException,null);
        }
    }
}
