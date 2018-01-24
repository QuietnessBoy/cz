package com.zhjl.tech.store.util.oss;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.CompleteMultipartUploadRequest;
import com.aliyun.oss.model.PartETag;
import com.aliyun.oss.model.UploadPartRequest;
import com.aliyun.oss.model.UploadPartResult;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Slf4j
public class OssUpload {

    private int blockSize;//上传数组标准长度
    private int nowSize = 0;//当前临时数组长度
    private byte[] temp;//临时数组
    private byte[] upbytes;//上传数组
    private OSSClient ossClient;
    private String bucketName;
    private String fileAddr;
    private String uploadId;
    private List<PartETag> partETags;

    public void init(int blockSize,OSSClient ossClient,String bucketName,String fileAddr,String uploadId,List<PartETag> partETags){
        this.blockSize = blockSize;
        temp = new byte[blockSize*2];
        upbytes = new byte[blockSize];
        this.ossClient = ossClient;
        this.bucketName = bucketName;
        this.fileAddr = fileAddr;
        this.uploadId = uploadId;
        this.partETags = partETags;
    }

    // length <= blockSize
    public void update(byte[] bytes, int length,int i){

        //move byte from bytes to tmep
        System.arraycopy(bytes,0,temp,nowSize,length);
        nowSize += length;

        if( nowSize >= blockSize ){
            //开始发送
            //赋值给发送数组
            System.arraycopy(temp,0,upbytes,0,blockSize);

            UploadPartRequest uploadPartRequest = new UploadPartRequest();
            uploadPartRequest.setBucketName(bucketName);
            uploadPartRequest.setKey(fileAddr);
            uploadPartRequest.setUploadId(uploadId);
            uploadPartRequest.setInputStream(new ByteArrayInputStream(upbytes));
            // 设置分片号，范围是1~10000，
            uploadPartRequest.setPartSize(blockSize);
            uploadPartRequest.setPartNumber(i);
            UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
            partETags.add(uploadPartResult.getPartETag());

            //压缩数组
            for( int j = 0; j< nowSize-blockSize;j++){
                temp[j]=temp[j+blockSize];
            }
            nowSize -= blockSize;
        }
    }

    public void end(int i){
        //发送 （temp,nowSzie）
        UploadPartRequest uploadPartRequest = new UploadPartRequest();
        uploadPartRequest.setBucketName(bucketName);
        uploadPartRequest.setKey(fileAddr);
        uploadPartRequest.setUploadId(uploadId);
        uploadPartRequest.setInputStream(new ByteArrayInputStream(temp));
        // 设置分片号，范围是1~10000，
        uploadPartRequest.setPartSize(nowSize);
        uploadPartRequest.setPartNumber(i);
        UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
        partETags.add(uploadPartResult.getPartETag());

        //结束分片任务
        Collections.sort(partETags, new Comparator<PartETag>() {
            @Override
            public int compare(PartETag p1, PartETag p2) {
                return p1.getPartNumber() - p2.getPartNumber();
            }
        });
        CompleteMultipartUploadRequest completeMultipartUploadRequest =
                new CompleteMultipartUploadRequest(bucketName, fileAddr, uploadId, partETags);
        ossClient.completeMultipartUpload(completeMultipartUploadRequest);
    }

}
