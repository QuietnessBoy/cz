package com.zhjl.tech.channel.mapper.channel;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.channel.model.channel.AttestChannelTest;

import java.util.List;

/**
 *
 * AttestChannelTest 表数据库控制层接口
 *
 */
public interface AttestChannelTestMapper extends AutoMapper<AttestChannelTest> {

    AttestChannelTest getAttestChannelTestByChannelOrdersn(String channelOrdersn);

    AttestChannelTest getAttestChannelTestByState(String state);

    List<AttestChannelTest> getAttestChannelTestByTestName(String testName);

    AttestChannelTest getAttestChannelTestByOrdersn(String ordersn);

}