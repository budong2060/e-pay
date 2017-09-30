/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50713
Source Host           : localhost:3306
Source Database       : e-pay

Target Server Type    : MYSQL
Target Server Version : 50713
File Encoding         : 65001

Date: 2017-09-30 18:39:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for pay_account
-- ----------------------------
DROP TABLE IF EXISTS `pay_account`;
CREATE TABLE `pay_account` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` varchar(10) NOT NULL COMMENT '用户ID',
  `user_type` bigint(3) DEFAULT NULL COMMENT '用户类型',
  `user_name` varchar(16) DEFAULT NULL COMMENT '用户名',
  `account_id` varchar(32) NOT NULL COMMENT '虚拟账户',
  `account_pwd` varchar(32) DEFAULT NULL COMMENT '虚拟账户密码',
  `account_status` bigint(3) DEFAULT NULL COMMENT '0-冻结  1-正常',
  `balance_amount` decimal(10,2) DEFAULT '0.00' COMMENT '余额',
  `frozen_amount` decimal(10,2) DEFAULT '0.00' COMMENT '冻结金额',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pay_balance
-- ----------------------------
DROP TABLE IF EXISTS `pay_balance`;
CREATE TABLE `pay_balance` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `balance_no` varchar(32) DEFAULT NULL,
  `mch_id` varchar(32) DEFAULT NULL COMMENT '商户号',
  `pay_channel` varchar(10) DEFAULT NULL COMMENT '渠道',
  `balance_date` datetime DEFAULT NULL COMMENT '对账日期',
  `sys_collect_amount` decimal(10,2) DEFAULT NULL COMMENT '系统收款',
  `actual_collect_amount` decimal(10,2) DEFAULT NULL COMMENT '第三方账户实际收款',
  `sys_refund_amount` decimal(10,2) DEFAULT NULL COMMENT '系统退款金额',
  `actual_refund_amount` decimal(10,2) DEFAULT NULL COMMENT '第三方账户实际退款金额',
  `balance_status` int(1) DEFAULT NULL COMMENT '对账状态（0-正常，1-异常）',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `mchId` (`mch_id`),
  KEY `payChannel` (`pay_channel`),
  KEY `balanceDate` (`balance_date`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pay_balance_item
-- ----------------------------
DROP TABLE IF EXISTS `pay_balance_item`;
CREATE TABLE `pay_balance_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `balance_no` varchar(32) NOT NULL COMMENT 'pay_balance外键',
  `mch_id` varchar(32) DEFAULT NULL COMMENT '商户号',
  `pay_way` int(3) DEFAULT NULL COMMENT '支付渠道',
  `bill_type` int(3) DEFAULT NULL COMMENT '类型（1-支付 2-退款）',
  `trade_no` varchar(32) DEFAULT NULL COMMENT '交易流水号',
  `third_trade_no` varchar(32) DEFAULT NULL COMMENT '第三方流水号',
  `trade_amount` decimal(10,2) DEFAULT NULL COMMENT '交易金额',
  `third_trade_amount` decimal(10,2) DEFAULT NULL,
  `trade_time` datetime DEFAULT NULL COMMENT '交易时间',
  `third_trade_time` datetime DEFAULT NULL COMMENT '第三方交易时间',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `balanceId` (`balance_no`)
) ENGINE=InnoDB AUTO_INCREMENT=104 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pay_bill
-- ----------------------------
DROP TABLE IF EXISTS `pay_bill`;
CREATE TABLE `pay_bill` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id,自增长',
  `bill_date` datetime NOT NULL COMMENT '账单日',
  `mch_id` varchar(32) NOT NULL COMMENT '商户id',
  `third_mch_id` varchar(32) DEFAULT NULL COMMENT '平台商户号',
  `third_sub_mch_id` varchar(32) DEFAULT NULL COMMENT '子商户id',
  `pay_channel` varchar(10) DEFAULT NULL COMMENT '支付渠道（ALI，WX...）',
  `pay_way` tinyint(3) DEFAULT NULL COMMENT '支付渠道',
  `bill_type` tinyint(1) NOT NULL COMMENT '账单类型 1:支付，2：退款',
  `order_no` varchar(32) DEFAULT NULL COMMENT '业务订单号',
  `trade_time` datetime DEFAULT NULL COMMENT '交易时间',
  `order_type` tinyint(1) DEFAULT NULL COMMENT '订单类型',
  `trade_no` varchar(64) DEFAULT NULL,
  `third_trade_no` varchar(32) NOT NULL COMMENT '第三方支付订单号',
  `trade_status` tinyint(3) DEFAULT NULL COMMENT '交易状态',
  `trade_amount` decimal(8,2) DEFAULT NULL COMMENT '订单总金额',
  `coupon_amount` decimal(8,2) DEFAULT '0.00' COMMENT '代金券或立减优惠金额',
  `trade_desc` varchar(512) DEFAULT NULL COMMENT '交易描述',
  `refund_no` varchar(64) DEFAULT NULL,
  `third_refund_no` varchar(32) DEFAULT NULL COMMENT '第三方退款单号',
  `refund_time` datetime DEFAULT NULL COMMENT '退款申请时间',
  `refund_finish_time` datetime DEFAULT NULL COMMENT '退款完成时间',
  `refund_amount` decimal(8,2) DEFAULT NULL COMMENT '退款金额',
  `refund_coupon_amount` decimal(8,2) DEFAULT '0.00' COMMENT '代金券或立减优惠退款金额',
  `refund_status` tinyint(3) DEFAULT NULL COMMENT '退款状态',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx` (`mch_id`,`pay_channel`)
) ENGINE=InnoDB AUTO_INCREMENT=136201 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='第三方支付账单表';

-- ----------------------------
-- Table structure for pay_config
-- ----------------------------
DROP TABLE IF EXISTS `pay_config`;
CREATE TABLE `pay_config` (
  `id` int(10) NOT NULL AUTO_INCREMENT,
  `mch_name` varchar(128) DEFAULT NULL COMMENT '商户名称',
  `mch_id` varchar(32) DEFAULT NULL COMMENT '商户ID',
  `pay_way` tinyint(3) DEFAULT NULL COMMENT '支付渠道',
  `pay_scene` tinyint(3) DEFAULT NULL COMMENT '支付场景',
  `status` tinyint(1) DEFAULT NULL COMMENT '0-停用  1-正常',
  `is_bill` tinyint(1) DEFAULT '0' COMMENT '是否需要下载对账单',
  `config` varchar(4096) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Table structure for pay_payment
-- ----------------------------
DROP TABLE IF EXISTS `pay_payment`;
CREATE TABLE `pay_payment` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `gid` varchar(32) DEFAULT NULL COMMENT '全局ID',
  `mch_id` varchar(32) DEFAULT NULL COMMENT '商户平台ID',
  `user_id` varchar(10) NOT NULL COMMENT '用户id',
  `order_no` varchar(32) NOT NULL COMMENT '支付订单号',
  `order_type` int(3) NOT NULL COMMENT '支付订单类型',
  `trade_time` datetime NOT NULL COMMENT '支付时间',
  `pay_channel` varchar(10) DEFAULT NULL COMMENT '支付渠道',
  `pay_way` int(3) NOT NULL COMMENT '支付方式',
  `trade_no` varchar(32) NOT NULL COMMENT '商户支付流水号',
  `trade_amount` decimal(10,2) DEFAULT NULL COMMENT '支付金额',
  `trade_finish_time` datetime DEFAULT NULL COMMENT '支付完成时间',
  `trade_status` int(3) DEFAULT NULL COMMENT '支付状态',
  `third_trade_amount` decimal(10,2) DEFAULT NULL COMMENT '第三方实际支付金额',
  `third_trade_no` varchar(32) DEFAULT NULL COMMENT '第三方支付流水号',
  `trade_desc` varchar(256) DEFAULT NULL COMMENT '描述',
  `notify_url` varchar(256) DEFAULT NULL COMMENT '支付成功通知上游业务方地址',
  `notify_code` int(3) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=72 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pay_payment_collect
-- ----------------------------
DROP TABLE IF EXISTS `pay_payment_collect`;
CREATE TABLE `pay_payment_collect` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `pay_id` int(11) NOT NULL COMMENT '支付订单表主键ID',
  `user_id` varchar(10) NOT NULL COMMENT '收款用户',
  `user_name` varchar(16) DEFAULT NULL COMMENT '收款用户名',
  `collect_amount` decimal(10,2) DEFAULT NULL COMMENT '收款金额',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pay_refund
-- ----------------------------
DROP TABLE IF EXISTS `pay_refund`;
CREATE TABLE `pay_refund` (
  `id` int(11) NOT NULL,
  `gid` varchar(32) DEFAULT NULL,
  `user_id` varchar(10) NOT NULL,
  `mch_id` varchar(32) DEFAULT NULL,
  `trade_no` varchar(32) NOT NULL COMMENT '商户支付流水号',
  `refund_no` varchar(32) NOT NULL COMMENT '商户退款流水号',
  `pay_channel` varchar(10) DEFAULT NULL,
  `pay_way` int(3) NOT NULL COMMENT '退款渠道',
  `refund_amount` decimal(10,2) DEFAULT NULL COMMENT '退款金额',
  `refund_time` datetime DEFAULT NULL COMMENT '退款时间',
  `refund_status` int(11) DEFAULT NULL COMMENT '退款状态',
  `refund_desc` varchar(256) DEFAULT NULL COMMENT '描述',
  `third_refund_no` varchar(32) DEFAULT NULL COMMENT '第三方退款流水号',
  `refund_finish_time` datetime DEFAULT NULL COMMENT '退款完成时间',
  `notify_url` varchar(256) DEFAULT NULL COMMENT '退款通知地址',
  `notify_code` int(11) DEFAULT NULL COMMENT '是否通知',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
