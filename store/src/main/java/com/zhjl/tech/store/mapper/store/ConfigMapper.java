package com.zhjl.tech.store.mapper.store;


import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.store.model.store.Config;

import java.util.List;

/**
 *
 * Config 表数据库控制层接口
 *
 */
public interface ConfigMapper extends AutoMapper<Config> {
    List<Config> getAllByConfigs();

    List<Config> getConfigByConfigTypeCount();

    List<Config> getConfigByConfigType(String configType);
}