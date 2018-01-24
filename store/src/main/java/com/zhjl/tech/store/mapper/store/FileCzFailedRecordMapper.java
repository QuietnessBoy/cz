package com.zhjl.tech.store.mapper.store;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.store.model.store.FileCzFailedRecord;

/**
 *
 * AttestChainedSolve 表数据库控制层接口
 *
 */
public interface FileCzFailedRecordMapper extends AutoMapper<FileCzFailedRecord> {

    FileCzFailedRecord getFileCzFailedRecordByOrdersn(String ordersn);

    FileCzFailedRecord getFileCzFailedRecordByCchannelOrdersn(String channelOrdersn);

}