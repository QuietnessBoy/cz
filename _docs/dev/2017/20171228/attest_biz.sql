/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : attest_biz

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-12-28 09:44:08
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `attest`
-- ----------------------------
DROP TABLE IF EXISTS `attest`;
CREATE TABLE `attest` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '索引',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `version` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]存证记录版本。格式：1-2^16递增版本号. ',
  `ancestors_ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '原始存证号.首次存证的订单号',
  `parent_ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '上一次存证号。续期时有效',
  `ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]订单号,全平台唯一标识.',
  `channel_id` varchar(3) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]渠道来源',
  `channel_userid` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '交易标识。渠道用户标识。',
  `channel_ordersn` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '交易标识。渠道存证订单唯一标识',
  `biz_sign` varchar(88) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]渠道对订单签名',
  `provinder_id` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]服务商',
  `chained` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]入链情况。1入链，0不入链\n暂时没用',
  `state_time` timestamp(6) NULL DEFAULT NULL COMMENT '订单状态更新时间',
  `state` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '业务订单状态.0.处理中 1.存证处理完成 2.上链成功',
  `wallet_addr` varchar(122) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]钱包网络地址。表示终端用户方式',
  `public_key` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]用户钱包公钥。',
  `attest_sign` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]客户签名。客户使用本人的钱包中私钥，对该该存证记录的字段进行计算',
  `attest_type` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]存证类型标识。1文件存证，2hash存证',
  `biz_type` varchar(2) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]业务类型。数据文件、视屏、音频、压缩等方式',
  `file_name` varchar(30) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]源文件名',
  `file_type` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]文件类型',
  `file_size` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]源文件长度',
  `file_sign` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]文件签名。使用本人钱包私钥对该数据进行计算',
  `file_hash` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]文件哈希值。sha256',
  `encrypted` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '加密情况。1加密，0不加密',
  `encrypt_alog` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '加密算法',
  `encrypt_key` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '加密秘钥',
  `owner_type` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]权属人类型。1自然人，2法人，0其他',
  `owner_id` varchar(18) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]权属人身份标识。身份证号、社会信用代码',
  `owner_name` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]权属人名称',
  `agent_name` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '代理人',
  `agent_phone` varchar(13) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '代理人电话',
  `agent_email` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '代理人邮箱',
  `origin_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '[上链数据]存证起始时间',
  `start_time` timestamp(6) NULL DEFAULT NULL COMMENT '[上链数据]当前存证起始时间',
  `expired_time` timestamp NULL DEFAULT NULL COMMENT '过期时间',
  `duration` varchar(5) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]当前存证持续时间(天),从start_time开始算.',
  `description` varchar(500) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]存证描述信息',
  `price` varchar(8) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]本次存证订单费用.',
  `request_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '请求标识，请求发起时间。格式：20170728172911',
  `platform_sign` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]存证平台对订单的签名',
  `chained_content` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_attest_id` (`id`) USING BTREE,
  UNIQUE KEY `idx_attest_channel_ordersn` (`channel_ordersn`) USING HASH,
  UNIQUE KEY `idx_attest_parent_ordersn` (`parent_ordersn`) USING BTREE,
  UNIQUE KEY `idx_attest_ordersn` (`ordersn`) USING BTREE,
  KEY `idx_attest_owner_id_and_file_hash` (`file_hash`,`owner_id`),
  KEY `idx_attest_ancestors_ordersn` (`ancestors_ordersn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='存证信息表（完备业务表）：存储存证的详细信息';

-- ----------------------------
-- Records of attest
-- ----------------------------

-- ----------------------------
-- Table structure for `attest_chained`
-- ----------------------------
DROP TABLE IF EXISTS `attest_chained`;
CREATE TABLE `attest_chained` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '上链订单Id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '订单创建时间',
  `ordersn` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '知金链存证平台存证号',
  `txid` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '上链请求标识',
  `send_time` timestamp(6) NULL DEFAULT NULL,
  `refresh_time` timestamp NULL DEFAULT NULL COMMENT '订单更新时间',
  `state` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '处理结果',
  `num` int(5) DEFAULT NULL COMMENT '异常次数',
  `remark` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `channel_ordersn` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_attest_chained_id` (`id`),
  UNIQUE KEY `idx_attest_chained_ordersn` (`ordersn`),
  UNIQUE KEY `idx_attest_chained_txid` (`txid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of attest_chained
-- ----------------------------

-- ----------------------------
-- Table structure for `attest_file`
-- ----------------------------
DROP TABLE IF EXISTS `attest_file`;
CREATE TABLE `attest_file` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '订单创建时间',
  `ordersn` varchar(21) COLLATE utf8_unicode_ci NOT NULL COMMENT '存证平台存证号',
  `file_name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '源文件名',
  `bucket_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '存储空间名，目前是osstestoss',
  `file_addr` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'OSS存储文件ID',
  `file_size` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '源文件长度',
  `file_hash` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '源文件hash',
  `file_sign` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文件签名。使用本人钱包私钥对该数据进行计算',
  `encrypted` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否加密？1加密，0不加密',
  `encrypt_alog` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '加密算法',
  `encrypt_key` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '加密秘钥',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_attest_file_ordersn` (`ordersn`) USING BTREE,
  UNIQUE KEY `idx_attest_file_id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='文件地址表：关联存证记录与区块文件，存证源文件    attest_biz\r\n';

-- ----------------------------
-- Records of attest_file
-- ----------------------------

-- ----------------------------
-- Table structure for `attest_history`
-- ----------------------------
DROP TABLE IF EXISTS `attest_history`;
CREATE TABLE `attest_history` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '索引',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '订单创建时间',
  `version` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]存证记录版本。格式：1-2^16递增版本号. ',
  `ancestors_ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '原始存证号.首次存证的订单号',
  `parent_ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '上一次存证号。续期时有效',
  `ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]订单号,全平台唯一标识.',
  `channel_id` varchar(3) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]渠道来源',
  `channel_userid` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '交易标识。渠道用户标识。',
  `channel_ordersn` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '交易标识。渠道存证订单唯一标识',
  `biz_sign` varchar(88) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]渠道对订单签名',
  `provinder_id` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]服务商',
  `chained` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]入链情况。1入链，0不入链\n暂时没用',
  `state_time` timestamp(6) NULL DEFAULT NULL COMMENT '订单状态更新时间',
  `state` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '业务订单状态.0.处理中 1.存证处理完成 2.上链成功',
  `wallet_addr` varchar(122) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]钱包网络地址。表示终端用户方式',
  `public_key` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]用户钱包公钥。',
  `attest_sign` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]客户签名。客户使用本人的钱包中私钥，对该该存证记录的字段进行计算',
  `attest_type` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]存证类型标识。1文件存证，2hash存证',
  `biz_type` varchar(2) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]业务类型。数据文件、视屏、音频、压缩等方式',
  `file_name` varchar(30) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]源文件名',
  `file_type` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]文件类型',
  `file_size` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]源文件长度',
  `file_sign` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]文件签名。使用本人钱包私钥对该数据进行计算',
  `file_hash` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]文件哈希值。sha256',
  `encrypted` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '加密情况。1加密，0不加密',
  `encrypt_alog` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '加密算法',
  `encrypt_key` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '加密秘钥',
  `owner_type` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]权属人类型。1自然人，2法人，0其他',
  `owner_id` varchar(18) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]权属人身份标识。身份证号、社会信用代码',
  `owner_name` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]权属人名称',
  `agent_name` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '代理人',
  `agent_phone` varchar(13) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '代理人电话',
  `agent_email` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '代理人邮箱',
  `origin_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '[上链数据]存证起始时间',
  `start_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '[上链数据]当前存证起始时间',
  `expired_time` timestamp NULL DEFAULT NULL COMMENT '过期时间',
  `duration` varchar(5) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]当前存证持续时间(天),从start_time开始算.',
  `description` varchar(500) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]存证描述信息',
  `price` varchar(8) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]本次存证订单费用.',
  `request_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '请求标识，请求发起时间。格式：20170728172911',
  `platform_sign` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]存证平台对订单的签名',
  `chained_content` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_attest_id` (`id`) USING BTREE,
  UNIQUE KEY `idx_attest_channel_ordersn` (`channel_ordersn`) USING HASH,
  UNIQUE KEY `idx_attest_parent_ordersn` (`parent_ordersn`) USING BTREE,
  UNIQUE KEY `idx_attest_ordersn` (`ordersn`) USING BTREE,
  KEY `idx_attest_owner_id_and_file_hash` (`file_hash`,`owner_id`),
  KEY `idx_attest_ancestors_ordersn` (`ancestors_ordersn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='存证信息表（完备业务表）：存储存证的详细信息';

-- ----------------------------
-- Records of attest_history
-- ----------------------------

-- ----------------------------
-- Table structure for `channel`
-- ----------------------------
DROP TABLE IF EXISTS `channel`;
CREATE TABLE `channel` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `update_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
  `create_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '记录创建时间',
  `channel_id` varchar(3) COLLATE utf8_unicode_ci NOT NULL COMMENT '渠道唯一标识：平台生成',
  `channel_public_key` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT '渠道公钥,Hex编码格式',
  `channel_private_key` varchar(66) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '渠道私钥. 一般没有的',
  `channel_ida` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '渠道唯一请求标识',
  `platform_publick_key` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT '平台对应此channel的公钥,Hex编码格式',
  `platform_private_key` varchar(66) COLLATE utf8_unicode_ci NOT NULL COMMENT '平台对应此channel的私钥',
  `platform_ida` varchar(30) COLLATE utf8_unicode_ci NOT NULL COMMENT '存证平台请求标识',
  `access_key` varchar(52) COLLATE utf8_unicode_ci NOT NULL COMMENT '渠道的访问标识',
  `name` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '渠道名称',
  `channel_type` varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '企业类型：1企业、2政府机关、3事业单位、4社会团体、5非盈利性组织、0其他',
  `biz_duration` varchar(3) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '营业年限：3/5/10/20',
  `scale` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '企业规模：100/300/500人',
  `biz` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '主营业务',
  `url` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '网址',
  `address` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '地址',
  `contact` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '联系人',
  `phone` varchar(13) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '联系电话',
  `verified` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否通过实名认证？1通过，0未通过',
  `license_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '营业执照全称',
  `license_num` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '社会统一信用代码',
  `account_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '对公账户名',
  `account_num` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '对公账户账号',
  `freezed` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '是否冻结？1冻结，0未冻结',
  PRIMARY KEY (`id`),
  KEY `idx_channel_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='渠道信息表：合作第三方组织或平台的描述。核心业务数据，注意考虑存证记录中的渠道代码不可更改性。渠道也可以视为组织。';

-- ----------------------------
-- Records of channel
-- ----------------------------
INSERT INTO `channel` VALUES ('1', '2017-12-12 15:38:42.132158', '2017-10-30 20:31:24.000000', '003', '049b13e268608d33986503331f56ede591b58fb5247fd987ae2b462d84f00f04d60a0cc094e91a18d07343365dbed98fd63735d97b237879a69bd3e80626265995', '00bc62c4f21d8c28284598e76ce75c60961279090d5350dc4e97fb49e650a5e9f1', 'super_channel_01', '0491fe9b14ee70e6994ca8f5bb72400aa7663a4451c5051e68d4d61de86e0020f6af1abb3b9a9181e08c92f8c386af55f10880a5e707ae53751958a3da94233eac', '00e18306c0bc1754acee9cb1c898086f8a859c3022e8d5a0f16f1657ab903c4657', 'zhijinlian_blockchain_cz2017', '6HpkbcRXFuhVSvCG7ipar5gEuSzNXtb9iccK1cYbfuDNknX8j', '杭州拾贝知识产权服务有限公司', null, null, null, null, null, null, null, null, null, null, null, null, null, '0');
INSERT INTO `channel` VALUES ('11', '2017-12-12 15:39:42.586597', '2017-10-04 15:14:11.000000', '004', '04f718134ce035d6d90cd3c3cd07a9d45d2adbfa38cb3037b9927ee892444c0d32597dff236c1b5ae32daee52211968d97d8be41dba01ef4de2b8a91788209fff5', '00f869d69d1a4d179879e290565b0d2479dcea28924474c1dac5e7a4a9905d150b', 'super_channel_01', '044c38e6a7b4421ebf15af726ec4ec6f70acc25e2df2a0e061103006e78618f5409e727376bd2bb2284e27d82babf42bc3c88267149fd1e5c703b6fc21f180979b', '3d5c1c122bf39ec20b5282ea82c83ed02b8fbdf37ad8d6b414d014c33f7e73e7', 'zhijinlian_blockchain_cz2017', '2FtizTUMvUv2XBegYUQ3bs7WS9y7qWjHFv1dgtTNt3amQXfdhh', '知金链网站', null, null, null, null, null, null, null, null, null, null, null, null, null, '0');
INSERT INTO `channel` VALUES ('14', '2017-12-12 15:37:36.062380', '2017-10-30 20:34:38.000000', '005', '04f244fc01a3c1f5e0a7c1fc45ae7d7a19fd6cee70dbbabbf5f49fb17211c278459dd9e97d5d466a283b81c477b709cd763968dce5bc0b81bed234d8cab226ab45', '2e6c7975dcac8040768550a36f347648f2cb029cc8b0aba41461d3af1e029707', 'super_channel_01', '04329b15fef0019bfefb95747dbe5e74fa0b9114dfe084baa68fe4b137206872757405a64150490e33158f91ae6ba76553b4f11d5b5b1520c8038f2b779fd79548', '51bbc1819e5466b491f077aabab3ca68ecbc679d00fd319a80474c5bb44ec7fc', 'zhijinlian_blockchain_cz2017', 'GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd', 'mr.wang', '1', null, null, null, null, null, null, null, null, null, null, null, null, '0');

-- ----------------------------
-- Table structure for `config`
-- ----------------------------
DROP TABLE IF EXISTS `config`;
CREATE TABLE `config` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `config_type` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '渠道ID',
  `config_key` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '键',
  `config_value` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '值',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `remark` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of config
-- ----------------------------
INSERT INTO `config` VALUES ('2', '005', 'hash_cz_ok_url', 'http://127.0.0.1:8083/channelTest/hashCzOk', '2017-12-27 19:59:56', '本地测试：文件Hash存证通知回调地址');
INSERT INTO `config` VALUES ('3', '005', 'xq_ok_url', 'http://127.0.0.1:8083/channelTest/CzXqOk', '2017-12-27 19:59:58', '本地测试：文件存证续期通知回调地址');
INSERT INTO `config` VALUES ('4', 'zjl', 'ordersn_url', 'http://127.0.0.1:8084/ordersn/createOrdersn', '2017-12-27 20:00:18', '知金链平台：请求订单号地址');
INSERT INTO `config` VALUES ('5', '003', 'cz_ok_url', 'http://cz.ipsebe.com/sebe/requestReceiveFinishCallback.do', '2017-12-27 20:00:46', '杭州拾贝：文件存证通知回调地址');
INSERT INTO `config` VALUES ('6', '003', 'hash_cz_ok_url', 'http://cz.ipsebe.com/sebe/saveHashFileCallback.do', '2017-12-27 20:00:48', '杭州拾贝：文件Hash存证通知回调地址');
INSERT INTO `config` VALUES ('7', '003', 'xq_ok_url', 'http://cz.ipsebe.com/sebe/extensionCallback.do', '2017-12-27 20:00:51', '杭州拾贝：文件存证续期通知回调地址');
INSERT INTO `config` VALUES ('8', '004', 'cz_ok_url', 'http://47.94.229.222/EvidenceModule/Evidence/dealEvidenceEnd', '2017-12-27 20:01:00', '存证网站：文件存证通知回调地址');
INSERT INTO `config` VALUES ('9', '004', 'hash_cz_ok_url', 'http://47.94.229.222/EvidenceModule/Evidence/dealHashEvidence', '2017-12-27 20:01:01', '存证网站：文件Hash存证通知回调地址');
INSERT INTO `config` VALUES ('10', '004', 'xq_ok_url', 'http://47.94.229.222/EvidenceModule/Evidence/dealRenewalEvidence', '2017-12-27 20:01:03', '存证网站：存证续期通知回调地址');
INSERT INTO `config` VALUES ('11', '005', 'cz_ok_url', 'http://127.0.0.1:8083/channelTest/fileCzOk', '2017-12-27 20:00:03', '本地测试：文件存证完成回调地址');
INSERT INTO `config` VALUES ('13', 'zjl', 'query_cz_chained_url', 'http://172.17.94.222:3000/queryCzByOrdersn', '2017-12-27 20:00:23', '知金链平台：查询存证上链接口');
INSERT INTO `config` VALUES ('14', 'zjl', 'add_cz_chained_url', 'http://172.17.94.222:3000/addCz', '2017-12-27 20:00:28', '知金链平台：增加存证上链接口');
INSERT INTO `config` VALUES ('15', 'store_location', 'beijingOSS1', '放置一个json串{域名，accesskey键值对，oss存储空间}', '2017-11-16 20:07:27', '说明');

-- ----------------------------
-- Table structure for `dictionary`
-- ----------------------------
DROP TABLE IF EXISTS `dictionary`;
CREATE TABLE `dictionary` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `version` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '版本号',
  `type` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '字典类型',
  `code` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '字典代码',
  `text` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '展现文本：社会统一信用代码',
  `parent_code` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '父字典代码',
  `remark` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '备注说明：6位行政区划代码',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idex_dictionary_id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of dictionary
-- ----------------------------

-- ----------------------------
-- Table structure for `fileczfailed_record`
-- ----------------------------
DROP TABLE IF EXISTS `fileczfailed_record`;
CREATE TABLE `fileczfailed_record` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '错误状态订单Id',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `ordersn` varchar(30) COLLATE utf8_unicode_ci NOT NULL,
  `state` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '状态码',
  `msg` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '状态信息',
  `channel_ordersn` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '渠道唯一标识',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_fileczfailed_record_id` (`id`) USING BTREE,
  UNIQUE KEY `idx_fileczfailed_record_ordersn` (`ordersn`(20)) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='文件存证处理失败回调状态码';

-- ----------------------------
-- Records of fileczfailed_record
-- ----------------------------

-- ----------------------------
-- Table structure for `prove`
-- ----------------------------
DROP TABLE IF EXISTS `prove`;
CREATE TABLE `prove` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `prove_id` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '出证订单唯一标识',
  `reason` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '出证理由',
  `apply_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '用户申请时间',
  `solve_unit_id` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '关联出证机构标识',
  `price` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '出证价格',
  `status` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '出证状态：1申请，2受理，3处理中，4完成，5邮寄',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='出证订单表：描述出证订单信息，与存证实体表关联。';

-- ----------------------------
-- Records of prove
-- ----------------------------

-- ----------------------------
-- Table structure for `sequence`
-- ----------------------------
DROP TABLE IF EXISTS `sequence`;
CREATE TABLE `sequence` (
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `current_value` int(11) NOT NULL,
  `increment` int(11) NOT NULL DEFAULT '1',
  `current_day` date NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of sequence
-- ----------------------------
INSERT INTO `sequence` VALUES ('ordersn', '0', '1', '2017-12-28');

-- ----------------------------
-- Table structure for `status`
-- ----------------------------
DROP TABLE IF EXISTS `status`;
CREATE TABLE `status` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '订单号',
  `channel_ordersn` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '渠道唯一标识',
  `state` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '订单当前状态',
  `state2` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '订单回调状态',
  `num` int(3) DEFAULT NULL COMMENT '异常次数记录',
  `remark` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_status_id` (`id`),
  UNIQUE KEY `idx_status_ordersn` (`ordersn`),
  KEY `idx_status_state` (`state`),
  KEY `idx_status_num` (`num`) USING BTREE,
  KEY `idx_status_channelOrdersn` (`channel_ordersn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='记录订单的状态。内部业务核查。';

-- ----------------------------
-- Records of status
-- ----------------------------

-- ----------------------------
-- Table structure for `temp_order`
-- ----------------------------
DROP TABLE IF EXISTS `temp_order`;
CREATE TABLE `temp_order` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '索引',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '订单创建时间',
  `version` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]存证记录版本。格式：1-2^16递增版本号. ',
  `ancestors_ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '原始存证号.首次存证的订单号',
  `parent_ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '上一次存证号。续期时有效',
  `ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]订单号,全平台唯一标识.',
  `channel_id` varchar(3) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]渠道来源',
  `channel_userid` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '交易标识。渠道用户标识。',
  `channel_ordersn` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '交易标识。渠道存证订单唯一标识',
  `biz_sign` varchar(88) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]渠道对订单签名',
  `provinder_id` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]服务商',
  `chained` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]入链情况。1入链，0不入链',
  `state_time` timestamp(6) NULL DEFAULT NULL COMMENT '订单状态更新时间',
  `state` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '业务订单状态.1.处理中 2.存证处理完成 3.上链成功',
  `wallet_addr` varchar(122) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]钱包网络地址。表示终端用户方式',
  `public_key` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]用户钱包公钥。',
  `attest_sign` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]客户签名。客户使用本人的钱包中私钥，对该该存证记录的字段进行计算',
  `attest_type` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]存证类型标识。1文件存证，2hash存证',
  `biz_type` varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]业务类型。数据文件、视屏、音频、压缩等方式',
  `file_name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]源文件名',
  `file_type` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]文件类型',
  `file_size` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]源文件长度',
  `file_sign` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]文件签名。使用本人钱包私钥对该数据进行计算',
  `file_hash` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]文件哈希值。sha256',
  `file_addr` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `encrypted` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '加密情况。1加密，0不加密',
  `encrypt_alog` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '加密算法',
  `encrypt_key` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '加密秘钥',
  `owner_type` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]权属人类型。1自然人，2法人，0其他',
  `owner_id` varchar(18) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]权属人身份标识。身份证号、社会信用代码',
  `owner_name` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]权属人名称',
  `agent_name` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '代理人',
  `agent_phone` varchar(13) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '代理人电话',
  `agent_email` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '代理人邮箱',
  `origin_time` timestamp(6) NULL DEFAULT NULL COMMENT '[上链数据]存证起始时间',
  `start_time` timestamp(6) NULL DEFAULT NULL COMMENT '[上链数据]当前存证起始时间',
  `duration` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]当前存证持续时间(天),从start_time开始算.',
  `description` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]存证描述信息',
  `price` varchar(8) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]本次存证订单费用.',
  `request_time` timestamp(6) NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '请求标识，请求发起时间。格式：20170728172911',
  `platform_sign` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]存证平台对订单的签名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_temp_order_id` (`id`) USING BTREE,
  UNIQUE KEY `idx_temp_order_channelOrdersn` (`channel_ordersn`) USING BTREE,
  KEY `idx_temp_order_ordersn` (`ordersn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='存证信息表（完备业务表）：存储存证的详细信息';

-- ----------------------------
-- Records of temp_order
-- ----------------------------

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  `wallet_addr` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '钱包地址',
  `public_key` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT '用户公钥',
  `encrypted_prikey` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '加密后的私钥',
  `encrypt` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '加密说明',
  `channel_id` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '渠道标识',
  `channel_userid` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '渠道用户标识',
  `user_type` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户类型：1法人，2自然人',
  `user_name` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '姓名、法人名称',
  `user_id` varchar(18) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '身份标识：1身份证号、2社会统一信用代码',
  `verified` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '是否通过实名认证？1通过，0未通过',
  `verify_time` timestamp(6) NULL DEFAULT NULL COMMENT '通过认证时间',
  `freezed` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '是否冻结？1冻结，0未冻结',
  `reamrk` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_id` (`id`),
  UNIQUE KEY `idx_user_channeluserid_channelid` (`channel_userid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='平台用户表';

-- ----------------------------
-- Records of user
-- ----------------------------

-- ----------------------------
-- Table structure for `warning`
-- ----------------------------
DROP TABLE IF EXISTS `warning`;
CREATE TABLE `warning` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `biz_type` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ordersn` varchar(22) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '存证号',
  `channel_ordersn` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '渠道号',
  `num` int(5) NOT NULL COMMENT '订单异常次数记录',
  `remark` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_warning_id` (`id`),
  KEY `idx_warning_warn_num` (`num`),
  KEY `idx_warning_ordersn` (`ordersn`) USING BTREE,
  KEY `idx_warning_channelOrdersn` (`channel_ordersn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of warning
-- ----------------------------

-- ----------------------------
-- Procedure structure for `test_insert`
-- ----------------------------
DROP PROCEDURE IF EXISTS `test_insert`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `test_insert`()
BEGIN 
DECLARE y TINYINT DEFAULT 1;
WHILE y<20
DO
insert into student(name,age) values('aaa',y); 
SET y=y+1; 
END WHILE ; 
commit; 
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `again`
-- ----------------------------
DROP FUNCTION IF EXISTS `again`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `again`() RETURNS int(11)
    DETERMINISTIC
BEGIN
	update sequence t set t.current_day = date(NOW()) ,  t.current_value = 0, t.increment=1  where t.name='ordersn';
	RETURN 1;
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `currval`
-- ----------------------------
DROP FUNCTION IF EXISTS `currval`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `currval`() RETURNS int(11)
    DETERMINISTIC
BEGIN
	DECLARE value INTEGER; 
     SET value = 0; 
     SELECT current_value INTO value 
          FROM sequence
          WHERE name = 'ordersn'; 
     RETURN value; 
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `is_today`
-- ----------------------------
DROP FUNCTION IF EXISTS `is_today`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `is_today`() RETURNS tinyint(4)
    DETERMINISTIC
BEGIN
	#Routine body goes here...
	DECLARE rs INTEGER; 
	select sequence.current_day = DATE(NOW()) into rs from sequence limit 1;
	RETURN(rs);
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `nextval`
-- ----------------------------
DROP FUNCTION IF EXISTS `nextval`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `nextval`() RETURNS int(11)
    DETERMINISTIC
BEGIN 
     UPDATE sequence 
          SET current_value = current_value + increment 
          WHERE name = 'ordersn'; 
     RETURN currval(); 
END
;;
DELIMITER ;

-- ----------------------------
-- Function structure for `setval`
-- ----------------------------
DROP FUNCTION IF EXISTS `setval`;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `setval`() RETURNS int(11)
    DETERMINISTIC
BEGIN 
     UPDATE sequence 
          SET current_value = value 
          WHERE name = seq_name; 
     RETURN currval(seq_name); 
END
;;
DELIMITER ;
