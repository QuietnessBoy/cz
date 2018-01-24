package com.zhjl.tech.channel.service.channel;

import com.zhjl.tech.channel.model.channel.AttestChannelTest;
import com.baomidou.framework.service.ISuperService;

import java.util.List;

/**
 *
 * AttestChannelTest 表数据服务层接口
 *
 */
public interface IAttestChannelTestService extends ISuperService<AttestChannelTest> {
    AttestChannelTest getAttestChannelTestByChannelOrdersn(String channelOrdersn);

    AttestChannelTest getAttestChannelTestByState(String state);

    List<AttestChannelTest> getAttestChannelTestByTestName(String testName);

    AttestChannelTest getAttestChannelTestByOrdersn(String ordersn);
}