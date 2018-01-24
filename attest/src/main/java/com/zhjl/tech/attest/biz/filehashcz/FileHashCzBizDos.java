package com.zhjl.tech.attest.biz.filehashcz;

import com.zhjl.tech.attest.model.attest.Attest;
import com.zhjl.tech.attest.model.attest.Status;
import com.zhjl.tech.attest.model.attest.TempOrder;
import com.zhjl.tech.attest.service.attest.*;
import com.zhjl.tech.common.exception.AttestBizException;
import com.zhjl.tech.common.exception.NormalException;
import com.zhjl.tech.common.constant.State;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class FileHashCzBizDos {

    public TempOrder getLockAndCheck(String channelOrdersn){

        // 加锁
        jdbcTemplate.execute("set innodb_lock_wait_timeout=1");

        //判断该临时订单是否在工作中
        TempOrder tempOrder;
        try {
            tempOrder = tempOrderService.getTempOrderForUpdateByChannelOrdersn(channelOrdersn);
        } catch (CannotAcquireLockException e) {
            log.info("遇有锁数据，本次处理中断,channelOrersn:{}", channelOrdersn);
            throw new NormalException("filehashcz遇有锁数据，channelOrersn="+channelOrdersn);
        }

        if (tempOrder == null) {
            log.warn("订单不存在.channelOrersn{}", channelOrdersn);
            throw new AttestBizException("filehashcz订单不存在，channelOrersn="+channelOrdersn);
        }

        // 根据channelOrdersn查找status表中对应订单记录
        Status status = statusService.getStatusByChannelOrdersn(channelOrdersn);

        //判断订单状态表是否存在该订单记录，如果不存在，则返回
        if (status == null) {
            log.warn("订单状态表未找到该订单记录.channelOrersn{}", channelOrdersn);
            throw new AttestBizException("filehashcz订单状态表未找到该订单记录，channelOrersn="+channelOrdersn);
        }

        //状态是否适合当前处理
        if (!status.getStateBiz().equals(State.Solve_FileHashCz_Ok)) {
            log.warn("订单状态不匹配该流程处理状态。channelOrersn={}", channelOrdersn);
            throw new NormalException("filehashcz订单状态不匹配该流程处理状态，channelOrersn="+channelOrdersn);
        }

        //判断正式表（attest）是否存在该订单记录
        Attest attest = attestService.getAttestByChannelOrdersn(tempOrder.getChannelOrdersn());
        if (attest != null) {
            log.warn("订单已经是正式订单.channelOrersn={}", channelOrdersn);
            throw new NormalException("filehashcz订单已经是正式订单，channelOrersn="+channelOrdersn);
        }

        return tempOrder;
    }

    @Resource
    private ITempOrderService tempOrderService;

    @Resource
    private IAttestService attestService;

    @Resource
    private IStatusService statusService;

    @Resource
    private JdbcTemplate jdbcTemplate;
}
