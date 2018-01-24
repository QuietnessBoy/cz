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
@TableName("attest_chained")
public class AttestChained implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/** 上链订单Id */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/** 知金链存证平台存证号 */
	private String ordersn;

	/** 上链请求标识 */
	private String txid;

	/** 请求上一次发送时间 */
	@TableField(value = "send_time")
	private Date sendTime;

	/** 请求下一次发送时间 */
	@TableField(value = "refresh_time")
	private Date refreshTime;

	/** 订单创差时间 */
	@TableField(value = "create_time")
	private Date createTime;

	/** 渠道订单号 */
	@TableField(value="channel_ordersn")
	private String channelOrdersn;

	/** 处理结果 */
	private String state;

	private int num;
	/** 备注 */
	private String remark;
}
