package com.zhjl.tech.channel.mvc;

import lombok.Data;

/**
 * Author: wildfist
 * Email: windsp@foxmail.com
 * Date: 17/4/5
 * Time: 下午5:50
 * CreateFileStreamDesc: 封装Json返回信息
 */
@Data
public class JsonResult {
    //是否成功
    private boolean success;
    //消息
    private String msg;
    //状态
    private String status;
    //返回结果对象
    private Object obj;
}
