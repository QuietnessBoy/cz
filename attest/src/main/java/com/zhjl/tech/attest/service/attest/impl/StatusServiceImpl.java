package com.zhjl.tech.attest.service.attest.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.attest.mapper.attest.StatusMapper;
import com.zhjl.tech.attest.model.attest.Status;
import com.zhjl.tech.attest.service.attest.IStatusService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Status 表数据服务层接口实现类
 *
 */
@Service
public class StatusServiceImpl extends SuperServiceImpl<StatusMapper, Status> implements IStatusService {

    @Resource
    private StatusMapper statusMapper;

    @Override
    public Status getStatusByChannelOrdersn(String channelOrdersn) {
        return statusMapper.getStatusByChannelOrdersn(channelOrdersn);
    }

    @Override
    public Status getStatusByOrdersn(String ordersn) {
        return statusMapper.getStatusByOrdersn(ordersn);
    }

    @Override
    public List<Status> getStatusByNum(int num) {
        return statusMapper.getStatusByNum(num);
    }

    @Override
    public List<Status> getStatusByStateBiz(String state) {
        return statusMapper.getStatusByStateBiz(state);
    }

    @Override
    public void updateStatusByChannelOrdersn(String channelordersn, String remark) {
        statusMapper.updateStatusByChannelOrdersn(channelordersn,remark);
    }

    @Override
    public List<Status> getStatusByStateBizInTime(String state, int num) {
        return statusMapper.getStatusByStateBizInTime(state,num);
    }

    @Override
    public List<Status> getStatusByStateNotifyInTime(String state, int num) {
        return statusMapper.getStatusByStateNotifyInTime(state,num);
    }

    @Override
    public List<Status> getStatusByMoreStateBiz(String state1, String state2, String state3) {
        return statusMapper.getStatusByMoreStateBiz(state1,state2,state3);
    }


}