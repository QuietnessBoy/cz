package test.com.tech.inter.Test.annotation;

import com.zhjl.tech.inter.controller.CzController;
import com.zhjl.tech.inter.model.inter.Status;
import com.zhjl.tech.inter.model.inter.Warning;
import com.zhjl.tech.inter.service.inter.IStatusService;
import com.zhjl.tech.inter.service.inter.IWarningService;
import org.junit.Test;
import test.com.tech.inter.base.BaseTest;

import javax.annotation.Resource;
import java.util.Date;

public class Atest extends BaseTest {

    @Resource
    CzController czController;

    @Resource
    IStatusService statusService;

    @Resource
    IWarningService warningService;


    @Test
    public void ttt(){
        czController.fileAttest(null,null,null);
    }



    @Test
    public void statusInsert(){

        Status status = new Status();
        status.setOrdersn("!11");
        status.setNum(5);
        status.setChannelOrdersn("12312312");
        statusService.insertSelective(status);
    }

    @Test
    public void statusUpdate(){
        Status status = statusService.getStatusByOrdersn("!11");
        status.setChannelOrdersn("3322");
        statusService.updateSelectiveById(status);
    }


    @Test
    public void warnTest(){
        Warning warning = new Warning();
        warning.setOrdersn("1111");
        warningService.insertSelective(warning);
    }

    @Test
    public void warnTest1(){
//        Warning warning = new Warning();
//        warning.setOrdersn("tttt1");
//        warningService.insertSelective(warning);
//        System.out.println("@@@@@@@@@@@@@@"+ JSONObject.toJSONString(warning));
//        Warning warning1 = new Warning();
//        warning1.setOrdersn("tttt2");
//        warningService.insertSelective(warning1);
//        System.out.println("##############"+ JSONObject.toJSONString(warning1));


        Warning warning2 = warningService.getWarningByChannelOrdersn("t1");
        warning2.setUpdateTime(new Date());
        warningService.updateSelectiveById(warning2);

    }
}
