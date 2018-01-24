package com.zhjl.tech.inter.model.inter;

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
@TableName("fileczfailed_record")
public class FileCzFailedRecord implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/** 上链订单Id */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/** 知金链存证平台存证号 */
	private String ordersn;

	/** 渠道订单号 */
	@TableField(value="channel_ordersn")
	private String channelOrdersn;

	/** 订单创差时间 */
	@TableField(value = "create_time")
	private Date createTime;

	/** 状态码 */
	private String state;

	/** 状态描述 */
	private String msg;
}
