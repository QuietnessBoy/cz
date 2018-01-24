package com.zhjl.tech.attest.mapper.attest;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.attest.model.attest.AttestHistory;

import java.util.List;

/**
 *
 * AttestHistory 表数据库控制层接口
 *
 */
public interface AttestHistoryMapper extends AutoMapper<AttestHistory> {

    AttestHistory getAttestHistoryByOrdersn(String ordersn);

    AttestHistory getAttestHistoryByChannelOrdersn(String channelOrdersn);

}