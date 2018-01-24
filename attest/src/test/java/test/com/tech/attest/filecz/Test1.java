package test.com.tech.attest.filecz;

import com.zhjl.tech.attest.annotationdemo.atlog.ZhijlLog;
import com.zhjl.tech.attest.biz.filecz.FileCzBiz;
import com.zhjl.tech.attest.biz.filehashcz.FileHashCzBiz;
import com.zhjl.tech.attest.biz.filehashcz.FileHashCzNotify;
import com.zhjl.tech.attest.model.attest.Attest;
import org.junit.Test;
import test.com.tech.attest.base.BaseTest;

import javax.annotation.Resource;

public class Test1 extends BaseTest {

    @Resource
    FileCzBiz fileCzBiz;

    @Test
    public void a(){
        String a = "aa2dda122211a";
//        fileCzBiz.solveNormal(a);
        fileCzBiz.solveFileCz(a);
//        fileCzBiz.test();
    }

    @Resource
    FileHashCzNotify fileHashCzNotify;

    @Test
    @ZhijlLog
    public void c(){
        Attest attest = new Attest();
        attest.setVersion("0.9");
        fileHashCzNotify.HashCzNotify(attest);
    }

    @Test
    public void b(){
       c();
    }
}
