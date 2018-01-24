package test.com.tech.tasks;

import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.tasks.model.tasks.RabbitmqMsg;
import com.zhjl.tech.tasks.service.tasks.IRabbitmqMsgService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import test.com.tech.tasks.base.BaseTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Slf4j
public class Test1 extends BaseTest{

    @Resource
    private IRabbitmqMsgService rabbitmqMsgService;

    @Test
    public void b(){
        long d = System.currentTimeMillis();
        for(int i=0;i<10000;i++){
//            List<RabbitmqMsg> list = rabbitmqMsgService.getMsgByRabbitMqQueue1("com.zhjl.tech.attest.CreateFileCz",new Date());
            List<RabbitmqMsg> list = rabbitmqMsgService.getMsgByRabbitMqQueue("com.zhjl.tech.attest.CreateFileCz");
        }
        long s = System.currentTimeMillis();
        log.info("@@@@@@@@:{}",String.valueOf(s-d));
    }
}
