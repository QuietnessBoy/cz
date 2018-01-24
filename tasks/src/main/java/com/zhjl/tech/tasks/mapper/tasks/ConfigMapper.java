package com.zhjl.tech.tasks.mapper.tasks;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.tasks.model.tasks.Config;

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