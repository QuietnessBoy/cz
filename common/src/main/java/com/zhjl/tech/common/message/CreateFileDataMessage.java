package com.zhjl.tech.common.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 存证请求对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateFileDataMessage implements Serializable{

    private String ordersn;
}
