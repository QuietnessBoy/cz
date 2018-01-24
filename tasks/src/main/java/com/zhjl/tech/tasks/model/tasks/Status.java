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
 *  订单状态表
 *
 */
@Data
@TableName("status")
public class Status implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/** id */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**  更新时间 not null */
	@TableField(value = "update_time")
	private Date updateTime;

	/**  创建时间 not null */
	@TableField(value = "create_time")
	private Date createTime;

	/** 订单号 */
	private String ordersn;

	/** 渠道唯一标识 not null*/
	@TableField(value = "channel_ordersn")
	private String channelOrdersn;

	/** 订单当前状态  not null*/
	@TableField(value = "state_biz")
	private String stateBiz;

	/* 订单回调状态*/
	@TableField(value = "state_notify")
	private String stateNotify;

	/* 异常次数 */
	private int num;

	/* 备注说明 */
	private String remark;
}
