package com.zhjl.tech.tasks.model.tasks;

import com.baomidou.mybatisplus.annotations.IdType;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 
 *
 */
public class Sequence implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(type = IdType.AUTO)
	private String name;

	/**  */
	@TableField(value = "current_value")
	private Integer currentValue;

	/**  */
	private Integer increment;

	/**  */
	@TableField(value = "current_day")
	private Date currentDay;


	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCurrentValue() {
		return this.currentValue;
	}

	public void setCurrentValue(Integer currentValue) {
		this.currentValue = currentValue;
	}

	public Integer getIncrement() {
		return this.increment;
	}

	public void setIncrement(Integer increment) {
		this.increment = increment;
	}

	public Date getCurrentDay() {
		return this.currentDay;
	}

	public void setCurrentDay(Date currentDay) {
		this.currentDay = currentDay;
	}

}
