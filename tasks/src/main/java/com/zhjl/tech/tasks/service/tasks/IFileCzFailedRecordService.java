package com.zhjl.tech.tasks.service.tasks;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.tasks.model.tasks.FileCzFailedRecord;

/**
 *
 * AttestChainedSolve 表数据服务层接口
 *
 */
public interface IFileCzFailedRecordService extends ISuperService<FileCzFailedRecord> {

    FileCzFailedRecord getFileCzFailedRecordByOrdersn(String ordersn);

    FileCzFailedRecord getFileCzFailedRecordByChannelOrdersn(String channelOrdersn);


}