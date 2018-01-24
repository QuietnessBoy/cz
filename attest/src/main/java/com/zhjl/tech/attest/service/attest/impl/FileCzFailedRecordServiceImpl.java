package com.zhjl.tech.attest.service.attest.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.attest.mapper.attest.FileCzFailedRecordMapper;
import com.zhjl.tech.attest.model.attest.FileCzFailedRecord;
import com.zhjl.tech.attest.service.attest.IFileCzFailedRecordService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class FileCzFailedRecordServiceImpl extends SuperServiceImpl<FileCzFailedRecordMapper, FileCzFailedRecord> implements IFileCzFailedRecordService {
    @Resource
    private FileCzFailedRecordMapper fileCzFailedRecordMapper;

    @Override
    public FileCzFailedRecord getFileCzFailedRecordByOrdersn(String ordersn) {
        return fileCzFailedRecordMapper.getFileCzFailedRecordByOrdersn(ordersn);
    }

    @Override
    public FileCzFailedRecord getFileCzFailedRecordByChannelOrdersn(String channelOrdersn) {
        return fileCzFailedRecordMapper.getFileCzFailedRecordByCchannelOrdersn(channelOrdersn);
    }
}
