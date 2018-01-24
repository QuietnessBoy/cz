package com.zhjl.tech.tasks.task.watch.czxq;

import com.zhjl.tech.common.constant.SysConfig;
import com.zhjl.tech.tasks.model.tasks.Attest;
import com.zhjl.tech.tasks.model.tasks.AttestHistory;
import com.zhjl.tech.tasks.service.tasks.IAttestHistoryService;
import com.zhjl.tech.tasks.service.tasks.IAttestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class WatchExpireCzXq {

    /**
     * 监听订单状态为续期订单回调失败的订单
     */
    @Transactional(rollbackFor = Exception.class)
    public void watchCzIsExpire() {
        log.info("[开始检测续期订单是否过时.]");

        // 查找订单状态存证完成且过期的最新订单数据
        List<Attest> list = attestService.getExpiredByAttests(SysConfig.BizSolveOk, SysConfig.GoChained);

        if(list.isEmpty()){
            log.info("未找到过时订单.");
            return;
        }

        for(Attest attest : list){
            // 根据祖先节点遍历所有存证订单
            List<Attest> list1 = attestService.getAttestByAncestorsOrdersn(attest.getAncestorsOrdersn());

            // 全部移至历史表
            for(Attest attest2 : list1) {
                AttestHistory attestHistory = attestHistoryService.getAttestHistoryByChannelOrdersn(attest2.getOrdersn());
                if (attestHistory != null) {
                    log.error("历史库已存在订单,终止操作");
                    continue;
                }
                AttestHistory attestHistory1 = new AttestHistory();
                BeanUtils.copyProperties(attest2, attestHistory1);
                attestHistory1.setState(SysConfig.Expired);   //已过期
                attestHistory1.setUpdateTime(new Date());   //已过期
                attestHistoryService.insertSelective(attestHistory1);
            }

            // 删除对应祖先节点订单信息
//            for(Attest attest1 : list1){
                attestService.deleteAttest(attest.getAncestorsOrdersn());
//            }
        }
    }


    @Resource
    private IAttestHistoryService attestHistoryService;

    @Resource
    private IAttestService attestService;

}
