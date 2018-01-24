package com.zhjl.tech.inter.configs;

import ch.qos.logback.ext.spring.web.WebLogbackConfigurer;
import com.zhjl.tech.inter.model.inter.Channel;
import com.zhjl.tech.inter.model.inter.Config;
import com.zhjl.tech.inter.service.inter.IChannelService;
import com.zhjl.tech.inter.service.inter.IConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SystemUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author  wildfist
 * Email: windsp@foxmail.com
 * Date: 2017/2/24
 * Time: 14:33
 * Describe: 初始化数据库
 */
@Slf4j
@Component
public class EnvConfig {

    public static Map<String,Channel> channelMap= new HashMap<>();

    @Resource
    IChannelService channelService;

    @Resource
    IConfigService configsService;

    /**
     * 初始化map. 在项目启动的时候,或者手动调用
     */
    @PostConstruct
    public void init(){
        //查询一次数据库，获取所有channel信息
        List<Channel> list = channelService.getAllByChannel();
        channelMap = new HashMap<>();
        for( Channel channel:list){
            channelMap.put(channel.getChannelId(), channel);
        }
        log.info("成功缓存channel表数据。");
    }

    /**
     * 清除channel数据
     */
    public void clean(){
        channelMap.clear();
    }


    /**
     * 初始化configs表数据
     */
    public static Map<String,String> configUrlMap = new HashMap<>();
    //todo 检查是否有人在用！
    public static Map<String,Map<String,String>> configChannnelIdMap = new HashMap<>();

    @PostConstruct
    public void configValueInit(){
        List<Config> list =configsService.getAllByConfigs();
        for(Config config :list){
            configUrlMap = new HashMap<>();
            List<Config> list1 = configsService.getConfigByConfigType(config.getConfigType());
            for(Config config2 : list1){
                configUrlMap.put(config2.getConfigKey(),config2.getConfigValue());
            }
            configChannnelIdMap.put(config.getConfigType(), configUrlMap);
        }
        log.info("成功缓存Config表数据.");
    }
}
