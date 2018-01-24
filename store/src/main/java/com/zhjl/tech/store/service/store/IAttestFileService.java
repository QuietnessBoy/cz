package com.zhjl.tech.store.service.store;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.store.model.store.AttestFile;

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