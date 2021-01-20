# 假日统计分析模块 #
## 游客接待人数走势 ##
### 路径 ###
<pre>http://192.168.10.39:30800/dataResource/statisticsData </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* dataResource:statisticsData
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  startTime | int  | 今年节日开始时间  |  Y |
|  endTime | int  | 今年节日结束时间  |  Y |
|  lastStartTime | int  | 去年节日开始时间（不存在，就传：0）  |  Y |
|  lastEndTime | int  | 去年节日结束时间（不存在，就传：0）  |  Y |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": {
    "lastTourismTrend": [
      {
        "count": 700,
        "countryName": null,
        "provName": null,
        "cityName": null,
        "statisticsTime": "2018-01-01"
      },
      {
        "count": 200,
        "countryName": null,
        "provName": null,
        "cityName": null,
        "statisticsTime": "2018-01-02"
      },
      {
        "count": 100,
        "countryName": null,
        "provName": null,
        "cityName": null,
        "statisticsTime": "2018-01-03"
      }
    ],
    "peopleCount": 1730,
    "compareInnerProvince": 1.3,
    "compareOuterProvince": 7,
    "peopleOuterProvince": 700,
    "peopleInnerProvince": 780,
    "tourismTrend": [
      {
        "count": 780,
        "countryName": null,
        "provName": null,
        "cityName": null,
        "statisticsTime": "2019-01-01"
      },
      {
        "count": 550,
        "countryName": null,
        "provName": null,
        "cityName": null,
        "statisticsTime": "2019-01-02"
      },
      {
        "count": 400,
        "countryName": null,
        "provName": null,
        "cityName": null,
        "statisticsTime": "2019-01-03"
      }
    ]
  }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  tourismTrend | 今年节假日游客趋势
|  lastTourismTrend | 去年节假日游客趋势（不存在去年数据，就不返回该字段）
|  countryName | 国家
|  provName | 省份
|  cityName | 城市
|  count | 接待游客人数
|  statisticsTime | 统计时间
|  peopleInnerProvince | 今年省内接待人数
|  peopleOuterProvince | 今年省外接待人数
|  compareInnerProvince | 同比省内（不存在去年数据，就不返回该字段）
|  compareOuterProvince | 同比省外（不存在去年数据，就不返回该字段）




## 按搜索条件分页统计 ##
### 路径 ###
<pre>http://192.168.10.39:30800/dataResource/conditionStatisticsData </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* dataResource:conditionStatisticsData
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  condition.startTime | int  | 开始时间  |  Y |
|  condition.endTime | int  | 结束时间  |  Y |
|  condition.countryName | string  | 国家  |  N |
|  condition.provName | string  | 省份  |  N |
|  condition.cityName | string  | 城市  |  N |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": {
    "records": [
      {
        "count": 780,
        "countryName": "中国",
        "provName": "四川",
        "cityName": "成都",
        "statisticsTime": "2019-01-01"
      },
      {
        "count": 300,
        "countryName": "中国",
        "provName": "广东",
        "cityName": "广州",
        "statisticsTime": "2019-01-02"
      },
      {
        "count": 400,
        "countryName": "中国",
        "provName": "广东",
        "cityName": "深圳",
        "statisticsTime": "2019-01-03"
      }
    ],
    "total": 4,
    "size": 3,
    "current": 1,
    "orders": [],
    "searchCount": true,
    "pages": 2
  }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  countryName | 国家
|  provName | 省份
|  cityName | 城市
|  count | 接待游客人数
|  statisticsTime | 统计时间



## 导出Excel ##
### 路径 ###
<pre>http://192.168.10.39:30800/dataResource/export </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* dataResource:export
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
|  condition.countryName | string  | 国家  |  N |
|  condition.provName | string  | 省份  |  N |
|  condition.cityName | string  | 城市  |  N |



## 假日列表 ##
### 路径 ###
<pre>http://localhost:30800/dataResource/holiday/{year} </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* dataResource:holiday
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
  "data": {
    "2018": [],
    "2019": [
      {
        "id": 1,
        "year": 2019,
        "name": "元旦节",
        "startDate": 1546099200000,
        "endDate": 1546272000000,
        "dateList": [
          1546099200000,
          1546185600000,
          1546272000000
        ]
      },
      {
        "id": 2,
        "year": 2019,
        "name": "春节",
        "startDate": 1549209600000,
        "endDate": 1549728000000,
        "dateList": [
          1549209600000,
          1549296000000,
          1549382400000,
          1549468800000,
          1549555200000,
          1549641600000,
          1549728000000
        ]
      }
    ]
  }
}
</pre>
### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  2019 | 今年
|  2018 | 去年
|  year | 年份
|  name | 假日名称
|  startDate | 开始时间
|  endDate | 结束时间



## 进城车流量走势 ##
### 路径 ###
<pre>http://192.168.10.39:30800/traffic/graph?nowBeginTime=t1&nowEndTime=t2&lastBeginTime=t3&lastEndTime=t4 </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* traffic:graph
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  nowBeginTime | int  | 今年节日开始时间  |  Y |
|  nowEndTime | int  | 今年节日结束时间  |  Y |
|  lastBeginTime | int  | 去年节日开始时间（不存在，就传：0）  |  Y |
|  lastEndTime | int  | 去年节日结束时间（不存在，就传：0）  |  Y |
### 返回结果 ###
<pre>
{
    "code": "00000000",
    "message": "处理成功",
    "success": true,
    "data": {
        "graphs": [
            {
                "sortDays": 1,
                "nowHolidayTime": 1546272000000,
                "nowYearFlow": 3043,
                "lastYearFlow": 0
            },
            ...
        ],
        "flowCount": 8552,
        "innerProvinceFlowCount": 6970,
        "outProvinceFlowCount": 1582,
        "innerProvinceOnYear": -36,
        "outProvinceOnYear": -33
    }
}

</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|graphs | 图表数据 
|sortDays | 节假日的第几天 |
|nowHolidayTime | 当前节假日对应的时间 
|lastHolidayTime | 去年节假日时间
|nowYearFlow | 今年节假日的进城流量 
|lastYearFlow | 去年节假日的进城流量 
|flowCount | 今年进城总流量 
|innerProvinceFlowCount | 省内车辆进城总流量 
|outProvinceFlowCount | 省外车辆进城总流量  
|innerProvinceOnYear | 省内环比数据 
|outProvinceOnYear | 省外环比数据



## 按条件统计交通列表 ##
### 路径 ###
<pre>http://localhost:30800/traffic/table?beginTime=t1&endTime=t2&provinceName=四川 </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* traffic:table
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  beginTime | int  | 今年节日开始时间  |  Y |
|  endTime | int  | 今年节日结束时间  |  Y |
|  provinceName | string  | 省份  |  N |
|  countryName | string  | 城市  |  N |
### 返回结果 ###
<pre>
{
  "code": "00000000",
  "message": "处理成功",
  "success": true,
  "data": [
    {
      "flow": 18407,
      "provinceName": "四川",
      "cityName": "成都",
      "statisticsTime": 1533052800000
    },
    {
      "flow": 1393,
      "provinceName": "四川",
      "cityName": "乐山",
      "statisticsTime": 1533052800000
    },
    {
      "flow": 1390,
      "provinceName": "四川",
      "cityName": "泸州",
      "statisticsTime": 1533052800000
    }
  ]
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------:    
|flow | 进城车辆数 
|provinceName | 省份 |
|cityName | 城市 
|statisticsTime | 统计时间 



## 旅游交通导出Excel ##
### 路径 ###
<pre>http://localhost:30800/traffic/export?beginTime=t1&endTime=t2&provinceName=四川 </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* traffic:export
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  beginTime | int  | 今年节日开始时间  |  Y |
|  endTime | int  | 今年节日结束时间  |  Y |
|  provinceName | string  | 省份  |  N |
|  cityName | string  | 城市  |  N |



## 旅游交通省份接口 ##
### 路径 ###
<pre>http://localhost:30800/traffic/province </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* traffic:province
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
    "河北",
    "辽宁",
    "安徽",
    "江苏",
    "湖北",
    "山西",
    "吉林",
    "广东",
    "宁夏",
    "北京",
    "河南",
    "黑龙江",
    "山东",
    "浙江",
    "广西",
    "内蒙古",
    "福建",
    "四川",
    "重庆",
    "天津",
    "云南",
    "湖南",
    "新疆",
    "江西",
    "甘肃",
    "陕西",
    "贵州",
    "青海",
    "西藏",
    "海南",
    "上海"
  ]
}
</pre>



## 旅游交通城市接口 ##
### 路径 ###
<pre>http://localhost:30800/traffic/city?provinceName={provinceName} </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* traffic:city
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  provinceName | string  | 省份  |  Y |
### 返回结果 ###
<pre>
{
  "code": "00000000",
  "message": "处理成功",
  "success": true,
  "data": [
    "成都",
    "绵阳",
    "自贡",
    "攀枝花",
    "泸州",
    "德阳",
    "广元",
    "遂宁",
    "内江",
    "乐山",
    "资阳",
    "宜宾",
    "南充",
    "达州",
    "雅安",
    "阿坝藏族羌族自治州",
    "甘孜藏族自治州",
    "凉山彝族自治州",
    "广安",
    "巴中",
    "眉山"
  ]
}
</pre>
