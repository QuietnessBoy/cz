package com.zhjl.tech.inter.service.inter;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.inter.model.inter.FileCzFailedRecord;

/**
 *
 * AttestChainedSolve 表数据服务层接口
 *
 */
public interface IFileCzFailedRecordService extends ISuperService<FileCzFailedRecord> {

    FileCzFailedRecord getFileCzFailedRecordByOrdersn(String ordersn);

    FileCzFailedRecord getFileCzFailedRecordByChannelOrdersn(String channelOrdersn);


}