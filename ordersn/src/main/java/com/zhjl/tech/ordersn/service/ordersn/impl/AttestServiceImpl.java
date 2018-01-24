package com.zhjl.tech.ordersn.service.ordersn.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.ordersn.mapper.ordersn.AttestMapper;
import com.zhjl.tech.ordersn.model.ordersn.Attest;
import com.zhjl.tech.ordersn.service.ordersn.IAttestService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


/**
 *
 * Attest 表数据服务层接口实现类
 *
 */
@Service
public class AttestServiceImpl extends SuperServiceImpl<AttestMapper, Attest> implements IAttestService {

    @Resource
    private AttestMapper attestMapper;


    @Override
    public Attest getAttestByOrdersn(String ordersn) {
        return attestMapper.getAttestByOrdersn(ordersn);
    }
}