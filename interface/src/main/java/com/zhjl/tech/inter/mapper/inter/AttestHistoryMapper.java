package com.zhjl.tech.inter.mapper.inter;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.inter.model.inter.AttestHistory;

import java.util.List;

/**
 *
 * AttestHistory 表数据库控制层接口
 *
 */
public interface AttestHistoryMapper extends AutoMapper<AttestHistory> {

    List<AttestHistory> getAllByAttestHistory();

    AttestHistory getOrdersnByAttestHistory(String ordersn);

    AttestHistory getChannelOrdersnByAttestHistory(String channelOrdersn);

}