-- demos.t_blog definition

CREATE TABLE `t_blog` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '标题',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '内容',
  `cover_thumb` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '封面',
  `status` int NOT NULL DEFAULT '1' COMMENT '状态',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='blog';

INSERT INTO t_blog (id, title, content, cover_thumb, status, create_time, update_time) VALUES(1, '111', '11111111', '22222222', 1, '2023-01-12 16:38:26', '2023-01-12 16:38:26');
INSERT INTO t_blog (id, title, content, cover_thumb, status, create_time, update_time) VALUES(2, '111', '11111111', '22222222', 1, '2023-01-12 16:40:19', '2023-01-12 16:40:19');
INSERT INTO t_blog (id, title, content, cover_thumb, status, create_time, update_time) VALUES(3, '111', '11111111', '22222222', 1, '2023-01-12 16:41:13', '2023-01-12 16:41:13');
INSERT INTO t_blog (id, title, content, cover_thumb, status, create_time, update_time) VALUES(4, '111', '11111111', '22222222', 1, '2023-01-12 16:43:36', '2023-01-12 16:43:36');
INSERT INTO t_blog (id, title, content, cover_thumb, status, create_time, update_time) VALUES(5, '111', '11111111', '22222222', 1, '2023-01-12 16:44:28', '2023-01-12 16:44:28');
