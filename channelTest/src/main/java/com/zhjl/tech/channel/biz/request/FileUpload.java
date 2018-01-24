package com.zhjl.tech.channel.biz.request;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Slf4j
@Service
public class FileUpload {
    public String uploadFile(String URL,String signType, String sign,String accessKey, String random, String ordersn, String fileToken, String filepath){
        URI url = null;
        try {
            url = new URIBuilder(URL).
                    setParameter("signType", signType).
                    setParameter("sign", sign).
                    setParameter("accessKey", accessKey).
                    setParameter("random", random).
                    setParameter("ordersn", ordersn).
                    setParameter("fileToken", fileToken).build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            log.warn("发送消息失败。",e);
        }
        FileBody file = new FileBody(new File(filepath));
        HttpEntity reqEntity = MultipartEntityBuilder.create().addPart("file", file).build();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost(url);

        try {
            httppost.setEntity(reqEntity);
            HttpResponse response = httpclient.execute(httppost);
            if (response.getStatusLine().getStatusCode() == 200) {
                System.out.println("服务器正常响应!!!!");
            }
            httpclient.close();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getFilename();
    }
}
