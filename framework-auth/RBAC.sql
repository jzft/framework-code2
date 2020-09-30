CREATE TABLE `t_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记位',
  `permission` varchar(255) DEFAULT NULL COMMENT '权限名称',
  `url` varchar(255) NOT NULL COMMENT '资源路径',
  `perm` varchar(255) DEFAULT NULL COMMENT '权限代码',
  `parent_id` int(11) DEFAULT NULL COMMENT '父权限id',
  `sys_type` varchar(255) DEFAULT NULL COMMENT '系统类型',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `icon` varchar(50) DEFAULT NULL COMMENT '图标',
  `order_num` int(11) DEFAULT NULL COMMENT '显示排序',
  `menu_type` int(2) DEFAULT NULL COMMENT '菜单类型 1-菜单 2-按钮',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `visible` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可见',
  `cacheable` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可以缓存',
  `available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;





CREATE TABLE `t_properties_extra` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `table_name` varchar(50) DEFAULT NULL COMMENT '表名',
  `ref_id` int(11) DEFAULT NULL COMMENT '关联外表的主键id',
  `p_name` varchar(50) DEFAULT NULL COMMENT '属性名称',
  `p_value` varchar(255) DEFAULT NULL COMMENT '属性值',
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `t_name_ref_key_ind` (`table_name`,`ref_id`,`p_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `t_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '角色名称',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `role_code` varchar(50) NOT NULL COMMENT '角色代码',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `visible` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可见',
  `available` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否有效',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;


CREATE TABLE `t_role_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_datetime` datetime DEFAULT NULL,
  `delete_flag` int(11) DEFAULT '0',
  `permission_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `role_permission_ind` (`role_id`,`permission_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;



CREATE TABLE `t_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '用户名',
  `nick_name` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '昵称',
  `pwd` varchar(255) CHARACTER SET utf8 DEFAULT NULL COMMENT '密码',
  `type` int(11) DEFAULT NULL COMMENT '类型',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除标记',
  `parent_id` int(11) DEFAULT NULL COMMENT '父id',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `visible` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否可见',
  PRIMARY KEY (`id`),
  KEY `username_ind` (`user_name`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;



CREATE TABLE `t_user_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_datetime` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `delete_flag` tinyint(1) NOT NULL DEFAULT '0' COMMENT '删除标记',
  `role_id` int(11) NOT NULL COMMENT '角色id',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`),
  KEY `user_role_ind` (`user_id`,`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;