package com.zhjl.tech.attest.biz.filecz;

import com.zhjl.tech.attest.biz.common.CommonBiz;
import com.zhjl.tech.attest.model.attest.Attest;
import com.zhjl.tech.attest.model.attest.Channel;
import com.zhjl.tech.attest.model.attest.Status;
import com.zhjl.tech.attest.model.attest.TempOrder;
import com.zhjl.tech.attest.service.attest.IAttestService;
import com.zhjl.tech.attest.service.attest.IStatusService;
import com.zhjl.tech.attest.service.attest.ITempOrderService;
import com.zhjl.tech.common.exception.AttestBizException;
import com.zhjl.tech.common.exception.NormalException;
import com.zhjl.tech.common.constant.State;
import com.zhjl.tech.common.dto.interfaces.AttestDto;
import com.zhjl.tech.common.encrypt.asymmetric.SM2;
import com.zhjl.tech.common.encrypt.asymmetric.SM2Impl;
import com.zhjl.tech.common.encrypt.asymmetric.SM2KeyPair;
import com.zhjl.tech.common.zjlsign.filecz.FileCzAttestSign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class FileCzBizDos {

    public TempOrder getLockAndCheckStatus(String channelOrdersn){
        //加锁
        jdbcTemplate.execute("set innodb_lock_wait_timeout=1");

        // 判断该临时订单是有在工作
        TempOrder tempOrder = null;
        try {
            tempOrder = tempOrderService.getTempOrderForUpdateByChannelOrdersn(channelOrdersn);
        } catch (CannotAcquireLockException e) {
            log.info("获取tmporder表锁失败，处理中断。channelOrersn:{}", channelOrdersn);
            throw new NormalException("获取tmporder表锁失败，处理中断");
        }

        if (tempOrder == null) {
            log.warn("订单不存在.channelOrersn{}", channelOrdersn);
            throw new AttestBizException("订单不存在.channelOrersn=" + channelOrdersn);
        }

        //如果状态为  "文件存证接收处理完成"  则进行处理
        Status status = statusService.getStatusByChannelOrdersn(channelOrdersn);
        if (status == null) {
            log.warn("订单状态记录不存在,channelOrersn{}", channelOrdersn);
            throw new AttestBizException("订单状态记录不存在.channelOrersn=" + channelOrdersn);
        }

        //触发情况1：重发消息，当第一条任务执行完毕，第二个任务才开始执行，这个时候会触发
        if (!status.getStateBiz().equals(State.Request_File_Ok)) {
            log.warn("状态不符合当前处理环节,channelOrersn{}", channelOrdersn);
            throw new NormalException("状态不符合当前处理环节,channelOrersn{}" + channelOrdersn);
        }
        return tempOrder;
    }

    /**
     * 替终端用户做的事情
     * 这将修改attest对象的内容
     * @param channel
     * @param attest
     */
    public void thingsUserShouldDo(Channel channel, Attest attest) {
        //根据attest对象获取对应用户的keypair，同时设置attest对象的钱包地址等
        SM2KeyPair sm2KeyPair = commonBiz.genUserKeyPair(attest);

        //根据用户公钥生成订单的用户待签名字符串
        SM2 sm2 = new SM2Impl();
        AttestDto attestDto1 = new AttestDto();
        BeanUtils.copyProperties(attest,attestDto1);
        String attestSign = FileCzAttestSign.gen(sm2KeyPair, channel.getChannelPublicKey(), channel.getChannelIda(),attestDto1);
        attest.setAttestSign(attestSign);
    }


    /**
     * 订单号是否为重复的判断
     * @param ordersn
     * @throws AttestBizException
     */
    public void checkDumplicateOrdersn( String ordersn) {
        Attest orderAttest = attestService.getAttestByOrdersn(ordersn);
        if (orderAttest != null) {
            log.error("ordersn生成重复 !{}",ordersn);
            throw new AttestBizException("ordersn生成重复:" + orderAttest.getOrdersn());
        }
    }

    @Resource
    private ITempOrderService tempOrderService;

    @Resource
    private IAttestService attestService;

    @Resource
    private IStatusService statusService;

    @Resource
    private JdbcTemplate jdbcTemplate;

    @Resource
    private CommonBiz commonBiz;

}
