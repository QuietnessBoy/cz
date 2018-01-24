package com.zhjl.tech.common.channeldemo;

public class ChannelConfig {

    //渠道密钥对。在存证平台注册的时候生成，然后保存起来。
    public static final String spublicKey = "048dbbc57d90f4f5ff4aa5a16d05067531cb3a9137d92a262550bfb17bd1f293d9cc9713d58a226df329855a58e0553a858c1da56fc0b61e8f4386083db34d737a";
    public static final String  sprivateKey = "76e046ff78a0ba45fed32ebf0c7b61eadfa2dc503fb5fe94f60edfec0a46a0c0";


    //渠道accessKey, 注册的时候由存证平台返回。
    public static final String accessKey = "8cfLPyeWH4o2SXdMwHd8qDaou8Ta7F7p5HJnvdhm6QCz";

    //渠道标识信息, 渠道进行sm2签名时的ida参数，注册的时候由存证平台返回。
    public static final String channelIDA = "super_channel_01";

    //存证平台公钥, 注册的时候由存证平台返回。
    public static final String platformPublicKey = "041c2b6997fc2e54d3e895cf08be78ca551fba1951165d68b307e3f72efa06cf2f939eee797c47958ef059d6dfa703bf8271f4080381e734fbd617b452afbfe5b4";

    //存证平台IDA信息, 存证平台进行sm2签名时的ida参数，注册的时候由存证平台返回。
    public static final String platformIDA = "zhijinlian_blockchain_cz2017";
}
