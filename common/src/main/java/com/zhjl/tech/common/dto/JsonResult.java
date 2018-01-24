package com.zhjl.tech.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: wildfist
 * Email: windsp@foxmail.com
 * Date: 17/4/5
 * Time: 下午5:50
 * CreateFileStreamDesc: 封装Json返回信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class JsonResult<T> {
    /**
     *
     是否成功
     */
    private boolean success;

    /**
     * 结果说明
     */
    private String msg;

    /**
     * 结果状态码
     */
    private String status;

    /**
     * 返回的结果对象
     */
    private T obj;

}
