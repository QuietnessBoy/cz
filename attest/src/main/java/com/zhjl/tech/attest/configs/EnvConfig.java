package com.zhjl.tech.attest.configs;

import com.zhjl.tech.attest.model.attest.Channel;
import com.zhjl.tech.attest.model.attest.Config;
import com.zhjl.tech.attest.service.attest.IChannelService;
import com.zhjl.tech.attest.service.attest.IConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: wildfist
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

//    @PostConstruct
//    public void initURL(){
//        System.out.println("cache 中文");
//        log.info("start to cache the common URL---------------->>>>> 中文");
//            log.info("cache done!");
//    }


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
        log.info("成功缓存Config表数据.{}",configChannnelIdMap);
    }
}
