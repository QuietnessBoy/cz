package com.zhjl.tech.common.dto.chain;

import lombok.Data;

@Data
public class AddCzChainRequestObj {
    /**
     * 本次请求的事务id，可随机生成。用于回调对接
     */
    private String Txid;
    private String Ordersn ;
    private String CzObj;
    private String Sign;
}
