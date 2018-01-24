package com.zhjl.tech.store.service.store.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.store.mapper.store.FileCzFailedRecordMapper;
import com.zhjl.tech.store.model.store.FileCzFailedRecord;
import com.zhjl.tech.store.service.store.IFileCzFailedRecordService;
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
