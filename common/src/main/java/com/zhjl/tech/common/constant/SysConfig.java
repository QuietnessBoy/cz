package com.zhjl.tech.common.constant;

/**
 * @author wind
 *
 * 系统参数 ，当前版本，version=0.9的固定参数
 */
public class SysConfig {

    /**
     * 服务商ID
     * 对应attest表中privinderId
      */
    public static  String DefaultProvinderId = "ZHJL";

    /**
     * 版本号
     * 对应attest表中的version字段
     * 0.9版本
     */
    public static  String Version = "0.9";

    /**
     * 未加密
     * 对应attest表中encrypt字段
     * 0代表未加密
     */
    public static  String No_Encrypt = "0";

    /**
     *  文件存证类型
     *  attest表的attest_type字段
     *  "1"代表文件存证，"0"代表文件Hash存证
     */
    public static  String Attest_Type_File = "1";
    public static  String Attest_Type_Hash = "2";

    /**
     * 对应config表config_type字段
     */
    public static final String Config_type_zjl = "zjl";

    /**
     * 签名类型
     * 参与存证业务时的签名参数之一，也标识签名类型为MD5,默认为MD5
     */
    public static final String SignType="MD5";

    /**
     * 编码规则
     * 存证系统编码规则同意为UTF-8
     */
    public static final String CharsetName ="UTF-8";

    /**
     * 知金链IDA ,各个请求签名的附加参数，用于生成sign
     */
    public static final String Rune = "zhijinlian_cz_201710";

    /**
     *  时间转换格式
     *  数据库统一时间字段格式
     */
    public static final String Format="yyyyMMdd-HHmmss";

    /**
     * 异常订单处理次数限制
     * status表/attest_chained表中num字段，都是用于记录当前存证订单异常次数
     */
    public static final int Num= 5 ;

    /**
     * 上链限流RateLimiter每秒不超过几个任务被提交
     * 单位：上链订单数/秒
     */
    public static final double limiterNum=10;

    /**
     * 上链限流几毫秒执行一次
     * 单位：毫秒
     */
    public static final Integer limiterSleepNum=100;

    /**
     * 上链限流每失败一次推后时间间隔
     * 单位：毫秒
     */
    public static final Integer limiterNextTime=120000;

    /***************平台内部属性*************************/
    /**
     * 是否上链
     * attest表中chained字段的取值
     * 1为上链
     */
    public static String ChainedOk = "1";

    /**
     * 业务报警类型。
     * Warning表中对应type字段。标识哪一步除了异常
     * 1为业务处理阶段异常，2为上链处理阶段异常
     */
    public static String BizType = "1";
    public static String ChainedType = "2";

    /***************业务处理参数*************************/
    /**
     * 订单状态。对应attest表state字段。
     * "-1"表示处理失败。带源文件的存证过程中会出现，原因包括：oss地址不对、文件大小不对、文件hash不对。
     * "0"表示处理中
     * "1"表示业务处理完成
     * "2"表示已经上链
     * "-99"表示订单已经过期
     */
    public static  String SolveFaild = "-1";
    public static  String UnSolve = "0";
    public static  String BizSolveOk = "1";
    public static  String GoChained = "2";
    public static  String Expired = "-99";

    /**
     * 上链状态,attestChained表的setState字段取值范围.
     * 0表示未调用上链接口数据当前状态
     * -1表示订单上链失败/订单请求接口失败
     * 1标识订单请求上链接口成功
     */
    public static String UnFinishChainedAddCz = "0";
    public static String FailedChainedAddCz = "-1";
    public static String SuccessChainedAddCz = "1";


    /***************rabbitMq消息地址*************************/

    // 推送文件存证请求至attest的rabbitMq队列key
    public static  String CreateFileCz= "CreateFileCz";
    // 推送文件存证请求至store的rabbitMq队列key
    public static String CreateFileCzByAddr="CreateFileCzByAddr";

    // 推送文件Hash存证请求至attest的rabbitMq队列key
    public static  String CreateHashCz="CreateHashCz";
    // 推送存证续期请求至attest的rabbitMq队列key
    public static  String CreateCzXq="CreateCzXq";


    // 各个队列名称
    public static  String CreateFileCzQueue= "com.zhjl.tech.attest.CreateFileCz";
    public static  String CreateFileCzByAddrQueue= "com.zhjl.tech.store.CreateFileCzByAddr";
    public static  String CreateHashCzQueue= "com.zhjl.tech.attest.CreateHashCz";
    public static  String CreateCzXqQueue= "com.zhjl.tech.attest.CreateCzXq";

    /********************config表key值************************/

    /**
     * config表中对应key字段
     * 获取特定渠道的指定功能的url的具体地址
     */
    public static String cz_ok_url="cz_ok_url";
    public static String hash_cz_ok_url="hash_cz_ok_url";
    public static String xq_ok_url="xq_ok_url";
    public static String query_cz_chained_url="query_cz_chained_url";
    public static String add_cz_chained_url="add_cz_chained_url";

    /**
     * 对应config表中key字段
     * 存证平台获取订单号地址的key值
     */
    public static String ordersnUrl = "ordersn_url";

    /**
     * 用于请求订单号时的参数设置
     * 1为存储存证，3为区块链存证
     */
    public static String SaveAttest ="1";
    public static String BlockChainAttest ="3";

    /**
     * 每次扫描超时订单条数
     * 用于定时机制扫描过期订单的订单数
     * 单位：扫描订单数
     */
    public static int Count = 500;

    /**
     * store文件处理失败异常信息
     */
    public static String HashException = "文件摘要不匹配";
    public static String FileSizeException = "文件字节数不匹配";
    /**
     * 文件存证失败，回调success返回值
     */
    public static String FileCzsuccess = "false";

}
