package com.zhjl.tech.common.dto.interfaces;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * 用于根据订单号查询接口返回对象
 *
 */
@Data
public class QueryCallBackData implements Serializable {

	/*##################用户存证信息######################*/

	/** 交易标识。渠道存证订单唯一标识 */
	private String channelOrdersn;

	/** [上链数据]入链情况。1入链，0不入链 */
	private String chained;

	/** [上链数据]业务类型。数据文件、视屏、音频、压缩等方式 */
	private String bizType;

	//订单签名
	private String bizSign;

	/** [上链数据]源文件名 */
	private String fileName;

	/** [上链数据]文件类型 */
	private String fileType;

	/** [上链数据]源文件长度 */
	private String fileSize;

	/** [上链数据]文件哈希值。sha256 */
	private String fileHash;

	/** [上链数据]存证人类型。1自然人，2法人，0其他 */
	private String ownerType;

	/** [上链数据]存证人身份标识。身份证号、社会信用代码 */
	private String ownerId;

	/** [上链数据]存证人名称 */
	private String ownerName;

	/** 代理人 */
	private String agentName;

	/** 代理人电话 */
	private String agentPhone;

	/** 代理人邮箱 */
	private String agentEmail;

	/** 开始时间 */
	private String startTime;

	/** [上链数据]当前存证持续时间(天),从start_time开始算. */
	private String duration;

	/** [上链数据]存证描述信息 */
	private String description;

	/** [上链数据]本次存证订单费用. */
	private String price;

}
