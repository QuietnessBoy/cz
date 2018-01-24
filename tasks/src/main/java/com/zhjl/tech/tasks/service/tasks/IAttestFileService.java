package com.zhjl.tech.tasks.service.tasks;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.tasks.model.tasks.AttestFile;

import java.util.List;

/**
 *
 * AttestFile 表数据服务层接口
 *
 */
public interface IAttestFileService extends ISuperService<AttestFile> {

    List<AttestFile> getAllAttestFile();

    AttestFile getAttestFileByOrdersn(String ordersn);

}