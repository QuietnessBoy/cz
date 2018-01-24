package com.zhjl.tech.tasks.model.tasks;

import com.baomidou.mybatisplus.annotations.IdType;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 平台用户表
 *
 */
@Data
@TableName("user")
public class User implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**  */
	@TableField(value = "update_time")
	private Date updateTime;

	/**  */
	@TableField(value = "create_time")
	private Date createTime;

	/**  */
	@TableField(value = "wallet_addr")
	private String walletAddr;

	/** 用户公钥 */
	@TableField(value = "public_key")
	private String publicKey;

	/** 加密后的私钥 */
	@TableField(value = "encrypted_prikey")
	private String encryptedPrikey;

	/** 加密说明 */
	private String encrypt;

	/** 渠道标识 */
	@TableField(value = "channel_id")
	private String channelId;

	/** 渠道用户标识 */
	@TableField(value = "channel_userid")
	private String channelUserid;

	/** 用户类型：1法人，2自然人 */
	@TableField(value = "user_type")
	private String userType;

	/** 姓名、法人名称 */
	@TableField(value = "user_name")
	private String userName;

	/** 身份标识：1身份证号、2社会统一信用代码 */
	@TableField(value = "user_id")
	private String userId;

	/** 是否通过实名认证？1通过，0未通过 */
	private String verified;

	/** 通过认证时间 */
	@TableField(value = "verify_time")
	private Date verifyTime;

	/** 是否冻结？1冻结，0未冻结 */
	private String freezed;

	/** 备注信息 */
	private String reamrk;
}
