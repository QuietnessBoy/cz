package test.com.tech.store.filecz;

import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.message.CreateFileDataMessage;
import com.zhjl.tech.store.biz.filecz.FileCzFileBiz;
import com.zhjl.tech.store.model.store.*;
import com.zhjl.tech.store.service.store.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import test.com.tech.store.base.BaseTest;

import javax.annotation.Resource;

@Slf4j
public class FileCzTest extends BaseTest {


    /**
     * 文件管理系统controller方法测试
     * 测试思路
     * 1. 获取业务处理子系统(attest)处理成功的数据信息，调用store controller方法
     * 2. 当正式表attest订单状态为1时，status表中state状态为“[File]文件存证完成3”
     * 3. 当正式表attest订单状态为-1时，status表中state状态为“[File]文件存证处理失败4”
     * 4. attestFile表中bucketName存在，fileAddr改变成对应fileKey,以channelOrdersn+32位随机数
     * 5. 生成上链数据，查看attest_chained表中是否存在数据
     * 6. 看该条订单记录是否上链，如果上链成功，attest_chained表中删除该数据，attest表state字段为2；
     *    如果失败，该状态为0/-1/1，记录异常状态，查看attest_chained表中num字段，等待重新发送
     *
     *
     *    (expected=NormalException.class)
     */
    @Test
    public void fileCz(){
        String channelOrdersn = "ordersnb0205236f";
        Attest attest = attestService.getAttestByChannelOrdersn(channelOrdersn);
        fileCzFileBiz.solveFileCzFile(new CreateFileDataMessage(attest.getOrdersn()));
        //延时
        try {
            log.info("延时10秒测试");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("延时机制异常.", e);
        }
        //attest_file表中有数据
        AttestFile attestFile = attestFileService.getAttestFileByOrdersn(attest.getOrdersn());
        Assert.assertEquals(true,attestFile != null);

        //attest表中state字段为1  处理成功
        if(SysConfig.BizSolveOk.equals(attest.getState())){
            //status表state字段为文件存证处理完成2
            Status status = statusService.getStatusByChannelOrdersn(channelOrdersn);
            Assert.assertEquals(true, State.Receive_File_Gen_BlockFile_Ok.equals(status.getStateBiz()));

            //上链表中有数据
            AttestChained attestChained = attestChainedService.getAttestChainedByOrdersn(attest.getOrdersn());
            Assert.assertEquals(true,attestChained != null);

            //attest_file表中BucketName有数据
            Assert.assertEquals(true,attestFile.getBucketName() != null);
            log.info("attestFile的fileAddr："+attestFile.getFileAddr());
        }else if (SysConfig.SolveFaild.equals(attest.getState())){
            //status表state字段为文件存证处理失败4
            Status status = statusService.getStatusByChannelOrdersn(channelOrdersn);
            Assert.assertEquals(true, State.File_Failed.equals(status.getStateBiz()));

            //上链表中没有数据
            AttestChained attestChained = attestChainedService.getAttestChainedByOrdersn(attest.getOrdersn());
            Assert.assertEquals(false,attestChained != null);
        }

        //temporder表中有对应channelordersn没有数据
        TempOrder tempOrder = tempOrderService.getTempOrderByChannelOrdersn(channelOrdersn);
        Assert.assertEquals(false,tempOrder != null);

    }

    @Resource
    private ITempOrderService tempOrderService;

    @Resource
    private IStatusService statusService;

    @Resource
    private IAttestFileService attestFileService;

    @Resource
    private IAttestService attestService;

    @Resource
    private FileCzFileBiz fileCzFileBiz;

    @Resource
    private IAttestChainedService attestChainedService;



    @Test
    public void a(){
        CreateFileDataMessage createFileDataMessage = new CreateFileDataMessage();
        createFileDataMessage.setOrdersn("asdfdasfsad");
        fileCzFileBiz.solveFileCzFile(createFileDataMessage);
    }
}

