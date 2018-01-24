package com.zhjl.tech.inter.mapper.inter;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.inter.model.inter.FileCzFailedRecord;

/**
 *
 * AttestChainedSolve 表数据库控制层接口
 *
 */
public interface FileCzFailedRecordMapper extends AutoMapper<FileCzFailedRecord> {

    FileCzFailedRecord getFileCzFailedRecordByOrdersn(String ordersn);

    FileCzFailedRecord getFileCzFailedRecordByCchannelOrdersn(String channelOrdersn);

}