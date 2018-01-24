package com.zhjl.tech.common.annotation.controllers;

import com.zhjl.tech.common.constant.JsonConfig;
import com.zhjl.tech.common.dto.JsonResult;
import com.zhjl.tech.common.memory.LoggersCache;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 统一controller的异常封装处理
 */
@Component
@Aspect
@Slf4j
public class ZhijlCtrlAspect {

    @Resource
    private LoggersCache loggersCache;

    @Pointcut("@annotation(com.zhjl.tech.common.annotation.controllers.ZhijlCtrl)")
    private void zjlctrl(){}//定义一个切入点

    @Around(value ="zjlctrl() && args(object,..) ")
    public Object interceptorApplogic(ProceedingJoinPoint pj, Object object) throws Throwable {
        String className = pj.getTarget().getClass().getName();
        String methodName = pj.getSignature().getName();

        //获取日志对象
        Logger logger = loggersCache.getLogger(className);

        //获取response对象
        HttpServletResponse response = null;
        Object[] arguments = pj.getArgs();
        for( Object argument : arguments){
            if(argument instanceof HttpServletResponse){
                response = (HttpServletResponse)argument;
            }
        }

        //通用业务处理
        JsonResult jsonResult;
        try {
            jsonResult = (JsonResult)pj.proceed();
            if (jsonResult.isSuccess()) {
                response.setStatus(HttpStatus.SC_OK);
            } else {
                response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            response.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
            logger.error( methodName+"内部异常.", e);
            jsonResult = new JsonResult(false, JsonConfig.SystemExceptionDesc, JsonConfig.SystemException,null);
        }
        return jsonResult;
    }
}