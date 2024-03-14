-- `cn-chat`.cc_msg_2024_03_10 definition

CREATE TABLE `cc_msg` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` bigint NOT NULL DEFAULT '0',
  `terminal_id` bigint NOT NULL DEFAULT '0',
  `seq` bigint NOT NULL DEFAULT '0',
  `msg_id` varchar(50) NOT NULL DEFAULT '' COMMENT '消息id',
  `payload` text COMMENT '聊天内容',
  `biz_id` varchar(50) NOT NULL DEFAULT '' COMMENT 'biz id',
  `chat_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '聊天时间',
  `direction` varchar(10) NOT NULL DEFAULT '' COMMENT '反向:IN, 前向:OUT',
  `lng` decimal(21,18) DEFAULT NULL COMMENT '经度',
  `lat` decimal(21,18) DEFAULT NULL COMMENT '纬度',
  `status` varchar(50) NOT NULL DEFAULT '' COMMENT '前向消息状态:WAIT_SEND/SENDING/SN_RECEIVE',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_terminal_id` (`terminal_id`),
  KEY `idx_msg_id` (`msg_id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='聊天记录';

INSERT INTO cc_msg_2024_03_10 (user_id, terminal_id, seq, msg_id, payload, chat_time, direction, status)
VALUES(104, 37, 134, 'ce53d8996b334217933262ba29810a90', '消息内容-1', '2024-03-14 10:23:55', 'IN', 'ALREADY_RECEIVED');
