package test.com.tech.attest.Test;

import com.rabbitmq.client.Channel;
import com.zhjl.tech.attest.model.attest.Attest;
import com.zhjl.tech.attest.rabbitmq.send.CreateFileCzSender;
import com.zhjl.tech.attest.service.attest.IAttestService;
import com.zhjl.tech.common.message.CreateFileDataMessage;
import org.junit.Test;
import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import test.com.tech.attest.base.BaseTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class AttestTest extends BaseTest {


    @Resource
    IAttestService attestService;

    @Resource
    ConnectionFactory connectionFactory;

    @Resource
    CreateFileCzSender createFileCzSender;

    @Test
    public void add100000Test(){

        attestService.insertSelective(new Attest());
    }

    @Test
    public void a() throws IOException, TimeoutException, NoSuchAlgorithmException, KeyManagementException, URISyntaxException {
        Connection conn = connectionFactory.createConnection();
        Channel channel = conn.createChannel(false);
        long a = (int) channel.messageCount("CreateFileCz");
        System.out.println("@@@@@@@@@@@@@@@@@@@@@@"+a);
        System.out.println("##################"+conn.getLocalPort());
    }

    @Test
    public void b(){
        CreateFileDataMessage createFileDataMessage = new CreateFileDataMessage();
        createFileDataMessage.setOrdersn("12321321");
        createFileCzSender.send(createFileDataMessage);
    }
}
