package com.zhjl.tech.tasks.task.watch.filecz;

import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.tasks.model.tasks.Status;
import com.zhjl.tech.tasks.service.tasks.IStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 监听订单状态未文件存证请求接收完成订单
 */
@Slf4j
@Service
public class WatchStoreFileCz {

    /**
     * 监听文件存证订单当前状态为通知渠道失败的订单信息
     */
    public void watchFileCzCallBackOk() {
        //查找订单集合
        List<Status> list = statusService.getStatusByStateNotifyInTime(State.CallBack_File_Failed, SysConfig.Num);

        if(list.isEmpty()){
            log.warn("未找到近期文件存证回调失败的订单.");
            return;
        }
        //解析查询到的处理失败,回调失败的订单
        solveAttestList.solve(list);
    }

    /**
     * 监听订单处理失败/回调失败的订单，继续回调
     */
    public void watchSolveFaild() {
        //查找订单集合
        List<Status> list = statusService.getStatusByStateNotifyInTime(State.FailedAndCallBackFailed,SysConfig.Num);

        if(list.isEmpty()){
            log.warn("未找到近期处理失败，回调失败的订单.");
            return;
        }
        //解析查询到的处理失败,回调失败的订单
        solveAttestList.solveFiled(list);
    }

    @Resource
    private IStatusService statusService;

    @Resource
    private SolveAttestList solveAttestList;

}
