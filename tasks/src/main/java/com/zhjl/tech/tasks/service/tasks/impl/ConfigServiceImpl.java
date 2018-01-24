package com.zhjl.tech.tasks.service.tasks.impl;

import com.baomidou.framework.service.impl.SuperServiceImpl;
import com.zhjl.tech.tasks.mapper.tasks.ConfigMapper;
import com.zhjl.tech.tasks.model.tasks.Config;
import com.zhjl.tech.tasks.service.tasks.IConfigService;
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