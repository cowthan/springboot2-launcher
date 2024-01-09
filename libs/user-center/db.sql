-- Create syntax for TABLE 't_user'
CREATE TABLE `t_user` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` bigint(20) NOT NULL DEFAULT '0',
  `username` varchar(100) NOT NULL DEFAULT '' COMMENT '用户名',
  `password` varchar(200) NOT NULL DEFAULT '' COMMENT '密码',
  `uid` varchar(100) NOT NULL DEFAULT '' COMMENT 'user id  微信openId',
  `sid` varchar(100) NOT NULL DEFAULT '' COMMENT '短id，一般是4位数，根据用户数量加长',
  `big_role` varchar(24) NOT NULL DEFAULT '' COMMENT '大角色，admin，dev，app',
  `app_role` varchar(100) NOT NULL DEFAULT '' COMMENT '角色，逗号分隔',
  `admin_role` varchar(100) NOT NULL DEFAULT '' COMMENT '角色，逗号分隔',
  `gender` tinyint(3) NOT NULL DEFAULT '0' COMMENT '性别，0表示未知，1表示男，2女表示女',
  `nickname` varchar(64) NOT NULL DEFAULT '' COMMENT '昵称',
  `head_icon` varchar(1024) NOT NULL DEFAULT '' COMMENT '姓名',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态：1可用, 0禁用',
  `deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '添加时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_uid` (`uid`) USING BTREE,
  UNIQUE KEY `idx_sid` (`sid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户表';

INSERT INTO `t_user` (`id`, `app_id`, `username`, `password`, `gender`, `nickname`, `head_icon`, `uid`, `sid`, `big_role`, `app_role`, `admin_role`, `status`, `deleted`, `gmt_create`, `gmt_modified`)
VALUES
	(1, 1, '13333333333', '123456', 0, '二渠', 'http://image.bighole.club/magic/202107/14/1626192738701_8041.png', '1', '1', 'admin', '', '', 1, 0, '2021-06-20 00:31:34', '2021-06-20 14:53:48');


-- Create syntax for TABLE 't_user_profile'
CREATE TABLE `t_user_profile` (
  `id` bigint(20) unsigned NOT NULL COMMENT '主键',
  `app_id` bigint(20) NOT NULL DEFAULT '0',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `username` varchar(100) NOT NULL DEFAULT '' COMMENT '账号，冗余字段',
  `nickname` varchar(64) NOT NULL DEFAULT '' COMMENT '昵称',
  `head_icon` varchar(1024) NOT NULL DEFAULT '' COMMENT '姓名',
  `signature` varchar(256) NOT NULL DEFAULT '' COMMENT '签名',
  `gender` tinyint(3) NOT NULL DEFAULT '0' COMMENT '性别，0表示未知，1表示男，2女表示女',
  `email` varchar(100) NOT NULL DEFAULT '' COMMENT '邮箱',
  `age` int(9) NOT NULL DEFAULT '0' COMMENT '年龄',
  `birth` varchar(24) NOT NULL DEFAULT '' COMMENT '生日',
  `mobile` varchar(32) NOT NULL DEFAULT '' COMMENT '用户mobile，最大长度32字符，非中国大陆手机号码需要填写国家代码(如美国：+1-xxxxxxxxxx)或地区代码(如香港：+852-xxxxxxxx)，可设置为空字符串',
  `address` varchar(200) NOT NULL DEFAULT '' COMMENT '地址',
  `extra` varchar(1024) NOT NULL DEFAULT '' COMMENT '扩展字段，json',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '状态：1可用, 0禁用',
  `deleted` tinyint(3) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `lat` double(10,6) NOT NULL DEFAULT '0.000000',
  `lon` double(10,6) NOT NULL DEFAULT '0.000000',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_user_id` (`user_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='用户资料表';

INSERT INTO `t_user_profile` (`id`, `app_id`, `user_id`, `username`, `nickname`, `head_icon`, `signature`, `gender`, `email`, `age`, `birth`, `mobile`, `address`, `extra`, `status`, `deleted`, `gmt_create`, `gmt_modified`, `lat`, `lon`)
VALUES
	(1, 1, 1, '13333333333', '二渠', 'http://image.bighole.club/magichorn2021-06/20/1624165737700_9721.png', '', 0, '13333333333@qq.com', 0, '', '13333333333', '', '', 1, 0, '2021-06-20 00:32:23', '2021-06-20 15:08:52', 0.000000, 0.000000);

CREATE TABLE `t_user_session` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` bigint(20) NOT NULL DEFAULT '0',
  `k` varchar(256) NOT NULL DEFAULT '' COMMENT 'key',
  `v` text NOT NULL COMMENT 'value',
  `die_time` int(11) NOT NULL DEFAULT '0' COMMENT '-1永久，0立即过期，正数秒，表示过期时间',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '1正常 0禁用',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '乐观锁',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_k` (`k`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='session存储';


CREATE TABLE `t_admin_menu` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `app_id` bigint(20) NOT NULL DEFAULT '0' comment '菜单所属应用，以此字段提供不同应用有不同菜单的功能',
  `big_role` varchar(64) NOT NULL DEFAULT '' COMMENT 'admin或dev，表示哪个管理后台',
  `menus` text NOT NULL COMMENT '菜单配置，一个大json',
  `status` tinyint(3) unsigned NOT NULL DEFAULT '1' COMMENT '1正常 0禁用',
  `gmt_create` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '添加时间',
  `gmt_modified` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='管理后台菜单';

INSERT INTO demos2.t_admin_menu
(id, app_id, big_role, menus, status, gmt_create, gmt_modified)
VALUES(1, 1, 'admin', ' [
    {
      "name": "说明文档",
      "path": "/dev/sys",
      "hidden": false,
      "redirect": "noRedirect",
      "component": "Layout",
      "alwaysShow": true,
      "meta": {
        "title": "说明文档",
        "icon": "log",
        "noCache": false
      },
      "children": [
        {
          "name": "doc",
          "path": "doc",
          "hidden": false,
          "component": "dev/sys/doc",
          "meta": {
            "title": "使用手册",
            "icon": "dashboard",
            "noCache": false
          }
        }
      ]
    },
    {
      "name": "页面构建",
      "path": "/dev/forms",
      "hidden": false,
      "redirect": "noRedirect",
      "component": "Layout",
      "alwaysShow": true,
      "meta": {
        "title": "页面构建",
        "icon": "log",
        "noCache": false
      },
      "children": [
        {
          "name": "form_build",
          "path": "form_build",
          "hidden": false,
          "component": "dev/form_build/index",
          "meta": {
            "title": "表单生成",
            "icon": "log",
            "noCache": false
          }
        }
      ]
    },
    {
      "name": "表单示例",
      "path": "/dev/send",
      "hidden": false,
      "redirect": "noRedirect",
      "component": "Layout",
      "alwaysShow": true,
      "meta": {
        "title": "表单示例",
        "icon": "log",
        "noCache": false
      },
      "children": [

        {
          "name": "long-task.vue",
          "path": "long-task.vue",
          "hidden": false,
          "component": "dev/send/long-task.vue",
          "meta": {
            "title": "长任务",
            "icon": "log",
            "noCache": false
          }
        }
      ]
    },



    {
      "name": "开发者工具",
      "path": "/dev/tools",
      "hidden": false,
      "redirect": "noRedirect",
      "component": "Layout",
      "alwaysShow": true,
      "meta": {
        "title": "开发者工具",
        "icon": "log",
        "noCache": false
      },
      "children": [
        {
          "name": "bytes-lookup",
          "path": "bytes-lookup",
          "hidden": false,
          "component": "dev/tools/bytes-lookup",
          "meta": {
            "title": "美化字节流",
            "icon": "log",
            "noCache": false
          }
        },
        {
          "name": "data-convert",
          "path": "data-convert",
          "hidden": false,
          "component": "dev/tools/data-convert",
          "meta": {
            "title": "数据转换",
            "icon": "log",
            "noCache": false
          }
        }

      ]
    }
  ]', 1, '2023-02-09 22:52:10', '2024-01-09 21:03:10');
