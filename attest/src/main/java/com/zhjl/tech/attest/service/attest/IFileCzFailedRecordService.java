package com.zhjl.tech.attest.service.attest;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.attest.model.attest.FileCzFailedRecord;

/**
 *
 * AttestChainedSolve 表数据服务层接口
 *
 */
public interface IFileCzFailedRecordService extends ISuperService<FileCzFailedRecord> {

    FileCzFailedRecord getFileCzFailedRecordByOrdersn(String ordersn);

    FileCzFailedRecord getFileCzFailedRecordByChannelOrdersn(String channelOrdersn);


}