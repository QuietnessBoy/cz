package test.com.tech.store.base.fileCz;

import com.zhjl.tech.common.message.CreateFileDataMessage;
import com.zhjl.tech.store.biz.filecz.FileCzFileBiz;
import com.zhjl.tech.store.service.store.IAttestService;
import org.junit.Test;
import test.com.tech.store.base.BaseTest;

import javax.annotation.Resource;

public class FileCzFileBizTest extends BaseTest {
    @Resource
    FileCzFileBiz fileCzFileBiz;

    @Resource
    IAttestService attestService;


    @Test
    public void te(){
        CreateFileDataMessage createFileDataMessage = new CreateFileDataMessage();
        createFileDataMessage.setOrdersn("005361171211000005");
//        solveFile.solveFile(createFileDataMessage);
//        getOssByFile91.solveFile(createFileDataMessage);
//        getOssByFile91.solveFileCzFile(createFileDataMessage);
//        try {
//            URL url = new URL("https://osstestoss.oss-cn-beijing.aliyuncs.com/%E6%B5%8B%E8%AF%95-ordersn7cfc2fedc?Expires=1509951371&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=pUSwJFmQXTG%2FYwooSAkr9GIUVEw%3D");
//            getOssByFile91.solveFile(url,"005331171106000019");
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        }
//        solveFile.block("005351171102000006");
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //System.out.println( "aceess success [" + sdf.format(new Date()) + "]");
//        CreateAttestDetailMessage createAttestDetailMessage = new CreateAttestDetailMessage();
//        createAttestDetailMessage.setOrdersn("005341171109000019");
//        filehashcz.acceptHashAttestInteral(createAttestDetailMessage);
//        xqcz.acceptContinueAttest(createAttestDetailMessage);
        fileCzFileBiz.solveFile(createFileDataMessage);

//        Attest lastAttest = attestService.getAttestByOrdersn("005351171118000030");
//        //计算上一条最新订单所剩保存期限   为starttime毫秒数+保存期限毫秒数-当前日期毫秒数
//        long ms = lastAttest.getStartTime().getTime()+(long)(Float.valueOf(lastAttest.getDuration())*24*3600*1000)-new Date().getTime();
//        System.out.println("还剩毫秒数："+ms);
//        DecimalFormat df   = new DecimalFormat("0.000");
//        String days = df.format( (float)ms/(24*3600*1000));
//        long hh = (long)(Float.valueOf(days)*24*3600*1000);
//        System.out.println("还剩天数："+days);
//        String Duration = String.valueOf(Float.valueOf("3")+Float.valueOf(days));
//        System.out.println("相加："+Duration);
//        float a = Float.valueOf(lastAttest.getDuration())+Float.valueOf(lastAttest.getDuration());
//        System.out.println("相加："+a);
    }

}
