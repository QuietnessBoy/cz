package com.zhjl.tech.inter.service.inter.impl;

import com.zhjl.tech.inter.mapper.inter.AttestFileMapper;
import com.zhjl.tech.inter.model.inter.AttestFile;
import com.zhjl.tech.inter.service.inter.IAttestFileService;
import org.springframework.stereotype.Service;

import com.baomidou.framework.service.impl.SuperServiceImpl;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * AttestFile 表数据服务层接口实现类
 *
 */
@Service
public class AttestFileServiceImpl extends SuperServiceImpl<AttestFileMapper, AttestFile> implements IAttestFileService {

    @Resource
    private AttestFileMapper attestFileMapper;

    @Override
    public List<AttestFile> getAllAttestFile() {
        return attestFileMapper.getAllAttestFile();
    }

    @Override
    public AttestFile getAttestFileByOrdersn(String ordersn) {
        return attestFileMapper.getAttestFileByOrdersn(ordersn);
    }

}