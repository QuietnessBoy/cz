package com.zhjl.tech.common.annotation.controllers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 功能：通用的controller返回结果异常处理
 *
 * 对象要求：
 * 1 类必须有@slf4j注解
 * 2 方法必须有HttpServletResponse参数
 * 3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.PARAMETER})
public @interface ZhijlCtrl {

    String value() default "";

}