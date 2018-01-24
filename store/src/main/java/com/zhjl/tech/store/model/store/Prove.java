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
 * 出证订单表：描述出证订单信息，与存证实体表关联。
 *
 */
@Data
@TableName("prove")
public class Prove implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/** 出证订单唯一标识 */
	@TableField(value = "prove_id")
	private String proveId;

	/** 出证理由 */
	private String reason;

	/** 用户申请时间 */
	@TableField(value = "apply_time")
	private Date applyTime;

	/** 关联出证机构标识 */
	@TableField(value = "solve_unit_id")
	private String solveUnitId;

	/** 出证价格 */
	private String price;

	/** 出证状态：1申请，2受理，3处理中，4完成，5邮寄 */
	private String status;

	/** 创建时间 */
	@TableField(value = "create_time")
	private Date createTime;

	/** 修改时间 */
	@TableField(value = "update_time")
	private Date updateTime;
}
