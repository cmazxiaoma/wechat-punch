//wx_user
DROP TABLE IF EXISTS `wx_user`;
CREATE TABLE `wx_user` (
  `id` SMALLINT NOT NULL AUTO_INCREMENT,
  `nick_name` VARCHAR(255) DEFAULT NULL,
  `sex` ENUM('1','2') NOT NULL DEFAULT '1',
  `city` VARCHAR(255) DEFAULT NULL,
  `country` VARCHAR(255) DEFAULT NULL,
  `province` VARCHAR(255) DEFAULT NULL,
  `openid` VARCHAR(255) DEFAULT NULL,
  `headimgurl` VARCHAR(500) DEFAULT NULL,
  `language` VARCHAR(20) DEFAULT NULL,
  `privilege` VARCHAR(20) DEFAULT NULL,
  `create_date` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `update_date` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;

//wx_user_time_sheet
CREATE TABLE `wx_user_time_sheet`(
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `open_id` VARCHAR(50) NOT NULL,
  `location_latitude` VARCHAR(100) NOT NULL,
  `location_longitude` VARCHAR(100) NOT NULL,
  `location_info` VARCHAR(100) NOT NULL,
  /*
	-1:未考勤
	0: 正常
	1：迟到
	2：早退
	3：加班
	4：请假
  */
  `status` SMALLINT NOT NULL DEFAULT '-1' COMMENT '-1:未考勤 0: 正常 1：迟到 2：早退 3：加班 4：请假',
  `first_punch_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `last_punch_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `punch_date` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=INNODB CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

//添加索引
CREATE INDEX idx_openid ON wx_user_time_sheet(open_id);
CREATE INDEX idex_user_id ON wx_user_time_sheet(user_id);

//punch_role
CREATE TABLE `punch_role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `gowork_date` VARCHAR(10)NOT NULL,
  `offwork_date` VARCHAR(10)NOT NULL,
  `overtime_start_date` VARCHAR(10)NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=INNODB DEFAULT CHARSET=utf8;


