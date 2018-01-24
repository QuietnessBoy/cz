package com.zhjl.tech.common.dto.interfaces;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 *
 * 存证数字校验接口接收对象
 *
 */
@Data
public class DigitalCertificateDto implements Serializable {

	/** 本次请求参数的签名 MD5*/
	@NotBlank(message="请求签名类型不能为空")
	@Size(max=10, message = "请求签名类型不能超过10字符.")
	private String signType;

	/** 本次请求参数的签名 */
	@NotBlank(message="请求参数签名不能为空")
	@Size(max=24, message = "请求参数签名不能超过24字符.")
	private String sign;

	/** 请求标识，固定值 */
	@NotBlank(message="请求标识不能为空")
	@Size(max=66, message = "请求标识不能超过66字符.")
	private String accessKey;

	//随机数
	@NotBlank(message="随机数不能为空")
	@Size(max=32, message = "请求标识不能超过32字符.")
	private String random;

	/*##################用户存证信息######################*/

	/** [上链数据]渠道来源 */
	@NotBlank(message="渠道id不能为空")
	@Size(max=3, message = "渠道id不能超过20字符.")
	private String channelId;

	/** [上链数据]文件哈希值。sha256 */
	@NotBlank(message="文件哈希值不能为空")
	@Size(max=44, message = "文件哈希值不能超过44字符.")
	private String fileHash;

	/** [上链数据]存证人身份标识。身份证号、社会信用代码 */
	@NotBlank(message="存证人身份标识不能为空")
	@Size(max=20, message = "存证人身份标识不能超过20字符.")

	private String ownerId;
}
