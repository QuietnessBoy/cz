/*
Navicat MySQL Data Transfer

Source Server         : localhsot
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : attest_biz

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2017-12-22 18:19:45
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for attest
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
) ENGINE=InnoDB AUTO_INCREMENT=96 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='存证信息表（完备业务表）：存储存证的详细信息';

-- ----------------------------
-- Records of attest
-- ----------------------------
INSERT INTO `attest` VALUES ('46', '2017-12-21 14:13:43', '2017-12-21 14:13:43.000000', '0.9', '005361171221000005', null, '005361171221000005', '005', 'e3c', 'ordersne683cc273', '5VewTi5uCzvvZfyDcz/RM8Km8AXzOTC04F3QvOYabjhKZmMOvvxSXrx7B26aGsJVU1iJ55pkHViMoASk045s1A==', 'ZHJL', '1', '2017-12-21 14:13:50.042000', '1', 'ncCKSSgZZi2DvbvdPXkjhz1cnb4GgLywVaTtcnMZfysTGz2LC', '04c28d6224f2113deec12e1de4de7d5b9564c437014d315da3443f39829d01bb7b0634a1b28ccfadf484abd01cba639f15b51e29bcb12042a6d59b84a038b0c2ff', '36vr0Pi4FzcFx3RHE6jVqM03PfljGkaZnanOmC4KKbw0mFPswyyhvWXVz/kKoIIGy8mQXIgKz4zPVnAVbHdYQA==', '1', '6', '测试-ordersne683cc273', '.79e', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid2', '存证人5d99', 'agent480eb', '4628044', '37c4fb5560a@.com', '2017-12-21 14:13:47.941000', '2017-12-21 14:13:47.849000', '2017-12-22 14:13:48', '1', '描述:测试-ordersne683cc273ordersne683cc273', '200', '2017-12-21 14:13:26.000000', 'Y8CKPbIj9RuvvYqqTASe98nFyqfy5KzUe7LHVLS+YAiCyvrDSCUZb+lWboh4zSfOFNTni3AG7t3HOwcMKFBOyw==', 'CgExEhIwMDUzNjExNzEyMjEwMDAwMDUaATAiIgog40UyO6Mnw4lWsBdPActxGTTk8O2d4Hor8vO7t1r6Vuc=');
INSERT INTO `attest` VALUES ('47', '2017-12-21 14:21:20', '2017-12-21 14:21:21.000000', '0.9', '005311171221000006', null, '005311171221000006', '005', '40c', 'ordersnf67cb2df4', 'kOaxquvCtVWuN71bJQRQ2yxBz4c57CJsidWwChS5zdg8gg+B7BSMgSzaUiPwru4/M93ZALBEsOmNQuFs60K93Q==', 'ZHJL', '1', '2017-12-21 14:21:22.919000', '1', '2DMEY85vhcmRSqVU7hiP3LsxzcYbhQ3bjH7FR3XZeVBrKhort8', '0429c55c42b0f430a5b9e1a6a9df8ebf1663dd1aab3b11df015b80abe288380fdbec5143652053b38c511355ec8b5db9d88f06669963af885a55752e746ca6af56', 'hGbO+5aUvyAzW8+yin6mniR8JHS4+73cuz9XbfMNsjVGWyChxdHFcz/YirjG2Sp/89LCIoIGRF4FOJjy1F+vxg==', '1', '1', '测试-ordersnf67cb2df4', '.138', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid10', '存证人a71a', 'agentaac46', '5452556', '06800e68e77@.com', '2017-12-21 14:21:21.795000', '2017-12-21 14:21:21.782000', '2017-12-22 14:21:22', '1', '描述:测试-ordersnf67cb2df4ordersnf67cb2df4', '200', '2017-12-21 14:21:11.000000', 'lIWXGyD/LUX0dOuDiGBbMGb+yI71u/FWepa4A2rxmHhiqOt0QsCxj1GlbvxwmUYkHztwCVXJsRq/0WlVTiTejA==', 'CgExEhIwMDUzMTExNzEyMjEwMDAwMDYaATAiIgoggsa8a/wHqh8NotbO2M3QLppNTCVxOlce66gkl6jyxuc=');
INSERT INTO `attest` VALUES ('48', '2017-12-21 14:22:24', '2017-12-21 14:22:25.000000', '0.9', '005311171221000007', null, '005311171221000007', '005', '21d', 'ordersn9c2909b51', '55oSfvVjfEl0+MICG7ekO6oNplBwL4EoqT+HV1Mc31anOwBd1B4ELiVqUcMGKNn/0WtaV4WLW4M9mSWeu1gWaw==', 'ZHJL', '1', '2017-12-21 14:22:26.822000', '1', 'RDXMdJ6soHiPhT17S14mGagvqmJoypiyCQD7YU1z3R5sdEwub', '04e2463aaf5954f46548f8aa699a2982935c253a91907a137f1c5e2427b60d9677904e9b6295262c7ac58436cfae8f30e28efe1ef93327884fed52e025bdd0d534', '7mg7UgVXhJlV1Or1R8tauyuZhjPYKQJytmNYAz5idjfawSTArk33OQXY35Jkh4UPWLqpTpqQ9PgiKxiy5t0GCw==', '1', '1', '测试-ordersn9c2909b51', '.52b', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid1', '存证人5a09', 'agentfe1d3', '9526652', '174bc9a16ad@.com', '2017-12-21 14:22:25.804000', '2017-12-21 14:22:25.785000', '2017-12-26 14:22:26', '5', '描述:测试-ordersn9c2909b51ordersn9c2909b51', '200', '2017-12-21 14:22:12.000000', 'cADrkoRpbSkf1QZ+n9naufJNToWws4NsaCNYP4MK4jfvO5LlNzX+sMMxhgUTCp5HbFpVw2npNXeI3r/4hVRDRw==', 'CgExEhIwMDUzMTExNzEyMjEwMDAwMDcaATAiIgogaaGvlu/inRlZwwXXBHCae92cpk38RwiosAcv4MgkPCk=');
INSERT INTO `attest` VALUES ('49', '2017-12-21 15:32:47', '2017-12-21 15:32:56.000000', '0.9', '005311171221000008', null, '005311171221000008', '005', '4ea', 'ordersn52e6bfb60', '54ctt7XQB0BbDkeUgUoj3OyTMtul0Blu2p4T4eknv+hZX7hNlHsP57S8Zo/1Eu+lSEVkdkTK7/qqFb+fsta41w==', 'ZHJL', '1', '2017-12-21 15:32:55.977000', '1', 'xJTRrYFZfL4i2BX3c9HeCn1VihnjKCbPobKBeKhEr1QnyYZw9', '045dcfd13a3050c0927e72baef7ae06077e0d1c5421bad946a35c4cd962773e669ddbc329ea997a2ea99b0a6f8f8a00db2b0a0bfcf413f5386a19bd62f9a29c183', 'Vria8QFzJrBqC6u+pn9ZJO2mVrg3S9LraKyxJsMMUiXteNZ6qGtrWjk9oQXpGmRC/BEUWaIJXvebgxFNsTFcdQ==', '1', '1', '测试-ordersn52e6bfb60', '.bec', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid5', '存证人9bb7', 'agent96768', '4182866', '738eecfd598@.com', '2017-12-21 15:32:55.985000', '2017-12-21 15:32:55.816000', '2017-12-23 15:32:56', '2', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"738eecfd598@.com\",\"agentName\":\"agent96768\",\"agentPhone\":\"4182866\",\"b', '200', '2017-12-21 15:32:25.000000', '2gRlnFc9lGTqc/gsDyFMf5HdEOhIQeU8HTj9y24eWttHgP5ohLcP7t8zN6Nb2FZ4SC7x8nXks93ASr9B6G9Qnw==', 'CgExEhIwMDUzMTExNzEyMjEwMDAwMDgaATAiIgogZNM0a6z5IIJMdF2upEDDSBNgzR0bQg9kKx4K7Voa68U=');
INSERT INTO `attest` VALUES ('50', '2017-12-21 16:57:55', '2017-12-21 16:57:55.000000', '0.9', '005321171221000009', null, '005321171221000009', '005', '1a7', 'ordersneb8049d81', 'Utjf7W7x7xNRu/ofgJceRx2Tt2IpdJgFRNPscE76mT3Vh7NIQkQvM93WNK3MwJB6lz1CX56YbZxX9SomAjMi0w==', 'ZHJL', '1', '2017-12-21 16:57:55.274000', '0', '2p4a2RB7KovNiGyJ9fnsveR7qJGsLp363nd1HcJZ4oFK8sP9Ew', '04ae127fbd9390407796329253091c5d5d3b2130b6b55af43e8f407be4a2968c98291e992cab7195552e145f73d08c309fd6c1bf9b0df6db9345f8621ec5e76c10', 'LiHlbKSyxGCwlY+Xbn1F1hBnSP5Ca4JCU8yBi0j/jrhNmg4v2YrbY7f9hOWt8EJSOX7IKP9nXoujoYy1qKCygw==', '1', '2', '测试-ordersneb8049d81', '.afb', '13', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid1', '存证人1db9', 'agent720c5', '9083844', '427c440d652@.com', '2017-12-21 16:58:03.397000', '2017-12-21 16:58:03.346000', '2017-12-26 16:58:03', '5', '描述:测试-ordersneb8049d81ordersneb8049d81', '200', '2017-12-21 16:57:15.000000', '2Te6JNAEvLm/8lkLU7Bp3N20vfbv95syCUy7H+SIeFKmdOCIsz1IMkJFEsfruCdkeEruoFx+5XZSe2SuOzQ5hQ==', null);
INSERT INTO `attest` VALUES ('51', '2017-12-21 17:03:53', '2017-12-21 17:03:54.000000', '0.9', '005341171221000010', null, '005341171221000010', '005', 'f0c', 'ordersn4f8aebf63', '7pXn9PRDByYA5yIWlALQncsaiew/pvAp1qcPBwZc9eRIBsAh2VE56BjfafUnf61YHKYFeUlg7AMiuW1nhfTPqw==', 'ZHJL', '1', '2017-12-21 17:04:00.207000', '1', 'mAjf1ARNndG5Ntnrnq1KF5pmTV89HZQB1NN41PKMWzjnEmg4L', '0412498b09d33b017a5f23f17ba4f4469b46a949c08c84f3d8a872c1ced21088e8363a165f5970b8f7667cfc304b1b6b11cfb49bd31a4339d7df004522a0ef3d01', 'GloJyKAoxzymM4FqIDHgm/XCAuan9SWMNw7NmN8fcOegvRtbndVrdMjXvE8M/KSnBcwGQVYSWbitrvY1lWrOnQ==', '1', '4', '测试-ordersn4f8aebf63', '.1c1', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid8', '存证人c20f', 'agent50a1e', '1593291', 'a10a66374c7@.com', '2017-12-21 17:03:56.506000', '2017-12-21 17:03:56.491000', '2017-12-22 17:03:56', '1', '描述:测试-ordersn4f8aebf63ordersn4f8aebf63', '200', '2017-12-21 17:03:22.000000', 'BzyA/WAMZcs134omtZt9hwzy6OuTKnVMa8ud0M18WswNBPLkpQLhwqD7CJIrKgrt4I/jmF+CryQ9ND2+soylNA==', 'CgExEhIwMDUzNDExNzEyMjEwMDAwMTAaATAiIgogZoiI7HUTYTZJFSwS5IHtGZkK+xv6eYyChPg5jPprhaA=');
INSERT INTO `attest` VALUES ('52', '2017-12-21 17:04:16', '2017-12-21 17:04:20.000000', '0.9', '005321171221000011', null, '005321171221000011', '005', '94e', 'ordersne6ea58029', '/gGkRLWNRO/Bejv4mXGM/Tg35xLD7fTv4zGp6jM2tELRocBxZVRq9YVD20KrMI81esBEIkzM4yPf3tqiE3eJbg==', 'ZHJL', '1', '2017-12-21 17:04:20.426000', '1', '73n7SFxcfjNXcrtCpTpc4saFHvi2VKyUNMUVYFv7qz1vNaubJ', '04b33fc3e5cdb6dbc4fc4ca9c59a38435142b0a79c11d4c2961cb20b12befa9bf16bee3e1aca68b19fa45718349207d95dc546afd5d666e00ee1d7aa94eb95081f', 'N+13WI8QSCl7JV8psXQ+20/brtwwuZEWyvZ90PmbYyN2LwRRNq7Cw+Aejs0n4iR7jCzzLudc3qWrGVxczPbttw==', '1', '2', '测试-ordersne6ea58029', '.b71', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid2', '存证人53b8', 'agent0d63a', '1730530', 'a022d6fbe1f@.com', '2017-12-21 17:04:20.429000', '2017-12-21 17:04:20.192000', '2017-12-24 17:04:20', '3', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"a022d6fbe1f@.com\",\"agentName\":\"agent0d63a\",\"agentPhone\":\"1730530\",\"b', '200', '2017-12-21 17:04:07.000000', '3bV47mjb3xIV8NW1QG6KnVbR2AUJM9T+67rUbFCIHL0/jWMRMAswioY2jETufzN9WYhWTHTw1Rp1rrUSoCQ3og==', 'CgExEhIwMDUzMjExNzEyMjEwMDAwMTEaATAiIgogXV8GRTB1lcHN6W/YJoa8uqJ92pojpf/vxroPCkBmjew=');
INSERT INTO `attest` VALUES ('53', '2017-12-21 18:43:48', '2017-12-21 18:43:59.000000', '0.9', '005341171221000012', null, '005341171221000012', '005', '462', 'ordersnfce718ad5', '2UkmeCjNadu6Wb5RlAlhypsNYC26KNLIcxh86DpZf/+qg4IPFaN5EuII9KnedRutvcj7SCUGm5z0IxqVNrWkVA==', 'ZHJL', '1', '2017-12-21 18:43:59.336000', '1', '2PhXhVTuNcKTUMtMDBfWsUMBXDRoMjSgyZwGN54enrnBG8MS3z', '04d2e9a439a6504cf71b67a237b9a96bd24559398ad57ca06e4704af6dce1cc09aa94e5aa5aedfbf6a23d2b61107125562eb81471ebd080b3da2d4ef07ea3172c3', 'IbzT6rbbxRAsoF6KfYRiFj/HyUJJB6tvJy9MtxV9GF5RdOD5xqvUCjQ7s15KgOzEh1otGd/1edkbRPAFOzZODw==', '1', '4', '测试-ordersnfce718ad5', '.721', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid6', '存证人6402', 'agentf07aa', '6055217', '07b08ec9e19@.com', '2017-12-21 18:43:59.346000', '2017-12-21 18:43:59.325000', '2017-12-26 18:43:59', '5', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"07b08ec9e19@.com\",\"agentName\":\"agentf07aa\",\"agentPhone\":\"6055217\",\"b', '200', '2017-12-21 18:42:40.000000', 'XoQDaTPQEx93qx9CYitiF/rKdyd3u7ooHwx0uEUffhwODa1K+/K8h+P3D/9qiqTFmZbQ8t9HhpIewATHvap7yQ==', 'CgExEhIwMDUzNDExNzEyMjEwMDAwMTIaATAiIgogNtyuIb+tHUOdZMfgAV8ngEVIV4QceueU0VnCKLWqlH4=');
INSERT INTO `attest` VALUES ('54', '2017-12-21 18:44:20', '2017-12-21 18:44:21.000000', '0.9', '005361171221000013', null, '005361171221000013', '005', 'bcc', 'ordersn252ec3885', '18ICnfahlszFq79H6J5pqv2uFu/qpxjJfIC32HIUh52eb2Tbo2sJK7TSHTKp1GBlDqCoSXqTuoohDSsfNGzcOw==', 'ZHJL', '1', '2017-12-21 18:44:29.843000', '1', 'qS4UTb7cedQMV5wdTDLGQ6wXH7U1L9Kx4SvMgdukZouwz8XpN', '0496e1f7bb318c62555a05ec5670d6d498f9449b466279b99981333c2b95296ce4049dc61e7ecf10fdee7d737814bc9d8db6f0f486586c802da9a2b5b34f1ae48b', 'cgPg7N9ypLnoyDW8hJsmxQ9ZoKRgYRALagcdQI1H+KFdW/Ktn0QILe3K9GAIxvmYeSGXGxcJqie+/q6kn2++ag==', '1', '6', '测试-ordersn252ec3885', '.987', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid7', '存证人dc68', 'agentae9c7', '7001412', '3d89e0c1d61@.com', '2017-12-21 18:44:21.084000', '2017-12-21 18:44:21.078000', '2017-12-25 18:44:21', '4', '描述:测试-ordersn252ec3885ordersn252ec3885', '200', '2017-12-21 18:44:09.000000', 'vNnj33Ir6UOHEdzSHKPU70E0Kiq22b6PuFSLF4DrsFTKJBvcACoVcAIBEHLAI3Hk7y+N0KBGQAs0ZXC5Q/6mwQ==', 'CgExEhIwMDUzNjExNzEyMjEwMDAwMTMaATAiIgogwbPb7bXwoxwnMKvRrBMp1zKwCQVQUGduPdIuHh1Hz9Y=');
INSERT INTO `attest` VALUES ('55', '2017-12-21 20:56:18', '2017-12-21 20:56:18.000000', '0.9', '005321171221000014', null, '005321171221000014', '005', 'mrWW', 'ordersnefb9607c4', '5YSnLlYmk5cNaLBP8q9nyYP752b0f88e1JV9Bucr+SLm2k1/W7FGNu7N8mFHmEsJA7ql8vXojkPFmgsPNK6cVg==', 'ZHJL', '1', '2017-12-21 20:56:44.164000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'UiXNy6m6vugnATwBWRjACcF0mp9ejL72rm8ljJg9Pq6oIW4E3oJ7Rtbd4xeaFm7fhaqMMPCKpgmIfvdNNKhGxA==', '1', '2', '测试-ordersnefb9607c4', '.225', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid9', '存证人7037', 'agenta830c', '8724481', '6cb90cd65c7@.com', '2017-12-21 20:56:33.168000', '2017-12-21 20:56:32.866000', '2017-12-23 20:56:33', '2', '描述:测试-ordersnefb9607c4ordersnefb9607c4', '200', '2017-12-21 20:56:16.000000', 'HeCwzxNAvjmKzbS3/+F+txUIswmcrWce4yNNhO81zhFpcdD3h7kCqWQQ3Lg7nnNejagCQdZM09Zo+oK6A3P58w==', 'CgExEhIwMDUzMjExNzEyMjEwMDAwMTQaATAiIgog5U1002tK3S/rTSyVdbWFEpBJuPxVqnKNVIj313nlNiw=');
INSERT INTO `attest` VALUES ('56', '2017-12-21 20:58:41', '2017-12-21 20:58:43.000000', '0.9', '005361171221000015', null, '005361171221000015', '005', 'mrWW', 'ordersnb4a52ceca', 'Gjb573PLtoLLtAVVU68ag/nC4K/x/hxT4BHlTVOwfKGXJyU6dyR1TgjnQBom6BwGpkLFyKESHwe+LuRHUSbwLw==', 'ZHJL', '1', '2017-12-21 20:58:43.051000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'SvaV4z8efmE8BEuAZx3Vt2aGO3RngkB81p9WY+tXgaSK38QDFeREpDXhFLTF8uYrts0d+SwvaQFGXDBtJMqKMw==', '1', '6', '测试-ordersnb4a52ceca', '.811', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid2', '存证人38a4', 'agentc3ab0', '9562624', '4ae7373107d@.com', '2017-12-21 20:58:43.145000', '2017-12-21 20:58:42.871000', '2017-12-25 20:58:43', '4', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"4ae7373107d@.com\",\"agentName\":\"agentc3ab0\",\"agentPhone\":\"9562624\",\"b', '200', '2017-12-21 20:58:40.000000', 'bdS3bgfh+yZOD23FxgOm8m3E4V32DM0+NOj7zR7svZruB/yBY/QZvOGzoqeQiOt1Qt2NfeOErlBAYoxzvw/mlw==', 'CgExEhIwMDUzNjExNzEyMjEwMDAwMTUaATAiIgognflz20NoCv5PqfnxwdOlrehIFfo43O5tjgYxXOJwgik=');
INSERT INTO `attest` VALUES ('57', '2017-12-21 21:08:42', '2017-12-21 21:09:57.000000', '0.9', '005331171221002608', null, '005331171221002608', '005', 'mrWW', 'ordersnf264a7bc7', '80G9RsCl93C52xSuFm7UWdH59W1I+zzHl03elWCPgE4zPoPppGEKMjzs2XfU8hogLjpW2za5Yt+ZmdZV11xUIw==', 'ZHJL', '1', '2017-12-21 21:09:57.329000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'me5L7op4v9sExxeERS/wvgRu61r0FM+DtN0vYmGMjUtuucW7pWenU2Ap3ItkudAmOYJ6MDiHaooVORfLW2O9jA==', '1', '3', '测试-ordersnf264a7bc7', '.564', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid11', '存证人18de', 'agent33cd7', '6972924', '7ef3b66a309@.com', '2017-12-21 21:09:57.341000', '2017-12-21 21:09:57.318000', '2017-12-23 21:09:57', '2', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"7ef3b66a309@.com\",\"agentName\":\"agent33cd7\",\"agentPhone\":\"6972924\",\"b', '200', '2017-12-21 21:08:41.000000', 'mPb/y9pBbHPcooCFdFc6z73YLdOv9nOyq4Fv231IpRY6Ko4pW/QCh5P62OJhsZnHGxeCDz5AcSKQZmwILZsJNg==', 'CgExEhIwMDUzMzExNzEyMjEwMDI2MDgaATAiIgogeTTXKqOA53+3BXLEbXugwqUkZSZoiiZ5d+vOgROwy9s=');
INSERT INTO `attest` VALUES ('58', '2017-12-21 21:09:44', '2017-12-21 21:09:58.000000', '0.9', '005361171221002609', null, '005361171221002609', '005', 'mrWW', 'ordersnc097889c9', 'TFI07JWYygRWht1cABuLRJT/sCwkyJLbzAc81n4aaOL0XwFmf0h1qQKY3a2eRsZ81mOBYBzVSyY6CC6rsPd7Zg==', 'ZHJL', '1', '2017-12-21 21:09:57.964000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', '7sT5hzKpj9UwG4ceayZaM5Dp45shaGNmzjpizVHR6VqGJ3p4ubP4jXvD3Hkc341B/7BmJDCrOjXE9Qfw9jF/9g==', '1', '6', '测试-ordersnc097889c9', '.a52', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid3', '存证人4e4e', 'agent15e61', '8642681', '61d9b0378c0@.com', '2017-12-21 21:09:57.967000', '2017-12-21 21:09:57.956000', '2017-12-23 21:09:58', '2', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"61d9b0378c0@.com\",\"agentName\":\"agent15e61\",\"agentPhone\":\"8642681\",\"b', '200', '2017-12-21 21:09:43.000000', 'Phn+wrNiUpFiLxZk6rFI54ezH6p9/58rXe4kVBCmayWiYhbGoQWKSxE3sQhmkkY4AQFggTvjR2qJqlrR9Jckjw==', 'CgExEhIwMDUzNjExNzEyMjEwMDI2MDkaATAiIgogtqb1eDQeKQgJcU7ce9MF4Y4Bgd65dxPLALxXYsE6k3s=');
INSERT INTO `attest` VALUES ('59', '2017-12-21 21:18:44', '2017-12-21 21:19:58.000000', '0.9', '005341171221002658', null, '005341171221002658', '005', 'mrWW', 'ordersn5d05fabea', 'Cj7fpPUnfHzMT0LjwW7IYAJDLTND9nvgX2YBCaRENIjaGxg1F9RUfKvB8H5Sz6M4JUIxyX+wwrjuvPbp9kzzSA==', 'ZHJL', '1', '2017-12-21 21:19:58.293000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'V+uA9csq6tWMrT6lxznEgxLbNdGhbGSEB8mvWSU0hVXSwuWpMa6uhidAz8Qjrv9Uvw2g55A5vuEAWynd4vnZlQ==', '1', '4', '测试-ordersn5d05fabea', '.a8b', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid2', '存证人d361', 'agent05dce', '4084985', '194e1119623@.com', '2017-12-21 21:19:58.305000', '2017-12-21 21:19:58.283000', '2017-12-26 21:19:58', '5', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"194e1119623@.com\",\"agentName\":\"agent05dce\",\"agentPhone\":\"4084985\",\"b', '200', '2017-12-21 21:18:43.000000', 'v1ULQ+BRSkcBsJJj3zyC7v95cbJXfKERHctUdJGsQAglSQnRRpnvPD73R72AmWpoFpActHuifncV8/Wu8qFYPQ==', 'CgExEhIwMDUzNDExNzEyMjEwMDI2NTgaATAiIgogY5Buf5+iPijdIxYQPOh+9NQbmNDFDuBqJPgPx04YWk8=');
INSERT INTO `attest` VALUES ('60', '2017-12-22 11:21:19', '2017-12-21 21:28:02.000000', '0.9', '005341171221002659', null, '005341171221002659', '005', 'mrWW', 'ordersn608770e19', 'r6mMN8fJeAfU/QV2LFQxGT0qavUOnDrKuBMSKEgzveqcAfuCWzH/T6Oqhwja8bF4aWe8qXXjNulqyJZ7FJPI6w==', 'ZHJL', '1', '2017-12-21 21:28:01.940000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'jJSjgsekMw01efns1V0pfrghPe2ilMfIgY5dU4WfBRH9BE4E2LiDCvL/GQDwItcq+Ihz8YvpX1FBTKNgBjKH9g==', '1', '4', '测试-ordersn608770e19', '.bdb', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid5', '存证人9b0f', 'agent0e2f3', '847402', 'c1f099c8971@.com', '2017-12-21 21:28:01.944000', '2017-12-21 21:28:01.931000', '2017-12-22 21:28:02', '1', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"c1f099c8971@.com\",\"agentName\":\"agent0e2f3\",\"agentPhone\":\"847402\",\"bi', '200', '2017-12-21 21:28:00.000000', 'lchZKtm3p/qita+4NoKGg5z1CHYyZW1zjXX1PFQZQPfbHxMm8H2/1x+NFyV9sDkeaXLMYe/nb9Zizto8Mhw9iw==', 'CgExEhIwMDUzNDExNzEyMjEwMDI2NTkaATAiIgogtg6HafS45457Ybr2ZkYNBjgzJw0lWFh/+4nYzYA5/XA=');
INSERT INTO `attest` VALUES ('61', '2017-12-22 11:21:54', '2017-12-21 21:28:34.000000', '0.9', '005351171221002660', null, '005351171221002660', '005', 'mrWW', 'ordersn1d17addcd', 'b6ZMofXPaZtNdT+LINfizO8MHKb5uN7Hn9AP3khNxmz9JuKKErc3r04Zbr320icHcXLAlVZZLSI1YAFMimxiDg==', 'ZHJL', '1', '2017-12-21 21:28:33.579000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'LlVPDYw2c/o6yVX+AFO/Lp+gTteQvhDg5HgieFvhtN91bgOlXnZbuKXu0cBE1df3eEgaf4Hjk0AGdxQFK+eUgg==', '1', '5', '测试-ordersn1d17addcd', '.165', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid6', '存证人0090', 'agentaea5b', '7397838', '1bb0bd64961@.com', '2017-12-21 21:28:33.582000', '2017-12-21 21:28:33.574000', '2017-12-23 21:28:34', '2', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"1bb0bd64961@.com\",\"agentName\":\"agentaea5b\",\"agentPhone\":\"7397838\",\"b', '200', '2017-12-21 21:28:32.000000', 'VXS9Y8wtfQ4KUThfBLu7ibfaC3sGf0rs8YkV82QjJAqr3Zo1p1Dk933i/09+Qq6BbP3bx8xwaywiL4Mih9LqHw==', 'CgExEhIwMDUzNTExNzEyMjEwMDI2NjAaATAiIgogN9SwcHHOROzlLdeW4AiW/fM9iUvPsJ+G94DuSYEtHEA=');
INSERT INTO `attest` VALUES ('62', '2017-12-22 11:22:28', '2017-12-21 21:30:59.000000', '0.9', '005331171221002661', null, '005331171221002661', '005', 'mrWW', 'ordersn83189e5ac', '//FXo6dVEWpfpk8GUQbpn3yoAk+zyZQwySLtXz33uEbypJZM/vRjR3T4QTrZ3YrtMbOAXxMXr+c1+MANLUicpQ==', 'ZHJL', '2', '2017-12-21 21:30:59.473000', '2', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'awizBkIFmZoohIRwcnGmVGqbp6oMllVAUXbLoc719kgKleizw30w6c3cLbOG+WKRgzyAdXv+ORv9nVsoCIcuJw==', '1', '3', '测试-ordersn83189e5ac', '.5f9', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid4', '存证人4ae4', 'agentcad26', '2789174', 'c8c24b0b898@.com', '2017-12-21 21:30:59.477000', '2017-12-21 21:30:59.467000', '2017-12-22 21:30:59', '1', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"c8c24b0b898@.com\",\"agentName\":\"agentcad26\",\"agentPhone\":\"2789174\",\"b', '200', '2017-12-21 21:30:57.000000', 'h/xeiqCQ/bHy7kCXbhp4wBZ9dA/XAbIphUf0eYr1tTGM1La2jvnNTjr1We2j7AVY075WsFogCVLceHmHBgm9XQ==', 'CgExEhIwMDUzMzExNzEyMjEwMDI2NjEaATAiIgogWWKXCmGyoW3MhAhGweeaL181lDnLAX9mk/TMeyQgTKA=');
INSERT INTO `attest` VALUES ('63', '2017-12-22 11:10:29', '2017-12-22 09:20:27.000000', '0.9', '005331171221002661', '005331171221002661', '005331171222000001', '005', 'mrWW', 'ordersnc76c93820', 'fUgiUvu5aTZvwH5hVxHzvo52kNObeiKKeXK3B+wq4hayM71f3QUKE1W75KT0vPQxADdmv1UV3IhOzO+PUY+/qw==', 'ZHJL', '2', '2017-12-22 09:20:32.158000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'yFbA9FlZvlp8n3BAmRi0v7fAFkXzbid0dMm6MyTuvAZVOnN4B4q5U1DSmasjnmYZR/vw6Jg+4zEyY+BQuV8wEw==', '1', '3', '测试-ordersn83189e5ac', '.5f9', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid4', '存证人4ae4', 'agentcad26', '2789174', 'c8c24b0b898@.com', '2017-12-22 09:20:32.179002', '2017-12-22 21:30:59.000000', '2017-12-27 21:30:59', '5', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"c8c24b0b898@.com\",\"agentName\":\"agentcad26\",\"agentPhone\":\"2789174\",\"b', '200', '2017-12-22 09:20:25.000000', 'oQBxOGbeEHNwUG/RO8HkQBPtFrK/saBr0ZUhZKVks/A07Z5ka41n25aWgGobRvjlOyp2/ZFVvQSNiBhv+2LOiw==', null);
INSERT INTO `attest` VALUES ('64', '2017-12-22 11:10:44', '2017-12-22 09:23:33.000000', '0.9', '005331171221002661', '005331171222000001', '005331171222000002', '005', 'mrWW', 'ordersnf032f0bb8', 'CeQZfdFsYh36kWN6mYszDhFjrkqigppGChM0JwL6VzADgnWuMINv7PsFP0jwFYyT7a8DVWDTdoYSEYHXTqa27w==', 'ZHJL', '2', '2017-12-22 09:23:34.545000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'QX3g8P3KubiUVsEFb+JxmX9ZqiAMEwpJ8g6wHxHt0DHfcBjnaGPeBG1L5TRMhSUHR+3aVM6CbXz74KcpCbovvw==', '1', '3', '测试-ordersn83189e5ac', '.5f9', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid4', '存证人4ae4', 'agentcad26', '2789174', 'c8c24b0b898@.com', '2017-12-22 09:23:34.565002', '2017-12-27 21:30:59.000000', '2017-12-29 21:30:59', '2', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"c8c24b0b898@.com\",\"agentName\":\"agentcad26\",\"agentPhone\":\"2789174\",\"b', '200', '2017-12-22 09:23:32.000000', 'Kbwj0PluPq7ViKB0w94PfmFzIxAH+Et2AwdGtaniTjV9i1x7aEjQy6+n/rDUYGN0n21WR3u7iXYfs9O/ovZkIQ==', null);
INSERT INTO `attest` VALUES ('65', '2017-12-22 11:10:44', '2017-12-22 09:24:49.000000', '0.9', '005331171221002661', '005331171222000002', '005331171222000003', '005', 'mrWW', 'ordersn4f96f4cd7', '28mjD+uykp0B8GzxVJ9LSsBN88YhO7yGgeiz/NQ4qpDAqKkVnPVh9EOGa7ZC82py4Q1w8HLxo4jcZ3hkn0tcfQ==', 'ZHJL', '2', '2017-12-22 09:24:49.450000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', '46HxjPYsePgsTjJ0hzTWnBiJ1wSjzoLpLqQbal5FRJJ3eVQa47eIYxItP+yy6tHmPChXCw71eA+RreVmwHXS+g==', '1', '3', '测试-ordersn83189e5ac', '.5f9', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid4', '存证人4ae4', 'agentcad26', '2789174', 'c8c24b0b898@.com', '2017-12-22 09:24:49.450002', '2017-12-29 21:30:59.000000', '2018-01-03 21:30:59', '5', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"c8c24b0b898@.com\",\"agentName\":\"agentcad26\",\"agentPhone\":\"2789174\",\"b', '200', '2017-12-22 09:24:47.000000', '249IpwqRk/+rmTQRqHbkZFaGTaMKDhoOdQnY9+9beTxQMrKrYNkI55rbsSP2fXEW3Lpo+XWckFFZIIhUIQge6g==', null);
INSERT INTO `attest` VALUES ('66', '2017-12-22 11:10:45', '2017-12-22 09:29:03.000000', '0.9', '005331171221002661', '005331171222000003', '005331171222000180', '005', 'mrWW', 'ordersn82c82bd80', 'nuSsmuuE8xpMaFHzlU8/BEX0L/1g20CB686Y6zmec2XFexdWHL99H1MvVNdCBIegT+OHOmtr3iLBKpDe+egNdA==', 'ZHJL', '2', '2017-12-22 09:30:21.151000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'xptPBUUUQOp2uJcS5Afe2fg5aEppam1t0RXkQtwIEfZgTmVZ+E3U+3omuQ36UAaW3XytE7SEgd2DvDJkmBTPVQ==', '1', '3', '测试-ordersn83189e5ac', '.5f9', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid4', '存证人4ae4', 'agentcad26', '2789174', 'c8c24b0b898@.com', '2017-12-22 09:30:21.171002', '2018-01-03 21:30:59.000000', '2018-01-07 21:30:59', '4', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"c8c24b0b898@.com\",\"agentName\":\"agentcad26\",\"agentPhone\":\"2789174\",\"b', '200', '2017-12-22 09:29:02.000000', 'znGmlzPHkBaJy/a2jWd6a0wTZfSAe5nt/DJvTc8ipWzJbXLUFZ29U6lqHNxlXzbg3eVSAH1/VvtqfotF5JmnkQ==', null);
INSERT INTO `attest` VALUES ('67', '2017-12-22 11:22:34', '2017-12-22 09:30:56.000000', '0.9', '005331171221002661', '005331171222000180', '005331171222000181', '005', 'mrWW', 'ordersndd66e97ef', 'HEIoTm4WlNH5e+HI4+rLovzpoeFRbT0CFbbHGW8VLYD4UgU79UMYXsW3deSxJC/EkEjHMGTnQJrNkwnFHrKQWw==', 'ZHJL', '2', '2017-12-22 09:30:57.203000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'ErjniW1Cx6SbiBnx62ShAPTODw/6DGEClSP7pglNZDYHivj40HP7r70aLTAHiPNiqaZX8x4MDbXobPWwMLcCKQ==', '1', '3', '测试-ordersn83189e5ac', '.5f9', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid4', '存证人4ae4', 'agentcad26', '2789174', 'c8c24b0b898@.com', '2017-12-22 09:30:57.213002', '2018-01-07 21:30:59.000000', '2018-01-09 21:30:59', '2', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"c8c24b0b898@.com\",\"agentName\":\"agentcad26\",\"agentPhone\":\"2789174\",\"b', '200', '2017-12-22 09:30:55.000000', 'jp/Xi6BIWw9qMQvo61s8KyDKCxFJJx5WK0ZXLloDunZ1S6wjPTwCiHUY1UvXraQ1kqDqjv1FTFCoMJPQHGVTGw==', null);
INSERT INTO `attest` VALUES ('68', '2017-12-22 11:10:47', '2017-12-22 09:38:38.000000', '0.9', '005331171221002661', '005331171222000181', '005331171222000182', '005', 'mrWW', 'ordersn3860b815c', '2BZ5up052zf95RHeo8sK8jjK1tvT4/Sg/Wf8iG9765YxTErnz4YBWhAkThWdjpwaJwgwMwC7ENnshgvNcXfbcg==', 'ZHJL', '2', '2017-12-22 09:38:39.249000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'mkigAaECMA6p+n5AkYf9aeQ//WhSXu3s9Oy1fK0Hh6NKMppfPXQiAIkEBCehd2tI1WFYH31h0bKeu58hLZB//g==', '1', '3', '测试-ordersn83189e5ac', '.5f9', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid4', '存证人4ae4', 'agentcad26', '2789174', 'c8c24b0b898@.com', '2017-12-22 09:38:39.294002', '2018-01-09 21:30:59.000000', '2018-01-11 21:30:59', '2', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"c8c24b0b898@.com\",\"agentName\":\"agentcad26\",\"agentPhone\":\"2789174\",\"b', '200', '2017-12-22 09:38:37.000000', 'KtUaxnkmlqXtiq7YJrHEcnYTFflIVkQr6DkSLupP9ypnDV9kHGUyjegrr6OywchxsaORS0L1Y6xWL5EhofCABw==', null);
INSERT INTO `attest` VALUES ('69', '2017-12-22 11:10:48', '2017-12-22 09:40:46.000000', '0.9', '005331171221002661', '005331171222000182', '005331171222000183', '005', 'mrWW', 'ordersn72a093b0d', 'Lh6GGm0zWZ67Vtz8tWXwf35wWBJ2ohcSJ9926MtFiVoPr9axGfCz1iM7UrNs6Xa7Ra7dFbH/1AX4udcZDXETcw==', 'ZHJL', '2', '2017-12-22 09:40:46.428000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'DNNZPPVOUxihrqtDK/VhDMq3ISmWGP0EzWsGjwv0rhWfrMD+A4ZHr9siXOR6ALeb+o7Ts9ht5mHSxa1Cc+enbA==', '1', '3', '测试-ordersn83189e5ac', '.5f9', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid4', '存证人4ae4', 'agentcad26', '2789174', 'c8c24b0b898@.com', '2017-12-22 09:40:46.438002', '2018-01-11 21:30:59.000000', '2018-01-16 21:30:59', '5', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"c8c24b0b898@.com\",\"agentName\":\"agentcad26\",\"agentPhone\":\"2789174\",\"b', '200', '2017-12-22 09:40:45.000000', 'OHB9Bw9bCLP3cFzSlzx39nE386GgsnYNWCHTPkP0c2bvxpkOjp5mxQjJK2d59pBAHNvptao9U9tiWXS4p0Uxbg==', null);
INSERT INTO `attest` VALUES ('70', '2017-12-22 11:10:49', '2017-12-22 09:41:50.000000', '0.9', '005331171221002661', '005331171222000183', '005331171222000184', '005', 'mrWW', 'ordersnf64d3dfc3', '3BMXJe8boE2E6xXXpxQt7X+ol9f4NZDLdH57yF90qGc0SG5hwIibCqZlJaOsfZNQ2GHgYqDmr7zMzpLy5K9ttQ==', 'ZHJL', '2', '2017-12-22 09:41:50.014000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'ztJzT4YPjgVKMLf/HH2v8FK8C4dI+T5QavhoUi+mw6biCizbcgwGMNc5jgOOmW6zBsNMMpQT9vPsTUH3Rv5O5Q==', '1', '3', '测试-ordersn83189e5ac', '.5f9', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid4', '存证人4ae4', 'agentcad26', '2789174', 'c8c24b0b898@.com', '2017-12-22 09:41:50.024002', '2018-01-16 21:30:59.000000', '2018-01-20 21:30:59', '4', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"c8c24b0b898@.com\",\"agentName\":\"agentcad26\",\"agentPhone\":\"2789174\",\"b', '200', '2017-12-22 09:41:49.000000', 'NBK1T5uPVq9p3HMCM70LYAMCzcPyJ5rdoeWVy9DztCiUVrr2ukuEwkpg/uB7RFiU0uZkbpjYnrGV5yiOgjG0dQ==', null);
INSERT INTO `attest` VALUES ('71', '2017-12-22 11:13:54', '2017-12-22 09:43:14.000000', '0.9', '005331171221002661', '005331171222000184', '005331171222000185', '005', 'mrWW', 'ordersndd88dbe1a', 'uOJIIFHTIpvmrboKpCWRDXkL1b/MqwPo+y94YzuJjWIAaseJyzDt2YogSETSq/FfdHTlejB3mGGB6lEayRnZHQ==', 'ZHJL', '2', '2017-12-22 09:43:14.845000', '2', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'gmYN2Fp+f2tLY28SHqU0nay/mP9wpPmG6feSdHKr3L6uXlfHWBdnfpD5FQYwIZsE9SGTnFvYiSGUHIrO0cFLWA==', '1', '3', '测试-ordersn83189e5ac', '.5f9', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid4', '存证人4ae4', 'agentcad26', '2789174', 'c8c24b0b898@.com', '2017-12-22 09:43:14.845002', '2018-01-20 21:30:59.000000', '2018-01-22 21:30:59', '2', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"c8c24b0b898@.com\",\"agentName\":\"agentcad26\",\"agentPhone\":\"2789174\",\"b', '200', '2017-12-22 09:43:13.000000', 'n7vGWEDumIPIxd9KYcHlU7FS5R0aafrNzw9Nb6bnBb0vYFAW8xVdwjgGZOEeT71laRiWrgrC4dcaMcAY9fEyEQ==', null);
INSERT INTO `attest` VALUES ('72', '2017-12-22 11:33:56', '2017-12-22 11:33:56.000000', '0.9', '005351171222000186', null, '005351171222000186', '005', 'mrWW', 'ordersn071cae996', 'NMXlTtaq1DTC+J581R/wRw/3TMXv3qQuy7Kx5gvEnmBU/Pdst+wftVdpT3SpSetZ3x80H64aT4xT+wgxjR8Zww==', 'ZHJL', '1', '2017-12-22 11:34:05.543000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', '12VRfkYcz9V7Q7d2YpoaHwq57YAOadsECEvr8pySvy8bP6lEPXinwsQg04+glVd/5OwkgDCjrYKOMGCc/5+DWQ==', '1', '5', '测试-ordersn071cae996', '.a10', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid11', '存证人70bc', 'agent7c830', '6796622', 'ed490509fc4@.com', '2017-12-22 11:34:03.523000', '2017-12-22 11:34:03.426000', '2017-12-23 11:34:03', '1', '描述:测试-ordersn071cae996ordersn071cae996', '200', '2017-12-22 11:33:54.000000', '3Wdzw201o/BICT814keuX5mBZv4DIaVtayGOYpHokTlj91Wrddl0pjf21b25POmLTTuo0jIMiwj7lgKChBDO9A==', null);
INSERT INTO `attest` VALUES ('73', '2017-12-22 11:34:33', '2017-12-22 11:34:34.000000', '0.9', '005351171222000186', '005351171222000186', '005351171222000187', '005', 'mrWW', 'ordersncd701d1f0', '8DW31tlshEsYSykRE9RhD8dENBlLx68pAzIzdGMkbvBHu3LL+VXcXhxORM2U79wcap+yt5K7wc/C2UCQFuG1wA==', 'ZHJL', '1', '2017-12-22 11:34:34.549000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'H2AVtxuOAY1PSQJPN3dOcQCNEQTLWHeFAWZ7SQd0ti4M3Nt/a+dPbS+aARHq2ZOAEMd86Bue0JeoOxuls3abig==', '1', '5', '测试-ordersn071cae996', '.a10', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid11', '存证人70bc', 'agent7c830', '6796622', 'ed490509fc4@.com', '2017-12-22 11:34:34.552002', '2017-12-23 11:34:03.000000', '2017-12-28 11:34:03', '5', '描述:测试-ordersn071cae996ordersn071cae996', '200', '2017-12-22 11:34:33.000000', 'o3WPT8clxo8SLA698f9jrWgdEAjbo7+KtIQapgSXBl6ip4y0UYZpTYdLk4cNQ77Yp789jlEhP9aNsgza3juDOw==', null);
INSERT INTO `attest` VALUES ('74', '2017-12-22 11:34:45', '2017-12-22 11:34:45.000000', '0.9', '005351171222000186', '005351171222000187', '005351171222000188', '005', 'mrWW', 'ordersn01f5b4a06', '4l10nF2OHI/a8OUXGwhhX687JNS3Xv7yKsthmB2jFZNRi/Rprhx9x7FZ7JXJpyekdMNwryFNAA8tHupbch6nGQ==', 'ZHJL', '1', '2017-12-22 11:34:45.454000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'psYUx45JFJr9uFi9TVoGc4wr+EUnUiO85EkVgzEqWBej6I/K0dqgykM9lw/aSCbpDwqTNBiYXdJVQViV/fogfA==', '1', '5', '测试-ordersn071cae996', '.a10', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid11', '存证人70bc', 'agent7c830', '6796622', 'ed490509fc4@.com', '2017-12-22 11:34:45.458002', '2017-12-28 11:34:03.000000', '2018-01-02 11:34:03', '5', '描述:测试-ordersn071cae996ordersn071cae996', '200', '2017-12-22 11:34:44.000000', 'lGtOjJOM5bxR4/dbvYkQDzQNakX8mW/hrafLa6QLbUFrUyLMk0r8u0eqv6QoyTROC1G3dujAC/12Z0hYNFf4wQ==', null);
INSERT INTO `attest` VALUES ('75', '2017-12-22 11:34:51', '2017-12-22 11:34:52.000000', '0.9', '005351171222000186', '005351171222000188', '005351171222000189', '005', 'mrWW', 'ordersn7dbaaf005', '8JRo0UbWA2USSfkk25WplUFYTqybathQrE02YP/E0CeKjFrPvsitLMBsjN4MmJDR/1raNJnk1g8/jfxhth2uMw==', 'ZHJL', '1', '2017-12-22 11:34:52.122000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', '6W6ALMaV0FHktzRXw+hBQpZRuV0pZzCITySyDXCWvGH8X1ANVvJD/CmCW2uyUyHI7XhEWVXiD4COIY3XRhFNmw==', '1', '5', '测试-ordersn071cae996', '.a10', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid11', '存证人70bc', 'agent7c830', '6796622', 'ed490509fc4@.com', '2017-12-22 11:34:52.126002', '2018-01-02 11:34:03.000000', '2018-01-07 11:34:03', '5', '描述:测试-ordersn071cae996ordersn071cae996', '200', '2017-12-22 11:34:51.000000', 'nle5aTanniZk6GOvnXHN6NFAjR+afsxORD2i9WSteQXVS+C04m0nz3XJm7YYefKlmS31lynDRkIVpqJ3uUr9mA==', null);
INSERT INTO `attest` VALUES ('78', '2017-12-22 15:05:51', '2017-12-22 15:05:51.295000', '0.9', '005351171222000186', '005351171222000189', '005351171222000190', '005', 'mrWW', 'ordersn6dcd29737', '2KMm29V0ihsU/VV/ZRrS2x/m1xF8aK32DyEJ3oRU8Zk29wi2IxNBpyCKK/bXis1rruY+dnytxcGWzxjpEust+g==', 'ZHJL', '1', '2017-12-22 15:05:55.739000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'Wr363bP6dB8cNTqAfe02ZUg6BoFtzl1F5el+qyjwfimrvUxO+76YllzXKaH1y5lebkDnpXTv9zgNyWs3K9+58w==', '1', '5', '测试-ordersn071cae996', '.a10', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid11', '存证人70bc', 'agent7c830', '6796622', 'ed490509fc4@.com', '2017-12-22 15:05:55.817002', '2018-01-07 11:34:03.000000', '2018-01-12 11:34:03', '5', '描述:测试-ordersn071cae996ordersn071cae996', '200', '2017-12-22 15:05:48.000000', 'mLVIs7EthN91/S5oMnZCxPO7zclc5xhiGWjEfeAXOKPYgfp9TG4I3OPrwcOLRtVZ0LTkGSP40G1tLGqlGdUmNA==', null);
INSERT INTO `attest` VALUES ('79', '2017-12-22 15:09:17', '2017-12-22 15:09:17.727000', '0.9', '005321171222000191', null, '005321171222000191', '005', 'mrWW', 'ordersn45857d608', 'rC3gEcNE9g4NY9YBHe1ikZuD7ZjT56x1qmSGhFLL3wzAogBDMwnnz+GYUYiDre1bOlvtrwHkcu3DpA5Jn+6WIA==', 'ZHJL', '1', '2017-12-22 15:09:21.772000', '-1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'Zo+RANFsi8xpMQPq+SIAG3N9u+kErUkJEfslSkUrLsJmnD9mnotV99NwS4tdc2mSt6oj9KjphRs/GacaHIH38g==', '1', '2', '测试-ordersn45857d608', '.be2', '4', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid10', '存证人c169', 'agent2b61f', '6974212', 'ed8e2059ac3@.com', '2017-12-22 15:09:18.365000', '2017-12-22 15:09:18.210000', '2017-12-25 15:09:18', '3', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"ed8e2059ac3@.com\",\"agentName\":\"agent2b61f\",\"agentPhone\":\"6974212\",\"b', '200', '2017-12-22 15:09:16.000000', '8wIKCBte/kIRXAx3ke/yLBZieXvyLHQ14scmc3yVJ6FZMG6/ecvl5l0pII+pZIsPn7pX0P4hxuQsxkW8tyNCCQ==', null);
INSERT INTO `attest` VALUES ('80', '2017-12-22 15:35:47', '2017-12-22 15:35:47.821000', '0.9', '005361171222000192', null, '005361171222000192', '005', 'mrWW', 'ordersn1eafa7fdc', 'ZYgG3HctdngOFLqhq1JrWmI/CdZgkD6DsElPEvgEm+PY1/ahWCNkDOjuE+qTvaszwHe5NCZNQp/XLSQc7pNvgQ==', 'ZHJL', '1', '2017-12-22 15:35:50.052000', '-1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'AWSJO5ixOeRZGGkLZ+Ov6hfv7avvFwZyc8DU31uvP/NGGVhKLNaAjwabHQg1STKVq+Kk1pIOyKM5b0asytrvQQ==', '1', '6', '测试-ordersn1eafa7fdc', '.67d', '4', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid7', '存证人39c9', 'agent7d51d', '1513621', 'ce8983a64a0@.com', '2017-12-22 15:35:48.408000', '2017-12-22 15:35:48.403000', '2017-12-23 15:35:48', '1', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"ce8983a64a0@.com\",\"agentName\":\"agent7d51d\",\"agentPhone\":\"1513621\",\"b', '200', '2017-12-22 15:35:46.000000', 'owr77yG5ll/CDqnzvqg44sXIv0+KiYxQF4S9Rg40bVv8a043HRfYMVcO+Y4lDxkaY2OKYtf1WXRo9Msgm8woAQ==', null);
INSERT INTO `attest` VALUES ('81', '2017-12-22 15:36:26', '2017-12-22 15:36:26.173000', '0.9', '005321171222000193', null, '005321171222000193', '005', 'mrWW', 'ordersncd9d2d13f', 'nVF1kYcow7Ezf8Ud/8DnRZvqKqoWyQiEK98DvUpaCvVTHIDqtH/XT/M1E+AusqoeV0f6ZbluYMBn09Bu+y7nfQ==', 'ZHJL', '1', '2017-12-22 15:36:28.896000', '-1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'qVg9STaJVIlY4MvRp0OMF/LYibi/8Z/EfJXqJD7VirTBlu9haCEzH6rNLvdDEFY3Qq6XiPIhW2RgtWOba4qWRg==', '1', '2', '测试-ordersncd9d2d13f', '.dbe', '4', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid11', '存证人6290', 'agentd0752', '1618205', '30d00eb1c99@.com', '2017-12-22 15:36:26.618000', '2017-12-22 15:36:26.613000', '2017-12-25 15:36:27', '3', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"30d00eb1c99@.com\",\"agentName\":\"agentd0752\",\"agentPhone\":\"1618205\",\"b', '200', '2017-12-22 15:36:24.000000', 'K3t1+ECChmoosH+RFTU2d0Hgo8llnXj1kEL5hzWF0L4D4/oedOmNyqPoQwRgRm3R1nRiFAAiYkLYDSSYCTFNxA==', null);
INSERT INTO `attest` VALUES ('82', '2017-12-22 15:38:17', '2017-12-22 15:38:17.942000', '0.9', '005351171222000194', null, '005351171222000194', '005', 'mrWW', 'ordersnf7adab306', '063CuQKl6OQHPmlx1nJJtT0eZWqDvBLM8CNdpH/RBh6yS9wf9v585tnWLdf1+/XbCy7JNqPEcqtdR8H1v6zsnQ==', 'ZHJL', '1', '2017-12-22 15:38:20.438000', '-1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'GqENhxIFyI8SNTR+Fe7z9Z/HeMmbwQZdAM5SD3vaAXa49plxv1/ns3A6b1odlubEjOD+jxo8Iz8kNB9gqwUJhA==', '1', '5', '测试-ordersnf7adab306', '.cb9', '4', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid10', '存证人bcf6', 'agent88567', '7606737', '9b35bc8407b@.com', '2017-12-22 15:38:19.163000', '2017-12-22 15:38:19.141000', '2017-12-26 15:38:19', '4', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"9b35bc8407b@.com\",\"agentName\":\"agent88567\",\"agentPhone\":\"7606737\",\"b', '200', '2017-12-22 15:38:16.000000', 'dsybkvuKWVvXASuy8GaDsXPcz3f5DizO7DClAV9PbRb4GBSTepn/dkKPW6OcDxEehmWoxXnsEC+ysAFMzOYW9g==', null);
INSERT INTO `attest` VALUES ('83', '2017-12-22 16:40:21', '2017-12-22 16:40:20.965000', '0.9', '005311171222000195', null, '005311171222000195', '005', 'mrWW', 'ordersnb098e7539', 'cxwJXRAkhVHZdcM2MaBpomuxOGlkm1zkq/NTC6IpJiF+Rgnxg58jOdRVhKL4UOru0J3hAa2iVL9x2qhoVywr8Q==', 'ZHJL', '1', '2017-12-22 16:49:39.669000', '-1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'Kjz9pn4ysFECKCvwcbMaayipmOoJN0ioFWNCLI5seZJPLA/H1Fi8ukwTi03N21Vu4vAXaHK23fjXHEf6Y8eveA==', '1', '1', '测试-ordersnb098e7539', '.b8e', '4', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid5', '存证人f1a0', 'agent7e25a', '4426259', 'f2322dd0b58@.com', '2017-12-22 16:40:28.357000', '2017-12-22 16:40:28.306000', '2017-12-23 16:40:28', '1', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"f2322dd0b58@.com\",\"agentName\":\"agent7e25a\",\"agentPhone\":\"4426259\",\"b', '200', '2017-12-22 16:40:16.000000', 'fA7fGZhQz3StwnH+VIR6iaybTV9mtp8xddrPXoODpOgwbj09yPqgLRtjSz+kCzsQqQX2zjk+2fC9N5tVpBeyzg==', null);
INSERT INTO `attest` VALUES ('84', '2017-12-22 17:11:29', '2017-12-22 17:11:29.533000', '0.9', '005341171222000196', null, '005341171222000196', '005', 'mrWW', 'ordersnf3fb76377', 'bkXG+k45Q0wR8hKoWWAUxkmODsZw9o7BOPWEa/dMrnK0jQ2oUSpp0p14JCsPQ6RFxENAoewwRbv4BL2Kn5kU1w==', 'ZHJL', '1', '2017-12-22 17:11:30.528000', '1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'GaBi0lGu+/l2DVoB85HSeAFnRTwj52Hf88UqwRtXuMjW4CAVgwdq3iLJGwdEHHgtN4UQEf9y6Xjv8F0cSlcrlA==', '1', '4', '测试-ordersnf3fb76377', '.24c', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid6', '存证人77ec', 'agente3eb4', '6284661', '7f3fafdbe31@.com', '2017-12-22 17:11:29.537000', '2017-12-22 17:11:29.528000', '2017-12-24 17:11:30', '2', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"7f3fafdbe31@.com\",\"agentName\":\"agente3eb4\",\"agentPhone\":\"6284661\",\"b', '200', '2017-12-22 17:11:28.000000', 'YQ8xtu0dT6eJ1D8w3mLC9oohG9i2FxBAnoY6Uyz7UmZVtlGlsiHOTz+9j6SBygdljHruRBqji7kKA5BQE2RTKQ==', null);
INSERT INTO `attest` VALUES ('85', '2017-12-22 17:01:11', '2017-12-22 17:01:11.993000', '0.9', '005331171222000197', null, '005331171222000197', '005', 'mrWW', 'ordersn950ee3195', 'JMEz3s8mMrRbyUrHxWv/gf6GC/Yeb8i1YSkptJ/TsAFdMETeyKpoMT4DDXTD4PE0YqqcQRV/RdC2l/5fjk7IHA==', 'ZHJL', '1', '2017-12-22 17:01:14.306000', '-1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', '7UPsehmKriR+gdLEQz83bOgMDJccYAgXsQQLLcN5+ftLl7SLnvTeu+Mx1X39EEb6HeGb7Lah+bvu15JpxYt6pA==', '1', '3', '测试-ordersn950ee3195', '.b3b', '4', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid6', '存证人cc4f', 'agentedb57', '1973728', '4f060d903ba@.com', '2017-12-22 17:01:12.766000', '2017-12-22 17:01:12.749000', '2017-12-24 17:01:13', '2', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"4f060d903ba@.com\",\"agentName\":\"agentedb57\",\"agentPhone\":\"1973728\",\"b', '200', '2017-12-22 17:01:10.000000', '9atajQeK/5IXixvf+uRPeM6yKJykxRJ7Yu2ihsbcGF16TyKbpYE7JfRVNzh7We9HdFE9FHuPs0Utgs5nV34RaA==', null);
INSERT INTO `attest` VALUES ('86', '2017-12-22 17:04:34', '2017-12-22 17:04:34.810000', '0.9', '005361171222000198', null, '005361171222000198', '005', 'mrWW', 'ordersn779668de4', 'Q4+wve35o/8I3bFDpRWcvgfYUC5cAR1SJTEtSnxdVKjGoKojBTtgvLZGgES4OiD2LOEdjsGFEDYm8zRTRLHqHQ==', 'ZHJL', '1', '2017-12-22 17:04:36.208000', '-1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'LVwUm9TGi0brG7H1jfuUgeMYqWYZUmJJ3tMNECrs5u+t+k1EGtrOw6GLREQTsPcelZB2zcOfbF8B56CivlGsgw==', '1', '6', '测试-ordersn779668de4', '.349', '4', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid10', '存证人d5c2', 'agentdd3e0', '2574234', '1a3ae9588b2@.com', '2017-12-22 17:04:34.972000', '2017-12-22 17:04:34.962000', '2017-12-25 17:04:35', '3', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"1a3ae9588b2@.com\",\"agentName\":\"agentdd3e0\",\"agentPhone\":\"2574234\",\"b', '200', '2017-12-22 17:04:33.000000', 'PGoJjQsUNDXw6m7lJE9umsURKabhZjHJfXfSthmClDNNQW/bBtQSc+bHefECW1hmdEM1bbS+1in2E5drMK8sSw==', null);
INSERT INTO `attest` VALUES ('87', '2017-12-22 17:18:52', '2017-12-22 17:18:52.184000', '0.9', '005331171222000199', null, '005331171222000199', '005', 'mrWW', 'ordersn808bd0523', 'vIO+Sii99MvwV2ZTgw/t0KJmOYQoivwrOJVWq2NxfIChUN3EF7ygfgwr5HRyzI6ODT9MZBvWuGlRoQWqMmLx3Q==', 'ZHJL', '1', '2017-12-22 17:18:54.632000', '-1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'nTFrHwVlTSgYKBFwbBvBCqGybIKmSo20wzDMgSV/cnUeoSlQmTT3uYSaAU/Say3DyCglXx1VXgx7ZwkZidh4ZQ==', '1', '3', '测试-ordersn808bd0523', '.d66', '4', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid8', '存证人1c94', 'agent9730d', '657738', '6f418eb832a@.com', '2017-12-22 17:18:52.677000', '2017-12-22 17:18:52.666000', '2017-12-27 17:18:53', '5', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"6f418eb832a@.com\",\"agentName\":\"agent9730d\",\"agentPhone\":\"657738\",\"bi', '200', '2017-12-22 17:18:51.000000', 'H0RIObLykTrO5hj+IQhhLspxfnPH4aDo+nJjIMCw+SLSmRsTBjOBlW1GcePBxjGiPgOsDFfz42uCEbNhUIsBsA==', null);
INSERT INTO `attest` VALUES ('88', '2017-12-22 17:21:20', '2017-12-22 17:21:20.861000', '0.9', '005331171222000200', null, '005331171222000200', '005', 'mrWW', 'ordersnb0aab43c0', 'axhI8gvTMh/ye425ydl8YvJvFJ/dEgf2LhCx6bUotHKrp024XAIj/v/UNHTyhaGrK41UCyQMcB2ok3nzz+5ebA==', 'ZHJL', '1', '2017-12-22 17:21:21.958000', '-1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'X+Juwmd8dprTgp8dQIsnpJNiK2h8WOuC2OH5pduPsmlZxvygfqgYd+vw2NZyPZF7qnFinedzeLxcrHAoVaHTIA==', '1', '3', '测试-ordersnb0aab43c0', '.d27', '4', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid11', '存证人8cf4', 'agent10f45', '1682160', '352f425e37e@.com', '2017-12-22 17:21:21.192000', '2017-12-22 17:21:21.187000', '2017-12-23 17:21:21', '1', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"352f425e37e@.com\",\"agentName\":\"agent10f45\",\"agentPhone\":\"1682160\",\"b', '200', '2017-12-22 17:21:19.000000', '5YtEzeZ776G4Qsc44M9cGk1dKVeSjJfPwqrpqCyfElzDzkbGoeEiSDq6tlNxMGxP0z5RaJfVM601CM0w8o9Pxw==', null);
INSERT INTO `attest` VALUES ('89', '2017-12-22 17:24:41', '2017-12-22 17:24:41.027000', '0.9', '005351171222000201', null, '005351171222000201', '005', 'mrWW', 'ordersn0dec96f94', 'cIfAlC0MMy8wjy9eMdI6hI+IxVtGMAYQXg8UXZtc46eEVDMF7FS5C1my+2ijeGq7rc/W0ncUASIYzhLLbHr14w==', 'ZHJL', '1', '2017-12-22 17:24:42.202000', '-1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'XAEsmRzsmV/EGYMxum3yvCbg7+YzXNHsD+mJ8tO1RtxX2dTlZWeThDeGr48D+w3byyRKQ/hQyuJCjp8OaDpJNg==', '1', '5', '测试-ordersn0dec96f94', '.33f', '4', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid3', '存证人b543', 'agentfe90a', '831201', 'd536858888b@.com', '2017-12-22 17:24:41.343000', '2017-12-22 17:24:41.334000', '2017-12-23 17:24:41', '1', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"d536858888b@.com\",\"agentName\":\"agentfe90a\",\"agentPhone\":\"831201\",\"bi', '200', '2017-12-22 17:24:39.000000', 'pBjBzqGE2ZVGwJ7gbvIRf1bI5yBqrSYjhShKA+6/LR2awXE4krgPPgXduQGSQXIZxSAvRVuFecu1XkmTewT1Nw==', null);
INSERT INTO `attest` VALUES ('90', '2017-12-22 17:25:07', '2017-12-22 17:25:06.986000', '0.9', '005351171222000202', null, '005351171222000202', '005', 'mrWW', 'ordersn816825c14', 'LyCjKNoKfG02GqH9xMocmeMjMpbxI00811q6oqK9ltH4Cs4obfgTnQYe6ZOETdegVOeD1CNJjmAjfaDFV2zOLQ==', 'ZHJL', '1', '2017-12-22 17:25:07.853000', '-1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'eyJM4mcOsMf7w1hKW5nw0GejNCqKgGx3i7l7BdemItOJWnVJ+AO60BDUksvxWfPbsQfCKIf+mKSNYwTxjpkOOw==', '1', '5', '测试-ordersn816825c14', '.8e0', '4', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid1', '存证人9d0e', 'agent1e4d2', '285582', '6e55898bd15@.com', '2017-12-22 17:25:07.277000', '2017-12-22 17:25:07.271000', '2017-12-23 17:25:07', '1', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"6e55898bd15@.com\",\"agentName\":\"agent1e4d2\",\"agentPhone\":\"285582\",\"bi', '200', '2017-12-22 17:25:05.000000', '+X35w1Tj6qS0m6g/RhZIKLbkrk/Tbr7PMaEcTeS4hyG178sx9LYSGjVB+Fm5VYwwExKWSKK+pKlpP6hH0VIeJQ==', null);
INSERT INTO `attest` VALUES ('91', '2017-12-22 17:27:46', '2017-12-22 17:27:46.720000', '0.9', '005341171222000203', null, '005341171222000203', '005', 'mrWW', 'ordersn28716ed3b', 'somj9NivTRt7cf7pSSD3ibXm5r+L7U7znrXWKqBH3kEZczVC9iaP3ySAyHzBo7SGqp6KSsaV35YG7j5cIqxIHw==', 'ZHJL', '1', '2017-12-22 17:27:47.658000', '-1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', '9+GYO5uOz6fMcIo3SMMRG1aRJhnB9vf6yM/SqwuwmBf/BB3vVVqys9RbOmze8lPs338VH/vzOBSXJHwzNEOKVg==', '1', '4', '测试-ordersn28716ed3b', '.c97', '4', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid5', '存证人b6f8', 'agent45124', '5932064', '3c01622fd51@.com', '2017-12-22 17:27:47.044000', '2017-12-22 17:27:47.038000', '2017-12-27 17:27:47', '5', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"3c01622fd51@.com\",\"agentName\":\"agent45124\",\"agentPhone\":\"5932064\",\"b', '200', '2017-12-22 17:27:45.000000', 'Bbn791i2DJF5H0ybvS/t+erssYGK37ymy57K4hNye+SE9hUsPSx3CiSFDipO+xBwsv9vA2YII8lWR5OEKxoEJg==', null);
INSERT INTO `attest` VALUES ('92', '2017-12-22 17:30:29', '2017-12-22 17:30:29.691000', '0.9', '005361171222000204', null, '005361171222000204', '005', 'mrWW', 'ordersn71f2a1d1e', '/Z3KY7u1W7OKpeJqchM/NdfcB1eFkYGQcfqUmkcBDIHm0r0A6+3NsC0YwexOnClC1ObqwEYm+VUqv70pqEgZzQ==', 'ZHJL', '1', '2017-12-22 17:30:30.695000', '-1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'Ktue3BX5t1d5ObNos+qABFKK8agVx7rpRGnza3om+5zA7c1EFSObctn8BEdRyurf12nSfcVWV4qByJp2B9GvGA==', '1', '6', '测试-ordersn71f2a1d1e', '.87f', '4', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid7', '存证人ae71', 'agent88151', '4355627', '861a309a0ac@.com', '2017-12-22 17:30:30.044000', '2017-12-22 17:30:30.036000', '2017-12-23 17:30:30', '1', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"861a309a0ac@.com\",\"agentName\":\"agent88151\",\"agentPhone\":\"4355627\",\"b', '200', '2017-12-22 17:30:28.000000', 'RcyKS08SvIsGNYGnxLqTP3/W+wmEg+3CoVfmIU8K8+N1fIX3A6mJGzTboHBxjbdClhAcFCAeDsUYjhCzm1qlmg==', null);
INSERT INTO `attest` VALUES ('93', '2017-12-22 17:32:43', '2017-12-22 17:32:43.244000', '0.9', '005351171222000205', null, '005351171222000205', '005', 'mrWW', 'ordersne42192fa4', 'GVjuB2k70OITlDp+/hSkMD5/Dopem1s1lSs6g6XaIiu8WP81FVTxOnnd9Qr64O1vpVs/9o5zFY/kPrSmynqIwg==', 'ZHJL', '1', '2017-12-22 17:32:43.885000', '-1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'FsRuW9TOjDzhryNHzxlPcbQigAlrmBPvbTMyG7Z8XiOl0WUpfhUKNWJzJPy3NDOHXzfFcBiey3pdFe9AA9LoQA==', '1', '5', '测试-ordersne42192fa4', '.83a', '4', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid4', '存证人eabc', 'agent8330b', '9817468', 'bf5cd582334@.com', '2017-12-22 17:32:43.553000', '2017-12-22 17:32:43.547000', '2017-12-26 17:32:44', '4', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"bf5cd582334@.com\",\"agentName\":\"agent8330b\",\"agentPhone\":\"9817468\",\"b', '200', '2017-12-22 17:32:42.000000', 'Y9mXC3kiDSqeDiWdVnSSWy+9V9FFIt9eMl9Hxgrz9YC8WMznal4fVYP7PrNQn+zyTT6AmIJGu3TTr5WXQO9cuA==', null);
INSERT INTO `attest` VALUES ('94', '2017-12-22 17:36:18', '2017-12-22 17:36:18.006000', '0.9', '005351171222000206', null, '005351171222000206', '005', 'mrWW', 'ordersna98aa4c00', 'D1ZZxcMPOAayse6BlmzwTMKkmnpdnfJRJ6rT93hpbPiNZjUn1Qpy174jri5MAINzMiow3IdYndS6y8eC6/jSGw==', 'ZHJL', '1', '2017-12-22 17:36:19.008000', '-1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', 'QvU5RW9Wo0/Rq8XTk8xZ6GEuk17tsdUWlp2P55iBXp8yIL79t0KpKxM2bQzP/BaCDWbHyoPXMiRnMUo68iM1sQ==', '1', '5', '测试-ordersna98aa4c00', '.5f4', '4', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '2', 'ownerid4', '存证人f3d0', 'agente7ddc', '3545187', '1b4008beff3@.com', '2017-12-22 17:36:18.409000', '2017-12-22 17:36:18.405000', '2017-12-24 17:36:18', '2', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"1b4008beff3@.com\",\"agentName\":\"agente7ddc\",\"agentPhone\":\"3545187\",\"b', '200', '2017-12-22 17:36:17.000000', 'uc/FEDQRO1HKUfse8j55CUHWaWwb0cvj8Z5W74qm0TRp+P7jHnLu7smxw4fwIaC5AZ1yG3SXRV8erspFF2jEJw==', null);
INSERT INTO `attest` VALUES ('95', '2017-12-22 17:37:11', '2017-12-22 17:37:11.168000', '0.9', '005311171222000207', null, '005311171222000207', '005', 'mrWW', 'ordersnc0c1fdee2', 'IBlMUJFLk4bB43pfS3BVyApUxd6LpZObZobokMZWn9LRZuDjQQa5/q7gneYaKzqvTPsYKoA4SWNCHFIz27zryQ==', 'ZHJL', '1', '2017-12-22 17:37:12.373000', '-1', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', '0cq8LjS69gvk/VMCkSIG/OeWNzadZJTMvMGr7UjQQDZVjChPlWIbekjtKFGJWKsJaJmzNTcH3dFg5nFGRzHkBw==', '1', '1', '测试-ordersnc0c1fdee2', '.f17', '4', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, '1', 'ownerid3', '存证人8824', 'agent7f14a', '6872882', '3a027c47204@.com', '2017-12-22 17:37:11.555000', '2017-12-22 17:37:11.550000', '2017-12-27 17:37:12', '5', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"3a027c47204@.com\",\"agentName\":\"agent7f14a\",\"agentPhone\":\"6872882\",\"b', '200', '2017-12-22 17:37:10.000000', 'Pu3HgqgDnN9HY+M3RTGE6rm62UC7eXVOm6pXWOnbCq3RdYn5C/icWjPQa3Y54083bNqaf/Bf2AN1nHtBBS6bIA==', null);

-- ----------------------------
-- Table structure for attest_chained
-- ----------------------------
DROP TABLE IF EXISTS `attest_chained`;
CREATE TABLE `attest_chained` (
  `id` int(5) NOT NULL AUTO_INCREMENT COMMENT '上链订单Id',
  `ordersn` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '知金链存证平台存证号',
  `txid` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '上链请求标识',
  `send_time` timestamp(6) NULL DEFAULT NULL,
  `refresh_time` timestamp NULL DEFAULT NULL COMMENT '订单更新时间',
  `create_time` timestamp(6) NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT '订单创建时间',
  `state` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '处理结果',
  `num` int(5) DEFAULT NULL COMMENT '异常次数',
  `remark` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注',
  `channel_ordersn` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_attest_chained_id` (`id`),
  UNIQUE KEY `idx_attest_chained_ordersn` (`ordersn`),
  UNIQUE KEY `idx_attest_chained_txid` (`txid`)
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of attest_chained
-- ----------------------------
INSERT INTO `attest_chained` VALUES ('43', '005361171221000005', 'accae9fb0037437b', '2017-12-21 21:31:06.211000', '2017-12-21 21:33:06', '2017-12-21 14:13:50.000000', '0', '242', null, 'ordersne683cc273');
INSERT INTO `attest_chained` VALUES ('44', '005311171221000006', 'ad6fb6aa643b48b0', '2017-12-21 21:32:10.185000', '2017-12-21 21:34:10', '2017-12-21 14:21:23.000000', '0', '221', null, 'ordersnf67cb2df4');
INSERT INTO `attest_chained` VALUES ('45', '005311171221000007', '9c33b61c0c564773', '2017-12-21 21:32:52.944000', '2017-12-21 21:34:53', '2017-12-21 14:22:27.000000', '0', '220', null, 'ordersn9c2909b51');
INSERT INTO `attest_chained` VALUES ('46', '005311171221000008', '61675577dc684a84', '2017-12-21 21:30:01.838000', '2017-12-21 21:32:02', '2017-12-21 15:32:57.000000', '0', '153', null, 'ordersn52e6bfb60');
INSERT INTO `attest_chained` VALUES ('47', '005341171221000010', '6fa2efd7dcf546d4', '2017-12-21 21:33:14.377000', '2017-12-21 21:35:14', '2017-12-21 17:04:00.000000', '0', '101', null, 'ordersn4f8aebf63');
INSERT INTO `attest_chained` VALUES ('48', '005321171221000011', 'd116dae9afee45bc', '2017-12-21 21:33:35.827000', '2017-12-21 21:35:36', '2017-12-21 17:04:21.000000', '0', '101', null, 'ordersne6ea58029');
INSERT INTO `attest_chained` VALUES ('49', '005341171221000012', '4d7f375cbbd44ed7', '2017-12-21 21:34:18.668000', '2017-12-21 21:36:19', '2017-12-21 18:44:00.000000', '0', '57', null, 'ordersnfce718ad5');
INSERT INTO `attest_chained` VALUES ('50', '005361171221000013', '7641f4bd833e4265', '2017-12-21 21:31:27.571000', '2017-12-21 21:33:28', '2017-12-21 18:44:30.000000', '0', '56', null, 'ordersn252ec3885');
INSERT INTO `attest_chained` VALUES ('51', '005321171221000014', 'dcdc378c4b93495e', '2017-12-21 21:30:23.192000', '2017-12-21 21:32:23', '2017-12-21 20:56:45.000000', '0', '9', null, 'ordersnefb9607c4');
INSERT INTO `attest_chained` VALUES ('52', '005361171221000015', '3a7e383d01ba46aa', '2017-12-21 21:33:57.267000', '2017-12-21 21:35:57', '2017-12-21 20:58:44.000000', '0', '9', null, 'ordersnb4a52ceca');
INSERT INTO `attest_chained` VALUES ('53', '005331171221002608', 'c492e73cacc24ef0', '2017-12-21 21:35:02.981000', '2017-12-21 21:37:03', '2017-12-21 21:09:58.000000', '0', '6', null, 'ordersnf264a7bc7');
INSERT INTO `attest_chained` VALUES ('54', '005361171221002609', '6a8b3290f343466b', '2017-12-21 21:29:40.602000', '2017-12-21 21:31:41', '2017-12-21 21:09:58.000000', '0', '5', null, 'ordersnc097889c9');
INSERT INTO `attest_chained` VALUES ('55', '005341171221002658', 'c45bfaf67b5a4b0a', '2017-12-21 21:32:31.579000', '2017-12-21 21:34:32', '2017-12-21 21:19:59.000000', '0', '3', null, 'ordersn5d05fabea');
INSERT INTO `attest_chained` VALUES ('56', '005341171221002659', '0d35a65afa91437b', '2017-12-21 21:30:44.948000', '2017-12-21 21:32:45', '2017-12-21 21:28:02.000000', '0', '1', null, 'ordersn608770e19');
INSERT INTO `attest_chained` VALUES ('57', '005351171221002660', 'a9bcbb26f3e3407c', '2017-12-21 21:31:48.896000', '2017-12-21 21:33:49', '2017-12-21 21:28:34.000000', '0', '1', null, 'ordersn1d17addcd');
INSERT INTO `attest_chained` VALUES ('58', '005331171221002661', 'd762200840cb4b4f', '2017-12-21 21:34:40.413000', '2017-12-21 21:36:40', '2017-12-21 21:31:00.000000', '0', '1', null, 'ordersn83189e5ac');
INSERT INTO `attest_chained` VALUES ('59', '005331171222000002', null, null, '2017-12-22 09:23:35', '2017-12-22 09:23:35.000000', '0', '0', null, 'ordersnf032f0bb8');
INSERT INTO `attest_chained` VALUES ('60', '005331171222000003', null, null, '2017-12-22 09:24:50', '2017-12-22 09:24:50.000000', '0', '0', null, 'ordersn4f96f4cd7');
INSERT INTO `attest_chained` VALUES ('61', '005331171222000180', null, null, '2017-12-22 09:30:21', '2017-12-22 09:30:21.000000', '0', '0', null, 'ordersn82c82bd80');
INSERT INTO `attest_chained` VALUES ('62', '005331171222000181', null, null, '2017-12-22 09:30:59', '2017-12-22 09:30:59.000000', '0', '0', null, 'ordersndd66e97ef');
INSERT INTO `attest_chained` VALUES ('63', '005331171222000182', null, null, '2017-12-22 09:38:40', '2017-12-22 09:38:40.000000', '0', '0', null, 'ordersn3860b815c');
INSERT INTO `attest_chained` VALUES ('64', '005331171222000183', null, null, '2017-12-22 09:40:47', '2017-12-22 09:40:47.000000', '0', '0', null, 'ordersn72a093b0d');
INSERT INTO `attest_chained` VALUES ('65', '005331171222000184', null, null, '2017-12-22 09:41:50', '2017-12-22 09:41:50.000000', '0', '0', null, 'ordersnf64d3dfc3');
INSERT INTO `attest_chained` VALUES ('66', '005331171222000185', null, null, '2017-12-22 09:43:15', '2017-12-22 09:43:15.000000', '0', '0', null, 'ordersndd88dbe1a');
INSERT INTO `attest_chained` VALUES ('67', '005351171222000186', null, null, '2017-12-22 11:34:06', '2017-12-22 11:34:06.000000', '0', '0', null, 'ordersn071cae996');
INSERT INTO `attest_chained` VALUES ('68', '005351171222000187', null, null, '2017-12-22 11:34:35', '2017-12-22 11:34:35.000000', '0', '0', null, 'ordersncd701d1f0');
INSERT INTO `attest_chained` VALUES ('69', '005351171222000188', null, null, '2017-12-22 11:34:46', '2017-12-22 11:34:46.000000', '0', '0', null, 'ordersn01f5b4a06');
INSERT INTO `attest_chained` VALUES ('70', '005351171222000189', null, null, '2017-12-22 11:34:53', '2017-12-22 11:34:53.000000', '0', '0', null, 'ordersn7dbaaf005');
INSERT INTO `attest_chained` VALUES ('71', '005351171222000190', null, null, '2017-12-22 15:05:56', '2017-12-22 15:05:56.402000', '0', '0', null, 'ordersn6dcd29737');
INSERT INTO `attest_chained` VALUES ('72', '005331171222000197', null, null, '2017-12-22 17:01:15', '2017-12-22 17:01:14.566000', '0', '0', null, 'ordersn950ee3195');
INSERT INTO `attest_chained` VALUES ('73', '005361171222000198', null, null, '2017-12-22 17:04:36', '2017-12-22 17:04:36.426000', '0', '0', null, 'ordersn779668de4');
INSERT INTO `attest_chained` VALUES ('74', '005341171222000196', null, null, '2017-12-22 17:11:31', '2017-12-22 17:11:30.794000', '0', '0', null, 'ordersnf3fb76377');
INSERT INTO `attest_chained` VALUES ('75', '005331171222000199', null, null, '2017-12-22 17:18:55', '2017-12-22 17:18:54.929000', '0', '0', null, 'ordersn808bd0523');
INSERT INTO `attest_chained` VALUES ('76', '005331171222000200', null, null, '2017-12-22 17:21:22', '2017-12-22 17:21:22.063000', '0', '0', null, 'ordersnb0aab43c0');
INSERT INTO `attest_chained` VALUES ('77', '005351171222000201', null, null, '2017-12-22 17:24:42', '2017-12-22 17:24:42.328000', '0', '0', null, 'ordersn0dec96f94');
INSERT INTO `attest_chained` VALUES ('78', '005351171222000202', null, null, '2017-12-22 17:25:08', '2017-12-22 17:25:07.912000', '0', '0', null, 'ordersn816825c14');
INSERT INTO `attest_chained` VALUES ('79', '005341171222000203', null, null, '2017-12-22 17:27:48', '2017-12-22 17:27:47.729000', '0', '0', null, 'ordersn28716ed3b');
INSERT INTO `attest_chained` VALUES ('80', '005361171222000204', null, null, '2017-12-22 17:30:31', '2017-12-22 17:30:30.793000', '0', '0', null, 'ordersn71f2a1d1e');
INSERT INTO `attest_chained` VALUES ('81', '005351171222000205', null, null, '2017-12-22 17:32:44', '2017-12-22 17:32:44.030000', '0', '0', null, 'ordersne42192fa4');
INSERT INTO `attest_chained` VALUES ('82', '005351171222000206', null, null, '2017-12-22 17:36:19', '2017-12-22 17:36:19.080000', '0', '0', null, 'ordersna98aa4c00');
INSERT INTO `attest_chained` VALUES ('83', '005311171222000207', null, null, '2017-12-22 17:37:13', '2017-12-22 17:37:12.517000', '0', '0', null, 'ordersnc0c1fdee2');

-- ----------------------------
-- Table structure for attest_file
-- ----------------------------
DROP TABLE IF EXISTS `attest_file`;
CREATE TABLE `attest_file` (
  `id` int(5) NOT NULL AUTO_INCREMENT,
  `update_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
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
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='文件地址表：关联存证记录与区块文件，存证源文件    attest_biz\r\n';

-- ----------------------------
-- Records of attest_file
-- ----------------------------
INSERT INTO `attest_file` VALUES ('21', '2017-12-21 14:13:43.000000', '2017-12-21 14:13:43.000000', '005361171221000005', '测试-ordersne683cc273', 'testcz', '005361171221000005-18bd149910fa4ed1be6c990d3ea51d56', '9', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('22', '2017-12-21 14:21:20.000000', '2017-12-21 14:21:21.000000', '005311171221000006', '测试-ordersnf67cb2df4', 'testcz', '005311171221000006-b5ffde9c7f9641178ea81bf49ea7943f', '9', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('23', '2017-12-21 14:22:24.000000', '2017-12-21 14:22:25.000000', '005311171221000007', '测试-ordersn9c2909b51', 'testcz', '005311171221000007-03dd6a2c687f4a1a83d7b386207f5628', '9', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('24', '2017-12-21 16:57:55.000000', '2017-12-21 16:57:55.000000', '005321171221000009', '测试-ordersneb8049d81', null, 'http://testcz.oss-cn-beijing.aliyuncs.com/%E6%B5%8B%E8%AF%95-ordersneb8049d81?Expires=1515574637&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=oKD0Q1u87yZ99rNTsSosNp%2BJKqc%3D', '13', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('25', '2017-12-21 17:03:53.000000', '2017-12-21 17:03:54.000000', '005341171221000010', '测试-ordersn4f8aebf63', 'testcz', '005341171221000010-133945e07c2d42fcaa9400920aa15399', '9', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('26', '2017-12-21 18:44:20.000000', '2017-12-21 18:44:21.000000', '005361171221000013', '测试-ordersn252ec3885', 'testcz', '005361171221000013-1791db0f60394bd79cdc94bad53846f5', '9', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('27', '2017-12-21 20:56:18.000000', '2017-12-21 20:56:18.000000', '005321171221000014', '测试-ordersnefb9607c4', 'testcz', '005321171221000014-a67ec848446a49129f7931314e59690e', '9', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('28', '2017-12-22 11:33:56.000000', '2017-12-22 11:33:56.000000', '005351171222000186', '测试-ordersn071cae996', 'testcz', '005351171222000186-2cece1263af24dbf9538e1245f9c24b3', '9', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('29', '2017-12-22 15:09:17.000000', '2017-12-22 15:09:17.727000', '005321171222000191', '测试-ordersn45857d608', null, 'http://testcz.oss-cn-beijing.aliyuncs.com/%E6%B5%8B%E8%AF%95-ordersn45857d608?Expires=1515654557&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=7PBnMNVGBUGqF1PN5nWClMBaJZQ%3D', '4', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('30', '2017-12-22 15:35:47.000000', '2017-12-22 15:35:47.821000', '005361171222000192', '测试-ordersn1eafa7fdc', null, 'http://testcz.oss-cn-beijing.aliyuncs.com/%E6%B5%8B%E8%AF%95-ordersn1eafa7fdc?Expires=1515656147&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=XJ9pRA%2FSHkA9h9VmhhTaq%2BJqsjM%3D', '4', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('31', '2017-12-22 15:36:26.000000', '2017-12-22 15:36:26.173000', '005321171222000193', '测试-ordersncd9d2d13f', null, 'http://testcz.oss-cn-beijing.aliyuncs.com/%E6%B5%8B%E8%AF%95-ordersncd9d2d13f?Expires=1515656185&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=Kbis2UDuW6fiCk3A06DiX0m%2BSec%3D', '4', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('32', '2017-12-22 15:38:17.000000', '2017-12-22 15:38:17.942000', '005351171222000194', '测试-ordersnf7adab306', null, 'http://testcz.oss-cn-beijing.aliyuncs.com/%E6%B5%8B%E8%AF%95-ordersnf7adab306?Expires=1515656297&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=lNomCJrjeRTYQmTR4tNW50C%2Bv4I%3D', '4', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('33', '2017-12-22 16:40:21.000000', '2017-12-22 16:40:20.965000', '005311171222000195', '测试-ordersnb098e7539', null, 'http://testcz.oss-cn-beijing.aliyuncs.com/%E6%B5%8B%E8%AF%95-ordersnb098e7539?Expires=1515660018&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=Y%2Fi36pYenzP8csFDDakLHu%2ByC0w%3D', '4', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('34', '2017-12-22 17:11:29.000000', '2017-12-22 17:11:29.533000', '005341171222000196', '测试-ordersnf3fb76377', 'testcz', '005341171222000196-53a61391fb764755af3ce45bc93af7a2', '9', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('35', '2017-12-22 17:01:11.000000', '2017-12-22 17:01:11.993000', '005331171222000197', '测试-ordersn950ee3195', null, 'http://testcz.oss-cn-beijing.aliyuncs.com/%E6%B5%8B%E8%AF%95-ordersn950ee3195?Expires=1515661271&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=nRP1jJ4PNNXzV1tSCSVhhyJu%2FMc%3D', '4', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('36', '2017-12-22 17:04:34.000000', '2017-12-22 17:04:34.810000', '005361171222000198', '测试-ordersn779668de4', null, 'http://testcz.oss-cn-beijing.aliyuncs.com/%E6%B5%8B%E8%AF%95-ordersn779668de4?Expires=1515661474&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=WKWDNTn8HRnhix9IusaB5BgQWfo%3D', '4', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('37', '2017-12-22 17:18:52.000000', '2017-12-22 17:18:52.184000', '005331171222000199', '测试-ordersn808bd0523', null, 'http://testcz.oss-cn-beijing.aliyuncs.com/%E6%B5%8B%E8%AF%95-ordersn808bd0523?Expires=1515662331&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=yBdmJu5nZITn5Gjb4dc7SkKtaNI%3D', '4', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('38', '2017-12-22 17:21:20.000000', '2017-12-22 17:21:20.861000', '005331171222000200', '测试-ordersnb0aab43c0', null, 'http://testcz.oss-cn-beijing.aliyuncs.com/%E6%B5%8B%E8%AF%95-ordersnb0aab43c0?Expires=1515662480&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=rbq8%2BWnAOCN12yr140McfUoVwYg%3D', '4', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('39', '2017-12-22 17:24:41.000000', '2017-12-22 17:24:41.027000', '005351171222000201', '测试-ordersn0dec96f94', null, 'http://testcz.oss-cn-beijing.aliyuncs.com/%E6%B5%8B%E8%AF%95-ordersn0dec96f94?Expires=1515662680&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=Q4fFzGOYY8m%2B7rnbIwSQY5K2fM0%3D', '4', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('40', '2017-12-22 17:25:07.000000', '2017-12-22 17:25:06.986000', '005351171222000202', '测试-ordersn816825c14', null, 'http://testcz.oss-cn-beijing.aliyuncs.com/%E6%B5%8B%E8%AF%95-ordersn816825c14?Expires=1515662706&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=4OlhDD6%2FodvXIAxESILX0C%2BTqNo%3D', '4', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('41', '2017-12-22 17:27:46.000000', '2017-12-22 17:27:46.720000', '005341171222000203', '测试-ordersn28716ed3b', null, 'http://testcz.oss-cn-beijing.aliyuncs.com/%E6%B5%8B%E8%AF%95-ordersn28716ed3b?Expires=1515662866&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=a7%2FznQqDQvpH2JNbtTTm%2FvKp1Zk%3D', '4', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('42', '2017-12-22 17:30:29.000000', '2017-12-22 17:30:29.691000', '005361171222000204', '测试-ordersn71f2a1d1e', null, 'http://testcz.oss-cn-beijing.aliyuncs.com/%E6%B5%8B%E8%AF%95-ordersn71f2a1d1e?Expires=1515663029&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=42fsSsfaU%2B84Kq1fcrwQuhyMwTM%3D', '4', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('43', '2017-12-22 17:32:43.000000', '2017-12-22 17:32:43.244000', '005351171222000205', '测试-ordersne42192fa4', null, 'http://testcz.oss-cn-beijing.aliyuncs.com/%E6%B5%8B%E8%AF%95-ordersne42192fa4?Expires=1515663163&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=6Hw1fcUIzNN9ZhiFMkaJabeMmfI%3D', '4', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('44', '2017-12-22 17:36:18.000000', '2017-12-22 17:36:18.006000', '005351171222000206', '测试-ordersna98aa4c00', null, 'http://testcz.oss-cn-beijing.aliyuncs.com/%E6%B5%8B%E8%AF%95-ordersna98aa4c00?Expires=1515663377&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=bysdf2OYT16S8K6oNObh92k%2Bui8%3D', '4', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);
INSERT INTO `attest_file` VALUES ('45', '2017-12-22 17:37:11.000000', '2017-12-22 17:37:11.168000', '005311171222000207', '测试-ordersnc0c1fdee2', null, 'http://testcz.oss-cn-beijing.aliyuncs.com/%E6%B5%8B%E8%AF%95-ordersnc0c1fdee2?Expires=1515663430&OSSAccessKeyId=LTAIBZFtpYe3Cmdn&Signature=1rLlTb0eLdujxchbEEqKrFQPCqY%3D', '4', 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null);

-- ----------------------------
-- Table structure for attest_history
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
-- Table structure for channel
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
-- Table structure for config
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
INSERT INTO `config` VALUES ('2', '005', 'hash_cz_ok_url', 'http://127.0.0.1:8083/channelTest/hashCzOk', '2017-12-13 16:16:49', '文件Hash存证通知回调地址');
INSERT INTO `config` VALUES ('3', '005', 'xq_ok_url', 'http://127.0.0.1:8083/channelTest/CzXqOk', '2017-12-13 16:16:53', '文件存证续期通知回调地址');
INSERT INTO `config` VALUES ('4', 'zjl', 'ordersn_url', 'http://127.0.0.1:8084/ordersn/createOrdersn', '2017-12-13 16:16:17', '请求订单号地址');
INSERT INTO `config` VALUES ('5', '003', 'cz_ok_url', 'http://cz.ipsebe.com/sebe/requestReceiveFinishCallback.do', '2017-11-14 10:58:41', '文件存证通知回调地址');
INSERT INTO `config` VALUES ('6', '003', 'hash_cz_ok_url', 'http://cz.ipsebe.com/sebe/saveHashFileCallback.do', '2017-11-14 10:58:50', '文件Hash存证通知回调地址');
INSERT INTO `config` VALUES ('7', '003', 'xq_ok_url', 'http://cz.ipsebe.com/sebe/extensionCallback.do', '2017-11-14 10:58:53', '文件存证续期通知回调地址');
INSERT INTO `config` VALUES ('8', '004', 'cz_ok_url', 'http://47.94.229.222/EvidenceModule/Evidence/dealEvidenceEnd', '2017-11-14 10:58:55', '文件存证通知回调地址');
INSERT INTO `config` VALUES ('9', '004', 'hash_cz_ok_url', 'http://47.94.229.222/EvidenceModule/Evidence/dealHashEvidence', '2017-11-14 10:58:56', '文件Hash存证通知回调地址');
INSERT INTO `config` VALUES ('10', '004', 'xq_ok_url', 'http://47.94.229.222/EvidenceModule/Evidence/dealRenewalEvidence', '2017-11-14 10:58:59', '存证续期通知回调地址');
INSERT INTO `config` VALUES ('11', '005', 'cz_ok_url', 'http://127.0.0.1:8083/channelTest/fileCzOk', '2017-12-13 16:16:58', '文件存证完成回调地址');
INSERT INTO `config` VALUES ('13', 'zjl', 'query_cz_chained_url', 'http://172.17.94.222:3000/queryCzByOrdersn', '2017-12-11 22:48:13', '查询存证上链接口');
INSERT INTO `config` VALUES ('14', 'zjl', 'add_cz_chained_url', 'http://172.17.94.222:3000/addCz', '2017-12-11 22:48:16', '增加存证上链接口');
INSERT INTO `config` VALUES ('15', 'store_location', 'beijingOSS1', '放置一个json串{域名，accesskey键值对，oss存储空间}', '2017-11-16 20:07:27', '说明');

-- ----------------------------
-- Table structure for dictionary
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
  `update_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idex_dictionary_id` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of dictionary
-- ----------------------------

-- ----------------------------
-- Table structure for prove
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
  `update_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' ON UPDATE CURRENT_TIMESTAMP(6) COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='出证订单表：描述出证订单信息，与存证实体表关联。';

-- ----------------------------
-- Records of prove
-- ----------------------------

-- ----------------------------
-- Table structure for sequence
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
INSERT INTO `sequence` VALUES ('ordersn', '207', '1', '2017-12-22');

-- ----------------------------
-- Table structure for status
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
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='记录订单的状态。内部业务核查。';

-- ----------------------------
-- Records of status
-- ----------------------------
INSERT INTO `status` VALUES ('55', '2017-12-21 14:13:50', '2017-12-21 14:13:43.000000', '005361171221000005', 'ordersne683cc273', '[File]文件存证完成3', null, '0', null);
INSERT INTO `status` VALUES ('56', '2017-12-21 14:21:23', '2017-12-21 14:21:21.000000', '005311171221000006', 'ordersnf67cb2df4', '[File]文件存证完成3', null, '0', null);
INSERT INTO `status` VALUES ('57', '2017-12-21 14:22:27', '2017-12-21 14:22:25.000000', '005311171221000007', 'ordersn9c2909b51', '[File]文件存证完成3', null, '0', null);
INSERT INTO `status` VALUES ('58', '2017-12-21 15:32:48', '2017-12-21 15:32:48.000000', '005311171221000008', 'ordersn52e6bfb60', '[Hash]Hash存证业务处理完成2', '[Hash]通知渠道成功3', '0', '');
INSERT INTO `status` VALUES ('59', '2017-12-21 16:57:55', '2017-12-21 16:57:55.000000', '005321171221000009', 'ordersneb8049d81', '[File]文件存证业务处理完成2', null, '0', null);
INSERT INTO `status` VALUES ('60', '2017-12-21 17:04:00', '2017-12-21 17:03:54.000000', '005341171221000010', 'ordersn4f8aebf63', '[File]文件存证完成3', null, '0', null);
INSERT INTO `status` VALUES ('61', '2017-12-21 17:04:16', '2017-12-21 17:04:16.000000', '005321171221000011', 'ordersne6ea58029', '[Hash]Hash存证业务处理完成2', null, '0', '存证完成');
INSERT INTO `status` VALUES ('62', '2017-12-21 18:43:48', '2017-12-21 18:43:48.000000', '005341171221000012', 'ordersnfce718ad5', '[Hash]Hash存证业务处理完成2', null, '0', '存证完成');
INSERT INTO `status` VALUES ('63', '2017-12-21 18:44:30', '2017-12-21 18:44:21.000000', '005361171221000013', 'ordersn252ec3885', '[File]文件存证完成3', null, '0', null);
INSERT INTO `status` VALUES ('64', '2017-12-21 20:56:44', '2017-12-21 20:56:18.000000', '005321171221000014', 'ordersnefb9607c4', '[File]文件存证完成3', null, '0', null);
INSERT INTO `status` VALUES ('65', '2017-12-21 20:58:41', '2017-12-21 20:58:42.000000', '005361171221000015', 'ordersnb4a52ceca', '[Hash]Hash存证业务处理完成2', null, '0', '存证完成');
INSERT INTO `status` VALUES ('66', '2017-12-21 21:04:26', '2017-12-21 21:04:26.000000', null, 'ordersn37d2fe64b', '[Hash]Hash存证接收处理完成1', null, '0', null);
INSERT INTO `status` VALUES ('67', '2017-12-21 21:08:42', '2017-12-21 21:08:43.000000', '005331171221002608', 'ordersnf264a7bc7', '[Hash]Hash存证业务处理完成2', null, '0', '存证完成');
INSERT INTO `status` VALUES ('68', '2017-12-21 21:09:44', '2017-12-21 21:09:45.000000', '005361171221002609', 'ordersnc097889c9', '[Hash]Hash存证业务处理完成2', null, '0', '存证完成');
INSERT INTO `status` VALUES ('69', '2017-12-21 21:18:44', '2017-12-21 21:18:45.000000', '005341171221002658', 'ordersn5d05fabea', '[Hash]Hash存证业务处理完成2', null, '0', '存证完成');
INSERT INTO `status` VALUES ('70', '2017-12-21 21:28:01', '2017-12-21 21:28:02.000000', '005341171221002659', 'ordersn608770e19', '[Hash]Hash存证业务处理完成2', null, '0', '存证完成');
INSERT INTO `status` VALUES ('71', '2017-12-21 21:28:33', '2017-12-21 21:28:33.000000', '005351171221002660', 'ordersn1d17addcd', '[Hash]Hash存证业务处理完成2', null, '0', '存证完成');
INSERT INTO `status` VALUES ('72', '2017-12-21 21:30:59', '2017-12-21 21:30:59.000000', '005331171221002661', 'ordersn83189e5ac', '[Hash]Hash存证业务处理完成2', null, '0', '存证完成');
INSERT INTO `status` VALUES ('73', '2017-12-22 09:20:26', '2017-12-22 09:20:27.000000', '005331171222000001', 'ordersnc76c93820', '[czxq]存证续期业务处理完成2', null, '0', null);
INSERT INTO `status` VALUES ('74', '2017-12-22 09:23:33', '2017-12-22 09:23:33.000000', '005331171222000002', 'ordersnf032f0bb8', '[czxq]存证续期业务处理完成2', null, '0', null);
INSERT INTO `status` VALUES ('75', '2017-12-22 09:24:48', '2017-12-22 09:24:49.000000', '005331171222000003', 'ordersn4f96f4cd7', '[czxq]存证续期业务处理完成2', null, '0', null);
INSERT INTO `status` VALUES ('76', '2017-12-22 09:29:02', '2017-12-22 09:29:03.000000', '005331171222000180', 'ordersn82c82bd80', '[czxq]存证续期业务处理完成2', null, '0', null);
INSERT INTO `status` VALUES ('77', '2017-12-22 09:30:56', '2017-12-22 09:30:56.000000', '005331171222000181', 'ordersndd66e97ef', '[czxq]存证续期业务处理完成2', null, '0', null);
INSERT INTO `status` VALUES ('78', '2017-12-22 09:38:38', '2017-12-22 09:38:38.000000', '005331171222000182', 'ordersn3860b815c', '[czxq]存证续期业务处理完成2', null, '0', null);
INSERT INTO `status` VALUES ('79', '2017-12-22 09:40:45', '2017-12-22 09:40:46.000000', '005331171222000183', 'ordersn72a093b0d', '[czxq]存证续期业务处理完成2', null, '0', null);
INSERT INTO `status` VALUES ('80', '2017-12-22 09:41:49', '2017-12-22 09:41:50.000000', '005331171222000184', 'ordersnf64d3dfc3', '[czxq]存证续期业务处理完成2', null, '0', null);
INSERT INTO `status` VALUES ('81', '2017-12-22 09:43:14', '2017-12-22 09:43:15.000000', '005331171222000185', 'ordersndd88dbe1a', '[czxq]存证续期业务处理完成2', null, '0', null);
INSERT INTO `status` VALUES ('82', '2017-12-22 11:34:06', '2017-12-22 11:33:56.000000', '005351171222000186', 'ordersn071cae996', '[File]文件存证完成3', null, '0', null);
INSERT INTO `status` VALUES ('83', '2017-12-22 11:34:34', '2017-12-22 11:34:34.000000', '005351171222000187', 'ordersncd701d1f0', '[czxq]存证续期业务处理完成2', null, '0', null);
INSERT INTO `status` VALUES ('84', '2017-12-22 11:34:45', '2017-12-22 11:34:45.000000', '005351171222000188', 'ordersn01f5b4a06', '[czxq]存证续期业务处理完成2', null, '0', null);
INSERT INTO `status` VALUES ('85', '2017-12-22 11:34:51', '2017-12-22 11:34:52.000000', '005351171222000189', 'ordersn7dbaaf005', '[czxq]存证续期业务处理完成2', null, '0', null);
INSERT INTO `status` VALUES ('86', '2017-12-22 15:05:51', '2017-12-22 15:05:51.483000', '005351171222000190', 'ordersn6dcd29737', '[czxq]存证续期业务处理完成2', null, '0', null);
INSERT INTO `status` VALUES ('87', '2017-12-22 15:09:17', '2017-12-22 15:09:17.781000', '005321171222000191', 'ordersn45857d608', '[File]文件存证业务处理完成2', null, '0', null);
INSERT INTO `status` VALUES ('88', '2017-12-22 15:35:47', '2017-12-22 15:35:47.860000', '005361171222000192', 'ordersn1eafa7fdc', '[File]文件存证业务处理完成2', null, '0', null);
INSERT INTO `status` VALUES ('89', '2017-12-22 15:36:26', '2017-12-22 15:36:26.267000', '005321171222000193', 'ordersncd9d2d13f', '[File]文件存证业务处理完成2', null, '0', null);
INSERT INTO `status` VALUES ('90', '2017-12-22 15:38:18', '2017-12-22 15:38:18.027000', '005351171222000194', 'ordersnf7adab306', '[File]文件存证业务处理完成2', null, '0', null);
INSERT INTO `status` VALUES ('91', '2017-12-22 16:49:40', '2017-12-22 16:40:21.099000', '005311171222000195', 'ordersnb098e7539', '[File]文件存证处理失败', null, '0', null);
INSERT INTO `status` VALUES ('92', '2017-12-22 17:11:31', '2017-12-22 17:11:29.107000', '005341171222000196', 'ordersnf3fb76377', '[File]文件存证完成3', null, '0', null);
INSERT INTO `status` VALUES ('93', '2017-12-22 17:01:14', '2017-12-22 17:01:12.053000', '005331171222000197', 'ordersn950ee3195', '[File]文件存证处理失败', null, '0', null);
INSERT INTO `status` VALUES ('94', '2017-12-22 17:04:36', '2017-12-22 17:04:34.841000', '005361171222000198', 'ordersn779668de4', '[File]文件存证处理失败', null, '0', null);
INSERT INTO `status` VALUES ('95', '2017-12-22 17:18:55', '2017-12-22 17:18:52.362000', '005331171222000199', 'ordersn808bd0523', '[File]文件存证处理失败', null, '0', null);
INSERT INTO `status` VALUES ('96', '2017-12-22 17:21:22', '2017-12-22 17:21:20.934000', '005331171222000200', 'ordersnb0aab43c0', '[File]文件存证处理失败', null, '0', null);
INSERT INTO `status` VALUES ('97', '2017-12-22 17:24:42', '2017-12-22 17:24:41.088000', '005351171222000201', 'ordersn0dec96f94', '[File]文件存证处理失败', null, '0', null);
INSERT INTO `status` VALUES ('98', '2017-12-22 17:25:08', '2017-12-22 17:25:07.040000', '005351171222000202', 'ordersn816825c14', '[File]文件存证处理失败', null, '0', null);
INSERT INTO `status` VALUES ('99', '2017-12-22 17:27:48', '2017-12-22 17:27:46.806000', '005341171222000203', 'ordersn28716ed3b', '[File]文件存证处理失败', null, '0', null);
INSERT INTO `status` VALUES ('100', '2017-12-22 17:30:31', '2017-12-22 17:30:29.750000', '005361171222000204', 'ordersn71f2a1d1e', '[File]文件存证处理失败', null, '0', null);
INSERT INTO `status` VALUES ('101', '2017-12-22 17:32:44', '2017-12-22 17:32:43.309000', '005351171222000205', 'ordersne42192fa4', '[File]文件存证处理失败', null, '0', null);
INSERT INTO `status` VALUES ('102', '2017-12-22 17:36:19', '2017-12-22 17:36:18.077000', '005351171222000206', 'ordersna98aa4c00', '[File]文件存证处理失败', null, '0', null);
INSERT INTO `status` VALUES ('103', '2017-12-22 17:37:12', '2017-12-22 17:37:11.240000', '005311171222000207', 'ordersnc0c1fdee2', '[File]文件存证处理失败', null, '0', null);

-- ----------------------------
-- Table structure for temp_order
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
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='存证信息表（完备业务表）：存储存证的详细信息';

-- ----------------------------
-- Records of temp_order
-- ----------------------------
INSERT INTO `temp_order` VALUES ('16', '2017-12-21 21:04:26', '2017-12-21 21:04:26.000000', '0.9', null, null, null, '005', 'mrWW', 'ordersn37d2fe64b', 'U2Zu4gYwv/1jEpmNli38kP98LEpx3dumdU02hjqZ2OJ2h5Acia2tvTfrKXmS9KCPcNxsh8HJhPKS/xgB3ugbqA==', 'ZHJL', '1', '2017-12-21 21:04:26.123000', '0', null, null, null, '1', '5', '测试-ordersn37d2fe64b', '.405', '9', null, 'IPD2FUYIDPHOKw68hiFvj3wAc2TNtCLO0WdyqmVTLv4=', null, null, null, null, '2', 'ownerid1', '存证人01f2', 'agent1ca7b', '4380042', '31e91d92fd3@.com', null, null, '5', '描述:{\"accessKey\":\"GeA29xKy7QTeQkLXWSiBaEWqV6FidrKH5zddHv4zKwmTmgkcd\",\"agentEmail\":\"31e91d92fd3@.com\",\"agentName\":\"agent1ca7b\",\"agentPhone\":\"4380042\",\"b', '200', '2017-12-21 21:04:24.000000', null);

-- ----------------------------
-- Table structure for user
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
  `verify_time` timestamp(6) NOT NULL DEFAULT '0000-00-00 00:00:00.000000' COMMENT '通过认证时间',
  `freezed` varchar(1) COLLATE utf8_unicode_ci NOT NULL COMMENT '是否冻结？1冻结，0未冻结',
  `reamrk` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '备注信息',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_user_id` (`id`),
  UNIQUE KEY `idx_user_channeluserid_channelid` (`channel_userid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='平台用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('19', '2017-12-19 14:07:59', '2017-12-19 14:07:59.000000', 'RDn9WN48soDtTkKwgDAzp1gKKBRAreHokEecUL61fidC6v6Et', '0422b3ecef16bb561bbc4940e857146a53e13b687b6458160bf769a4e7e7c44bb2c6d3a887a62ed631d2338ee10effc8b5982eb433e65db49c65b0b2b711bab891', '00d7d8fac6935a44faa37cea298601371a34ea58d447ed494749c5cf142bee16f1', null, '005', '22d', null, null, null, null, '2017-12-19 14:07:59.360000', '0', null);
INSERT INTO `user` VALUES ('20', '2017-12-21 11:35:58', '2017-12-21 11:35:58.000000', '15Cr3JvK78Qm32VcZXEXnPQfUfdrKPVMv47anb1JdtkeuR6J1', '0429dda27473e49b9cb07977694864ba03be61a675047ef5b6afa9c62575b13a63f1d82985a922fbe7579acfa64b67a764edffb6da2ebd7cdc427fb4fa2992e5f3', '00c648087bc9d86264ed2ebebf9e52242183834b4cf8b6d0cddb38655c5d8c9507', null, '005', '950', null, null, null, null, '2017-12-21 11:35:58.264612', '0', null);
INSERT INTO `user` VALUES ('21', '2017-12-21 11:51:03', '2017-12-21 11:51:03.000000', '2vM55QNKUzKuFxFGK2DPh2bkzenG89KYBAd3fsbpM7okE8VuWW', '0405f9cc6625bb063e9f26c1dd1592aeb136a85e016167fbfc9f56d07a3bce0b7185b71b13b59df1005ea2995712c15938360ef7db16b489c36b53ab2770446e13', '0f9b31fadf606f0acee781a4f472dfc6c1d156677107cdfd3ce759ff495d5bdb', null, '005', '6b6', null, null, null, null, '2017-12-21 11:51:03.071612', '0', null);
INSERT INTO `user` VALUES ('22', '2017-12-21 13:00:24', '2017-12-21 13:00:24.000000', '2bsekRkT7qEnLPY5RSk189Sp8wbZ6skSabATiCEH9YuXtXDvxb', '0410dce64b14b672692b96f377aaf4b00d2036b4772d17270e12b5433cbfabc50bc7c349912fd9d9d8df24390b13472fd4ccbeabe6f7f0d20ce0a0ce6348adf3ba', '00ae1016d7da4c50f299008a243dd0c437411065a664cb8b2dd1db56dda2c88a95', null, '005', 'a69', null, null, null, null, '2017-12-21 13:00:24.430612', '0', null);
INSERT INTO `user` VALUES ('23', '2017-12-21 13:06:43', '2017-12-21 13:06:43.000000', '2dig7ZQPVzdq11xEZB4iJsTWAKTmspvq7ocmz2iJ7bJYmzDCCX', '04963758dbdbe54b32f13fb73976d3bd3d96dad4ce35c25f321ee51eea99efc98c11fe14500754492a3acc36312c51cc50fe8f476661e788bb850e266988edf050', '296d98a853c634e50496e21bdad26b430a9ed058dfd70ede03f2ea686ff9ad9d', null, '005', '8fb', null, null, null, null, '2017-12-21 13:06:43.509612', '0', null);
INSERT INTO `user` VALUES ('24', '2017-12-21 14:13:46', '2017-12-21 14:13:46.000000', 'ncCKSSgZZi2DvbvdPXkjhz1cnb4GgLywVaTtcnMZfysTGz2LC', '04c28d6224f2113deec12e1de4de7d5b9564c437014d315da3443f39829d01bb7b0634a1b28ccfadf484abd01cba639f15b51e29bcb12042a6d59b84a038b0c2ff', '00e5350b46f4604bef5da02d04bbd03527b827d6968aa9a4a2669d5b0f50f36226', null, '005', 'e3c', null, null, null, null, '2017-12-21 14:13:46.034000', '0', null);
INSERT INTO `user` VALUES ('25', '2017-12-21 14:21:21', '2017-12-21 14:21:21.000000', '2DMEY85vhcmRSqVU7hiP3LsxzcYbhQ3bjH7FR3XZeVBrKhort8', '0429c55c42b0f430a5b9e1a6a9df8ebf1663dd1aab3b11df015b80abe288380fdbec5143652053b38c511355ec8b5db9d88f06669963af885a55752e746ca6af56', '00e280ce5c53942b70d1b4e00f492b47c835f00e7a16e664c58e2a53d0c70aca8f', null, '005', '40c', null, null, null, null, '2017-12-21 14:21:21.163000', '0', null);
INSERT INTO `user` VALUES ('26', '2017-12-21 14:22:25', '2017-12-21 14:22:25.000000', 'RDXMdJ6soHiPhT17S14mGagvqmJoypiyCQD7YU1z3R5sdEwub', '04e2463aaf5954f46548f8aa699a2982935c253a91907a137f1c5e2427b60d9677904e9b6295262c7ac58436cfae8f30e28efe1ef93327884fed52e025bdd0d534', '7ddbe497f689274f0494b111b667c66262cd93fe0bf261eb1983db53178aea21', null, '005', '21d', null, null, null, null, '2017-12-21 14:22:25.271000', '0', null);
INSERT INTO `user` VALUES ('27', '2017-12-21 15:32:52', '2017-12-21 15:32:52.000000', 'xJTRrYFZfL4i2BX3c9HeCn1VihnjKCbPobKBeKhEr1QnyYZw9', '045dcfd13a3050c0927e72baef7ae06077e0d1c5421bad946a35c4cd962773e669ddbc329ea997a2ea99b0a6f8f8a00db2b0a0bfcf413f5386a19bd62f9a29c183', '00f82ec0e7a8ddb0f82bcbe5644a3b21c130127927481bf660ad182dd31c6d0161', null, '005', '4ea', null, null, null, null, '2017-12-21 15:32:52.062000', '0', null);
INSERT INTO `user` VALUES ('28', '2017-12-21 16:58:00', '2017-12-21 16:58:00.000000', '2p4a2RB7KovNiGyJ9fnsveR7qJGsLp363nd1HcJZ4oFK8sP9Ew', '04ae127fbd9390407796329253091c5d5d3b2130b6b55af43e8f407be4a2968c98291e992cab7195552e145f73d08c309fd6c1bf9b0df6db9345f8621ec5e76c10', '709fe91fda5a4419ac962ab3a86f417edda12d1207ae6499c8b571e560c6f2c2', null, '005', '1a7', null, null, null, null, '2017-12-21 16:58:00.242000', '0', null);
INSERT INTO `user` VALUES ('29', '2017-12-21 17:03:55', '2017-12-21 17:03:55.000000', 'mAjf1ARNndG5Ntnrnq1KF5pmTV89HZQB1NN41PKMWzjnEmg4L', '0412498b09d33b017a5f23f17ba4f4469b46a949c08c84f3d8a872c1ced21088e8363a165f5970b8f7667cfc304b1b6b11cfb49bd31a4339d7df004522a0ef3d01', '3271a38450cfb9276a7a68f04fce083e4e1f5106bc9e8a92aeac6d617bcadbf6', null, '005', 'f0c', null, null, null, null, '2017-12-21 17:03:55.099000', '0', null);
INSERT INTO `user` VALUES ('30', '2017-12-21 17:04:19', '2017-12-21 17:04:20.000000', '73n7SFxcfjNXcrtCpTpc4saFHvi2VKyUNMUVYFv7qz1vNaubJ', '04b33fc3e5cdb6dbc4fc4ca9c59a38435142b0a79c11d4c2961cb20b12befa9bf16bee3e1aca68b19fa45718349207d95dc546afd5d666e00ee1d7aa94eb95081f', '00db8bd81c0ed43834e196b5311c3fec954472c757d6d06377ef861e02e7d47407', null, '005', '94e', null, null, null, null, '2017-12-21 17:04:19.600000', '0', null);
INSERT INTO `user` VALUES ('31', '2017-12-21 18:43:52', '2017-12-21 18:43:53.000000', '2PhXhVTuNcKTUMtMDBfWsUMBXDRoMjSgyZwGN54enrnBG8MS3z', '04d2e9a439a6504cf71b67a237b9a96bd24559398ad57ca06e4704af6dce1cc09aa94e5aa5aedfbf6a23d2b61107125562eb81471ebd080b3da2d4ef07ea3172c3', '0d020e07c5d922d3a0f6f41e7fa7f9e7764167b6971f1c1d1c94fb85827a978d', null, '005', '462', null, null, null, null, '2017-12-21 18:43:52.801000', '0', null);
INSERT INTO `user` VALUES ('32', '2017-12-21 18:44:20', '2017-12-21 18:44:21.000000', 'qS4UTb7cedQMV5wdTDLGQ6wXH7U1L9Kx4SvMgdukZouwz8XpN', '0496e1f7bb318c62555a05ec5670d6d498f9449b466279b99981333c2b95296ce4049dc61e7ecf10fdee7d737814bc9d8db6f0f486586c802da9a2b5b34f1ae48b', '159bbc2afa6dfabc9d6e60aa021a4aaf5327f4f0b24eba10ead5da5873437edb', null, '005', 'bcc', null, null, null, null, '2017-12-21 18:44:20.862000', '0', null);
INSERT INTO `user` VALUES ('33', '2017-12-21 20:56:25', '2017-12-21 20:56:26.000000', '2PbrHPMWtqoWgZhEwApFkm2XDbfAuSFpPQyrYGgp7UrLP1rkF7', '04ea8653607935e0927b1c2bbbac4f2f8acdb75f6a94139df6a752565ec6d27ceff448ee28134a615dd48ca2939a37618560c8223009252166d744415a13fb62ba', '5d6d57f9f28e248941c56ef30959bad06cc67000a504b4b43091440bc9776304', null, '005', 'mrWW', null, null, null, null, '2017-12-21 20:56:25.735000', '0', null);

-- ----------------------------
-- Table structure for warning
-- ----------------------------
DROP TABLE IF EXISTS `warning`;
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
  UNIQUE KEY `idx_warning_ordersn` (`ordersn`) USING BTREE,
  UNIQUE KEY `idx_warning_channelOrdersn` (`channel_ordersn`) USING BTREE,
  KEY `idx_warning_warn_num` (`num`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of warning
-- ----------------------------
INSERT INTO `warning` VALUES ('3', '2017-12-21 14:20:00', '2017-12-21 14:20:00.082000', '2', '005361171221000005', 'ordersne683cc273', '18', null);
INSERT INTO `warning` VALUES ('4', '2017-12-21 14:30:00', '2017-12-21 14:30:00.033000', '2', '005311171221000006', 'ordersnf67cb2df4', '9', null);
INSERT INTO `warning` VALUES ('5', '2017-12-21 14:30:00', '2017-12-21 14:30:00.104000', '2', '005311171221000007', 'ordersn9c2909b51', '7', null);
INSERT INTO `warning` VALUES ('6', '2017-12-21 15:40:00', '2017-12-21 15:40:00.092000', '2', '005311171221000008', 'ordersn52e6bfb60', '5', null);
INSERT INTO `warning` VALUES ('7', '2017-12-21 17:15:00', '2017-12-21 17:15:00.070000', '2', '005341171221000010', 'ordersn4f8aebf63', '6', null);
INSERT INTO `warning` VALUES ('8', '2017-12-21 17:15:00', '2017-12-21 17:15:00.346000', '2', '005321171221000011', 'ordersne6ea58029', '5', null);
INSERT INTO `warning` VALUES ('9', '2017-12-21 18:55:00', '2017-12-21 18:55:00.059000', '2', '005341171221000012', 'ordersnfce718ad5', '5', null);
INSERT INTO `warning` VALUES ('10', '2017-12-21 18:55:00', '2017-12-21 18:55:00.126000', '2', '005361171221000013', 'ordersn252ec3885', '5', null);
INSERT INTO `warning` VALUES ('11', '0000-00-00 00:00:00', '2017-12-21 19:53:38.609000', '3', '1312', '12321', '2', null);
INSERT INTO `warning` VALUES ('12', '2017-12-21 21:15:00', '2017-12-21 21:15:00.121000', '2', '005321171221000014', 'ordersnefb9607c4', '5', null);
INSERT INTO `warning` VALUES ('13', '2017-12-21 21:20:00', '2017-12-21 21:20:00.156000', '2', '005361171221000015', 'ordersnb4a52ceca', '6', null);
INSERT INTO `warning` VALUES ('14', '2017-12-21 21:30:00', '2017-12-21 21:30:00.162000', '2', '005331171221002608', 'ordersnf264a7bc7', '5', null);
INSERT INTO `warning` VALUES ('15', '2017-12-21 21:30:00', '2017-12-21 21:30:00.244000', '2', '005361171221002609', 'ordersnc097889c9', '5', null);

-- ----------------------------
-- Function structure for again
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
-- Function structure for currval
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
-- Function structure for is_today
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
-- Function structure for nextval
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
-- Function structure for setval
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
