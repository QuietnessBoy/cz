package com.zhjl.tech.channel.tool.oss;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.OSSObjectSummary;
import com.aliyun.oss.model.ObjectListing;
import com.zhjl.tech.channel.configs.Configs;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class HelloOSS {

    // endpoint是访问OSS的域名。如果您已经在OSS的控制台上 创建了Bucket，请在控制台上查看域名。
    private static String endpoint = "oss-cn-beijing.aliyuncs.com";//State.oss_endpoint;
    // accessKeyId和accessKeySecret是OSS的访问密钥，您可以在控制台上创建和查看，
    private static String accessKeyId = "LTAIBZFtpYe3Cmdn";//State.oss_accessKeyId;
    private static String accessKeySecret = "HBLurx6XLwk7uhhGY0wYirAiRQLZ3J";//State.oss_accessKeySecret;

    // Bucket用来管理所存储Object的存储空间，详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
    // Bucket命名规范如下：只能包括小写字母，数字和短横线（-），必须以小写字母或者数字开头，长度必须在3-63字节之间。
    private static String bucketName = "testcz";//State.bucketName;//zjloss

    // Object是OSS存储数据的基本单元，称为OSS的对象，也被称为OSS的文件。详细描述请参看“开发人员指南 > 基本概念 > OSS基本概念介绍”。
    // Object命名规范如下：使用UTF-8编码，长度必须在1-1023字节之间，不能以“/”或者“\”字符开头。
    //为下载时的文件名，需带文件后缀
    private static String firstKey = Configs.firstKey;

    public static void main(String[] args) {

        // 生成OSSClient，您可以指定一些参数，详见“SDK手册 > Java-SDK > 初始化”，
        OSSClient ossClient = new OSSClient(endpoint, accessKeyId, accessKeySecret);

        try {

            // 判断Bucket是否存在。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
//            if (ossClient.doesBucketExist(bucketName)) {
//                System.out.println("您已经创建Bucket：" + bucketName + "。");
//            } else {
//                System.out.println("您的Bucket不存在，创建Bucket：" + bucketName + "。");
//                // 创建Bucket。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
//                ossClient.createBucket(bucketName);
//            }

            // 查看Bucket信息。详细请参看“SDK手册 > Java-SDK > 管理Bucket”。
//            BucketInfo info = ossClient.getBucketInfo(bucketName);
//            System.out.println("Bucket " + bucketName + "的信息如下：");
//            System.out.println("\t数据中心：" + info.getBucket().getLocation());
//            System.out.println("\t创建时间：" + info.getBucket().getCreationDate());
//            System.out.println("\t用户标志：" + info.getBucket().getOwner());

            // 把字符串存入OSS，Object的名称为firstKey。详细请参看“SDK手册 > Java-SDK > 上传文件”。
//            InputStream is = new ByteArrayInputStream("Hello OSS".getBytes());
//            ossClient.putObject(bucketName, firstKey, is);
//            System.out.println("Object：" + firstKey + "存入OSS成功。");

            // 下载文件。详细请参看“SDK手册 > Java-SDK > 下载文件”。---读取到文件信息
//            OSSObject ossObject = ossClient.getObject(bucketName, firstKey);
//            InputStream inputStream = ossObject.getObjectContent();
//            StringBuilder objectContent = new StringBuilder();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
//            while (true) {
//                String line = reader.readLine();
//                if (line == null)
//                    break;
//                objectContent.append(line);
//            }
//            inputStream.close();
//            System.out.println("Object：" + firstKey + "的内容是：" + objectContent);

            // 文件存储入OSS，Object的名称为fileKey。详细请参看“SDK手册 > Java-SDK > 上传文件”。
//            String fileKey = "文档.docx";
//            ossClient.putObject(bucketName, fileKey, new File("D://文档.docx"));
//            System.out.println("Object：" + fileKey + "存入OSS成功。");

            // 查看Bucket中的Object。详细请参看“SDK手册 > Java-SDK > 管理文件”。
//            ObjectListing objectListing = ossClient.listObjects(bucketName);
//            List<OSSObjectSummary> objectSummary = objectListing.getObjectSummaries();
//            System.out.println("您有以下Object：");
//            for (OSSObjectSummary object : objectSummary) {
//                System.out.println("\t" + object.getKey());
//            }
            // 删除Object。详细请参看“SDK手册 > Java-SDK > 管理文件”。
//            ossClient.deleteObject(bucketName, "005321171116000134b290d1cd-b9c5-45a7-90c0-88037f525d57");
//            System.out.println("删除Object：" + firstKey + "成功。");
//            ossClient.deleteObject(bucketName, "123");
//            System.out.println("删除Object：" + firstKey + "成功。");

            // 下载地址>>>设置URL过期时间为半小时
//            Date expiration = new Date(System.currentTimeMillis() + 3600 * 500);
            // 生成URL
//            URL url = ossClient.generatePresignedUrl(bucketName, firstKey, expiration);
//            System.out.println(firstKey+"的URL是："+url.toString());

//            //获取URL的文件保存在里设置的路径中
//            downLoadFromUrl(url,firstKey,"D://OSS//");

        } catch (OSSException oe) {
            oe.printStackTrace();
        } catch (ClientException ce) {
            ce.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ossClient.shutdown();
        }
    }

    /**
     * 解析URL下载文件
     */
    public static void  downLoadFromUrl(URL url,String fileName,String savePath) throws IOException{
        //URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection)url.openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3*1000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");

        //得到输入流
        InputStream inputStream = conn.getInputStream();
        //获取自己数组
        byte[] getData = readInputStream(inputStream);

        //文件保存位置
        File saveDir = new File(savePath);
        if(!saveDir.exists()){
            saveDir.mkdir();
        }
        File file = new File(saveDir+File.separator+fileName);
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(getData);
        if(fos!=null){
            fos.close();
        }
        if(inputStream!=null){
            inputStream.close();
        }
    }
    /**
     * 从输入流中获取字节数组
      */
    public static  byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }

}
