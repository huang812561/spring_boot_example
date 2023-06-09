-- ----------------------------
-- Table structure for system_log
-- ----------------------------

DROP TABLE IF EXISTS `system_log`;
CREATE TABLE `system_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `service_name` varchar(200)  NULL,
  `business_name` varchar(200)  NULL,
  `class_name` varchar(200) NULL,
  `method_name` varchar(200) NULL,
  `method_desc` varchar(200) NULL,
  `params` text  NULL,
  `run_time` bigint(20) NULL ,
  `remark` varchar(500)  NULL ,
  `create_date` datetime(0) NULL,
  PRIMARY KEY (`id`) USING BTREE
);
