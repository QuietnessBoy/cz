SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS  `attest`;
CREATE TABLE `attest` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '索引',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '订单创建时间',
  `version` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]存证记录版本。格式：1-2^16递增版本号. ',
  `ancestors_ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '原始存证号.首次存证的订单号',
  `parent_ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '上一次存证号。续期时有效',
  `ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]订单号,全平台唯一标识.',
  `channel_id` varchar(3) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]渠道来源',
  `channel_userid` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '交易标识。渠道用户标识。',
  `channel_ordersn` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '交易标识。渠道存证订单唯一标识',
  `biz_sign` varchar(88) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]渠道对订单签名',
  `provinder_id` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]服务商',
  `chained` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]入链情况。1入链，0不入链',
  `state_time` timestamp(6) NULL DEFAULT NULL COMMENT '订单状态更新时间',
  `state` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '业务订单状态.1.处理中 2.存证处理完成 3.上链成功',
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

DROP TABLE IF EXISTS  `attest_chained`;
CREATE TABLE `attest_chained` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '上链订单Id',
  `ordersn` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '知金链存证平台存证号',
  `txid` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '上链请求标识',
  `send_time` timestamp(6) NULL DEFAULT NULL,
  `refresh_time` timestamp NULL DEFAULT NULL COMMENT '订单更新时间',
  `create_time` timestamp(6) NULL DEFAULT NULL COMMENT '订单创建时间',
  `state` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '处理结果',
  `num` int(5) DEFAULT NULL COMMENT '异常次数',
  `remark` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `channel_ordersn` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_attest_chained_id` (`id`),
  UNIQUE KEY `idx_attest_chained_ordersn` (`ordersn`),
  UNIQUE KEY `idx_attest_chained_txid` (`txid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS  `attest_file`;
CREATE TABLE `attest_file` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `update_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
  `create_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '订单创建时间',
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

DROP TABLE IF EXISTS  `attest_history`;
CREATE TABLE `attest_history` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '索引',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '订单创建时间',
  `version` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]存证记录版本。格式：1-2^16递增版本号. ',
  `ancestors_ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '原始存证号.首次存证的订单号',
  `parent_ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '上一次存证号。续期时有效',
  `ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]订单号,全平台唯一标识.',
  `channel_id` varchar(3) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]渠道来源',
  `channel_userid` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '交易标识。渠道用户标识。',
  `channel_ordersn` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '交易标识。渠道存证订单唯一标识',
  `biz_sign` varchar(88) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]渠道对订单签名',
  `provinder_id` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]服务商',
  `chained` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]入链情况。1入链，0不入链',
  `expired_time` timestamp NULL DEFAULT NULL COMMENT '过期时间',
  `state_time` timestamp(6) NULL DEFAULT NULL COMMENT '订单状态更新时间',
  `state` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '业务订单状态.1.处理中 2.存证处理完成 3.上链成功',
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
  `duration` varchar(5) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]当前存证持续时间(天),从start_time开始算.',
  `description` varchar(500) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]存证描述信息',
  `price` varchar(8) COLLATE utf8_unicode_ci NOT NULL COMMENT '[上链数据]本次存证订单费用.',
  `request_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '请求标识，请求发起时间。格式：20170728172911',
  `platform_sign` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[上链数据]存证平台对订单的签名',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_attest_history_id` (`id`) USING BTREE,
  UNIQUE KEY `idx_attest_history_originalOrdersn` (`parent_ordersn`) USING BTREE,
  UNIQUE KEY `idx_attetst_history_ordersn` (`ordersn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='存证信息表（完备业务表）：存储存证的详细信息';

DROP TABLE IF EXISTS  `channel`;
CREATE TABLE `channel` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `update_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
  `create_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '记录创建时间',
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
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='渠道信息表：合作第三方组织或平台的描述。核心业务数据，注意考虑存证记录中的渠道代码不可更改性。渠道也可以视为组织。';

insert into `channel`(`id`,`update_time`,`create_time`,`channel_id`,`channel_public_key`,`channel_private_key`,`channel_ida`,`platform_publick_key`,`platform_private_key`,`platform_ida`,`access_key`,`name`,`channel_type`,`biz_duration`,`scale`,`biz`,`url`,`address`,`contact`,`phone`,`verified`,`license_name`,`license_num`,`account_name`,`account_num`,`freezed`) values
('1','2017-12-01 17:07:56','2017-10-30 20:31:24','003','04a817f0a15522b80c2974995f20f0686efad4f62e7fa77248a0aa54016fd56e02bee20bfea2616eaff6a1f2eb5d89444704ba450036530fe664df0bf8e88f8319','753912b20a9a24d9954f9f92f8c90278b3a672ae20b0364b8e2071a4d94473b2','super_channel_01','04152e7d1c170d21e52a2d0620f4f7449be23a510208a8ca09ed3c675f0d26a6929ffba6bece9be9dde20ce3fcfeb1d16d8b665d63dc012e89fffd1981c39e5bdc','169e948d0487de90a03039ae242269efb47ddb0a0fcb2cc9ae8650f7d0f3fdf1','zhijinlian_blockchain_cz2017','2tBboQXaG8L4Msw59ZRm7pbq3xWxrbcjqA7FkNrnxsVRohJS6o','杭州拾贝知识产权服务有限公司',null,null,null,null,null,null,null,null,null,null,null,null,null,'0'),
('11','2017-10-23 11:35:19','2017-10-04 15:14:11','004','048dbbc57d90f4f5ff4aa5a16d05067531cb3a9137d92a262550bfb17bd1f293d9cc9713d58a226df329855a58e0553a858c1da56fc0b61e8f4386083db34d737a','76e046ff78a0ba45fed32ebf0c7b61eadfa2dc503fb5fe94f60edfec0a46a0c0','super_channel_01','0492e4c7d9ccc08e72ed13b47c94a6da71777052d1c30cdef19ea444a0bf0b90b61fd9f62ce6807f8676ff006e98601876d18508816601d1ea0cfcea7f6dbb6acf','09d36db177fe8d427365f0f66f7f7bf370b9c1c395b8c73155b83c356284df51','zhijinlian_blockchain_cz2017','ZvSmW2K36upCN4tJ9wL7N3xaEde8JYMbJotWdr7daUiaNhZRk','知金链网站',null,null,null,null,null,null,null,null,null,null,null,null,null,'0'),
('14','2017-10-30 20:36:32','2017-10-30 20:34:38','005','04ea3d76b9afdcce27ec9b03224f4080fddaabbd95b3d93b5946b5ca5d2a1b6c5a6b6d8bf6dcba2f2920dccb269e7ea0df825a52e2292593dac3051540b917265a','00856b9174ab913cf3aac0dec107650c31cb3875d224ffe0648f6158544b7e56ed','super_channel_01','046413e68d62e39de4896e0cc6595571b128e14aa9be49fc4f52fce415616be307a1e20bd5699148af4ec6217c73c8d71f653c62a0172765e3b54a06aee2c3d76c','008498ec66fd304faa295a7de72895ff64b2a442c50293f570217881e89d6f0e9b','zhijinlian_blockchain_cz2017','2kDv8FXEzb69pk261PXPQ65KezG2fzbbHX7SrUEYkkabk74yHa','mr.wang','1',null,null,null,null,null,null,null,null,null,null,null,null,'0'),
('15','2017-10-30 20:36:35','2017-10-30 20:36:37','006','042badc24548ed79733090594d555a106cd1d5e88ea7c55866d0bcfbc794ec779645d0ecf36152bac0cadeefd27e4f38c99b29892f90193cd5ed6446cde02499ee','50334716ab411a5c155aaf584c552983b490fb28a1b84850981918ec7fb9ed90','super_channel_01','045dfe4291145d852427ae72cd2e6a3ead1d926e5406807e5d038691d2e92e3cc9223ef86c11e9a736166f59ca1120f650caa7b1eaa0df98fc3a73ff28843d7256','7786aa8be66582cc9c4a9f98cda5a8c4b6a5174f8a7dca3b0905908ee5c99929','zhijinlian_blockchain_cz2017','uMFf6mFp2CqYckJVZPaU8uZwz3RgorNst1CfMunexXzNaCvHN','mr.huang','1',null,null,null,null,null,null,null,null,null,null,null,null,'0');
DROP TABLE IF EXISTS  `config`;
CREATE TABLE `config` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `config_type` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '渠道ID',
  `config_key` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '键',
  `config_value` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '值',
  `create_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '创建时间',
  `remark` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

insert into `config`(`id`,`config_type`,`config_key`,`config_value`,`create_time`,`remark`) values
('2','005','hash_cz_ok_url','http://cz.zhijl.com/channelTool/hashCzOk','2017-11-23 20:20:11','文件Hash存证通知回调地址'),
('3','005','xq_ok_url','http://cz.zhijl.com/channelTool/CzXqOk','2017-11-23 20:20:14','文件存证续期通知回调地址'),
('4','002','ordersn_url','http://172.17.94.204:9020/ordersn/createOrdersn','2017-11-24 10:01:37','请求订单号地址'),
('5','003','cz_ok_url','http://testcz.ipsebe.com/sebe/requestReceiveFinishCallback.do','2017-12-01 17:23:55','文件存证通知回调地址'),
('6','003','hash_cz_ok_url','http://testcz.ipsebe.com/sebe/saveHashFileCallback.do','2017-12-01 17:23:55','文件Hash存证通知回调地址'),
('7','003','xq_ok_url','http://testcz.ipsebe.com/sebe/extensionCallback.do','2017-12-01 17:23:55','文件存证续期通知回调地址'),
('8','004','cz_ok_url','http://47.94.229.222/EvidenceModule/Evidence/dealEvidenceEnd','2017-11-14 10:58:54','文件存证通知回调地址'),
('9','004','hash_cz_ok_url','http://47.94.229.222/EvidenceModule/Evidence/dealHashEvidence','2017-11-14 10:58:56','文件Hash存证通知回调地址'),
('10','004','xq_ok_url','http://47.94.229.222/EvidenceModule/Evidence/dealRenewalEvidence','2017-11-14 10:58:58','存证续期通知回调地址'),
('11','005','cz_ok_url','http://cz.zhijl.com/channelTool/fileCzOk','2017-11-23 20:19:54','文件存证完成回调地址'),
('13','001','query_cz_chained_url','http://172.17.94.211:80/queryCzByOrdersn','2017-11-28 18:15:30','查询存证上链接口'),
('14','001','add_cz_chained_url','http://172.17.94.211:80/addCz','2017-11-28 18:15:30','增加存证上链接口'),
('15','store_location','beijingOSS1','放置一个json串{域名，accesskey键值对，oss存储空间}','2017-11-16 20:07:27','说明'),
('16','006','cz_ok_url','http://cz.zhijl.com/channelTool/fileCzOk','2017-11-28 11:25:42','文件存证完成回调地址'),
('17','006','hash_cz_ok_url','http://cz.zhijl.com/channelTool/hashCzOk','2017-11-28 11:26:43','文件Hash存证通知回调地址'),
('18','006','xq_ok_url','http://cz.zhijl.com/channelTool/CzXqOk','2017-11-28 11:31:40','文件存证续期通知回调地址');
DROP TABLE IF EXISTS  `dictionary`;
CREATE TABLE `dictionary` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `version` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '版本号',
  `type` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '字典类型',
  `code` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '字典代码',
  `text` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '展现文本：社会统一信用代码',
  `parent_code` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '父字典代码',
  `remark` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '备注说明：6位行政区划代码',
  `create_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '创建时间',
  `update_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idex_dictionary_id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS  `prove`;
CREATE TABLE `prove` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `prove_id` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '出证订单唯一标识',
  `reason` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '出证理由',
  `apply_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '用户申请时间',
  `solve_unit_id` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '关联出证机构标识',
  `price` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '出证价格',
  `status` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '出证状态：1申请，2受理，3处理中，4完成，5邮寄',
  `create_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '创建时间',
  `update_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='出证订单表：描述出证订单信息，与存证实体表关联。';

DROP TABLE IF EXISTS  `sequence`;
CREATE TABLE `sequence` (
  `name` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `current_value` int(11) NOT NULL,
  `increment` int(11) NOT NULL DEFAULT '1',
  `current_day` date NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

insert into `sequence`(`name`,`current_value`,`increment`,`current_day`) values
('ordersn','0','1','2017-12-01');
DROP TABLE IF EXISTS  `status`;
CREATE TABLE `status` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000',
  `ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '订单号',
  `channel_ordersn` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '渠道唯一标识',
  `state` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '订单当前状态',
  `num` int(3) DEFAULT NULL COMMENT '异常次数记录',
  `remark` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_status_id` (`id`),
  UNIQUE KEY `idx_status_ordersn` (`ordersn`),
  KEY `idx_status_state` (`state`),
  KEY `idx_status_num` (`num`) USING BTREE,
  KEY `idx_status_channelOrdersn` (`channel_ordersn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='记录订单的状态。内部业务核查。';

DROP TABLE IF EXISTS  `temp_order`;
CREATE TABLE `temp_order` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '索引',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '订单创建时间',
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
  `origin_time` timestamp(6) NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '[上链数据]存证起始时间',
  `start_time` timestamp(6) NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '[上链数据]当前存证起始时间',
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

DROP TABLE IF EXISTS  `user`;
CREATE TABLE `user` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000',
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
  `verify_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '通过认证时间',
  `freezed` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '是否冻结？1冻结，0未冻结',
  `reamrk` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_id` (`id`),
  UNIQUE KEY `idx_user_channeluserid_channelid` (`channel_userid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='平台用户表';

DROP TABLE IF EXISTS  `warning`;
CREATE TABLE `warning` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `biz_type` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ordersn` varchar(22) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '存证号',
  `channel_ordersn` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '渠道号',
  `num` int(5) NOT NULL COMMENT '订单异常次数记录',
  `remark` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_warning_id` (`id`),
  UNIQUE KEY `idx_warning_ordersn` (`ordersn`),
  UNIQUE KEY `idx_warning_channelOrdersn` (`channel_ordersn`) USING BTREE,
  KEY `idx_warning_warn_num` (`num`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

SET FOREIGN_KEY_CHECKS = 1;

/* FUNCTIONS */;
DROP FUNCTION IF EXISTS `again`;
DELIMITER $$
CREATE FUNCTION `again`() RETURNS int(11)
    DETERMINISTIC
BEGIN
	update sequence t set t.current_day = date(NOW()) ,  t.current_value = 0, t.increment=1  where t.name='ordersn';
	RETURN 1;
END
$$
DELIMITER ;

DROP FUNCTION IF EXISTS `currval`;
DELIMITER $$
CREATE FUNCTION `currval`() RETURNS int(11)
    DETERMINISTIC
BEGIN
	DECLARE value INTEGER; 
     SET value = 0; 
     SELECT current_value INTO value 
          FROM sequence
          WHERE name = 'ordersn'; 
     RETURN value; 
END
$$
DELIMITER ;

DROP FUNCTION IF EXISTS `is_today`;
DELIMITER $$
CREATE FUNCTION `is_today`() RETURNS tinyint(4)
    DETERMINISTIC
BEGIN
	#Routine body goes here...
	DECLARE rs INTEGER; 
	select sequence.current_day = DATE(NOW()) into rs from sequence limit 1;
	RETURN(rs);
END
$$
DELIMITER ;

DROP FUNCTION IF EXISTS `nextval`;
DELIMITER $$
CREATE FUNCTION `nextval`() RETURNS int(11)
    DETERMINISTIC
BEGIN 
     UPDATE sequence 
          SET current_value = current_value + increment 
          WHERE name = 'ordersn'; 
     RETURN currval(); 
END
$$
DELIMITER ;

DROP FUNCTION IF EXISTS `setval`;
DELIMITER $$
CREATE FUNCTION `setval`() RETURNS int(11)
    DETERMINISTIC
BEGIN 
     UPDATE sequence 
          SET current_value = value 
          WHERE name = seq_name; 
     RETURN currval(seq_name); 
END
$$
DELIMITER ;

