package com.zhjl.tech.attest.service.attest;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.attest.model.attest.AttestHistory;

import java.util.List;

/**
 *
 * AttestHistory 表数据服务层接口
 *
 */
public interface IAttestHistoryService extends ISuperService<AttestHistory> {


    AttestHistory getAttestHistoryByOrdersn(String ordersn);

    AttestHistory getAttestHistoryByChannelOrdersn(String channelOrdersn);
}