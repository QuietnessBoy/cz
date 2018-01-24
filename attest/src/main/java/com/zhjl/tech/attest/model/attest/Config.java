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
 * 
 *
 */
@Data
@TableName("config")
public class Config implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/** 渠道ID */
	@TableField(value = "config_type")
	private String configType;

	/** 键 */
	@TableField(value = "config_key")
	private String configKey;

	/** 值 */
	@TableField(value = "config_value")
	private String configValue;

	/** 创建时间 */
	@TableField(value = "create_time")
	private Date createTime;

	/** 备注 */
	private String remark;

}
