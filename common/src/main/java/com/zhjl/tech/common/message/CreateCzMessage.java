package com.zhjl.tech.common.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 存证请求对象。
 * 文件存证流程、hash存证流程，inter推送给attest的时候的msg。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCzMessage implements Serializable{

    private String channelOrdersn;
}
