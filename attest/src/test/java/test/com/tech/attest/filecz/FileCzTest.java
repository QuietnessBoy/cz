package test.com.tech.attest.filecz;

import com.zhjl.tech.attest.biz.filecz.FileCzBiz;
import com.zhjl.tech.attest.model.attest.Attest;
import com.zhjl.tech.attest.model.attest.AttestFile;
import com.zhjl.tech.attest.model.attest.Status;
import com.zhjl.tech.attest.model.attest.TempOrder;
import com.zhjl.tech.attest.service.attest.IAttestFileService;
import com.zhjl.tech.attest.service.attest.IAttestService;
import com.zhjl.tech.attest.service.attest.IStatusService;
import com.zhjl.tech.attest.service.attest.ITempOrderService;
import com.zhjl.tech.common.constant.JsonConfig;
import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.utils.gen.filecz.GenFileCzDatas;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import test.com.tech.attest.base.BaseTest;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

@Slf4j
public class FileCzTest extends BaseTest {


    /**
     * attest文件处理系统controller方法测试. 不发消息
     * 测试思路
     * 1. 获取到interface处理成功后的请求channelOrdersn,调用attest controller方法进行核心业务处理
     * 2. 查看attest表是否存在该条记录，是否有ordersn/attestSign/wallerAddr/expireTime/ancestorsOrdersn
     * 3. 查看attestFile是否存在相关文件信息。fileAddr等
     * 4. 查看status表中state字段是否为“文件存证处理完成2”
     * 5. 查看tempOrder表中该条请求数据是否删除成功
     */
    @Test
    public void fileCz(){
        String channelOrdersn = "ordersnb0205236f";
        Attest attest = fileCzBiz.solveNormal(channelOrdersn);
        //attest表中有对应channelordersn有数据
        Assert.assertEquals(true,attest != null);

        //attest表中state字段为0
        Assert.assertEquals(SysConfig.UnSolve,attest.getState());
        //是否有ordersn/attestSign/wallerAddr/expireTime/ancestorsOrdersn
        Assert.assertEquals(true,attest.getOrdersn() != null);
        Assert.assertEquals(true,attest.getAttestSign() != null);
        Assert.assertEquals(true,attest.getWalletAddr() != null);
        Assert.assertEquals(true,attest.getExpiredTime() != null);
        Assert.assertEquals(true,attest.getAncestorsOrdersn() != null);

        //status表state字段为文件存证处理完成2
        Status status = statusService.getStatusByChannelOrdersn(channelOrdersn);
        Assert.assertEquals(true, State.Solve_FileCz_Ok.equals(status.getStateBiz()));

        //attest_file表中有数据
        AttestFile attestFile = attestFileService.getAttestFileByOrdersn(attest.getOrdersn());
        Assert.assertEquals(true,attestFile != null);
        Assert.assertEquals(true,attestFile.getFileAddr() != null);

        //temporder表中有对应channelordersn没有数据
        TempOrder tempOrder = tempOrderService.getTempOrderByChannelOrdersn(channelOrdersn);
        Assert.assertNull(tempOrder);
    }

    @Resource
    private ITempOrderService tempOrderService;

    @Resource
    private IStatusService statusService;

    @Resource
    private IAttestFileService attestFileService;

    @Resource
    private FileCzBiz fileCzBiz;

}

