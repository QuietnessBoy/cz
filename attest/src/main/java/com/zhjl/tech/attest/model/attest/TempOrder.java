package com.zhjl.tech.attest.model.attest;

import com.baomidou.mybatisplus.annotations.IdType;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 临时信息表，用于Interface存储渠道请求数据
 *
 */
@Data
@TableName("temp_order")
public class TempOrder implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/** 索引 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/** 更新时间 not null */
	@TableField(value = "update_time")
	private Date updateTime;

	/** 订单创建时间 not null */
	@TableField(value = "create_time")
	private Date createTime;

	/** [上链数据]存证记录版本。格式：1-2^16递增版本号. not null */
	private String version;

	////////////////////////////////////////////////////////////////请求参数开始

	// 该存证记录根订单号
	@TableField(value = "ancestors_ordersn")
	private String ancestorsOrdersn;

	/**[上链数据]续期的上一次订单号。参与bizSign生成，参与platformSign生成  */
	@TableField(value = "parent_ordersn")
	private String parentOrdersn;

	/** [上链数据]订单号,全平台唯一标识.，参与platformSign生成 */
	private String ordersn;

	/** [上链数据]渠道id,参与bizSign生成，参与platformSign生成 not null*/
	@TableField(value = "channel_id")
	private String channelId;

	/** 渠道用户id,参与bizSign生成，参与platformSign生成 not null*/
	@TableField(value = "channel_userid")
	private String channelUserid;

	/** 交易标识。渠道存证订单唯一标识。参与bizSign生成，参与platformSign生成  not null*/
	@TableField(value = "channel_ordersn")
	private String channelOrdersn;

	/** [上链数据]渠道业务签名。 参与platformSign生成  not null */
	@TableField(value = "biz_sign")
	private String bizSign;

	/** [上链数据]服务商。暂时不用201710 */
	@TableField(value = "provinder_id")
	private String provinderId;

	/** 入链情况。1入链，0不入链。参与bizSign生成，参与platformSign生成  not null*/
	private String chained;

	/** 订单状态 */
	private String state;

	/** 订单状态更新时间 */
	@TableField(value = "state_time")
	private Date stateTime;

	/** [上链数据]钱包网络地址。表示终端用户方式 */
	@TableField(value = "wallet_addr")
	private String walletAddr;

	/** [上链数据]用户钱包公钥。 */
	@TableField(value = "public_key")
	private String publicKey;

	/** [上链数据]客户签名。客户使用本人的钱包中私钥，对该该存证记录的字段进行计算 */
	@TableField(value = "attest_sign")
	private String attestSign;

	/** [上链数据]存证类型标识。1文件存证，2hash存证 */
	@TableField(value = "attest_type")
	private String attestType;

	/** [上链数据]业务类型。数据文件、视屏、音频、压缩等方式，参与bizSign生成，参与platformSign生成 not null*/
	@TableField(value = "biz_type")
	private String bizType;

	/** [上链数据]源文件名。参与bizSign生成，参与platformSign生成 not null*/
	@TableField(value = "file_name")
	private String fileName;

	/** [上链数据]文件类型 参与bizSign生成，参与platformSign生成  not null*/
	@TableField(value = "file_type")
	private String fileType;

	/** [上链数据]源文件长度,以字节为单位。参与bizSign生成，参与platformSign生成  not null*/
	@TableField(value = "file_size")
	private String fileSize;

/*  2017-10 该版本为存证平台内部生成字段，不参与本期接口输入，不参与上述两项签名。
	 */
	/** [上链数据]文件签名。使用本人钱包私钥对该数据进行计算*/
	@TableField(value = "file_sign")
	private String fileSign;

	/** [上链数据]文件哈希值。sm3。参与bizSign生成，参与platformSign生成  not null*/
	@TableField(value = "file_hash")
	private String fileHash;

	@TableField(value = "file_addr")
	private String fileAddr;

	/** 加密情况。1加密，0不加密  not null*/
	private String encrypted;

	/** 加密算法 */
	@TableField(value = "encrypt_alog")
	private String encryptAlog;

	/** 加密秘钥 */
	@TableField(value = "encrypt_key")
	private String encryptKey;

	/** [上链数据]存证人类型。1自然人，2法人，0其他 。参与bizSign生成，参与platformSign生成  not null*/
	@TableField(value = "owner_type")
	private String ownerType;

	/** [上链数据]存证人身份标识。身份证号、社会信用代码 。参与bizSign生成，参与platformSign生成  not null*/
	@TableField(value = "owner_id")
	private String ownerId;

	/** [上链数据]存证人名称。参与bizSign生成，参与platformSign生成  not null*/
	@TableField(value = "owner_name")
	private String ownerName;

	/** 代理人*/
	@TableField(value = "agent_name")
	private String agentName;

	/** 代理人电话 */
	@TableField(value = "agent_phone")
	private String agentPhone;

	/** 代理人邮箱 */
	@TableField(value = "agent_email")
	private String agentEmail;

	/** [上链数据]存证起始时间  not null*/
	@TableField(value = "origin_time")
	private Date originTime;

	/** [上链数据]当前存证起始时间  not null*/
	@TableField(value = "start_time")
	private Date startTime;

	/** [上链数据]当前存证持续时间(天),从start_time开始算.,全平台唯一标识.，参与platformSign生成  not null*/
	private String duration;

	/** [上链数据]存证描述信息  not null*/
	private String description;

	/** [上链数据]本次存证订单费用.  not null*/
	private String price;

	/** 请求标识，请求发起时间。格式：20170728172911. 参与渠道bizSign生成  not null*/
	@TableField(value = "request_time")
	private Date requestTime;

	/**[上链数据]平台platformSign */
	@TableField(value = "platform_sign")
	private String platformSign;

	////////////////////////////////////////////////////////////////请求参数结束
}
