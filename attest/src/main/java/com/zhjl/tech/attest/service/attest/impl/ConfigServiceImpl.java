package com.zhjl.tech.attest.service.attest.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.attest.mapper.attest.ConfigMapper;
import com.zhjl.tech.attest.model.attest.Config;
import com.zhjl.tech.attest.service.attest.IConfigService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * Config 表数据服务层接口实现类
 *
 */
@Service
public class ConfigServiceImpl extends SuperServiceImpl<ConfigMapper, Config> implements IConfigService {

    @Resource
    private  ConfigMapper configMapper;

    @Override
    public List<Config> getAllByConfigs() {
        return configMapper.getAllByConfigs();
    }

    @Override
    public List<Config> getConfigByConfigTypeCount() {
        return configMapper.getConfigByConfigTypeCount();
    }


    @Override
    public List<Config> getConfigByConfigType(String configType) {
        return configMapper.getConfigByConfigType(configType);
    }

}