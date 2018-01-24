package test.com.tech.inter.hashcz;

import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.utils.gen.filehashcz.GenFileHashCzDatas;
import com.zhjl.tech.inter.biz.hashcz.FileHashCzBiz;
import com.zhjl.tech.inter.model.inter.Status;
import com.zhjl.tech.inter.model.inter.TempOrder;
import com.zhjl.tech.inter.service.inter.IStatusService;
import com.zhjl.tech.inter.service.inter.ITempOrderService;
import org.junit.Assert;
import org.junit.Test;
import test.com.tech.inter.base.BaseTest;

import javax.annotation.Resource;
import java.util.UUID;

public class HashCzTest extends BaseTest {


    /**
     * 文件Hash存证正常流程
     * 测试思路
     * 1. 测试文件Hash存证interface的controller方法
     * 2. 查看TempOrder表是否存在该记录
     * 3. 查看status表是否存在该记录,并且status表state字段是否为“[Hash]Hash存证接收处理完成1”
     */
    @Test
    public void hashCz(){
        String userid = UUID.randomUUID().toString().replaceAll("-","").substring(0,3);
        AttestDto attestDto = genFileHashCzDatas.gen("D://11.txt",userid);
        JsonResult jsonResult = fileHashCzBiz.solveInternal(attestDto);

        //返回jsonResult为true   参数校验通过
        Assert.assertEquals(true,jsonResult.isSuccess());

        //temporder表中有对应channelordersn的数据
        TempOrder tempOrder = tempOrderService.getTempOrderByChannelOrdersn(attestDto.getChannelOrdersn());
        Assert.assertEquals(true,tempOrder != null);

        //status表state字段为[Hash]Hash存证接收处理完成1
        Status status = statusService.getStatusByChannelOrdersn(attestDto.getChannelOrdersn());
        Assert.assertEquals(true, State.Solve_FileHashCz_Ok.equals(status.getStateBiz()));
        System.out.println("-------------------ChannelOrdersn:"+attestDto.getChannelOrdersn());
    }
    @Resource
    private GenFileHashCzDatas genFileHashCzDatas;

    @Resource
    private FileHashCzBiz fileHashCzBiz;

    @Resource
    private ITempOrderService tempOrderService;

    @Resource
    private IStatusService statusService;
}
