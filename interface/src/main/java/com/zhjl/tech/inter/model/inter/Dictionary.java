package com.zhjl.tech.inter.model.inter;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.IdType;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

/**
 *
 * 
 *
 */
@Data
@TableName("dictionary")
public class Dictionary implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/** 版本号 */
	private String version;

	/** 字典类型 */
	private String type;

	/** 字典代码 */
	private String code;

	/** 展现文本：社会统一信用代码 */
	private String text;

	/** 父字典代码 */
	@TableField(value = "parent_code")
	private String parentCode;

	/** 备注说明：6位行政区划代码 */
	private String remark;

	/** 创建时间 */
	@TableField(value = "create_time")
	private Date createTime;

	/** 更新时间 */
	@TableField(value = "update_time")
	private Date updateTime;

}
