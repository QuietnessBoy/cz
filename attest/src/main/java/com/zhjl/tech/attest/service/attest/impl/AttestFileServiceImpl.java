package com.zhjl.tech.attest.service.attest.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.attest.mapper.attest.AttestFileMapper;
import com.zhjl.tech.attest.model.attest.AttestFile;
import com.zhjl.tech.attest.service.attest.IAttestFileService;
import org.springframework.stereotype.Service;

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