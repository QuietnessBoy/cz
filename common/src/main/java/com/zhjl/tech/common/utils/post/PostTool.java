package com.zhjl.tech.common.utils.post;

import com.zhjl.tech.common.constant.SysConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.client.methods.HttpPost;

@Slf4j
public class PostTool {
    /**
     *
     * @param url 请求的url
     * @param contenType "application/json"，或者其他
     * @param sendBody 请求体字符串
     * @return 结果对象
     * @throws IOException
     */
    public static PostResult solve(String url,String contenType, String sendBody ) throws IOException {

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);

        //设置传输格式
        post.setHeader("Content-Type", contenType);
        post.addHeader("Authorization", "Basic YWRtaW46");

        //以utf-8形式编码
        StringEntity s = new StringEntity(sendBody, SysConfig.CharsetName);
        s.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, contenType));
        post.setEntity(s);

        // 发送请求
        HttpResponse response = client.execute(post);

        // 获取响应输入流
        InputStream inStream = response.getEntity().getContent();
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                    inStream, SysConfig.CharsetName));

        StringBuilder strber = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            strber.append(line).append("\n");
        }
        inStream.close();

        String result = strber.toString();
        log.info("http post result={}", result);

        PostResult postResult = new PostResult();
        postResult.statusCode = response.getStatusLine().getStatusCode();
        postResult.content = result;

        return postResult;
    }

}
