package com.zhjl.tech.channel.service.channel.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.channel.mapper.channel.AttestChannelTestMapper;
import com.zhjl.tech.channel.model.channel.AttestChannelTest;
import com.zhjl.tech.channel.service.channel.IAttestChannelTestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * AttestChannelTest 表数据服务层接口实现类
 *
 */
@Service
public class AttestChannelTestServiceImpl extends SuperServiceImpl<AttestChannelTestMapper, AttestChannelTest> implements IAttestChannelTestService {

    @Resource
    AttestChannelTestMapper attestChannelTestMapper;

    @Override
    public AttestChannelTest getAttestChannelTestByChannelOrdersn(String channelOrdersn) {
        return attestChannelTestMapper.getAttestChannelTestByChannelOrdersn(channelOrdersn);
    }

    @Override
    public AttestChannelTest getAttestChannelTestByState(String state) {
        return attestChannelTestMapper.getAttestChannelTestByState(state);
    }

    @Override
    public List<AttestChannelTest> getAttestChannelTestByTestName(String testName) {
        return attestChannelTestMapper.getAttestChannelTestByTestName(testName);
    }

    @Override
    public AttestChannelTest getAttestChannelTestByOrdersn(String ordersn) {
        return attestChannelTestMapper.getAttestChannelTestByOrdersn(ordersn);
    }
}