package com.zhjl.tech.store.biz.filecz;


import com.zhjl.tech.common.exception.NormalException;
import com.zhjl.tech.common.exception.StoreBizException;
import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.message.CreateFileDataMessage;
import com.zhjl.tech.store.model.store.Attest;
import com.zhjl.tech.store.model.store.Status;
import com.zhjl.tech.store.service.store.IAttestService;
import com.zhjl.tech.store.service.store.IStatusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class SolveFileCzFileDos {

    public Attest getAttestWithLock(CreateFileDataMessage createFileDataMessage) {
        jdbcTemplate.execute("set innodb_lock_wait_timeout=1");

        //根据ordersn查看当前存证记录是否正在工作中
        Attest attest = null;
        try {
            attest = attestService.getAttestForUpdateByOrdersn(createFileDataMessage.getOrdersn());
        } catch (CannotAcquireLockException e) {
            log.info("遇有锁数据，本次处理中断.ordersn={}", createFileDataMessage.getOrdersn());
            throw new NormalException("遇有锁数据处理中断.ordersn:" + createFileDataMessage.getOrdersn());
        }

        if (attest == null) {
            log.warn("没有找到指定的订单.ordersn={}", createFileDataMessage.getOrdersn());
            throw new StoreBizException("没有找到指定的订单而中断.ordersn:" + createFileDataMessage.getOrdersn());
        }
        //如果状态为  "文件接收处理完成"  则进行处理
        Status status = statusService.getStatusByOrdersn(createFileDataMessage.getOrdersn());
        if (status == null) {
            log.warn("订单状态记录不存在.ordersn={}", createFileDataMessage.getOrdersn());
            throw new StoreBizException("订单状态记录不存在.ordersn:" + createFileDataMessage.getOrdersn());
        }

        //触发情况1：重发消息，当第一条任务执行完毕，第二个任务才开始执行，这个时候会触发
        if (!status.getStateBiz().equals(State.Solve_FileCz_Ok)) {
            log.warn("状态不符合当前处理环节.ordersn={}", createFileDataMessage.getOrdersn());
            throw new NormalException("状态不符合当前处理环节.ordersn:" + createFileDataMessage.getOrdersn());
        }
        return attest;
    }

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private IAttestService attestService;

    @Resource
    private IStatusService statusService;
}
