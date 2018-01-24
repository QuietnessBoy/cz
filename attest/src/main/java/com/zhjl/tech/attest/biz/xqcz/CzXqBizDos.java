package com.zhjl.tech.attest.biz.xqcz;

import com.zhjl.tech.attest.model.attest.TempOrder;
import com.zhjl.tech.attest.service.attest.ITempOrderService;
import com.zhjl.tech.common.exception.AttestBizException;
import com.zhjl.tech.common.exception.NormalException;
import com.zhjl.tech.common.message.CreateAttestDetailMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class CzXqBizDos {

    public TempOrder getTempOrderAndLock(CreateAttestDetailMessage createAttestDetailMessage) {
        // 加锁
        jdbcTemplate.execute("set innodb_lock_wait_timeout=1");

        // 判断临时订单记录是否在工作中
        TempOrder tempOrder = null;
        try {
            tempOrder = tempOrderService.getTempOrderForUpdateByChannelOrdersn(createAttestDetailMessage.getChannelOrdersn());
        } catch (CannotAcquireLockException e) {
            log.info("遇有锁数据，本次处理中断,channelOrersn:{}", createAttestDetailMessage.getChannelOrdersn());
            throw new NormalException("czxq,遇有锁数据，本次处理中断,channelOrersn:" + createAttestDetailMessage.getChannelOrdersn());
        }

        if (tempOrder == null) {
            log.warn("该订单记录不存在.channelOrersn:{}", createAttestDetailMessage.getChannelOrdersn());
            throw new AttestBizException("czxq,该订单记录不存在，本次处理中断,channelOrersn:" + createAttestDetailMessage.getChannelOrdersn());
        }
        return tempOrder;
    }

    @Resource
    JdbcTemplate jdbcTemplate;

    @Resource
    ITempOrderService tempOrderService;
}
