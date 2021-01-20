# 历史统计分析模块 #
## 游客接待数列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/dataResource/historyConditionStatisticsData </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* dataResource:historyConditionStatisticsData
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | int  | 当前页码  |  Y |
|  size | int  | 每页条数  |  Y |
|  condition.startTime | int  | 开始时间  |  Y |
|  condition.endTime | int  | 结束时间  |  Y |
|  condition.flag | string  | 统计类型，天：day，月：month，年：year  |  Y |
|  condition.scenicName | string  | 景区  |  N |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "peopleCount": 2430,
        "dataList": {
            "records": [
                {
                    "count": 500,
                    "scenicName": "瓦屋山景区",
                    "countryName": null,
                    "provName": null,
                    "cityName": null,
                    "statisticsTime": "2019-01-01"
                },
                {
                    "count": 580,
                    "scenicName": "七里坪景区",
                    "countryName": null,
                    "provName": null,
                    "cityName": null,
                    "statisticsTime": "2019-01-02"
                }
            ],
            "total": 7,
            "size": -1,
            "current": 1,
            "orders": [],
            "searchCount": true,
            "pages": -7
        }
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  peopleCount | 总人数汇总
|  scenicName | 景区
|  count | 接待游客人数
|  statisticsTime | 统计时间



## 游客接待数导出Excel ##
### 路径 ###
<pre>http://192.168.10.39:30800/dataResource/historyExport </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* dataResource:historyExport
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | int  | 当前页码  |  Y |
|  size | int  | 每页条数（导出不分页，直接传-1）  |  Y |
|  condition.startTime | int  | 开始时间  |  Y |
|  condition.endTime | int  | 结束时间  |  Y |
|  condition.flag | string  | 统计类型，天：day，月：month，年：year  |  Y |
|  condition.scenicName | string  | 景区  |  N |



## 进城车辆列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/traffic/historyByGraininess?beginTime=t1&endTime=t2&graininess=2&provinceName=四川&cityName=成都 </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* traffic:historyByGraininess
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  beginTime | int  | 开始时间  |  Y |
|  endTime | int  | 结束时间  |  Y |
|  graininess | int  | 颗粒度，1：天  2：月  3：年  |  Y |
|  provinceName | int  | 省份  |  N |
|  cityName | int  | 城市  |  N |
### 返回结果 ###
<pre>
{
  "code": "00000000",
  "message": "处理成功",
  "success": true,
  "data": {
    "externalTableVOList": [
      {
        "flow": 10676,
        "provinceName": "四川",
        "cityName": "成都",
        "statisticsTime": 1546272000000
      },
      {
        "flow": 840,
        "provinceName": "四川",
        "cityName": "自贡",
        "statisticsTime": 1546272000000
      }
    ],
    "flowCount": 26219,
    "graininess": 2
  }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|externalTableVOList | 表格数据 
|flow | 进城车流量 
|provinceName | 省份名称 
|cityName | 城市名称
|statisticsTime | 统计时间 
|flowCount | 总流量 
|graininess | 颗粒度 



## 进城车辆导出Excel ##
### 路径 ###
<pre>http://192.168.10.39:30800/traffic/historyExport?beginTime=t1&endTime=t2&graininess=1&provinceName=四川&cityName=成都 </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* traffic:historyExport
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  beginTime | int  | 开始时间  |  Y |
|  endTime | int  | 结束时间  |  Y |
|  graininess | int  | 颗粒度，1：天  2：月  3：年  |  Y |
|  provinceName | int  | 省份  |  N |
|  cityName | int  | 城市  |  N |