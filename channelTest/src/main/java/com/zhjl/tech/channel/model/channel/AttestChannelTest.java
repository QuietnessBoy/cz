package com.zhjl.tech.channel.model.channel;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.IdType;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 *
 * 存证信息表（完备业务表）：存储存证文件的详细信息
 *
 */
@Data
@TableName("attest_channel_test")
public class AttestChannelTest implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/** 索引 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**  */
	@TableField(value = "update_time")
	private Date updateTime;

	/**  */
	@TableField(value = "create_time")
	private Date createTime;

	/**  */
	@TableField(value = "access_key")
	private String accessKey;

	/**  */
	@TableField(value = "sign_type")
	private String signType;

	/**  */
	private String sign;

	/**  */
	private String random;

	/**  */
	@TableField(value = "biz_sign")
	private String bizSign;

	/** [上链数据]订单号,全平台唯一标识. */
	private String ordersn;

	/** [上链数据]渠道来源 */
	@TableField(value = "channel_id")
	private String channelId;

	/** 交易标识。渠道用户标识。 */
	@TableField(value = "channel_userid")
	private String channelUserid;

	/** 交易标识。渠道存证订单唯一标识 */
	@TableField(value = "channel_ordersn")
	private String channelOrdersn;

	/** [上链数据]入链情况。1入链，0不入链 */
	private String chained;

	/** [上链数据]业务类型。数据文件、视屏、音频、压缩等方式 */
	@TableField(value = "biz_type")
	private String bizType;

	/** [上链数据]源文件名 */
	@TableField(value = "file_name")
	private String fileName;

	/** [上链数据]文件类型 */
	@TableField(value = "file_type")
	private String fileType;

	/** [上链数据]源文件长度 */
	@TableField(value = "file_size")
	private String fileSize;

	/** [上链数据]文件哈希值。sha256 */
	@TableField(value = "file_hash")
	private String fileHash;

	@TableField(value = "file_addr")
	private String fileAddr;

	/** [上链数据]存证人类型。1自然人，2法人，0其他 */
	@TableField(value = "owner_type")
	private String ownerType;

	/** [上链数据]存证人身份标识。身份证号、社会信用代码 */
	@TableField(value = "owner_id")
	private String ownerId;

	/** [上链数据]存证人名称 */
	@TableField(value = "owner_name")
	private String ownerName;

	/** 代理人 */
	@TableField(value = "agent_name")
	private String agentName;

	/** 代理人电话 */
	@TableField(value = "agent_phone")
	private String agentPhone;

	/** 代理人邮箱 */
	@TableField(value = "agent_email")
	private String agentEmail;

	/** [上链数据]当前存证持续时间(天) */
	private String duration;

	/** [上链数据]存证描述信息 */
	private String description;

	/** [上链数据]本次存证订单费用. */
	private String price;

	/** 请求标识，请求发起时间。格式：20170728172911 */
	@TableField(value = "request_time")
	private String requestTime;

	/** 记录状态的处理状态 */
	private String state;

	/** 测试用例名称 */
	@TableField(value = "test_name")
	private String  testName;

	/** 测试用例名称 */
	@TableField(value = "start_time")
	private Date  startTime;

	/** 测试用例名称 */
	@TableField(value = "expired_time")
	private Date  expiredTime;
}
