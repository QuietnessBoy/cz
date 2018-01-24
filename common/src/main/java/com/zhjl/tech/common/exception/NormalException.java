package com.zhjl.tech.common.exception;

/**
 * @author wind
 *
 * 一般异常。当系统发生此类异常的时候，多数不是系统本身的错误（用户的fileadd无法下载）、或者属于并发的正常现象（多个任务抢一个锁）。
 * 遇到此异常，可以暂时性忽略。
 */
public class NormalException extends RuntimeException{

    public NormalException(String msg){
        super(msg);
    }
}
