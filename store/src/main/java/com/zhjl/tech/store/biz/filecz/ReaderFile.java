package com.zhjl.tech.store.biz.filecz;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.zhjl.tech.common.exception.StoreBizException;
import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.constant.JsonConfig;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.encrypt.digest.SM3;
import com.zhjl.tech.common.encrypt.digest.SM3Impl;
import com.zhjl.tech.store.model.store.Attest;
import com.zhjl.tech.store.model.store.AttestFile;
import com.zhjl.tech.store.service.store.IAttestFileService;
import com.zhjl.tech.store.util.oss.OssUpload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

/**
 * 读取文件流信息
 */
@Service
@Slf4j
public class ReaderFile {

    /**
     * 写入文件,保存本地
     *
     * @param inputStream
     * @param attest
     * @return
     * @throws IOException
     */
    @Transactional(rollbackFor = Exception.class)
    public void saveFileToLocalOss(InputStream inputStream, Attest attest) throws IOException {
        // endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。
        String endpoint = comBizConfig.getOss_endpoint();
        // accessKeyId和accessKeySecret是OSS的访问密钥，您可以在控制台上创建和查看，
        String accessKeyId = comBizConfig.getOss_accessKeyId();
        String accessKeySecret = comBizConfig.getOss_accessKeySecret();
        String bucketName = comBizConfig.getBucketName();
        //生成上传OSS文件key
        String random = UUID.randomUUID().toString().replaceAll("-","");

        //文件标识
        String fileAddr = new StringBuilder().append(attest.getOrdersn())
                .append("-").append(random).toString();
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        //初始化一个Multipart Upload事件。该操作会返回一个OSS服务器创建的全局唯一的Upload ID，用于标识本次Multipart Upload事件。
        // 用户可以根据这个ID来发起相关的操作，如中止Multipart Upload、查询Multipart Upload等。
        InitiateMultipartUploadRequest request = new InitiateMultipartUploadRequest(bucketName, fileAddr);
        InitiateMultipartUploadResult result = ossClient.initiateMultipartUpload(request);

        String uploadId = result.getUploadId();
        List<PartETag> partETags = new ArrayList<PartETag>();

        long fileSize = 0L;
        int i = 1;//分片上传分片号

        SM3 sm3 = new SM3Impl();
        byte[] bytes;

        bytes = new byte[SIZE];
        int length;
        OssUpload ossUpload = new OssUpload();
        ossUpload.init(SIZE, ossClient, bucketName, fileAddr, uploadId, partETags);
        while ((length = inputStream.read(bytes)) != -1) {
            ossUpload.update(bytes, length, i);
            sm3.update(bytes, 0, length);
            //计算字节长度
            fileSize += length;

            i++;
        }

        ossUpload.end(i);
        // 关闭流
        inputStream.close();

        // 计算文件摘要
        String fileHash = Base64.getEncoder().encodeToString(sm3.digest());

        // 根据返回的字节数判断是否与用户填写的fileSize是否匹配
        if (!Long.valueOf(attest.getFileSize()).equals(fileSize)) {
            log.warn("文件字节数不匹配,ordersn={},源文件:{},OSS文件:{}",attest.getOrdersn(), attest.getFileSize(), fileSize);
            // 删除文件
            ossClient.deleteObject(bucketName, fileAddr);
            log.info("删除文件，ordersn={}",attest.getOrdersn());

            //失败回调
            fileCzNotify.fileCzFiledNotify(attest,JsonConfig.ErrorOssFileSize,JsonConfig.ErrorOssFileSizeDesc);
            throw new StoreBizException(SysConfig.FileSizeException+",ordersn=" + attest.getOrdersn());
        }

        // 校验文件摘要是否匹配
        if (!attest.getFileHash().equals(fileHash)) {
            log.warn("文件摘要不匹配.ordersn={},渠道方:{},知金链:{}",attest.getOrdersn(), attest.getFileHash(), fileHash);
            // 删除文件!!!
            ossClient.deleteObject(bucketName, fileAddr);

            //失败回调
            fileCzNotify.fileCzFiledNotify(attest,JsonConfig.ErrorOssFileHash,JsonConfig.ErrorOssFileHashDesc);
            throw new StoreBizException(SysConfig.HashException+",ordersn=" + attest.getOrdersn());
        }

        // 更新AttestFile表
        AttestFile attestFile = attestFileService.getAttestFileByOrdersn(attest.getOrdersn());
        attestFile.setFileAddr(fileAddr);
        attestFile.setBucketName(bucketName);
        attestFileService.updateSelectiveById(attestFile);

        log.info("文件保存成功.ordersn={}", attest.getOrdersn());
    }


    @Resource
    private IAttestFileService attestFileService;

    @Resource
    private FileCzNotify fileCzNotify;

    @Resource
    private ComBizConfig comBizConfig;

    private static final int SIZE = 1024 * 1024;
}

