package com.zhjl.tech.inter.service.inter;

import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.inter.model.inter.Config;

import java.util.List;

/**
 *
 * Config 表数据服务层接口
 *
 */
public interface IConfigService extends ISuperService<Config> {
    List<Config> getAllByConfigs();

    List<Config> getConfigByConfigTypeCount();

    List<Config> getConfigByConfigType(String configType);
}