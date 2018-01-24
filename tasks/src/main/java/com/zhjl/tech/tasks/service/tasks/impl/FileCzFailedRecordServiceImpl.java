package com.zhjl.tech.tasks.service.tasks.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.tasks.mapper.tasks.FileCzFailedRecordMapper;
import com.zhjl.tech.tasks.model.tasks.FileCzFailedRecord;
import com.zhjl.tech.tasks.service.tasks.IFileCzFailedRecordService;
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
        return fileCzFailedRecordMapper.getFileCzFailedRecordByChannelOrdersn(channelOrdersn);
    }
}
