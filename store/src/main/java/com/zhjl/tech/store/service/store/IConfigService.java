package com.zhjl.tech.store.service.store;


import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.store.model.store.Config;

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