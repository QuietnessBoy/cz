package test.com.tech.inter.xqcz;

import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.dto.interfaces.AttestXqDto;
import com.zhjl.tech.common.utils.gen.xqcz.GenXqData;
import com.zhjl.tech.inter.biz.czxq.CzXqBiz;
import com.zhjl.tech.inter.model.inter.Attest;
import com.zhjl.tech.inter.model.inter.Status;
import com.zhjl.tech.inter.model.inter.TempOrder;
import com.zhjl.tech.inter.service.inter.IAttestService;
import com.zhjl.tech.inter.service.inter.IStatusService;
import com.zhjl.tech.inter.service.inter.ITempOrderService;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import test.com.tech.inter.base.BaseTest;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.UUID;

public class XqCzTest extends BaseTest {




    /**
     * 存证续期正常流程
     * 测试思路
     * 1. 测试存证续期interface的controller方法
     * 2. 查看TempOrder表是否存在该记录
     * 3. 查看status表是否存在该记录,并且status表state字段是否为“[czxq]存证续期接收处理完成1”
     */
    @Test
    public void xqCz(){
        String ordersn = "005331171228000003";
        Attest attest = attestService.getAttestByOrdersn(ordersn);
        AttestXqDto attestXqDto = new AttestXqDto();
        BeanUtils.copyProperties(attest, attestXqDto);
        attestXqDto.setAccessKey("GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd");
        String startTime =new SimpleDateFormat(SysConfig.Format).format(attest.getStartTime());
        AttestXqDto attestXqDto1 = genXqData.gen(attestXqDto,ordersn,startTime);
        JsonResult jsonResult = czXqBiz.solveInternal(attestXqDto1);

        //返回jsonResult为true   参数校验通过
        Assert.assertEquals(true,jsonResult.isSuccess());

        //temporder表中有对应channelordersn的数据
        TempOrder tempOrder = tempOrderService.getTempOrderByChannelOrdersn(attestXqDto.getChannelOrdersn());
        Assert.assertEquals(true,tempOrder != null);

        //status表state字段为[czxq]存证续期接收处理完成1
        Status status = statusService.getStatusByChannelOrdersn(attestXqDto.getChannelOrdersn());
        Assert.assertEquals(true, State.Solve_Xq_Ok.equals(status.getStateBiz()));
        System.out.println("-------------------ChannelOrdersn:"+attestXqDto.getChannelOrdersn());

    }
    @Resource
    private ITempOrderService tempOrderService;

    @Resource
    private IStatusService statusService;

    @Resource
    private IAttestService attestService;

    @Resource
    private CzXqBiz czXqBiz;

    @Resource
    private GenXqData genXqData;

}
