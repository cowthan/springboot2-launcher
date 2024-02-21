-- demos.tiny_id_info definition

CREATE TABLE `tiny_id_info` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `biz_type` varchar(63) NOT NULL DEFAULT '' COMMENT '业务类型，唯一',
  `begin_id` bigint NOT NULL DEFAULT '0' COMMENT '开始id，仅记录初始值，无其他含义。初始化时begin_id和max_id应相同',
  `max_id` bigint NOT NULL DEFAULT '0' COMMENT '当前最大id',
  `step` int DEFAULT '0' COMMENT '步长',
  `delta` int NOT NULL DEFAULT '1' COMMENT '每次id增量',
  `remainder` int NOT NULL DEFAULT '0' COMMENT '余数',
  `create_time` timestamp NOT NULL DEFAULT '2010-01-01 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '2010-01-01 00:00:00' COMMENT '更新时间',
  `version` bigint NOT NULL DEFAULT '0' COMMENT '版本号',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uniq_biz_type` (`biz_type`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COMMENT='id信息表';


-- demos.tiny_id_token definition

CREATE TABLE `tiny_id_token` (
  `id` int unsigned NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `token` varchar(255) NOT NULL DEFAULT '' COMMENT 'token',
  `biz_type` varchar(63) NOT NULL DEFAULT '' COMMENT '此token可访问的业务类型标识',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `create_time` timestamp NOT NULL DEFAULT '2010-01-01 00:00:00' COMMENT '创建时间',
  `update_time` timestamp NOT NULL DEFAULT '2010-01-01 00:00:00' COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 COMMENT='token信息表';

INSERT INTO tiny_id_info (id, biz_type, begin_id, max_id, step, delta, remainder, create_time, update_time, version) VALUES(2, 'blog', 1, 100001, 100000, 2, 1, '2018-07-21 23:52:58', '2023-02-15 18:57:47', 4);
INSERT INTO tiny_id_token (id, token, biz_type, remark, create_time, update_time) VALUES(2, '0f673adf80504e2eaa552f5d791b644c', 'blog', '1', '2017-12-14 16:36:46', '2017-12-14 16:36:48');
