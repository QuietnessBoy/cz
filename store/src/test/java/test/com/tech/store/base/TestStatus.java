package test.com.tech.store.base;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.store.model.store.Attest;
import com.zhjl.tech.store.model.store.Status;
import com.zhjl.tech.store.model.store.TempOrder;
import com.zhjl.tech.store.service.store.IAttestService;
import com.zhjl.tech.store.service.store.IStatusService;
import com.zhjl.tech.store.service.store.ITempOrderService;
import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.Date;

public class TestStatus extends BaseTest{
    @Resource
    private ITempOrderService tempOrderService;

    @Resource
    private IStatusService statusService;

    @Resource
    private IAttestService attestService;


    @Test
    public void t(){
//        TempOrder tempOrder = tempOrderService.getTempOrderByChannelOrdersn("ordersn0e70742aa");
//        System.out.println("-----------id-----------"+tempOrder.getId());
//        //删除临时表存证记录
//        tempOrderService.deleteSelective(tempOrder);
//        System.out.println("删除函数执行完成");

        Status status = statusService.getStatusByChannelOrdersn("ordersn82593f90d");
//        statusService.deleteSelective(status);
        //更改状态表业务状态 为文件存证处理失败
        status.setStateBiz("111");
        status.setUpdateTime(new Date());
        statusService.updateSelectiveById(status);
        System.out.println("更新完毕");

    }

    @Test
    public void a(){
        Attest attest = attestService.getAttestByChannelOrdersn("ordersnd3677dd12");
        if(attest!=null){
            System.out.println("attest找到");
            attestService.deleteSelective(attest);
            System.out.println("attest删除完毕");
        }else{
            System.out.println("attest未找到");
        }
    }

    @Test
    public void b(){

        TempOrder tempOrder = tempOrderService.getTempOrderByChannelOrdersn("ordersnd3677dd12");
        if(tempOrder!=null){
            System.out.println("tempOrder找到"+ JSON.toJSONString(tempOrder));
            TempOrder tempOrder1 = new TempOrder();
            tempOrder1.setChannelOrdersn("ordersnd3677dd12");
            tempOrderService.deleteSelective(tempOrder1);
            System.out.println("tempOrder删除完毕");
        }else{
            System.out.println("tempOrder未找到");
        }


    }

}
