package com.zhjl.tech.tasks.model.tasks;

import java.io.Serializable;
import java.security.Timestamp;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.IdType;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 *
 * 
 *
 */
@TableName("rabbitmq_msg")
@Data
public class RabbitmqMsg implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/**  */
	@TableField(value = "create_time")
	@JSONField(format="yyyyMMdd-HH:mm:ss")
	private Date createTime;


	/** 队列名称 */
	@TableField(value = "queue_name")
	private String queueName;

	/** 消息数量 */
	private long num;
}
