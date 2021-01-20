# 旅游消费模块 #
## 旅游消费柱状图表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismConsumption/provinceConsumptionList </pre>

### 请求方式 ###
* POST

### 权限标识 ###
* tourismConsumption:provinceConsumptionList

### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  

### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  beginDate | number  | 时间戳 不传时由后台计算，前端根据返回结果回填 |  N |
|  endDate | number  | 时间戳 不传时由后台计算，前端根据返回结果回填 |  N |

### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "beginDate": 1582992000000,
        "endDate": 1583596800000,
        "totalTransAt": 12139287.27,
        "innerProvince": [
            {
                "source": "成都市",
                "transAt": 2598638.95,
                "transNum": 22265,
                "acctNum": 4983,
                "perConsumption": 521.50,
                "perConsumptionPens": 4.47
            }
        ],
        "outerProvince": [
            {
                "source": "福建省",
                "transAt": 811599.86,
                "transNum": 2014,
                "acctNum": 555,
                "perConsumption": 1462.34,
                "perConsumptionPens": 3.63
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  beginDate | 开始日期，首次进入页面时回填
|  endDate | 结束日期，首次进入页面时回填 
|  totalTransAt | 消费总金额 
|  innerProvince | 省内
|  outerProvince | 省外
|  source | 来源地  省或市
|  transAt | 消费金额
|  transNum | 消费笔数
|  acctNum | 消费人次
|  perConsumption | 人均消费金额
|  perConsumptionPens | 人均消费笔数

## 消费汇总 ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismConsumption/consumptionSummary </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* tourismConsumption:consumptionSummary
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | int  | 当前页码  |  Y |
|  size | int  | 每页条数  |  Y |
|  condition.startTime | string  | 开始时间  |  Y |
|  condition.endTime | string  | 结束时间  |  Y |
|  condition.cbdName | string  | 商圈名称  |  N |
|  condition.travellerType | string  | 游客来源（1：省内，2：省外）  |  N |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "businessCircle": [
            {
                "name": "柳江古镇商圈",
                "value": 22001.76
            },
            {
                "name": "主城区商圈",
                "value": 20000.88
            },
            {
                "name": "瓦屋山商圈",
                "value": 16000.88
            },
            {
                "name": "七里坪商圈",
                "value": 15000.88
            },
            {
                "name": "玉屏山商圈",
                "value": 8888.88
            }
        ],
        "consumptionSummaryList": {
            "totalCount": 6,
            "totalPages": 1,
            "currentSize": 6,
            "pageNum": 1,
            "list": [
                {
                    "cbdName": "七里坪商圈",
                    "travellerType": "1",
                    "dealDay": "2019-11-26",
                    "transAt": 15000.88,
                    "transNum": 500,
                    "acctNum": 200,
                    "perConsumption": 75.0,
                    "perConsumptionPens": 2.5
                },
                {
                    "cbdName": "主城区商圈",
                    "travellerType": "2",
                    "dealDay": "2019-11-25",
                    "transAt": 20000.88,
                    "transNum": 800,
                    "acctNum": 400,
                    "perConsumption": 50.0,
                    "perConsumptionPens": 2.0
                }
            ]
        }
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  businessCircle | 商圈金额合计 
|  consumptionSummaryList | 分页列表展示
|  cbdName | 范围
|  travellerType | 游客来源
|  dealDay | 统计日期
|  transAt | 消费金额
|  transNum | 消费笔数
|  acctNum | 消费人次
|  perConsumption | 人均消费金额
|  perConsumptionPens | 人均消费笔数


## 消费来源地 ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismConsumption/consumptionSource </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* tourismConsumption:consumptionSource
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | int  | 当前页码  |  Y |
|  size | int  | 每页条数  |  Y |
|  condition.startTime | string  | 开始时间  |  Y |
|  condition.endTime | string  | 结束时间  |  Y |
|  condition.sourceProvince | string  | 来源省份code码  |  N |
|  condition.sourceCity | string  | 来源市区code码  |  N |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "consumptionSourceList": {
            "totalCount": 23,
            "totalPages": 3,
            "currentSize": 10,
            "pageNum": 1,
            "list": [
                {
                    "sourceProvince": "湖北省",
                    "sourceCity": "武汉市",
                    "dealDay": "2019-11-26",
                    "transAt": 15000.88,
                    "transNum": 600,
                    "acctNum": 300,
                    "perConsumption": 50.0,
                    "perConsumptionPens": 2.0
                },
                {
                    "sourceProvince": "广东省",
                    "sourceCity": "深圳市",
                    "dealDay": "2019-11-26",
                    "transAt": 15000.88,
                    "transNum": 600,
                    "acctNum": 300,
                    "perConsumption": 50.0,
                    "perConsumptionPens": 2.0
                }
            ]
        },
        "acctNumAmount": 5800.0,
        "transNumAmount": 12700.0,
        "transAtAmount": 313020.24
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  transAtAmount | 消费金额合计 
|  transNumAmount | 消费笔数合计 
|  acctNumAmount | 消费人次合计 
|  consumptionSourceList | 分页列表展示
|  sourceProvince | 来源省份
|  sourceCity | 来源市区
|  dealDay | 统计日期
|  transAt | 消费金额
|  transNum | 消费笔数
|  acctNum | 消费人次
|  perConsumption | 人均消费金额
|  perConsumptionPens | 人均消费笔数


## 行业贡献 ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismConsumption/industryContribution </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* tourismConsumption:industryContribution
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | int  | 当前页码  |  Y |
|  size | int  | 每页条数  |  Y |
|  condition.startTime | string  | 开始时间  |  Y |
|  condition.endTime | string  | 结束时间  |  Y |
|  condition.type | string  | 行业  |  N |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "industryContributionList": {
            "totalCount": 8,
            "totalPages": 1,
            "currentSize": 8,
            "pageNum": 1,
            "list": [
                {
                    "type": "餐饮",
                    "dealDay": "2019-12-01",
                    "transAt": 18000.0,
                    "transNum": 50,
                    "acctNum": 100,
                    "perConsumption": 180.0,
                    "perConsumptionPens": 0.5,
                    "transAtTotal": 30000.0,
                    "transAtRatio": 0.6
                },
                {
                    "type": "酒店",
                    "dealDay": "2019-12-01",
                    "transAt": 12000.0,
                    "transNum": 40,
                    "acctNum": 80,
                    "perConsumption": 150.0,
                    "perConsumptionPens": 0.5,
                    "transAtTotal": 30000.0,
                    "transAtRatio": 0.4
                },
                {
                    "type": "娱乐",
                    "dealDay": "2019-11-30",
                    "transAt": 10000.0,
                    "transNum": 100,
                    "acctNum": 200,
                    "perConsumption": 50.0,
                    "perConsumptionPens": 0.5,
                    "transAtTotal": 15000.0,
                    "transAtRatio": 0.67
                },
                {
                    "type": "购物",
                    "dealDay": "2019-11-30",
                    "transAt": 9000.9,
                    "transNum": 90,
                    "acctNum": 180,
                    "perConsumption": 50.01,
                    "perConsumptionPens": 0.5,
                    "transAtTotal": 30003.0,
                    "transAtRatio": 0.3
                }
            ]
        },
        "transAtTotal": 245011.0,
        "transAtAmount": 76801.7,
        "transAtRatioAmount": 0.31
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  transAtAmount | 游客消费金额合计 
|  transAtTotal | 消费总金额合计 
|  transAtRatioAmount | 贡献度合计 
|  industryContributionList | 分页列表展示
|  type | 行业
|  dealDay | 统计日期
|  transAt | 消费金额
|  transNum | 消费笔数
|  acctNum | 消费人次
|  perConsumption | 人均消费金额
|  perConsumptionPens | 人均消费笔数
|  transAtTotal | 消费总金额
|  transAtRatio | 游客贡献度


## 省市列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismConsumption/provinceList </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* tourismConsumption:provinceList
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 返回结果 ###
<pre>
{
    "code": "03010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "provId": "110000",
            "provName": "北京市",
            "cities": [
                {
                    "cityId": "110100",
                    "cityName": "北京市",
                    "counties": null
                }
            ]
        },
        {
            "provId": "130000",
            "provName": "河北省",
            "cities": [
                {
                    "cityId": "130100",
                    "cityName": "石家庄市",
                    "counties": null
                },
                {
                    "cityId": "130200",
                    "cityName": "唐山市",
                    "counties": null
                },
                {
                    "cityId": "130300",
                    "cityName": "秦皇岛市",
                    "counties": null
                },
                {
                    "cityId": "130400",
                    "cityName": "邯郸市",
                    "counties": null
                }
            ]
        }
    }
}
</pre>


## 商圈范围列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismConsumption/businessCircleList </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* tourismConsumption:businessCircleList
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "id": 106,
            "code": "016001",
            "name": "洪雅县",
            "orderNumber": 1,
            "parentCode": "HONGYA_BUSINESS_CIRCLE",
            "children": []
        },
        {
            "id": 107,
            "code": "016002",
            "name": "柳江古镇商圈",
            "orderNumber": 2,
            "parentCode": "HONGYA_BUSINESS_CIRCLE",
            "children": []
        },
        {
            "id": 112,
            "code": "016007",
            "name": "主城区商圈",
            "orderNumber": 7,
            "parentCode": "HONGYA_BUSINESS_CIRCLE",
            "children": []
        }
    ]
}
</pre>


## 假日统计分析-旅游收入走势统计 ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismConsumption/statisticsData </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* tourismConsumption:statisticsData
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  startTime | string  | 今年节日开始时间  |  Y |
|  endTime | string  | 今年节日结束时间  |  Y |
|  lastStartTime | string  | 去年节日开始时间  |  Y |
|  lastEndTime | string  | 去年节日结束时间  |  Y |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "consumptionTrend": [
            {
                "name": "2019-01-01",
                "value": 24001.76
            },
            {
                "name": "2019-01-02",
                "value": 18001.76
            },
            {
                "name": "2019-01-03",
                "value": 14001.76
            }
        ],
        "lastConsumptionTrend": [
            {
                "name": "2018-01-01",
                "value": 22001.76
            },
            {
                "name": "2018-01-02",
                "value": 16001.76
            },
            {
                "name": "2018-01-03",
                "value": 12001.76
            }
        ],
        "transAtTotal": 56005.28,
        "compareTransAt": "12.0%"
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  consumptionTrend | 今年节假日旅游收入趋势
|  lastConsumptionTrend | 去年节假日旅游收入趋势
|  transAtTotal | 节假日旅游总收入
|  compareTransAt | 同比变化


## 假日统计分析-旅游收入按搜索统计 ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismConsumption/conditionStatisticsData </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* tourismConsumption:conditionStatisticsData
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  startTime | string  | 开始时间  |  Y |
|  endTime | string  | 结束时间  |  Y |
|  cbdName | string  | 商圈名  |  N |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "cbdName": "柳江古镇商圈",
            "value": 22001.76,
            "dealDay": "2019-01-01"
        },
        {
            "cbdName": "主城区商圈",
            "value": 20000.88,
            "dealDay": "2019-01-01"
        },
        {
            "cbdName": "柳江古镇商圈",
            "value": 20000.88,
            "dealDay": "2019-01-02"
        },
        {
            "cbdName": "瓦屋山商圈",
            "value": 16000.88,
            "dealDay": "2019-01-01"
        },
        {
            "cbdName": "七里坪商圈",
            "value": 15000.88,
            "dealDay": "2019-01-01"
        },
        {
            "cbdName": "玉屏山商圈",
            "value": 8888.88,
            "dealDay": "2019-01-01"
        }
    ]
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  cbdName | 商圈名
|  value | 收入金额
|  dealDay | 统计时间


## 历史数据统计-旅游收入按搜索统计 ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismConsumption/historyConditionStatisticsData </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* tourismConsumption:historyConditionStatisticsData
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  startTime | string  | 开始时间  |  Y |
|  endTime | string  | 结束时间  |  Y |
|  flag | string  | day,month,year  |  Y |
|  cbdName | string  | 商圈名  |  N |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "cbdName": "柳江古镇商圈",
            "value": 22001.76,
            "dealDay": "2019-01-01"
        },
        {
            "cbdName": "主城区商圈",
            "value": 20000.88,
            "dealDay": "2019-01-01"
        },
        {
            "cbdName": "柳江古镇商圈",
            "value": 20000.88,
            "dealDay": "2019-01-02"
        },
        {
            "cbdName": "瓦屋山商圈",
            "value": 16000.88,
            "dealDay": "2019-01-01"
        },
        {
            "cbdName": "七里坪商圈",
            "value": 15000.88,
            "dealDay": "2019-01-01"
        },
        {
            "cbdName": "玉屏山商圈",
            "value": 8888.88,
            "dealDay": "2019-01-01"
        }
    ]
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  cbdName | 商圈名
|  value | 收入金额
|  dealDay | 统计时间


## 游客画像分析-游客消费类型占比 ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismConsumption/industryType/{year} </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* tourismConsumption:industryType
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  year | int  | 年份  |  Y |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "name": "餐饮",
            "value": 26000.8
        },
        {
            "name": "酒店",
            "value": 19000.0
        },
        {
            "name": "购物",
            "value": 17000.9
        },
        {
            "name": "娱乐",
            "value": 14800.0
        }
    ]
}
</pre>