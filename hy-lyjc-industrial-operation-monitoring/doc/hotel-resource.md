# 酒店资源模块 #
## 酒店资源列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/hotelResource/list </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* hotelResource:list
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | int  | 当前页码  |  Y |
|  size | int  | 每页条数  |  Y |
|  condition.name | string  | 酒店名称  |  N |
|  condition.businessCircle | string  | 所属商圈  |  N |
|  condition.area | string  | 所属区域  |  N |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "totalCount": 1,
        "totalPages": 1,
        "currentSize": 1,
        "pageNum": 1,
        "list": [
            {
                "stationId": "6xxxxxxxxxxxxx",
                "name": "xxxxxxxxxx6",
                "area": null,
                "address": "xxxxxxxx",
                "bedNum": 210,
                "phoneNum": "130xxxxxxxxx"
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  id | 资源ID 
|  stationId | 公安编码
|  name | 酒店名称
|  area | 所属区域
|  address | 酒店地址
|  bedNum | 床位
|  servicePhone | 服务电话


## 酒店入住->数据统计 ##
### 路径 ###
<pre>http://192.168.10.39:30800/hotelResource/hotelStatistics </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* hotelResource:hotelStatistics
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
    "data": {
        "name": null,
        "address": null,
        "hotelCount": 5,
        "estimateFull": 3,
        "bedNum": 1290.0,
        "realTimeCheckinCount": 1338.0,
        "estimateOccupancy": "100%",
        "todayReceptionCount": 2,
        "yesterdayReceptionCount": 4
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  hotelCount |  酒店总数
|  estimateFull | 估计满住
|  bedNum | 总床位数
|  realTimeCheckinCount | 实时入住
|  estimateOccupancy | 估算入住率
|  todayReceptionCount | 今日累计接待
|  yesterdayReceptionCount | 昨日累计接待


## 酒店入住->实时入住详情合计 ##
### 路径 ###
<pre>http://192.168.10.39:30800/hotelResource/hotelDetailStatistics </pre>
### 请求方式 ###
* POST
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是 
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  name | string  | 酒店名称  |  N |
|  businessCircle | string  | 所属商圈  |  N |
|  area | string  | 所属区域  |  N | 
### 返回结果 ###
<pre>
{
    "code": "08000000",
    "message": "success",
    "success": true,
    "data": {
        "name": null,
        "area": null,
        "businessCircle": null,
        "address": null,
        "hotelCount": null,
        "estimateFull": null,
        "bedNum": null,
        "realTimeCheckinCount": 49.0,
        "estimateOccupancy": null,
        "todayReceptionCount": 280,
        "yesterdayReceptionCount": 308
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  realTimeCheckinCount | 实时入住
|  todayReceptionCount | 今日累计接待
|  yesterdayReceptionCount | 昨日累计接待


## 酒店入住->酒店详情 ##
### 路径 ###
<pre>http://192.168.10.39:30800/hotelResource/detail?stationId=5114239447 </pre>
### 请求方式 ###
* GET
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  stationId | string  | 酒店唯一标识  |  Y |
### 返回结果 ###
<pre>
{
    "code": "08000000",
    "message": "success",
    "success": true,
    "data": {
        "stationId": "5114239447",
        "name": "四川信和文化旅游有限公司洲山道酒店管理分公司",
        "area": "七里坪镇",
        "businessCircle": "七里坪商圈",
        "address": "四川省洪雅县七里坪镇七里园路27号1单元1栋101号",
        "bedNum": 235,
        "phoneNum": "028-35075555"
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  stationId | 公安编码
|  name | 酒店名称
|  area | 所属区域
|  businessCircle | 所属商圈
|  address | 酒店地址
|  bedNum | 床位
|  servicePhone | 服务电话


## 酒店入住->实时入住详情（按床位数排序） ##
### 路径 ###
<pre>http://192.168.10.39:30800/hotelResource/hotelDetailList </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* hotelResource:hotelDetailList
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | int  | 当前页码  |  Y |
|  size | int  | 每页条数  |  Y |
|  condition.name | string  | 酒店名称  |  N |
|  condition.businessCircle | string  | 所属商圈  |  N |
|  condition.area | string  | 所属区域  |  N |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "totalCount": 4,
        "totalPages": 1,
        "currentSize": 4,
        "pageNum": 1,
        "list": [
            {
                "name": "xxxxxxxxxx3",
                "area": null,
                "address": "xxxxxxxx",
                "hotelCount": null,
                "estimateFull": null,
                "bedNum": 500.0,
                "realTimeCheckinCount": 504.0,
                "estimateOccupancy": "100%",
                "todayReceptionCount": 1,
                "yesterdayReceptionCount": 1
            },
            {
                "name": "xxxxxxxxxx6",
                "area": null,
                "address": "xxxxxxxx",
                "hotelCount": null,
                "estimateFull": null,
                "bedNum": 210.0,
                "realTimeCheckinCount": 346.0,
                "estimateOccupancy": "100%",
                "todayReceptionCount": 0,
                "yesterdayReceptionCount": 2
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  stationId | 酒店唯一标识
|  name | 酒店名称
|  area | 所属区域
|  address | 酒店地址
|  hotelCount |  酒店总数
|  estimateFull | 估计满住
|  bedNum | 总床位数
|  realTimeCheckinCount | 实时入住
|  estimateOccupancy | 估算入住率
|  todayReceptionCount | 今日累计接待
|  yesterdayReceptionCount | 昨日累计接待


## 酒店入住->实时入住详情（按实时入住人数排序） ##
### 路径 ###
<pre>http://192.168.10.39:30800/hotelResource/hotelDetailListOrderByPeopleDesc </pre>
### 请求方式 ###
* POST
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  name | string  | 酒店名称  |  N |
|  businessCircle | string  | 所属商圈  |  N |
|  area | string  | 所属区域  |  N |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
            {
                "name": "xxxxxxxxxx3",
                "area": null,
                "address": "xxxxxxxx",
                "hotelCount": null,
                "estimateFull": null,
                "bedNum": 500.0,
                "realTimeCheckinCount": 504.0,
                "estimateOccupancy": "100%",
                "todayReceptionCount": 1,
                "yesterdayReceptionCount": 1
            },
            {
                "name": "xxxxxxxxxx6",
                "area": null,
                "address": "xxxxxxxx",
                "hotelCount": null,
                "estimateFull": null,
                "bedNum": 210.0,
                "realTimeCheckinCount": 346.0,
                "estimateOccupancy": "100%",
                "todayReceptionCount": 0,
                "yesterdayReceptionCount": 2
            }
    ]
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  stationId | 酒店唯一标识
|  name | 酒店名称
|  area | 所属区域
|  address | 酒店地址
|  hotelCount |  酒店总数
|  estimateFull | 估计满住
|  bedNum | 总床位数
|  realTimeCheckinCount | 实时入住
|  estimateOccupancy | 估算入住率
|  todayReceptionCount | 今日累计接待
|  yesterdayReceptionCount | 昨日累计接待


## 游客画像分析->过夜留宿占比 ##
### 路径 ###
<pre>http://192.168.10.39:30800/hotelResource/stayOvernight </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* hotelResource:stayOvernight
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  year | int  | 年份  |  Y |
|  scope | int  | 范围（0：不限，1：省内，2：省外）  |  Y |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "name": "2天",
            "value": 2
        },
        {
            "name": "未过夜",
            "value": 1
        },
        {
            "name": "1天",
            "value": 1
        },
        {
            "name": "3天",
            "value": 1
        },
        {
            "name": "4天及以上",
            "value": 2
        }
    ]
}
</pre>


## 酒店接待数据->旅客接待量 ##
### 路径 ###
<pre>http://192.168.10.39:30800/hotelResource/hotelPassengerReceptionList </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* hotelResource:hotelPassengerReceptionList
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  name | string  | 酒店名称  |  N |
|  businessCircle | string  | 所属商圈  |  N |
|  area | string  | 所属区域  |  N |
|  startTime | string  | 开始时间（2019-01-01）  |  Y |
|  endTime | string  | 结束时间（2019-12-31）  |  Y |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "cumulativeReception": 203932,
        "dataList": [
            {
                "name": "洪雅华生温泉酒店",
                "area": "高庙镇",
                "businessCircle": "七里坪商圈",
                "address": "眉山市洪雅县高庙镇七里坪",
                "cumulativeReception": 27296
            },
            {
                "name": "洪雅峨嵋半山七里坪百斯特酒店管理有限公司",
                "area": "高庙镇",
                "businessCircle": "七里坪商圈",
                "address": "洪雅县高庙镇七里坪F2南区",
                "cumulativeReception": 20289
            },
            {
                "name": "洪雅县半岛度假酒店有限责任公司",
                "area": "高庙镇",
                "businessCircle": "七里坪商圈",
                "address": "洪雅县高庙镇七里村二组",
                "cumulativeReception": 11480
            }
         ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  name | 酒店名称
|  area | 所属区域
|  businessCircle | 所属商圈
|  address | 酒店地址
|  statisticalDate | 统计日期
|  cumulativeReception | 累计接待



## 酒店接待数据->旅客来源地 ##
### 路径 ###
<pre>http://192.168.10.39:30800/hotelResource/hotelTouristSourceList </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* hotelResource:hotelTouristSourceList
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | int  | 当前页码  |  Y |
|  size | int  | 每页条数  |  Y |
|  condition.provName | string  | 省份  |  N |
|  condition.cityName | string  | 城市  |  N |
|  condition.startTime | string  | 开始时间（2019-01-01）  |  Y |
|  condition.endTime | string  | 结束时间（2019-12-31）  |  Y |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "totalCount": 3,
        "totalPages": 1,
        "currentSize": 3,
        "pageNum": 1,
        "list": [
            {
                "provName": "四川省",
                "cityName": "广安市",
                "statisticalDate": "2019-11-18",
                "cumulativeReception": 1.0
            },
            {
                "provName": "四川省",
                "cityName": "自贡市",
                "statisticalDate": "2019-11-18",
                "cumulativeReception": 3.0
            },
            {
                "provName": "重庆市",
                "cityName": "重庆市",
                "statisticalDate": "2019-11-18",
                "cumulativeReception": 1.0
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  provName | 省份
|  cityName | 城市
|  statisticalDate | 统计日期
|  cumulativeReception | 累计接待