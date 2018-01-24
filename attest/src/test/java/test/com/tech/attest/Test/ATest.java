package test.com.tech.attest.Test;

import com.zhjl.tech.attest.model.attest.Status;
import com.zhjl.tech.attest.model.attest.TempOrder;
import com.zhjl.tech.attest.service.attest.IAttestService;
import com.zhjl.tech.attest.service.attest.IStatusService;
import com.zhjl.tech.attest.service.attest.ITempOrderService;
import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import org.junit.Test;
import test.com.tech.attest.base.BaseTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ATest extends BaseTest {

    @Resource
    IAttestService attestService;

    @Resource
    ITempOrderService tempOrderService;

    @Resource
    IStatusService statusService;

    @Test
    public void init() throws IOException {
        URL url = new URL("http://www.163.com");
        HttpURLConnection conn = null;

        conn = (HttpURLConnection) url.openConnection();

        //设置超时间为3秒
        conn.setConnectTimeout(3000);
        //防止屏蔽程序抓取而返回403错误
        conn.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
        //判断超时

        InputStream inputStream = conn.getInputStream();
//        InputStream inputStream = new FileInputStream("D://store扶我起来.war");
        byte[] bytes = new byte[20];
        int length = -1;
        while ((length = inputStream.read(bytes)) != -1) {
            System.out.println("l=" + length);
            System.out.println(new String(bytes));

        }
//        attestService.deleteAttest("005361171116000127");
    }


    @Test
    public void a() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(SysConfig.Format);
        Calendar time = Calendar.getInstance();
        Date date = simpleDateFormat.parse("20171111-174900");
        time.setTime(date);
        time.add(Calendar.DAY_OF_YEAR, 10);
        Date date1=time.getTime();
        String reStr = simpleDateFormat.format(date1);
    }


    @Test
    public void t(){
        TempOrder tempOrder = tempOrderService.getTempOrderByChannelOrdersn("ordersnd3677dd12");
        System.out.println("-----------id-----------"+tempOrder.getId());
        //删除临时表存证记录
        tempOrderService.deleteSelective(tempOrder);
        System.out.println("删除函数执行完成");


    }
}
