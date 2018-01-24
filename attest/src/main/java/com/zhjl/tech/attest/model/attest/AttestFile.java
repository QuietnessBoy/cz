package com.zhjl.tech.attest.model.attest;

import com.baomidou.mybatisplus.annotations.IdType;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * 文件地址表：关联存证记录与区块文件，存证源文件    attest_biz

 *
 */
@Data
@TableName("attest_file")
public class AttestFile implements Serializable {

	@TableField(exist = false)
	private static final long serialVersionUID = 1L;

	/**  */
	@TableId(type = IdType.AUTO)
	private Integer id;

	/** 更新时间  not null*/
	@TableField(value = "update_time")
	private Date updateTime;

	/** 订单创建时间 not null*/
	@TableField(value = "create_time")
	private Date createTime;

	/** 存证平台存证号 not null*/
	private String ordersn;

	/** 源文件名 not null*/
	@TableField(value = "file_name")
	private String fileName;

	/** 源文件存储地址 not null*/
	@TableField(value = "file_addr")
	private String fileAddr;

	/** 源文件长度 not null*/
	@TableField(value = "file_size")
	private String fileSize;

	/** 源文件hash  not null*/
	@TableField(value = "file_hash")
	private String fileHash;

	/** 文件签名。使用本人钱包私钥对该数据进行计算  not null*/
	@TableField(value = "file_sign")
	private String fileSign;

	/** 是否加密？1加密，0不加密  not null*/
	private String encrypted;

	/** 加密算法 */
	@TableField(value = "encrypt_alog")
	private String encryptAlog;

	/** 加密秘钥 */
	@TableField(value = "encrypt_key")
	private String encryptKey;
}
