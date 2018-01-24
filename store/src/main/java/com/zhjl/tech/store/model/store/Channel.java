package com.zhjl.tech.store.model.store;

import com.baomidou.mybatisplus.annotations.IdType;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 渠道信息表：合作第三方组织或平台的描述。核心业务数据，注意考虑存证记录中的渠道代码不可更改性。渠道也可以视为组织。
 *
 */
@Data
@TableName("channel")
public class Channel implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_time")
	private Date updateTime;

	/**
	 * 记录创建时间
	 */
	@TableField(value = "create_time")
	private Date createTime;

	/**
	 * 渠道唯一标识：平台生成
	 */
	@TableField(value = "channel_id")
	private String channelId;

	/**
	 * 渠道公钥,Hex编码格式
	 */
	@TableField(value = "channel_public_key")
	private String channelPublicKey;

	/**
	 * 渠道私钥
	 */
	@TableField(value = "channel_private_key")
	private String channelPrivateKey;

	/**
	 * 平台公钥,Hex编码格式
	 */
	@TableField(value = "platform_publick_key")
	private String platformPublickKey;

	/**
	 * 平台私钥
	 */
	@TableField(value = "platform_private_key")
	private String platformPrivateKey;

	/**
	 * 渠道唯一标识
	 */
	@TableField(value = "channel_ida")
	private String channelIda;

	/**
	 * 访问标识
	 */
	@TableField(value = "access_key")
	private String accessKey;

	/**
	 * 渠道名称
	 */
	private String name;

	/**
	 * 企业类型：1企业、2政府机关、3事业单位、4社会团体、5非盈利性组织、0其他
	 */
	@TableField(value = "channel_type")
	private String channelType;

	/**
	 * 营业年限：3/5/10/20
	 */
	@TableField(value = "biz_duration")
	private String bizDuration;

	/**
	 * 企业规模：100/300/500人
	 */
	private String scale;

	/**
	 * 主营业务
	 */
	private String biz;

	/**
	 * 网址
	 */
	private String url;

	/**
	 * 地址
	 */
	private String address;

	/**
	 * 联系人
	 */
	private String contact;

	/**
	 * 联系电话
	 */
	private String phone;

	/**
	 * 是否通过实名认证？1通过，0未通过
	 */
	private String verified;

	/**
	 * 营业执照全称
	 */
	@TableField(value = "license_name")
	private String licenseName;

	/**
	 * 社会统一信用代码
	 */
	@TableField(value = "license_num")
	private String licenseNum;

	/**
	 * 对公账户名
	 */
	@TableField(value = "account_name")
	private String accountName;

	/**
	 * 对公账户账号
	 */
	@TableField(value = "account_num")
	private String accountNum;

	/**
	 * 是否冻结？1冻结，0未冻结
	 */
	private String freezed;

}
