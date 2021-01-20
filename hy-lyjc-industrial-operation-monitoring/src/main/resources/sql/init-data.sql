USE `hy_lyjc_industrial_operation_monitoring`;

-- 菜单数据
INSERT INTO `hy_menu` (`id`, `name`, `code`, `path`, `permission`, `parent_id`, `module_type`, `icon`, `mark`, `weight`,
                       `create_time`, `update_time`)
VALUES (1, '资源管理', '01', '/home/ResourceManagement', NULL, 0, 'page', 'nav_zygl_n', NULL, 4, '2019-08-15 11:13:10',
        '2019-08-15 11:13:21'),
       (2, '旅游资源', '0101', '/home/ResourceManagement/Tourismmanagement', NULL, 1, 'page', NULL, NULL, 3,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (3, '公共资源', '0102', '/home/ResourceManagement/commonResource', NULL, 1, 'page', NULL, NULL, 4,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (4, '景区游客数据', '0103', '/home/ResourceManagement/dataResource', NULL, 1, 'page', NULL, NULL, 7,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (5, '产业管理', '02', '/home/industry', NULL, 0, 'page', 'nav_zygl', NULL, 2, '2019-08-15 11:13:10',
        '2019-08-15 11:13:21'),
       (6, '行业管理', '0201', '/home/industry/industryManagement', NULL, 5, 'page', NULL, NULL, 1, '2019-08-15 11:13:10',
        '2019-08-15 11:13:21'),
       (7, '投诉管理', '0202', '/home/industry/complainManagement', NULL, 5, 'page', NULL, NULL, 2, '2019-08-15 11:13:10',
        '2019-08-15 11:13:21'),
       (8, '决策分析', '03', '/home/StrategicAnalysis', NULL, 0, 'page', 'nav_jcfx', NULL, 3, '2019-08-15 11:13:10',
        '2019-08-15 11:13:21'),
       (9, '假日统计分析', '0301', '/home/StrategicAnalysis/HolidayStatisticalAnalysis', NULL, 8, 'page', NULL, NULL, 1,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (10, '历史数据统计', '0302', '/home/StrategicAnalysis/historicalData', NULL, 8, 'page', NULL, NULL, 4,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (11, '平台管理系统', '04', '/home/platform', NULL, 0, 'page', 'nav_group_n', NULL, 6, '2019-08-15 11:13:10',
        '2019-08-15 11:13:21'),
       (12, '平台用户', '0401', '/home/platform/user', NULL, 11, 'page', NULL, NULL, 2, '2019-09-12 16:43:05',
        '2019-09-12 16:43:05'),
       (13, '角色权限', '0402', '/home/platform/role', NULL, 11, 'page', NULL, NULL, 3, '2019-09-12 16:43:05',
        '2019-09-12 16:43:05'),
       (14, '查看', '010101', '-', 'tourismResource:list,tourismResource:findById,tourismResource:resourceTypeList', 2,
        'button', NULL, NULL, NULL, '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (15, '编辑', '010102', '-', 'tourismResource:update', 2, 'button', NULL, NULL, NULL, '2019-09-12 16:53:54',
        '2019-09-12 16:53:56'),
       (16, '删除', '010103', '-', 'tourismResource:delete', 2, 'button', NULL, NULL, NULL, '2019-09-12 16:53:54',
        '2019-09-12 16:53:56'),
       (17, '新增', '010104', '-', 'tourismResource:add,tourismResource:resourceTypeList', 2, 'button', NULL, NULL, NULL,
        '2019-09-12 17:05:17', '2019-09-12 17:05:37'),
       (18, '查看', '010201', '-', 'publicResource:list,publicResource:resourceTypeList,publicResource:findById', 3,
        'button', NULL, NULL, NULL, '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (19, '编辑', '010202', '-', 'publicResource:update', 3, 'button', NULL, NULL, NULL, '2019-09-12 16:53:54',
        '2019-09-12 16:53:56'),
       (20, '删除', '010203', '-', 'publicResource:delete', 3, 'button', NULL, NULL, NULL, '2019-09-12 16:53:54',
        '2019-09-12 16:53:56'),
       (21, '新增', '010204', '-', 'publicResource:add,publicResource:resourceTypeList', 3, 'button', NULL, NULL, NULL,
        '2019-09-12 17:05:17', '2019-09-12 17:05:37'),
       (22, '查看', '010301', '-', 'dataResource:list,dataResource:scenicList,traffic:province,traffic:city', 4, 'button',
        NULL, NULL, NULL, '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (23, '查看', '020101', '-', 'industryManage:list,industryManage:industryTypeList,industryManage:findById', 6,
        'button', NULL, NULL, NULL, '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (24, '编辑', '020102', '-', 'industryManage:update', 6, 'button', NULL, NULL, NULL, '2019-09-12 16:53:54',
        '2019-09-12 16:53:56'),
       (25, '删除', '020103', '-', 'industryManage:delete', 6, 'button', NULL, NULL, NULL, '2019-09-12 16:53:54',
        '2019-09-12 16:53:56'),
       (26, '新增', '020104', '-', 'industryManage:add', 6, 'button', NULL, NULL, NULL, '2019-09-12 17:05:17',
        '2019-09-12 17:05:37'),
       (27, '查看', '020201', '-',
        'complaintManage:list,complaintManage:typeList,complaintManage:channelList,complaintManage:findById,complaintManage:statisticsComplaint,complaintManage:chartComplaint,complaintManage:conditionStatisticsComplaint',
        7, 'button', NULL, NULL, NULL, '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (28, '导出', '020202', '-', 'complaintManage:export', 7, 'button', NULL, NULL, NULL, '2019-09-12 17:05:17',
        '2019-09-12 17:05:37'),
       (29, '同意受理', '020203', '-', 'handleResult:agreeHandle', 7, 'button', NULL, NULL, NULL, '2019-09-12 17:05:17',
        '2019-09-12 17:05:37'),
       (30, '拒绝受理', '020204', '-', 'handleResult:disagreeHandle', 7, 'button', NULL, NULL, NULL, '2019-09-12 17:05:17',
        '2019-09-12 17:05:37'),
       (31, '完成受理', '020205', '-', 'handleResult:finishHandle', 7, 'button', NULL, NULL, NULL, '2019-09-12 17:05:17',
        '2019-09-12 17:05:37'),
       (32, '查看', '030101', '-',
        'dataResource:statisticsData,dataResource:conditionStatisticsData,dataResource:holiday,traffic:province,traffic:city,traffic:graph,traffic:table',
        9, 'button', NULL, NULL, NULL, '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (33, '导出', '030102', '-', 'traffic:export,dataResource:export', 9, 'button', NULL, NULL, NULL,
        '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (34, '查看', '030201', '-',
        'dataResource:historyConditionStatisticsData,traffic:historyByGraininess,tourismConsumption:historyConditionStatisticsData',
        10, 'button', NULL, NULL, NULL, '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (35, '导出', '030202', '-', 'dataResource:historyExport,traffic:historyExport', 10, 'button', NULL, NULL, NULL,
        '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (36, '查看', '040101', '-',
        'um:user:list,um:company:list,um:department:findByCompanyId,um:role:findByUserId,um:role:list', 12, 'button',
        NULL, NULL, NULL, '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (37, '角色配置', '040102', '-', 'um:userRole:add,um:role:findByUserId', 12, 'button', NULL, NULL, NULL,
        '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (38, '查看', '040201', '-', 'um:role:list,um:role:info', 13, 'button', NULL, NULL, NULL, '2019-08-15 14:09:06',
        '2019-08-15 14:09:10'),
       (39, '新增', '040202', '-', 'um:role:add', 13, 'button', NULL, NULL, NULL, '2019-08-15 14:10:33',
        '2019-08-15 14:10:35'),
       (40, '编辑', '040203', '-', 'um:role:update', 13, 'button', NULL, NULL, NULL, '2019-08-15 14:12:43',
        '2019-08-15 14:12:43'),
       (41, '禁用', '040204', '-', 'um:role:disable', 13, 'button', NULL, NULL, NULL, '2019-08-15 14:13:38',
        '2019-08-15 14:13:38'),
       (42, '删除', '040205', '-', 'um:role:delete', 13, 'button', NULL, NULL, NULL, '2019-08-15 14:14:22',
        '2019-08-15 14:14:22'),
       (43, '启用', '040206', '-', 'um:role:enable', 13, 'button', NULL, NULL, NULL, '2019-08-15 18:31:38',
        '2019-08-15 18:31:38'),
       (44, '舆情管理', '0203', '/home/industry/publicSentiment', NULL, 5, 'page', '', NULL, 3, '2019-08-15 11:13:10',
        '2019-08-15 11:13:21'),
       (45, '游客画像分析', '0303', '/home/StrategicAnalysis/TouristPortraitsAnalysis', NULL, 8, 'page', NULL, NULL, 2,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (46, '查看', '030301', '-',
        'dataResource:sourceCityTop,hotelResource:stayOvernight,tourismConsumption:industryType', 10, 'button', NULL,
        NULL, NULL, '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (47, '查看', '020301', '-',
        'publicSentiment:list,publicSentiment:sevenPublicSentimentTrend,publicSentiment:thirtyPublicSentimentTrend', 44,
        'button', NULL, NULL, NULL, '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (48, '首页', '05', '/home', NULL, 0, 'page', 'breadcrumb', NULL, 0, '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (49, '运行监测 ', '06', '/home/OperationMonitoring', NULL, 0, 'page', 'nav_yxjc1', NULL, 1, '2019-11-15 10:50:48',
        '2019-11-15 10:50:48'),
       (50, '指挥调度 ', '07', '/home/CommandAndControl', NULL, 0, 'page', 'zhdd1', NULL, 5, '2019-11-15 10:50:48',
        '2019-11-15 10:50:48'),
       (51, '游客热力图', '0501', '/home/OperationMonitoring/TouristHeatMap', NULL, 49, 'page', NULL, NULL, 1,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (52, '酒店入住', '0502', '/home/OperationMonitoring/checkIn', NULL, 49, 'page', NULL, NULL, 2, '2019-08-15 11:13:10',
        '2019-08-15 11:13:21'),
       (53, '旅游消费', '0503', '/home/OperationMonitoring/TourismConsumption', NULL, 49, 'page', NULL, NULL, 3,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (54, '交通停车', '0504', '/home/OperationMonitoring/TrafficParking', NULL, 49, 'page', NULL, NULL, 4,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (55, '视频监控', '0505', '/home/OperationMonitoring/VideoMonitoring', NULL, 49, 'page', NULL, NULL, 5,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (56, '旅游资讯', '0506', '/home/OperationMonitoring/TouristInformation', NULL, 49, 'page', NULL, NULL, 6,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (57, '应急资源管理', '0701', '/home/CommandAndControl/EmergencyResource', NULL, 50, 'page', NULL, NULL, 1,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (58, '值班管理', '0702', '/home/CommandAndControl/DutyManagement', NULL, 50, 'page', NULL, NULL, 2,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (59, '应急预案管理', '0703', '/home/CommandAndControl/ContingencyPlan', NULL, 50, 'page', NULL, NULL, 3,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (60, '应急演练', '0704', '/home/CommandAndControl/EmergencyDrill', NULL, 50, 'page', NULL, NULL, 4,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (61, '应急事件管理', '0705', '/home/CommandAndControl/EmergencyEvents', NULL, 50, 'page', NULL, NULL, 5,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (62, '酒店资源', '0104', '/home/ResourceManagement/HotelResources', NULL, 1, 'page', NULL, NULL, 1,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (63, '旅游消费数据', '0105', '/home/ResourceManagement/TourismConsumptionData', NULL, 1, 'page', NULL, NULL, 5,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (64, '酒店接待数据', '0106', '/home/ResourceManagement/HotelReceptionData', NULL, 1, 'page', NULL, NULL, 6,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (65, '门票收入数据', '0108', '/home/ResourceManagement/TicketRevenueData', NULL, 1, 'page', NULL, NULL, 8,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (66, '电商趋势分析', '0304', '/home/StrategicAnalysis/EcommerceTrend', NULL, 8, 'page', NULL, NULL, 5,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (67, '参数配置', '0403', '/home/platform/parameterConfiguration', NULL, 11, 'page', NULL, NULL, 1,
        '2019-09-12 16:43:05', '2019-09-12 16:43:05'),
       (68, '查看', '040301', '-', 'scenic-tourist-capacity-config:listAll', 67, 'button', NULL, NULL, NULL,
        '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (69, '查看', '010401', '-', 'hotelResource:list', 62, 'button', NULL, NULL, NULL, '2019-09-12 16:53:54',
        '2019-09-12 16:53:56'),
       (70, '编辑', '040302', NULL, 'scenic-tourist-capacity-config:update', 67, 'button', NULL, NULL, 0,
        '2019-11-25 09:51:27', '2019-11-25 09:51:27'),
       (71, '查看', '070101', '-', 'emergency-resource:list', 57, 'button', NULL, NULL, NULL, '2019-09-12 16:53:54',
        '2019-09-12 16:53:56'),
       (72, '新增', '070102', '-', 'emergency-resource:save', 57, 'button', NULL, NULL, NULL, '2019-08-15 14:10:33',
        '2019-08-15 14:10:35'),
       (73, '删除', '070103', '-', 'emergency-resource:delete', 57, 'button', NULL, NULL, NULL, '2019-09-12 16:53:54',
        '2019-09-12 16:53:56'),
       (74, '编辑', '070104', '-', 'emergency-resource:update,emergency-resource:get', 57, 'button', NULL, NULL, NULL,
        '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (75, '查看', '070201', '-', 'duty-roster:listAll', 58, 'button', NULL, NULL, NULL, '2019-09-12 16:53:54',
        '2019-09-12 16:53:56'),
       (76, '编辑', '070202', '-', 'duty-roster:update', 58, 'button', NULL, NULL, NULL, '2019-09-12 16:53:54',
        '2019-09-12 16:53:56'),
       (77, '查看', '010601', '-', 'hotelResource:hotelTouristSourceList,hotelResource:hotelPassengerReceptionList', 64,
        'button', NULL, NULL, NULL, '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (78, '查看', '070301', '-', 'emergency-plan:list,emergency-plan:get', 59, 'button', NULL, NULL, NULL,
        '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (79, '新增', '070302', '-', 'emergency-plan:save', 59, 'button', NULL, NULL, NULL, '2019-08-15 14:10:33',
        '2019-08-15 14:10:35'),
       (80, '删除', '070303', '-', 'emergency-plan:delete', 59, 'button', NULL, NULL, NULL, '2019-09-12 16:53:54',
        '2019-09-12 16:53:56'),
       (81, '编辑', '070304', '-', 'emergency-plan:update', 59, 'button', NULL, NULL, NULL, '2019-09-12 16:53:54',
        '2019-09-12 16:53:56'),
       (82, '查看', '070401', '-', 'emergency-drill:list', 60, 'button', NULL, NULL, NULL, '2019-09-12 16:53:54',
        '2019-09-12 16:53:56'),
       (83, '新增', '070402', '-', 'emergency-drill:save', 60, 'button', NULL, NULL, NULL, '2019-08-15 14:10:33',
        '2019-08-15 14:10:35'),
       (84, '编辑', '070403', '-', 'emergency-drill:update,emergency-drill:get', 60, 'button', NULL, NULL, NULL,
        '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (85, '删除', '070404', '-', 'emergency-drill:delete', 60, 'button', NULL, NULL, NULL, '2019-09-12 16:53:54',
        '2019-09-12 16:53:56'),
       (86, '查看', '070501', '-', 'emergency-event:list,emergency-event:get', 61, 'button', NULL, NULL, NULL,
        '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (87, '新增', '070502', '-', 'emergency-event:save', 61, 'button', NULL, NULL, NULL, '2019-08-15 14:10:33',
        '2019-08-15 14:10:35'),
       (88, '指派', '070503', '-', 'emergency-event:assign,emergency-event:assign-user:listAll', 61, 'button', NULL,
        NULL, NULL, '2019-08-15 14:10:33', '2019-08-15 14:10:35'),
       (89, '反馈', '070504', '-', 'emergency-event:feedback', 61, 'button', NULL, NULL, NULL, '2019-08-15 14:10:33',
        '2019-08-15 14:10:35'),
       (90, '结案', '070505', '-', 'emergency-event:close', 61, 'button', NULL, NULL, NULL, '2019-08-15 14:10:33',
        '2019-08-15 14:10:35'),
       (91, '审核', '070506', '-', 'emergency-event:check', 61, 'button', NULL, NULL, NULL, '2019-08-15 14:10:33',
        '2019-08-15 14:10:35'),
       (92, '厕所资源', '0109', '/home/ResourceManagement/ToiletResources', NULL, 1, 'page', NULL, NULL, 2,
        '2019-08-15 11:13:10', '2019-08-15 11:13:21'),
       (93, '查看', '050201', '-', 'hotelResource:hotelDetailList,hotelResource:hotelStatistics', 52, 'button', NULL,
        NULL, NULL, '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (94, '查看', '050101', '-',
        'tourismHot:countyStatistical,tourist-heat-map:listAll,tourist-heat-map:scenic-status:listAll,tourismHot:countyTourismTrend,tourismHot:scenicTourismTrend',
        51, 'button', NULL, NULL, NULL, '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (95, '查看', '050301', '-', 'tourismConsumption:provinceConsumptionList', 53, 'button', NULL, NULL, NULL,
        '2019-09-12 16:53:54', '2019-09-12 16:53:56'),
       (96, '查看', '050401', '-', 'traffic:top,traffic:monitor,traffic:source,parking-lot:parking-space:status', 54,
        'button', NULL, NULL, NULL, '2019-09-12 16:53:54', '2019-09-12 16:53:56');