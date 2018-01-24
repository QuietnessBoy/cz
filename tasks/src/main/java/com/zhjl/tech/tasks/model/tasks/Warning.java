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
 * 
 *
 */
@Data
@TableName("warning")
public class Warning implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/** 索引 */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/** 记录更新时间 */
	@TableField(value = "update_time")
	private Date updateTime;

	/**  记录创建时间 */
	@TableField(value = "create_time")
	private Date createTime;

	/** 业务类型 1存证业务类型，2上链业务类型 */
	@TableField(value="biz_type")
	private String bizType;

	/** 存证号 */
	private String ordersn;

	/** 渠道订单号 */
	@TableField(value="channel_ordersn")
	private String channelOrdersn;

	/** 订单异常次数记录 */
	@TableField(value = "num")
	private int num;

	/**  */
	private String remark;

}
