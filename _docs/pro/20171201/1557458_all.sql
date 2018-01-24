SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS  `attest`;
CREATE TABLE `attest` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '����',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '��������ʱ��',
  `version` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]��֤��¼�汾����ʽ��1-2^16�����汾��. ',
  `ancestors_ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'ԭʼ��֤��.�״δ�֤�Ķ�����',
  `parent_ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '��һ�δ�֤�š�����ʱ��Ч',
  `ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]������,ȫƽ̨Ψһ��ʶ.',
  `channel_id` varchar(3) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]������Դ',
  `channel_userid` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '���ױ�ʶ�������û���ʶ��',
  `channel_ordersn` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '���ױ�ʶ��������֤����Ψһ��ʶ',
  `biz_sign` varchar(88) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]�����Զ���ǩ��',
  `provinder_id` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]������',
  `chained` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]���������1������0������',
  `state_time` timestamp(6) NULL DEFAULT NULL COMMENT '����״̬����ʱ��',
  `state` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'ҵ�񶩵�״̬.1.������ 2.��֤������� 3.�����ɹ�',
  `wallet_addr` varchar(122) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]Ǯ�������ַ����ʾ�ն��û���ʽ',
  `public_key` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]�û�Ǯ����Կ��',
  `attest_sign` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]�ͻ�ǩ�����ͻ�ʹ�ñ��˵�Ǯ����˽Կ���Ըøô�֤��¼���ֶν��м���',
  `attest_type` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]��֤���ͱ�ʶ��1�ļ���֤��2hash��֤',
  `biz_type` varchar(2) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]ҵ�����͡������ļ�����������Ƶ��ѹ���ȷ�ʽ',
  `file_name` varchar(30) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]Դ�ļ���',
  `file_type` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]�ļ�����',
  `file_size` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]Դ�ļ�����',
  `file_sign` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]�ļ�ǩ����ʹ�ñ���Ǯ��˽Կ�Ը����ݽ��м���',
  `file_hash` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]�ļ���ϣֵ��sha256',
  `encrypted` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '���������1���ܣ�0������',
  `encrypt_alog` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '�����㷨',
  `encrypt_key` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '������Կ',
  `owner_type` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]Ȩ�������͡�1��Ȼ�ˣ�2���ˣ�0����',
  `owner_id` varchar(18) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]Ȩ������ݱ�ʶ�����֤�š�������ô���',
  `owner_name` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]Ȩ��������',
  `agent_name` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '������',
  `agent_phone` varchar(13) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '�����˵绰',
  `agent_email` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '����������',
  `origin_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '[��������]��֤��ʼʱ��',
  `start_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '[��������]��ǰ��֤��ʼʱ��',
  `expired_time` timestamp NULL DEFAULT NULL COMMENT '����ʱ��',
  `duration` varchar(5) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]��ǰ��֤����ʱ��(��),��start_time��ʼ��.',
  `description` varchar(500) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]��֤������Ϣ',
  `price` varchar(8) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]���δ�֤��������.',
  `request_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '�����ʶ��������ʱ�䡣��ʽ��20170728172911',
  `platform_sign` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]��֤ƽ̨�Զ�����ǩ��',
  `chained_content` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_attest_id` (`id`) USING BTREE,
  UNIQUE KEY `idx_attest_channel_ordersn` (`channel_ordersn`) USING HASH,
  UNIQUE KEY `idx_attest_parent_ordersn` (`parent_ordersn`) USING BTREE,
  UNIQUE KEY `idx_attest_ordersn` (`ordersn`) USING BTREE,
  KEY `idx_attest_owner_id_and_file_hash` (`file_hash`,`owner_id`),
  KEY `idx_attest_ancestors_ordersn` (`ancestors_ordersn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='��֤��Ϣ���걸ҵ������洢��֤����ϸ��Ϣ';

DROP TABLE IF EXISTS  `attest_chained`;
CREATE TABLE `attest_chained` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '��������Id',
  `ordersn` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '֪������֤ƽ̨��֤��',
  `txid` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '���������ʶ',
  `send_time` timestamp(6) NULL DEFAULT NULL,
  `refresh_time` timestamp NULL DEFAULT NULL COMMENT '��������ʱ��',
  `create_time` timestamp(6) NULL DEFAULT NULL COMMENT '��������ʱ��',
  `state` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '������',
  `num` int(5) DEFAULT NULL COMMENT '�쳣����',
  `remark` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '��ע',
  `channel_ordersn` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_attest_chained_id` (`id`),
  UNIQUE KEY `idx_attest_chained_ordersn` (`ordersn`),
  UNIQUE KEY `idx_attest_chained_txid` (`txid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS  `attest_file`;
CREATE TABLE `attest_file` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `update_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '����ʱ��',
  `create_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '��������ʱ��',
  `ordersn` varchar(21) COLLATE utf8_unicode_ci NOT NULL COMMENT '��֤ƽ̨��֤��',
  `file_name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Դ�ļ���',
  `bucket_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '�洢�ռ�����Ŀǰ��osstestoss',
  `file_addr` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'OSS�洢�ļ�ID',
  `file_size` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Դ�ļ�����',
  `file_hash` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Դ�ļ�hash',
  `file_sign` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '�ļ�ǩ����ʹ�ñ���Ǯ��˽Կ�Ը����ݽ��м���',
  `encrypted` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '�Ƿ���ܣ�1���ܣ�0������',
  `encrypt_alog` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '�����㷨',
  `encrypt_key` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '������Կ',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_attest_file_ordersn` (`ordersn`) USING BTREE,
  UNIQUE KEY `idx_attest_file_id` (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='�ļ���ַ��������֤��¼�������ļ�����֤Դ�ļ�    attest_biz\r\n';

DROP TABLE IF EXISTS  `attest_history`;
CREATE TABLE `attest_history` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '����',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '��������ʱ��',
  `version` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]��֤��¼�汾����ʽ��1-2^16�����汾��. ',
  `ancestors_ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'ԭʼ��֤��.�״δ�֤�Ķ�����',
  `parent_ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '��һ�δ�֤�š�����ʱ��Ч',
  `ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]������,ȫƽ̨Ψһ��ʶ.',
  `channel_id` varchar(3) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]������Դ',
  `channel_userid` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '���ױ�ʶ�������û���ʶ��',
  `channel_ordersn` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '���ױ�ʶ��������֤����Ψһ��ʶ',
  `biz_sign` varchar(88) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]�����Զ���ǩ��',
  `provinder_id` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]������',
  `chained` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]���������1������0������',
  `expired_time` timestamp NULL DEFAULT NULL COMMENT '����ʱ��',
  `state_time` timestamp(6) NULL DEFAULT NULL COMMENT '����״̬����ʱ��',
  `state` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'ҵ�񶩵�״̬.1.������ 2.��֤������� 3.�����ɹ�',
  `wallet_addr` varchar(122) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]Ǯ�������ַ����ʾ�ն��û���ʽ',
  `public_key` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]�û�Ǯ����Կ��',
  `attest_sign` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]�ͻ�ǩ�����ͻ�ʹ�ñ��˵�Ǯ����˽Կ���Ըøô�֤��¼���ֶν��м���',
  `attest_type` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]��֤���ͱ�ʶ��1�ļ���֤��2hash��֤',
  `biz_type` varchar(2) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]ҵ�����͡������ļ�����������Ƶ��ѹ���ȷ�ʽ',
  `file_name` varchar(30) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]Դ�ļ���',
  `file_type` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]�ļ�����',
  `file_size` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]Դ�ļ�����',
  `file_sign` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]�ļ�ǩ����ʹ�ñ���Ǯ��˽Կ�Ը����ݽ��м���',
  `file_hash` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]�ļ���ϣֵ��sha256',
  `encrypted` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '���������1���ܣ�0������',
  `encrypt_alog` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '�����㷨',
  `encrypt_key` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '������Կ',
  `owner_type` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]Ȩ�������͡�1��Ȼ�ˣ�2���ˣ�0����',
  `owner_id` varchar(18) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]Ȩ������ݱ�ʶ�����֤�š�������ô���',
  `owner_name` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]Ȩ��������',
  `agent_name` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '������',
  `agent_phone` varchar(13) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '�����˵绰',
  `agent_email` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '����������',
  `origin_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '[��������]��֤��ʼʱ��',
  `start_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '[��������]��ǰ��֤��ʼʱ��',
  `duration` varchar(5) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]��ǰ��֤����ʱ��(��),��start_time��ʼ��.',
  `description` varchar(500) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]��֤������Ϣ',
  `price` varchar(8) COLLATE utf8_unicode_ci NOT NULL COMMENT '[��������]���δ�֤��������.',
  `request_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '�����ʶ��������ʱ�䡣��ʽ��20170728172911',
  `platform_sign` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]��֤ƽ̨�Զ�����ǩ��',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_attest_history_id` (`id`) USING BTREE,
  UNIQUE KEY `idx_attest_history_originalOrdersn` (`parent_ordersn`) USING BTREE,
  UNIQUE KEY `idx_attetst_history_ordersn` (`ordersn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='��֤��Ϣ���걸ҵ������洢��֤����ϸ��Ϣ';

DROP TABLE IF EXISTS  `channel`;
CREATE TABLE `channel` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `update_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '����ʱ��',
  `create_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '��¼����ʱ��',
  `channel_id` varchar(3) COLLATE utf8_unicode_ci NOT NULL COMMENT '����Ψһ��ʶ��ƽ̨����',
  `channel_public_key` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT '������Կ,Hex�����ʽ',
  `channel_private_key` varchar(66) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '����˽Կ. һ��û�е�',
  `channel_ida` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '����Ψһ�����ʶ',
  `platform_publick_key` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT 'ƽ̨��Ӧ��channel�Ĺ�Կ,Hex�����ʽ',
  `platform_private_key` varchar(66) COLLATE utf8_unicode_ci NOT NULL COMMENT 'ƽ̨��Ӧ��channel��˽Կ',
  `platform_ida` varchar(30) COLLATE utf8_unicode_ci NOT NULL COMMENT '��֤ƽ̨�����ʶ',
  `access_key` varchar(52) COLLATE utf8_unicode_ci NOT NULL COMMENT '�����ķ��ʱ�ʶ',
  `name` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '��������',
  `channel_type` varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '��ҵ���ͣ�1��ҵ��2�������ء�3��ҵ��λ��4������塢5��ӯ������֯��0����',
  `biz_duration` varchar(3) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Ӫҵ���ޣ�3/5/10/20',
  `scale` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '��ҵ��ģ��100/300/500��',
  `biz` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '��Ӫҵ��',
  `url` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '��ַ',
  `address` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '��ַ',
  `contact` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '��ϵ��',
  `phone` varchar(13) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '��ϵ�绰',
  `verified` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '�Ƿ�ͨ��ʵ����֤��1ͨ����0δͨ��',
  `license_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'Ӫҵִ��ȫ��',
  `license_num` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '���ͳһ���ô���',
  `account_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '�Թ��˻���',
  `account_num` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '�Թ��˻��˺�',
  `freezed` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '�Ƿ񶳽᣿1���ᣬ0δ����',
  PRIMARY KEY (`id`),
  KEY `idx_channel_id` (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='������Ϣ��������������֯��ƽ̨������������ҵ�����ݣ�ע�⿼�Ǵ�֤��¼�е��������벻�ɸ����ԡ�����Ҳ������Ϊ��֯��';

insert into `channel`(`id`,`update_time`,`create_time`,`channel_id`,`channel_public_key`,`channel_private_key`,`channel_ida`,`platform_publick_key`,`platform_private_key`,`platform_ida`,`access_key`,`name`,`channel_type`,`biz_duration`,`scale`,`biz`,`url`,`address`,`contact`,`phone`,`verified`,`license_name`,`license_num`,`account_name`,`account_num`,`freezed`) values
('1','2017-12-01 17:07:56','2017-10-30 20:31:24','003','04a817f0a15522b80c2974995f20f0686efad4f62e7fa77248a0aa54016fd56e02bee20bfea2616eaff6a1f2eb5d89444704ba450036530fe664df0bf8e88f8319','753912b20a9a24d9954f9f92f8c90278b3a672ae20b0364b8e2071a4d94473b2','super_channel_01','04152e7d1c170d21e52a2d0620f4f7449be23a510208a8ca09ed3c675f0d26a6929ffba6bece9be9dde20ce3fcfeb1d16d8b665d63dc012e89fffd1981c39e5bdc','169e948d0487de90a03039ae242269efb47ddb0a0fcb2cc9ae8650f7d0f3fdf1','zhijinlian_blockchain_cz2017','2tBboQXaG8L4Msw59ZRm7pbq3xWxrbcjqA7FkNrnxsVRohJS6o','����ʰ��֪ʶ��Ȩ�������޹�˾',null,null,null,null,null,null,null,null,null,null,null,null,null,'0'),
('11','2017-10-23 11:35:19','2017-10-04 15:14:11','004','048dbbc57d90f4f5ff4aa5a16d05067531cb3a9137d92a262550bfb17bd1f293d9cc9713d58a226df329855a58e0553a858c1da56fc0b61e8f4386083db34d737a','76e046ff78a0ba45fed32ebf0c7b61eadfa2dc503fb5fe94f60edfec0a46a0c0','super_channel_01','0492e4c7d9ccc08e72ed13b47c94a6da71777052d1c30cdef19ea444a0bf0b90b61fd9f62ce6807f8676ff006e98601876d18508816601d1ea0cfcea7f6dbb6acf','09d36db177fe8d427365f0f66f7f7bf370b9c1c395b8c73155b83c356284df51','zhijinlian_blockchain_cz2017','ZvSmW2K36upCN4tJ9wL7N3xaEde8JYMbJotWdr7daUiaNhZRk','֪������վ',null,null,null,null,null,null,null,null,null,null,null,null,null,'0'),
('14','2017-10-30 20:36:32','2017-10-30 20:34:38','005','04ea3d76b9afdcce27ec9b03224f4080fddaabbd95b3d93b5946b5ca5d2a1b6c5a6b6d8bf6dcba2f2920dccb269e7ea0df825a52e2292593dac3051540b917265a','00856b9174ab913cf3aac0dec107650c31cb3875d224ffe0648f6158544b7e56ed','super_channel_01','046413e68d62e39de4896e0cc6595571b128e14aa9be49fc4f52fce415616be307a1e20bd5699148af4ec6217c73c8d71f653c62a0172765e3b54a06aee2c3d76c','008498ec66fd304faa295a7de72895ff64b2a442c50293f570217881e89d6f0e9b','zhijinlian_blockchain_cz2017','2kDv8FXEzb69pk261PXPQ65KezG2fzbbHX7SrUEYkkabk74yHa','mr.wang','1',null,null,null,null,null,null,null,null,null,null,null,null,'0'),
('15','2017-10-30 20:36:35','2017-10-30 20:36:37','006','042badc24548ed79733090594d555a106cd1d5e88ea7c55866d0bcfbc794ec779645d0ecf36152bac0cadeefd27e4f38c99b29892f90193cd5ed6446cde02499ee','50334716ab411a5c155aaf584c552983b490fb28a1b84850981918ec7fb9ed90','super_channel_01','045dfe4291145d852427ae72cd2e6a3ead1d926e5406807e5d038691d2e92e3cc9223ef86c11e9a736166f59ca1120f650caa7b1eaa0df98fc3a73ff28843d7256','7786aa8be66582cc9c4a9f98cda5a8c4b6a5174f8a7dca3b0905908ee5c99929','zhijinlian_blockchain_cz2017','uMFf6mFp2CqYckJVZPaU8uZwz3RgorNst1CfMunexXzNaCvHN','mr.huang','1',null,null,null,null,null,null,null,null,null,null,null,null,'0');
DROP TABLE IF EXISTS  `config`;
CREATE TABLE `config` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `config_type` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '����ID',
  `config_key` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '��',
  `config_value` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'ֵ',
  `create_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '����ʱ��',
  `remark` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '��ע',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

insert into `config`(`id`,`config_type`,`config_key`,`config_value`,`create_time`,`remark`) values
('2','005','hash_cz_ok_url','http://cz.zhijl.com/channelTool/hashCzOk','2017-11-23 20:20:11','�ļ�Hash��֤֪ͨ�ص���ַ'),
('3','005','xq_ok_url','http://cz.zhijl.com/channelTool/CzXqOk','2017-11-23 20:20:14','�ļ���֤����֪ͨ�ص���ַ'),
('4','002','ordersn_url','http://172.17.94.204:9020/ordersn/createOrdersn','2017-11-24 10:01:37','���󶩵��ŵ�ַ'),
('5','003','cz_ok_url','http://testcz.ipsebe.com/sebe/requestReceiveFinishCallback.do','2017-12-01 17:23:55','�ļ���֤֪ͨ�ص���ַ'),
('6','003','hash_cz_ok_url','http://testcz.ipsebe.com/sebe/saveHashFileCallback.do','2017-12-01 17:23:55','�ļ�Hash��֤֪ͨ�ص���ַ'),
('7','003','xq_ok_url','http://testcz.ipsebe.com/sebe/extensionCallback.do','2017-12-01 17:23:55','�ļ���֤����֪ͨ�ص���ַ'),
('8','004','cz_ok_url','http://47.94.229.222/EvidenceModule/Evidence/dealEvidenceEnd','2017-11-14 10:58:54','�ļ���֤֪ͨ�ص���ַ'),
('9','004','hash_cz_ok_url','http://47.94.229.222/EvidenceModule/Evidence/dealHashEvidence','2017-11-14 10:58:56','�ļ�Hash��֤֪ͨ�ص���ַ'),
('10','004','xq_ok_url','http://47.94.229.222/EvidenceModule/Evidence/dealRenewalEvidence','2017-11-14 10:58:58','��֤����֪ͨ�ص���ַ'),
('11','005','cz_ok_url','http://cz.zhijl.com/channelTool/fileCzOk','2017-11-23 20:19:54','�ļ���֤��ɻص���ַ'),
('13','001','query_cz_chained_url','http://172.17.94.211:80/queryCzByOrdersn','2017-11-28 18:15:30','��ѯ��֤�����ӿ�'),
('14','001','add_cz_chained_url','http://172.17.94.211:80/addCz','2017-11-28 18:15:30','���Ӵ�֤�����ӿ�'),
('15','store_location','beijingOSS1','����һ��json��{������accesskey��ֵ�ԣ�oss�洢�ռ�}','2017-11-16 20:07:27','˵��'),
('16','006','cz_ok_url','http://cz.zhijl.com/channelTool/fileCzOk','2017-11-28 11:25:42','�ļ���֤��ɻص���ַ'),
('17','006','hash_cz_ok_url','http://cz.zhijl.com/channelTool/hashCzOk','2017-11-28 11:26:43','�ļ�Hash��֤֪ͨ�ص���ַ'),
('18','006','xq_ok_url','http://cz.zhijl.com/channelTool/CzXqOk','2017-11-28 11:31:40','�ļ���֤����֪ͨ�ص���ַ');
DROP TABLE IF EXISTS  `dictionary`;
CREATE TABLE `dictionary` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `version` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '�汾��',
  `type` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '�ֵ�����',
  `code` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '�ֵ����',
  `text` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT 'չ���ı������ͳһ���ô���',
  `parent_code` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '���ֵ����',
  `remark` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '��ע˵����6λ������������',
  `create_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '����ʱ��',
  `update_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '����ʱ��',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idex_dictionary_id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

DROP TABLE IF EXISTS  `prove`;
CREATE TABLE `prove` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `prove_id` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '��֤����Ψһ��ʶ',
  `reason` varchar(100) COLLATE utf8_unicode_ci NOT NULL COMMENT '��֤����',
  `apply_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '�û�����ʱ��',
  `solve_unit_id` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '������֤������ʶ',
  `price` varchar(10) COLLATE utf8_unicode_ci NOT NULL COMMENT '��֤�۸�',
  `status` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '��֤״̬��1���룬2����3�����У�4��ɣ�5�ʼ�',
  `create_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '����ʱ��',
  `update_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '�޸�ʱ��',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='��֤������������֤������Ϣ�����֤ʵ��������';

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
  `ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '������',
  `channel_ordersn` varchar(50) COLLATE utf8_unicode_ci NOT NULL COMMENT '����Ψһ��ʶ',
  `state` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '������ǰ״̬',
  `num` int(3) DEFAULT NULL COMMENT '�쳣������¼',
  `remark` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '��ע',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_status_id` (`id`),
  UNIQUE KEY `idx_status_ordersn` (`ordersn`),
  KEY `idx_status_state` (`state`),
  KEY `idx_status_num` (`num`) USING BTREE,
  KEY `idx_status_channelOrdersn` (`channel_ordersn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='��¼������״̬���ڲ�ҵ��˲顣';

DROP TABLE IF EXISTS  `temp_order`;
CREATE TABLE `temp_order` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '����',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '��������ʱ��',
  `version` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]��֤��¼�汾����ʽ��1-2^16�����汾��. ',
  `ancestors_ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'ԭʼ��֤��.�״δ�֤�Ķ�����',
  `parent_ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '��һ�δ�֤�š�����ʱ��Ч',
  `ordersn` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]������,ȫƽ̨Ψһ��ʶ.',
  `channel_id` varchar(3) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]������Դ',
  `channel_userid` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '���ױ�ʶ�������û���ʶ��',
  `channel_ordersn` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '���ױ�ʶ��������֤����Ψһ��ʶ',
  `biz_sign` varchar(88) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]�����Զ���ǩ��',
  `provinder_id` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]������',
  `chained` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]���������1������0������',
  `state_time` timestamp(6) NULL DEFAULT NULL COMMENT '����״̬����ʱ��',
  `state` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'ҵ�񶩵�״̬.1.������ 2.��֤������� 3.�����ɹ�',
  `wallet_addr` varchar(122) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]Ǯ�������ַ����ʾ�ն��û���ʽ',
  `public_key` varchar(150) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]�û�Ǯ����Կ��',
  `attest_sign` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]�ͻ�ǩ�����ͻ�ʹ�ñ��˵�Ǯ����˽Կ���Ըøô�֤��¼���ֶν��м���',
  `attest_type` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]��֤���ͱ�ʶ��1�ļ���֤��2hash��֤',
  `biz_type` varchar(2) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]ҵ�����͡������ļ�����������Ƶ��ѹ���ȷ�ʽ',
  `file_name` varchar(30) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]Դ�ļ���',
  `file_type` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]�ļ�����',
  `file_size` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]Դ�ļ�����',
  `file_sign` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]�ļ�ǩ����ʹ�ñ���Ǯ��˽Կ�Ը����ݽ��м���',
  `file_hash` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]�ļ���ϣֵ��sha256',
  `file_addr` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `encrypted` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '���������1���ܣ�0������',
  `encrypt_alog` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '�����㷨',
  `encrypt_key` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '������Կ',
  `owner_type` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]Ȩ�������͡�1��Ȼ�ˣ�2���ˣ�0����',
  `owner_id` varchar(18) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]Ȩ������ݱ�ʶ�����֤�š�������ô���',
  `owner_name` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]Ȩ��������',
  `agent_name` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '������',
  `agent_phone` varchar(13) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '�����˵绰',
  `agent_email` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '����������',
  `origin_time` timestamp(6) NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '[��������]��֤��ʼʱ��',
  `start_time` timestamp(6) NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '[��������]��ǰ��֤��ʼʱ��',
  `duration` varchar(5) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]��ǰ��֤����ʱ��(��),��start_time��ʼ��.',
  `description` varchar(500) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]��֤������Ϣ',
  `price` varchar(8) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]���δ�֤��������.',
  `request_time` timestamp(6) NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '�����ʶ��������ʱ�䡣��ʽ��20170728172911',
  `platform_sign` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '[��������]��֤ƽ̨�Զ�����ǩ��',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_temp_order_id` (`id`) USING BTREE,
  UNIQUE KEY `idx_temp_order_channelOrdersn` (`channel_ordersn`) USING BTREE,
  KEY `idx_temp_order_ordersn` (`ordersn`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='��֤��Ϣ���걸ҵ������洢��֤����ϸ��Ϣ';

DROP TABLE IF EXISTS  `user`;
CREATE TABLE `user` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000',
  `wallet_addr` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT 'Ǯ����ַ',
  `public_key` varchar(150) COLLATE utf8_unicode_ci NOT NULL COMMENT '�û���Կ',
  `encrypted_prikey` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '���ܺ��˽Կ',
  `encrypt` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '����˵��',
  `channel_id` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '������ʶ',
  `channel_userid` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '�����û���ʶ',
  `user_type` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '�û����ͣ�1���ˣ�2��Ȼ��',
  `user_name` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '��������������',
  `user_id` varchar(18) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '��ݱ�ʶ��1���֤�š�2���ͳһ���ô���',
  `verified` varchar(1) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '�Ƿ�ͨ��ʵ����֤��1ͨ����0δͨ��',
  `verify_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT 'ͨ����֤ʱ��',
  `freezed` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '�Ƿ񶳽᣿1���ᣬ0δ����',
  `reamrk` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '��ע��Ϣ',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_id` (`id`),
  UNIQUE KEY `idx_user_channeluserid_channelid` (`channel_userid`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='ƽ̨�û���';

DROP TABLE IF EXISTS  `warning`;
CREATE TABLE `warning` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `update_time` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  `biz_type` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ordersn` varchar(22) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '��֤��',
  `channel_ordersn` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '������',
  `num` int(5) NOT NULL COMMENT '�����쳣������¼',
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

