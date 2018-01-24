package com.zhjl.tech.ordersn.annotationdemo;

import org.springframework.stereotype.Component;

@Component
public class ThreadLocalVar {

    private ThreadLocal<String> traceId = new ThreadLocal<>();

    public void setTraceId(String tid){
        traceId.set(tid);
    }

    public String getTraceId(){
        return traceId.get();
    }
}
