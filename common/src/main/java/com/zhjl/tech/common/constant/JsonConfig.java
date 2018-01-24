package com.zhjl.tech.common.constant;

/**
 * @author wind
 * 配置jsonresult字典
 */
public class JsonConfig {

    /*****************JsonResult状态码字典******************/
    // 请求处理完成
    public static String SolveOk="1";
    public static String SolveOkDesc="存证请求接收完成.";

    public static String SearchOk="2";
    public static String SearchOkDesc="找到该订单.";

    public static String ChainedOk="3";
    public static String ChainedOkDesc="区块链上存在该订单.";


    public static String DownloadOk="4";
    public static String DownloadOkDesc="下载完毕.";

    public static String ChainedHanding="5";
    public static String ChainedHandingDesc="数据正在上链..";

    public static String Handing="6";
    public static String HandingDesc="该订单正处于业务受理中.";

    public static String SearchNo="7";
    public static String SearchNoDesc="未找到该订单.";

    // 订单暂未上链
    public static String ErrorChainedState="8";
    public static String ErrorChainedStateDesc="订单暂未上链.";

    //失败的状态

    public static String ErrorOSSHash="-2";
    public static String ErrorOSSHashDesc="OSS文件Hash与链上Hash值不匹配.";

    public static String ChainedHash="-3";
    public static String ChainedHashDesc="请求参数Hash值与链上Hash值不匹配.";

    public static String IsNullHash="-4";
    public static String IsNullHashDesc="对应OSS文件Hash值为空";

    public static String TimeOut="-5";
    public static String TimeOutDesc="请求oss服务器超时";

    // 系统异常
    public static String SystemException="-999";
    public static String SystemExceptionDesc="业务异常";

    public static String ErrorFileAddr ="-998";
    public static String ErrorFileAddrDesc ="OSS地址传输错误.";

    public static String FileStreamException ="-997";
    public static String FileStreamExceptionDesc ="获取文件流异常.";

    // [某请求参数]不能为空/不能超过*字符

    public static String ErrorData="-20";
    public static String ErrorRequestTimeFormat ="-21";
    public static String ErrorRequestTimeFormatDesc ="请求时间requestTime格式错误.请参考yyyyMMdd-HHmmss格式.";

    public static String ErrorRandom="-30";
    public static String ErrorRandomDesc="请求随机数不合法.";

    public static String ErrorChannelId="-40";
    public static String ErrorChannelIdDesc="渠道ID不存在";

    public static String ErrorAccessKey="-50";
    public static String ErrorAccessKeyDesc="请求标识accessKey不匹配该渠道accessKey";

    public static String ErrorSignType="-60";
    public static String ErrorSignTypeDesc="请求签名类型signType不匹配.";

    public static String ErrorSign="-70";
    public static String ErrorSignDesc="请求签名sign校验未通过.";

    public static String ErrorBizSign="-80";
    public static String ErrorBizSignDesc="订单签名bizSign校验未通过";

    public static String RepeatChannelOrdersn="-90";
    public static String RepeatChannelOrdersnDesc="渠道订单号channelOrdersn已存在.";

    public static String NoExistOrdersn ="-91";
    public static String NoExistOrdersnDesc="续期订单号不存在,无法续期.";

    public static String MatchChannelId="-92";
    public static String MatchChannelIdDesc="渠道ID(channelId)与待续期订单的channelId不匹配.";

    public static String MatchChannelUserId="-93";
    public static String MatchChannelUserIdDesc="渠道用户ID(channelUserId)与待续期订单的channelUserId不匹配.";

    public static String ErrorBizType="-94";
    public static String ErrorBizTypeDesc="渠道订单签名(bizType)与待续期订单的bizType不匹配.";

    public static String ErrorFileName="-95";
    public static String ErrorFileNameDesc="文件名称(fileName)与待续期订单的fileName不匹配.";

    public static String ErrorFileType="-96";
    public static String ErrorFileTypeDesc="文件类型(fileType)与待续期订单的fileType不匹配.";

    public static String ErrorFileSize="-97";
    public static String ErrorFileSizeDesc="文件长度(fileSize)与待续期订单的fileSize不匹配.";

    public static String ErrorFileHash="-98";
    public static String ErrorFileHashDesc="文件Hash值(fileHash)与待续期订单的fileHash不匹配.";

    public static String ErrorOwnerType="-99";
    public static String ErrorOwnerTypeDesc="存证人类型(ownerType)与待续期订单的ownerType不匹配.";

    public static String ErrorOwnerName="-100";
    public static String ErrorOwnerNameDesc="存证人姓名(ownerName)与待续期订单的ownerName不匹配.";

    public static String ErrorOwnerId="-101";
    public static String ErrorOwnerIdDesc="存证人身份标识(ownerId)与待续期订单的ownerId不匹配.";

    public static String ExistChannelOrdersn="-102";
    public static String ExistChannelOrdersnDesc="渠道订单号(channelOrdersn)已存在.";

    public static String ErrorOssFileSize="-103";
    public static String ErrorOssFileSizeDesc="文件长度不匹配.";

    public static String ErrorOssFileHash="-104";
    public static String ErrorOssFileHashDesc="文件Hash不匹配.";

}
