package com.zhjl.tech.tasks.annotationdemo.atlog;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 平台日志注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.PARAMETER})
public @interface ZhijlLog {

    String value() default "";

    String rquestMethod() default "";

    //traceID
    String traceIdLocal() default "";

    /**
     * 获取bizId的方法说明。目前只支持两个层次的查询
     *  形式 p[$(index)], 或者p[$(index)].$(method)
     *   p[0].getOrdersn ，调用第0个参数的getOrdersn方法获取bizid
     *   p[2]   ,直接采用第二个参数作为bizid
     * @return
     */
//    String traceLocation() default "";
//    String Sn() default "";
//    String Sntype() default "";

    String orderSn() default "";
    String ChannelorderSn() default "";

    String SPLIT = ".";
    String START = "[";
    String END = "]";
}