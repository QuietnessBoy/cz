package test.com.tech.store.base.fileCz;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.*;
import com.zhjl.tech.common.configs.ComBizConfig;
import com.zhjl.tech.common.encrypt.digest.SM3;
import com.zhjl.tech.common.encrypt.digest.SM3Impl;
import com.zhjl.tech.store.biz.filecz.FileCzFileBiz;
import com.zhjl.tech.store.biz.filecz.ReaderFile;
import com.zhjl.tech.store.service.store.IAttestService;
import com.zhjl.tech.store.util.oss.OssUpload;
import org.junit.Test;
import test.com.tech.store.base.BaseTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class FileCzFileBizOldTest extends BaseTest {

    @Resource
    private ComBizConfig comBizConfig;

    @Test
    public void te() throws IOException {
//        CreateFileDataMessage createFileTokenDataMessage = new CreateFileDataMessage();
//        createFileTokenDataMessage.setOrdersn("005351171118000031");
//        solveFile.solveFile(createFileTokenDataMessage);

        // 获取oss地址

        // guo
//        URL url = new URL("http://osstestoss.oss-cn-beijing.aliyuncs.com/6M-ordersnc076a4a92?Expires=1512959682&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=df3ijqsfTOi%2F05w4CiJ%2BLv6Alig%3D");
        // zjl
//        URL url = new URL("http://osstestoss.oss-cn-beijing.aliyuncs.com/yyyyyy-ordersnadeab04d3?Expires=1512965136&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=a1X%2BYsuzicpeH%2B3iD63xyCrBwRc%3D");
        URL url = new URL("http://www.ifeng.com");

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        //设置超时间为3秒
        conn.setConnectTimeout(3000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //判断超时

        //得到输入流
        InputStream inputStream = conn.getInputStream();

        //写入文件，获取Hash和字节数并进行计算
//            readerFile.readStream(inputStream, null);


        // endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。
        String endpoint = comBizConfig.getOss_endpoint();
        // accessKeyId和accessKeySecret是OSS的访问密钥，您可以在控制台上创建和查看，
        String accessKeyId = comBizConfig.getOss_accessKeyId();
        String accessKeySecret = comBizConfig.getOss_accessKeySecret();
        String bucketName = comBizConfig.getBucketName();
        //生成上传OSS文件key
        String random = UUID.randomUUID().toString().replaceAll("-","");
        String fileAddr = "abccc";// attest.getOrdersn()+random;

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

        bytes = new byte[102400];
        int length;

//        UploadPartRequest uploadPartRequest = new UploadPartRequest();
//        uploadPartRequest.setBucketName(bucketName);
//        uploadPartRequest.setKey(fileAddr);
//        uploadPartRequest.setUploadId(uploadId);
        OssUpload ossUpload = new OssUpload();
        ossUpload.init(1024*100,ossClient,bucketName,fileAddr,uploadId,partETags);
        while ((length = inputStream.read(bytes)) != -1) {
            System.out.println("长度：" + length);
            //收到bytes
            ossUpload.update(bytes, length,i);
            //采用分片上传
//            uploadPartRequest.setInputStream(new ByteArrayInputStream(bytes));
//            // 设置分片号，范围是1~10000，
//            uploadPartRequest.setPartSize(length);
//            uploadPartRequest.setPartNumber(i);
//            UploadPartResult uploadPartResult = ossClient.uploadPart(uploadPartRequest);
//            partETags.add(uploadPartResult.getPartETag());
//            i++;
//            sm3.update(bytes, 0, length);
            //计算字节长度
            fileSize += length;
        }
        System.out.println("总长度：" + fileSize);
        ossUpload.end(i);
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

        // 关闭流
        inputStream.close();


    }

}
