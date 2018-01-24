package com.zhjl.tech.store.service.store;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.store.model.store.FileCzFailedRecord;

/**
 *
 * AttestChainedSolve 表数据服务层接口
 *
 */
public interface IFileCzFailedRecordService extends ISuperService<FileCzFailedRecord> {

    FileCzFailedRecord getFileCzFailedRecordByOrdersn(String ordersn);

    FileCzFailedRecord getFileCzFailedRecordByChannelOrdersn(String channelOrdersn);


}