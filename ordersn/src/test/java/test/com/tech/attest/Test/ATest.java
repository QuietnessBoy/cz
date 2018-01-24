package test.com.tech.attest.Test;

import com.zhjl.tech.ordersn.biz.GenOrdersn;
import com.zhjl.tech.ordersn.service.ordersn.IAttestService;
import org.junit.Test;
import test.com.tech.attest.base.BaseTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ATest extends BaseTest {

    @Resource
    IAttestService attestService;

    @Test
    public void a() throws IOException {

        String dateStr = new SimpleDateFormat("yyMMdd").format(new Date());
        String nextStr =null;
        String ordersn = null;
        StringBuilder sb = new StringBuilder();

    }
}
