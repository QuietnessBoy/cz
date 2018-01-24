package com.zhjl.tech.tasks.mapper.tasks;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.tasks.model.tasks.AttestHistory;

/**
 *
 * AttestHistory 表数据库控制层接口
 *
 */
public interface AttestHistoryMapper extends AutoMapper<AttestHistory> {

    AttestHistory getAttestHistoryByOrdersn(String ordersn);

    AttestHistory getAttestHistoryByChannelOrdersn(String channelOrdersn);

}