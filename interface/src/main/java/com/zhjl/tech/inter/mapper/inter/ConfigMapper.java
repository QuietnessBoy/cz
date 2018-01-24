package com.zhjl.tech.inter.mapper.inter;

import com.baomidou.mybatisplus.mapper.AutoMapper;
import com.zhjl.tech.inter.model.inter.Config;

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