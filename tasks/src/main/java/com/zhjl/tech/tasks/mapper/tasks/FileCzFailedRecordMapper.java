package com.zhjl.tech.tasks.mapper.tasks;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.tasks.model.tasks.FileCzFailedRecord;

/**
 *
 * AttestChainedSolve 表数据库控制层接口
 *
 */
public interface FileCzFailedRecordMapper extends AutoMapper<FileCzFailedRecord> {

    FileCzFailedRecord getFileCzFailedRecordByOrdersn(String ordersn);

    FileCzFailedRecord getFileCzFailedRecordByChannelOrdersn(String channelOrdersn);

}