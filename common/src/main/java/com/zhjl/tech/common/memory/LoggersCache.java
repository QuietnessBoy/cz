package com.zhjl.tech.common.memory;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wind
 * 获取特定类的logger对象
 *
 */
@Component
public class LoggersCache {


    private Map<String,Logger> loggerMap = new ConcurrentHashMap<>();

    /**
     * 获取指定类的logger对象。可能获取空值
     * 必须满足ZhijlCtrl方法的前提，否则报异常
     * @param className
     * @return
     */
    public Logger getLogger(String className) throws IllegalAccessException, ClassNotFoundException, NoSuchFieldException {

        Logger logger = loggerMap.get(className);

        if( logger==null){
            Class clazz = Class.forName(className);

            //和上面一样，通过Class得到类声明的属性。
            Field field = clazz.getDeclaredField("log");
            field.setAccessible(true);
            //因为该属性是静态的，所以直接从类的Class里取。可能为空
            logger = (Logger)field.get(clazz) ;

            loggerMap.put(className,logger);
        }

        return logger;
    }

}
