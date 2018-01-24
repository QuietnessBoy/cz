package com.zhjl.tech.tasks.controller.chainedCallBack;

import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.tasks.controller.BaseController;
import com.zhjl.tech.tasks.model.tasks.Attest;
import com.zhjl.tech.tasks.model.tasks.AttestChained;
import com.zhjl.tech.tasks.model.tasks.Status;
import com.zhjl.tech.tasks.model.tasks.Warning;
import com.zhjl.tech.tasks.service.tasks.IAttestChainedService;
import com.zhjl.tech.tasks.service.tasks.IAttestService;
import com.zhjl.tech.tasks.service.tasks.IStatusService;
import com.zhjl.tech.tasks.service.tasks.IWarningService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

/**
 * 接收上链回调数据
 */
@Service
@Slf4j
@Controller
public class SolveCallBackAddAttestChained extends BaseController {

    @Resource
    private IAttestChainedService attestChainedService;

    @Resource
    private IWarningService warningService;

    @Resource
    private IAttestService attestService;

    @Resource
    private IStatusService statusService;

    @ResponseBody
    @RequestMapping("/addAttestChained")
    @Transactional(rollbackFor = Exception.class)
    public void addCzCallBack(HttpServletRequest request) {

        String txid = request.getParameter("txid");
        String success = request.getParameter("success");
        String datas = request.getParameter("datas");

        log.info("[接收区块链回调]:Txid={},success={},datas={}", txid, success, datas);

        AttestChained attestChained = attestChainedService.getAttestChainedByTxId(txid);

        if(attestChained == null){
            log.warn("未找到该上链数据.txid={}",txid);
            return;
        }else if ("true".equalsIgnoreCase(success)) {
            //删除对应数据记录
            attestChainedService.deleteById((long)attestChained.getId());

            //更新attest表数据
            Attest attest = attestService.getAttestByOrdersn(attestChained.getOrdersn());
            attest.setState(SysConfig.GoChained);
            attest.setStateTime(new Date());
            attest.setUpdateTime(new Date());
            attestService.updateSelectiveById(attest);

            //更新status表状态
            Status status = statusService.getStatusByOrdersn(attestChained.getOrdersn());
            status.setStateBiz("上链成功");
            status.setNum(0);
            status.setUpdateTime(new Date());
            statusService.updateSelectiveById(status);

            //删除warning表对应记录
            Warning warning = warningService.getWarningByChannelOrdersnAndBizType(attestChained.getChannelOrdersn(),SysConfig.ChainedType);
            if(warning != null){
                warningService.deleteById((long)warning.getId());
            }
            log.info("[上链成功]ordersn={}", attestChained.getOrdersn());
        } else {
            //更新订单记录，增加处理次数
            attestChained.setState(SysConfig.FailedChainedAddCz);
            attestChained.setRemark("上链失败");
            attestChainedService.updateSelectiveById(attestChained);
            log.warn("[上链失败]ordersn={}",attestChained.getOrdersn());
        }
    }
}
