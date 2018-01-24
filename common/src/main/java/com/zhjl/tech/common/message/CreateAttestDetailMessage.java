package com.zhjl.tech.common.message;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * xq业务流程，attest项目接收
 *
 */
//todo  查看是否需要这么多的字段
@Data
public class CreateAttestDetailMessage implements Serializable {


	/** 更新时间 */
	private Date updateTime;

	/** 订单创建时间 */
	private Date createTime;

	/** [上链数据]存证记录版本。格式：1-2^16递增版本号.  */
	private String version;

	/** [上链数据]订单号,全平台唯一标识. */
	private String ordersn;

	private String ancestorsOrdersn;

	/** 交易标识。表示存证原存证号 */
	private String parentOrdersn;

	/** [上链数据]渠道来源 */
	private String channelId;

	/** 交易标识。渠道用户标识。 */
	private String channelUserid;

	/** 交易标识。渠道存证订单唯一标识 */
	private String channelOrdersn;

	/** [上链数据]服务商 */
	private String provinderId;

	/** [上链数据]入链情况。1入链，0不入链 */
	private String chained;

	/** [上链数据]钱包网络地址。表示终端用户方式 */
	private String walletAddr;

	/** [上链数据]用户钱包公钥。 */
	private String publicKey;

	/** [上链数据]客户签名。客户使用本人的钱包中私钥，对该该存证记录的字段进行计算 */
	private String attestSign;

	/** [上链数据]存证类型标识。1文件存证，2hash存证 */
	private String attestType;

	/** [上链数据]业务类型。数据文件、视屏、音频、压缩等方式 */
	private String bizType;

	/** [上链数据]业务类型。数据文件、视屏、音频、压缩等方式 */
	private String bizSign;

	/** [上链数据]源文件名 */
	private String fileName;

	/** [上链数据]文件类型 */
	private String fileType;

	/** [上链数据]源文件长度 */
	private String fileSize;

	/** [上链数据]文件签名。使用本人钱包私钥对该数据进行计算 */
	private String fileSign;

	/** [上链数据]文件哈希值。sha256 */
	private String fileHash;

	/** 源文件存储地址 */
	private String fileAddr;

	/** 加密情况。1加密，0不加密 */
	private String encrypted;

	/** 加密算法 */
	private String encryptAlog;

	/** 加密秘钥 */
	private String encryptKey;

	/** [上链数据]存证人类型。1自然人，2法人，0其他 */
	private String ownerType;

	/** [上链数据]存证人身份标识。身份证号、社会信用代码 */
	private String ownerId;

	/** [上链数据]存证人名称 */
	private String ownerName;

	/** 代理人 */
	private String agentName;

	/** 代理人电话 */
	private String agentPhone;

	/** 代理人邮箱 */
	private String agentEmail;

	/** [上链数据]存证起始时间 */
	private Date originTime;

	/** [上链数据]当前存证起始时间 */
	private Date startTime;

	/** [上链数据]当前存证持续时间(天),从start_time开始算. */
	private String duration;

	/** [上链数据]存证描述信息 */
	private String description;

	/** [上链数据]本次存证订单费用. */
	private String price;

	/** 请求标识，请求发起时间。格式：20170728172911 */
	private Date requestTime;

	/*存证平台签名*/
	private String platformSign;

	/*请求上链对象*/
	private String chainedContent;

	/*存证到期时间*/
	private String expiredTime;
}
