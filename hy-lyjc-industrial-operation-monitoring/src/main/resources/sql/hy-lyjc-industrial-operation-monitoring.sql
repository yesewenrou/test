CREATE DATABASE IF NOT EXISTS `hy_lyjc_industrial_operation_monitoring` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE `hy_lyjc_industrial_operation_monitoring`;

CREATE TABLE IF NOT EXISTS `hy_menu`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT,
    `name`        varchar(32)         DEFAULT NULL COMMENT '菜单名称',
    `code`        varchar(32)         DEFAULT NULL COMMENT '唯一标识，类似于code编码',
    `path`        varchar(128)        DEFAULT NULL COMMENT '菜单路径',
    `permission`  varchar(1000)       DEFAULT NULL COMMENT '权限名称，用于注解校验',
    `parent_id`   int(11)    NOT NULL COMMENT '父级菜单',
    `module_type` varchar(32)         DEFAULT NULL COMMENT '菜单权限类型，标记是page还是button',
    `icon`        varchar(64)         DEFAULT NULL COMMENT '菜单图标',
    `mark`        varchar(256)        DEFAULT NULL COMMENT '备注',
    `weight`      int(11)             DEFAULT '0' COMMENT '权重，用于给菜单排序',
    `create_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_code` (`code`)
)
;

CREATE TABLE IF NOT EXISTS `hy_role`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT,
    `name`        varchar(32)         DEFAULT NULL COMMENT '角色名',
    `status`      tinyint(4)          DEFAULT '1' COMMENT '状态，1：启用 0：禁用',
    `deleted`     tinyint(4)          DEFAULT '0' COMMENT '标记删除，1：已删除 0：未删除',
    `create_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
)
;

CREATE TABLE IF NOT EXISTS `hy_role_menu`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT,
    `role_id`     bigint(11) NOT NULL COMMENT '角色ID',
    `menu_id`     bigint(11) NOT NULL COMMENT '菜单ID',
    `create_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
)
;

CREATE TABLE IF NOT EXISTS `hy_user_role`
(
    `id`          bigint(11) NOT NULL AUTO_INCREMENT,
    `user_id`     bigint(11) NOT NULL COMMENT '用户ID',
    `role_id`     bigint(11) NOT NULL COMMENT '角色ID',
    `create_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp  NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
)
;

CREATE TABLE IF NOT EXISTS `hy_complaint_manage`
(
    `id`               int(11)      NOT NULL AUTO_INCREMENT,
    `complaint_number` varchar(32)  NOT NULL COMMENT '投诉编号',
    `complaint_object` varchar(32)  NOT NULL COMMENT '被投诉对象',
    `complainant`      varchar(32)  NOT NULL COMMENT '投诉人',
    `sex`              varchar(32)           DEFAULT '' COMMENT '性别',
    `mobile`           varchar(32)  NOT NULL COMMENT '手机号',
    `content`          varchar(512) NOT NULL COMMENT '投诉内容',
    `certificate`      varchar(512) NOT NULL DEFAULT '' COMMENT '投诉图片凭证',
    `type`             varchar(32)  NOT NULL COMMENT '投诉分类',
    `channel`          varchar(32)  NOT NULL COMMENT '投诉渠道',
    `complaint_time`   date         NOT NULL COMMENT '投诉时间',
    `status`           tinyint(4)   NOT NULL DEFAULT '1' COMMENT '1：未处理，2：受理中，3：不受理，4：已完成',
    `create_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_complaint_number` (`complaint_number`) USING BTREE COMMENT '投诉编号唯一性'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `hy_data_resource`
(
    `id`           int(11)     NOT NULL AUTO_INCREMENT,
    `scenic_name`  varchar(32) NOT NULL COMMENT '景区名称',
    `country_name` varchar(32) NOT NULL COMMENT '国家',
    `prov_name`    varchar(32) NOT NULL DEFAULT '' COMMENT '省份',
    `city_name`    varchar(32) NOT NULL DEFAULT '' COMMENT '城市',
    `people_num`   int(11)     NOT NULL COMMENT '游客人数',
    `time`         date        NOT NULL COMMENT '统计日期',
    `create_time`  timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `datasource`   varchar(32) NOT NULL DEFAULT '眉山移动' COMMENT '数据来源',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `hy_handle_result`
(
    `id`                        int(11)      NOT NULL AUTO_INCREMENT,
    `complaint_id`              bigint(20)   NOT NULL COMMENT '投诉ID',
    `complaint_object_fullname` varchar(32)  NOT NULL DEFAULT '' COMMENT '被投诉对象全称',
    `industry_type`             varchar(32)  NOT NULL DEFAULT '' COMMENT '投诉行业分类',
    `reject_reason`             varchar(512) NOT NULL DEFAULT '' COMMENT '不受理原因',
    `assignee`                  varchar(32)  NOT NULL COMMENT '初审经办人，包含 已受理和不受理两种情况 ',
    `assignee_time`             timestamp    NOT NULL COMMENT '初审处理时间',
    `handler`                   varchar(32)  NOT NULL DEFAULT '' COMMENT '最终处理人，即填写处理方案',
    `handle_time`               timestamp    NULL     DEFAULT NULL COMMENT '最终处理时间',
    `handler_result`            varchar(512) NOT NULL DEFAULT '' COMMENT '最终处理结果',
    `create_time`               timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`               timestamp    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_complaint_id` (`complaint_id`) USING BTREE COMMENT '投诉单唯一'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `hy_industry_manage`
(
    `id`                INT(11)        NOT NULL AUTO_INCREMENT,
    `name`              VARCHAR(32)    NOT NULL COMMENT '资源名称',
    `area`              VARCHAR(32)    NOT NULL COMMENT '所属区域',
    `operating_subject` VARCHAR(32)    NOT NULL COMMENT '经营主体',
    `location`          VARCHAR(128)   NOT NULL COMMENT '地理位置',
    `type`              VARCHAR(32)    NOT NULL COMMENT '行业类型',
    `longitude`         DECIMAL(10, 6) NULL     DEFAULT NULL COMMENT '经度',
    `latitude`          DECIMAL(10, 6) NULL     DEFAULT NULL COMMENT '纬度',
    `service_phone`     VARCHAR(32)    NULL     DEFAULT NULL COMMENT '服务电话',
    `create_time`       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `unique_name` (`name`) USING BTREE
)
;

CREATE TABLE IF NOT EXISTS `hy_public_resource`
(
    `id`            INT(11)        NOT NULL AUTO_INCREMENT,
    `name`          VARCHAR(32)    NOT NULL COMMENT '资源名称',
    `area`          VARCHAR(32)    NOT NULL COMMENT '所属区域',
    `location`      VARCHAR(128)   NOT NULL COMMENT '地理位置',
    `type`          VARCHAR(32)    NOT NULL COMMENT '资源类型',
    `longitude`     DECIMAL(10, 6) NULL     DEFAULT NULL COMMENT '经度',
    `latitude`      DECIMAL(10, 6) NULL     DEFAULT NULL COMMENT '纬度',
    `service_phone` VARCHAR(32)    NULL     DEFAULT NULL COMMENT '服务电话',
    `create_time`   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `unique_name` (`name`) USING BTREE
)
;

CREATE TABLE IF NOT EXISTS `hy_tourism_income`
(
    `id`            int(11)        NOT NULL AUTO_INCREMENT,
    `income`        decimal(10, 2) NOT NULL COMMENT '收入金额',
    `income_source` varchar(32)    NOT NULL COMMENT '收入来源',
    `scenic_name`   varchar(32)    NOT NULL DEFAULT '' COMMENT '所属区域',
    `time`          date           NOT NULL COMMENT '统计日期',
    `create_time`   timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   timestamp      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `hy_tourism_resource`
(
    `id`            INT(11)        NOT NULL AUTO_INCREMENT,
    `name`          VARCHAR(32)    NOT NULL COMMENT '资源名称',
    `area`          VARCHAR(32)    NOT NULL COMMENT '所属区域',
    `location`      VARCHAR(128)   NOT NULL COMMENT '地理位置',
    `type`          VARCHAR(32)    NOT NULL COMMENT '资源类型',
    `longitude`     DECIMAL(10, 6) NULL     DEFAULT NULL COMMENT '经度',
    `latitude`      DECIMAL(10, 6) NULL     DEFAULT NULL COMMENT '纬度',
    `service_phone` VARCHAR(32)    NULL     DEFAULT NULL COMMENT '服务电话',
    `create_time`   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `unique_name` (`name`) USING BTREE
)
;

CREATE TABLE IF NOT EXISTS `hy_county_day_source_city`
(
    `id`           int(11)     NOT NULL AUTO_INCREMENT,
    `scenic_name`  varchar(32) NOT NULL DEFAULT '洪雅县' COMMENT '景区名称',
    `country_name` varchar(32) NOT NULL DEFAULT '中国' COMMENT '国家',
    `prov_name`    varchar(32) NOT NULL DEFAULT '' COMMENT '省份',
    `city_name`    varchar(32) NOT NULL DEFAULT '' COMMENT '城市',
    `people_num`   int(11)     NOT NULL COMMENT '游客人数',
    `time`         date        NOT NULL COMMENT '统计日期',
    `create_time`  timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `datasource`   varchar(32) NOT NULL DEFAULT '眉山移动' COMMENT '数据来源',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 318
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `hy_day_local`
(
    `id`          int(11)     NOT NULL AUTO_INCREMENT,
    `scenic_id`   varchar(32) NOT NULL COMMENT '景区ID',
    `scenic_name` varchar(32) NOT NULL COMMENT '景区名称',
    `people_num`  int(11)     NOT NULL COMMENT '游客人数',
    `member_type` int(11)     NOT NULL COMMENT '人员类型',
    `time`        date        NOT NULL COMMENT '统计日期',
    `create_time` timestamp   NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `datasource`  varchar(32) NOT NULL DEFAULT '眉山移动' COMMENT '数据来源',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8;

CREATE TABLE IF NOT EXISTS `hy_tourist_num_newest`
(
    `id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `scenic_id`    VARCHAR(50)         NOT NULL COMMENT '景区ID',
    `scenic_name`  VARCHAR(50)         NOT NULL COMMENT '景区名称',
    `people_num`   INT(10) UNSIGNED    NOT NULL COMMENT '人数',
    `member_type`  INT(10) UNSIGNED    NOT NULL DEFAULT '0' COMMENT '人员类型：0为游客 1 为居住 2为上班 ',
    `gmt_create`   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_scenic_id` (`scenic_id`),
    INDEX `idx_member_type` (`member_type`)
)
    COMMENT ='最新游客数量'
;

CREATE TABLE IF NOT EXISTS `hy_hour_local`
(
    `id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `scenic_id`    VARCHAR(50)         NOT NULL COMMENT '景区ID',
    `scenic_name`  VARCHAR(50)         NOT NULL COMMENT '景区名称',
    `people_num`   INT(10) UNSIGNED    NOT NULL COMMENT '人数',
    `member_type`  INT(10) UNSIGNED    NOT NULL COMMENT '人员类型:0为游客 1 为居住 2为上班',
    `time`         DATETIME            NOT NULL COMMENT '统计时间',
    `gmt_create`   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_scenic_id_time` (`scenic_id`, `time`),
    INDEX `idx_scenic_id` (`scenic_id`),
    INDEX `idx_time` (`time`)
)
    COMMENT ='各景区每小时游客数'
;

CREATE TABLE IF NOT EXISTS `hy_county_hour_local`
(
    `id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `scenic_id`    VARCHAR(50)         NOT NULL COMMENT '景区ID',
    `scenic_name`  VARCHAR(50)         NOT NULL COMMENT '景区名称',
    `people_num`   INT(10) UNSIGNED    NOT NULL COMMENT '人数',
    `member_type`  INT(10) UNSIGNED    NOT NULL COMMENT '人员类型:0为游客 1 为居住 2为上班',
    `time`         DATETIME            NOT NULL COMMENT '统计时间',
    `gmt_create`   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_scenic_id_time` (`scenic_id`, `time`),
    INDEX `idx_scenic_id` (`scenic_id`),
    INDEX `idx_time` (`time`)
)
    COMMENT ='县域每小时游客数'
;

CREATE TABLE IF NOT EXISTS `scenic_tourist_capacity_config`
(
    `id`                  BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `scenic_code`         VARCHAR(50)         NOT NULL COMMENT '景区编码',
    `saturation_capacity` INT(10) UNSIGNED    NOT NULL COMMENT '饱和容量',
    `overload_capacity`   INT(10) UNSIGNED    NOT NULL COMMENT '超负荷容量',
    `gmt_create`          DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`        DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_scenic_code` (`scenic_code`)
)
    COMMENT ='景区游客承载量配置'
;

CREATE TABLE IF NOT EXISTS `emergency_resource`
(
    `id`           BIGINT(20) UNSIGNED     NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`         VARCHAR(100)            NOT NULL COMMENT '资源名称',
    `type`         VARCHAR(50)             NOT NULL COMMENT '资源类型编码：数据字典',
    `inventory`    DECIMAL(10, 2) UNSIGNED NOT NULL COMMENT '资源数量',
    `unit`         VARCHAR(50)             NOT NULL COMMENT '资源数量单位',
    `gmt_create`   DATETIME                NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME                NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
)
    COMMENT ='应急资源管理'
;

CREATE TABLE IF NOT EXISTS `duty_roster`
(
    `id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `duty_time`    VARCHAR(50)         NOT NULL COMMENT '值班时间',
    `duty_person`  VARCHAR(50)         NULL     DEFAULT NULL COMMENT '值班人员',
    `contact`      VARCHAR(50)         NULL     DEFAULT NULL COMMENT '联系方式',
    `gmt_create`   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `uk_duty_time` (`duty_time`)
)
    COMMENT ='指挥调度值班表'
;

CREATE TABLE IF NOT EXISTS `emergency_plan`
(
    `id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `plan_name`    VARCHAR(50)         NOT NULL COMMENT '预案名称',
    `event_type`   VARCHAR(50)         NOT NULL COMMENT '事件类型',
    `event_level`  VARCHAR(50)         NOT NULL COMMENT '事件等级',
    `modifier_id`  BIGINT(20) UNSIGNED NOT NULL COMMENT '修改人ID',
    `gmt_create`   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_event_type` (`event_type`),
    INDEX `idx_event_level` (`event_level`)
)
    COMMENT ='应急预案管理'
;

CREATE TABLE IF NOT EXISTS `emergency_plan_attachment`
(
    `id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `plan_id`      BIGINT(20) UNSIGNED NOT NULL COMMENT '应急预案主键ID',
    `file_name`    VARCHAR(100)        NOT NULL COMMENT '文件名',
    `file_url`     VARCHAR(200)        NOT NULL COMMENT '文件地址',
    `gmt_create`   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_plan_id` (`plan_id`)
)
    COMMENT ='应急预案附件'
;

CREATE TABLE IF NOT EXISTS `emergency_drill`
(
    `id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `drill_title`  VARCHAR(50)         NOT NULL COMMENT '演练标题',
    `drill_date`   DATE                NOT NULL COMMENT '演练日期',
    `department`   VARCHAR(100)        NOT NULL COMMENT '参与部门',
    `event_type`   VARCHAR(50)         NOT NULL COMMENT '事件类型',
    `event_level`  VARCHAR(50)         NOT NULL COMMENT '事件等级',
    `gmt_create`   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_drill_date` (`drill_date`),
    INDEX `idx_event_type` (`event_type`),
    INDEX `idx_event_level` (`event_level`)
)
    COMMENT ='应急演练'
;

CREATE TABLE IF NOT EXISTS `emergency_drill_attachment`
(
    `id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `drill_id`     BIGINT(20) UNSIGNED NOT NULL COMMENT '应急演练主键ID',
    `file_name`    VARCHAR(100)        NOT NULL COMMENT '文件名',
    `file_url`     VARCHAR(200)        NOT NULL COMMENT '文件地址',
    `gmt_create`   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_drill_id` (`drill_id`)
)
    COMMENT ='应急演练附件'
;

CREATE TABLE IF NOT EXISTS `emergency_event`
(
    `id`               BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `event_name`       VARCHAR(50)         NOT NULL COMMENT '事件名称',
    `event_type`       VARCHAR(50)         NOT NULL COMMENT '事件类型',
    `event_level`      VARCHAR(50)         NOT NULL COMMENT '事件等级',
    `event_address`    VARCHAR(100)        NOT NULL COMMENT '事件地址',
    `event_content`    VARCHAR(255)        NOT NULL COMMENT '事件内容',
    `event_time`       DATETIME            NOT NULL COMMENT '事发时间',
    `contact`          VARCHAR(50)         NOT NULL COMMENT '联系方式',
    `event_status`     TINYINT(4) UNSIGNED NOT NULL COMMENT '事件状态 0-待审核、1-待处理、2-待结案、3-已结案、4-未通过',
    `check_status`     TINYINT(4) UNSIGNED NULL     DEFAULT NULL COMMENT '审核状态 0-通过、1-未通过',
    `check_content`    VARCHAR(255)        NULL     DEFAULT NULL COMMENT '审核状态原因描述',
    `check_user_id`    BIGINT(20) UNSIGNED NULL     DEFAULT NULL COMMENT '审核人员ID',
    `check_time`       DATETIME            NULL     DEFAULT NULL COMMENT '审核时间',
    `assigned_user_id` BIGINT(20) UNSIGNED NULL     DEFAULT NULL COMMENT '被指派人员ID',
    `assign_time`      DATETIME            NULL     DEFAULT NULL COMMENT '指派时间',
    `feedback_content` VARCHAR(255)        NULL     DEFAULT NULL COMMENT '反馈结果',
    `feedback_time`    DATETIME            NULL     DEFAULT NULL COMMENT '反馈时间',
    `close_content`    VARCHAR(255)        NULL     DEFAULT NULL COMMENT '结案处理结果',
    `close_time`       DATETIME            NULL     DEFAULT NULL COMMENT '结案时间',
    `gmt_create`       DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`     DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_event_type` (`event_type`),
    INDEX `idx_event_level` (`event_level`),
    INDEX `idx_event_status` (`event_status`)
)
    COMMENT ='应急事件管理'
;

CREATE TABLE IF NOT EXISTS `emergency_event_attachment`
(
    `id`           BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `event_id`     BIGINT(20) UNSIGNED NOT NULL COMMENT '应急事件主键ID',
    `type`         VARCHAR(50)         NOT NULL COMMENT '附件类型',
    `file_name`    VARCHAR(100)        NOT NULL COMMENT '文件名',
    `file_url`     VARCHAR(200)        NOT NULL COMMENT '文件地址',
    `gmt_create`   DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_event_id` (`event_id`)
)
    COMMENT ='应急事件附件'
;

-- 专题报告
CREATE TABLE IF NOT EXISTS `special_report`
(
    `id`              BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name`            VARCHAR(50)         NOT NULL COMMENT '专题报告名称',
    `type`            VARCHAR(50)         NOT NULL COMMENT '专题报告类型',
    `attachment_name` VARCHAR(100)        NOT NULL COMMENT '附件名称',
    `attachment_url`  VARCHAR(500)        NOT NULL COMMENT '附件地址',
    `report_time`     DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '生成日期',
    `gmt_create`      DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`    DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_type` (`type`)
)
    COMMENT ='专题报告'
;

-- 文化活动
DROP TABLE IF EXISTS `hy_cultural_activity`;
CREATE TABLE `hy_cultural_activity`
(
    `id`              bigint(20)                              NOT NULL AUTO_INCREMENT,
    `cultural_name`   varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '文化名城',
    `cultural_time`   datetime                                NOT NULL,
    `cover_photo_url` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '封面图片，可以不传',
    `file_urls`       varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '附件地址可以多个',
    `file_names`      varchar(255) COLLATE utf8mb4_general_ci NOT NULL COMMENT '附件名称，可以多个',
    `create_time`     datetime                                NOT NULL COMMENT '创建时间',
    `update_time`     datetime                                DEFAULT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='文化活动';

-- 接口管理
CREATE TABLE `api_manage`
(
    `id`                BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `interface_name`    VARCHAR(200)        NOT NULL COMMENT '接口名称',
    `interface_desc`    VARCHAR(1000)       NOT NULL COMMENT '接口描述',
    `data_source`       VARCHAR(50)         NOT NULL COMMENT '数据来源',
    `interface_address` VARCHAR(500)        NOT NULL COMMENT '接口地址',
    `gmt_create`        DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`      DATETIME            NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `uk_interface_name` (`interface_name`) USING BTREE,
    INDEX `idx_data_source` (`data_source`) USING BTREE
)
    COMMENT ='接口管理'
;
