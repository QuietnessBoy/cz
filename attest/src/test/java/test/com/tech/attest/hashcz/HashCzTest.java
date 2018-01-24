package test.com.tech.attest.hashcz;

import com.zhjl.tech.attest.biz.filehashcz.FileHashCzBiz;
import com.zhjl.tech.attest.model.attest.*;
import com.zhjl.tech.attest.service.attest.*;
import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.utils.gen.filehashcz.GenFileHashCzDatas;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import test.com.tech.attest.base.BaseTest;

import javax.annotation.Resource;
import java.util.UUID;

@Slf4j
public class HashCzTest extends BaseTest {


    /**
     * attest文件处理系统controller方法测试
     * 测试思路
     * 1. 获取到interface处理成功后的请求channelOrdersn,调用attest controller方法进行核心业务处理
     * 2. 查看attest表是否存在该条记录，是否有ordersn/attestSign/wallerAddr/expireTime/ancestorsOrdersn
     * 3. 查看status表中state字段是否为“[Hash]Hash存证业务处理完成2”
     * 4. 查看tempOrder表中该条请求数据是否删除成功
     */
    @Test
    public void hashCz(){
        String channelOrdersn = "ordersndb82c3662";
        fileHashCzBiz.sloveHashAttest(channelOrdersn);
        //延时
        try {
            log.info("延时10秒测试");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
            log.error("延时机制异常.", e);
        }
        //attest表中有对应channelordersn有数据
        Attest attest = attestService.getAttestByChannelOrdersn(channelOrdersn);
        Assert.assertEquals(true,attest != null);
        //是否有ordersn/attestSign/wallerAddr/expireTime/ancestorsOrdersn
        Assert.assertEquals(true,attest.getOrdersn() != null);
        Assert.assertEquals(true,attest.getAttestSign() != null);
        Assert.assertEquals(true,attest.getWalletAddr() != null);
        Assert.assertEquals(true,attest.getExpiredTime() != null);
        Assert.assertEquals(true,attest.getAncestorsOrdersn() != null);

        //attest表中state字段为1
        Assert.assertEquals(true, SysConfig.BizSolveOk.equals(attest.getState()));
        //status表state字段为Hash文件存证处理完成2
        Status status = statusService.getStatusByChannelOrdersn(channelOrdersn);
        Assert.assertEquals(true, State.Solve_Biz_Ok.equals(status.getStateBiz()));
        //attest_file表中没有数据
        AttestFile attestFile = attestFileService.getAttestFileByOrdersn(attest.getOrdersn());
        Assert.assertEquals(false,attestFile != null);
        //temporder表中有对应channelordersn没有数据
        TempOrder tempOrder = tempOrderService.getTempOrderByChannelOrdersn(channelOrdersn);
        Assert.assertEquals(false,tempOrder != null);
        //上链表中有数据
        AttestChained attestChained = attestChainedService.getAttestChainedByOrdersn(attest.getOrdersn());
        Assert.assertEquals(true,attestChained != null);
    }

    @Resource
    private IAttestFileService attestFileService;

    @Resource
    private IAttestChainedService attestChainedService;

    @Resource
    private IAttestService attestService;

    @Resource
    private FileHashCzBiz fileHashCzBiz;

    @Resource
    private ITempOrderService tempOrderService;

    @Resource
    private IStatusService statusService;
}
