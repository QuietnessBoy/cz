package test.com.zjl.tech.common;

import com.zhjl.tech.common.utils.post.PostResult;
import com.zhjl.tech.common.utils.post.PostTool;

import java.io.IOException;

public class PostTest {
    public static void main(String[] args) throws IOException {
        PostResult postResult = PostTool.solve("http://www.baidu.com","application/json","{}" );

        System.out.println(postResult.statusCode);
        System.out.println(postResult.content);
    }
}
