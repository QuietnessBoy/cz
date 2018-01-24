/*
Navicat MySQL Data Transfer

Source Server         : localhsot
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : attest_biz

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2018-01-16 16:17:39
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for rabbitmq_msg
-- ----------------------------
DROP TABLE IF EXISTS `rabbitmq_msg`;
CREATE TABLE `rabbitmq_msg` (
  `id` int(6) NOT NULL AUTO_INCREMENT,
  `create_time` timestamp(6) NULL DEFAULT NULL,
  `queue_name` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '队列名称',
  `num` longtext COLLATE utf8_unicode_ci COMMENT '消息数量',
  PRIMARY KEY (`id`),
  KEY `idx_rabbitmq_create_time` (`create_time`) USING BTREE,
  KEY `idx_rabbitmq_queue_name` (`queue_name`)
) ENGINE=InnoDB AUTO_INCREMENT=25234 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records of rabbitmq_msg
-- ----------------------------
