package com.zhjl.tech.attest.mapper.attest;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.attest.model.attest.AttestFile;

import java.util.List;

/**
 *
 * AttestFile 表数据库控制层接口
 *
 */
public interface AttestFileMapper extends AutoMapper<AttestFile> {

    List<AttestFile> getAllAttestFile();

    AttestFile getAttestFileByOrdersn(String ordersn);

}