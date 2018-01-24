package com.zhjl.tech.attest.service.attest;


import com.baomidou.framework.service.ISuperService;
import com.zhjl.tech.attest.model.attest.Config;

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