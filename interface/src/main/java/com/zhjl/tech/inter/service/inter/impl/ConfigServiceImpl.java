package com.zhjl.tech.inter.service.inter.impl;

import com.zhjl.tech.inter.mapper.inter.ConfigMapper;
import com.zhjl.tech.inter.model.inter.Config;
import com.zhjl.tech.inter.service.inter.IConfigService;
import org.springframework.stereotype.Service;

import com.baomidou.framework.service.impl.SuperServiceImpl;

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