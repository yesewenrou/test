# 交通停车模块 #
## 获取路段实时拥堵排行 ##
### 路径 ###
<pre>http://192.168.10.39:30800/traffic/top </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* traffic:top
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 返回结果 ###
<pre>
{
    "code": "00000000",
    "message": "处理成功",
    "success": true,
    "data": [
        {
            "cameraCode": "A0002",
            "roadId": 2,
            "roadSectionName": "广洪高速（丹棱仁美收费站-止戈枢纽）",
            "relationObject": "洪雅周边,瓦屋山",
            "totalMileage": 13.6,
            "roadSectionDirection": 1,
            "roadSectionDescribe": "A0002路段",
            "longitude": "103.451253",
            "latitude": "29.821838",
            "monitorSiteId": "KK-02",
            "tpi": 0.7,
            "jamLevelEnum": "UNBLOCKED",
            "jamLength": 0.0,
            "offsetJamLength": 0.0,
            "jamLengthRatio": 0,
            "avgSpeed": 46,
            "count": 58,
            "congestionTimeLength": 0,
            "congestionTimes": 0
        }
    ]
}
</pre>

### 返回参数说明 ###
名称 | 类型 | 描述 | 备注  
:--- | :---: | :--- | :---
code | string | 返回码 |
message | string | 返回描述 |
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | object | 返回数据 |
cameraCode | string | 摄像头编号
roadSectionName | string | 路段名称 |
relationObject | number | 关联对象 |
totalMileage | number | 总公里数 |
roadSectionDirection | number | 路段方向 | 1：上行  2：下行
roadSectionDescribe | string | 路段描述 |
longitude | string | 经度
latitude | string | 纬度
tpi | number | 拥堵指数 |
jamLength | number | 拥堵长度 |
offsetJamLength | number | 距离上次的拥堵便宜 | 正数:比上次拥堵 长度增长 x km,负数则相反
jamLengthRatio | number | 拥堵里程占比 |  单位:%
avgSpeed | number | 平均速度 |
count | number | 实时车流量 |
congestionTimeLength | number | 累计拥堵时长 |
congestionTimes | number | 累计拥堵次数 |


## 获取入城车流量监测 ##
### 路径 ###
<pre>http://192.168.10.39:30800/traffic/monitor </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* traffic:monitor
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 返回结果 ###
<pre>
{
    "code": "00000000",
    "message": "处理成功",
    "success": true,
    "data": {
        "dayFlowCount": 199,
        "realTimeFlowCount": 11,
        "carFlowTimeList": [
            {
                "time": 1575946800000,
                "flow": 33
            },
            {
                "time": 1575943200000,
                "flow": 138
            },
            {
                "time": 1575939600000,
                "flow": 28
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 |
message | string | 返回描述 |
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | object | 返回数据 |
dayFlowCount | number | 今日累计进城车辆 |
realTimeFlowCount | number | 实时进城车流量 |
carFlowTimeList | object | 车流量图表数据 |
carFlowTimeList.time | number | 时间 |
carFlowTimeList.flow | number | 流量 |


## 获取当天入城车辆来源地流量统计数据 ##
### 路径 ###
<pre>http://192.168.10.39:30800/traffic/source </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* traffic:source
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 返回结果 ###
<pre>
{
    "code": "00000000",
    "message": "处理成功",
    "success": true,
    "data": {
        "provinceGraphList": [
            {
                "provinceName": "省内",
                "flowNumber": 151
            },
            {
                "provinceName": "省外",
                "flowNumber": 34
            }
        ],
        "cityGraphList": [
            {
                "cityName": "眉山",
                "flowNumber": 65
            },
            {
                "cityName": "乐山",
                "flowNumber": 57
            },
            {
                "cityName": "成都",
                "flowNumber": 16
            },
            {
                "cityName": "雅安",
                "flowNumber": 6
            },
            {
                "cityName": "自贡",
                "flowNumber": 3
            },
            {
                "cityName": "其他",
                "flowNumber": 4
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 |
message | string | 返回描述 |
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | object | 返回数据 |
provinceGraphList | object | 省份数据 |
provinceGraphList.provinceName | string | 名称 |
provinceGraphList.flowNumber | number | 流量 |
cityGraphList | object | 城市数据 |
cityGraphList.cityName | string | 名称 |
cityGraphList.flowNumber | number | 流量 |

## 获取当天进县域车流监控 ##
### 路径 ###
<pre>http://192.168.10.39:30800/traffic/monitorSite </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* traffic:monitor
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 返回结果 ###
<pre>
{
    "code": "00000000",
    "message": "处理成功",
    "success": true,
    "data": {
        "inCityTodayCount": 1328,
        "realTimeInCityCount": 17,
        "monitorSiteToDays": [
            {
                "monitorSiteId": "KK-01",
                "monitorSiteName": "余坪镇白马村",
                "todayCount": 192
            },
            。。。
        ]
    }
}
</pre>

### 返回参数说明 ###
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 |
message | string | 返回描述 |
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | object | 返回数据 |
data.inCityTodayCount | number | 当天进县城车流量 |
data.realTimeInCityCount | number | 实时进县城车流量 |
data.monitorSiteToDays | object | 当日监控点车流量 |
data.monitorSiteToDays.monitorSiteId | string | 监控点id |
data.monitorSiteToDays.monitorSiteName | string | 监控点名称 |
data.monitorSiteToDays.monitorSiteId | number | 当日车流量 |

## 获取车辆来源层级数据 ##
### 路径 ###
<pre>http://192.168.10.39:30800/traffic/provinceCity </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* traffic:provinceCity
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  statisticsObject | string  | 统计对象  |  Y |
|  statisticsBeginTime | number  | 统计开始时间  |  Y |
|  statisticsEndTime | number  | 统计结束时间  |  Y |
### 返回结果 ###
<pre>
{
    "code": "00000000",
    "message": "处理成功",
    "success": true,
    "data": {
        "flowCount": 0,
        "dataSource": "卡口",
        "countryFlowList": [
            {
                "countryName": "中国",
                "flowCount": 0,
                "provinceFlowList": [
                    {
                        "provinceName": "河北",
                        "flowCount": 0,
                        "cityFlowList": [
                            {
                                "cityName": "石家庄",
                                "flowCount": 0
                            },
                            。。。
                        ]
                    },
                    。。。
                ]
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 |
message | string | 返回描述 |
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | object | 返回数据 |
data.flowCount | number | 车流合计 |
data.realTimeInCityCount | number | 实时进县城车流量 |
data.dataSource | string | 数据来源 |
data.countryFlowList | object | 国家数据 |
data.countryFlowList.countryName | string | 国家名称 |
data.countryFlowList.flowCount | number | 国家车流量 |
data.countryFlowList.provinceFlowList | object | 省份数据 |
data.countryFlowList.provinceFlowList.provinceName | string | 省份名称 |
data.countryFlowList.provinceFlowList.flowCount | number | 省份车流量 |
data.countryFlowList.provinceFlowList.cityFlowList | object | 城市数据 |
data.countryFlowList.provinceFlowList.cityFlowList.cityName | object | 城市名称 |
data.countryFlowList.provinceFlowList.cityFlowList.flowCount | number | 城市车流量 |