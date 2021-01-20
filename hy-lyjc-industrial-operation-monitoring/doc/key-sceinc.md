# 重点景区模块 #
## 游客数据 ##
### 路径 ###
<pre>http://192.168.10.39:30800/keyScenic/touristList </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* keyScenic:touristList
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | int  | 当前页码  |  Y |
|  size | int  | 每页条数  |  Y |
|  condition.scenicName | string  | 景区名称  |  Y |
|  condition.startTime | long  | 统计开始时间  |  Y |
|  condition.endTime | long  | 统计结束时间  |  Y |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "totalCount": 48,
        "totalPages": 5,
        "currentSize": 10,
        "pageNum": 1,
        "list": [
            {
                "realTimeTouristNum": 1726,
                "todayTouristCount": 1739,
                "lastTodayTouristCount": 3018,
                "compareLastTouristCount": 42.38,
                "compareLastExtendType": -1,
                "beforeLastTodayTouristCount": 0,
                "compareBeforeLastTouristCount": null,
                "compareBeforeLastExtendType": -1,
                "responseTime": "2020-01-13 16:25:30"
            },
            {
                "realTimeTouristNum": 1728,
                "todayTouristCount": 1739,
                "lastTodayTouristCount": 3018,
                "compareLastTouristCount": 42.38,
                "compareLastExtendType": -1,
                "beforeLastTodayTouristCount": 0,
                "compareBeforeLastTouristCount": null,
                "compareBeforeLastExtendType": -1,
                "responseTime": "2020-01-13 15:40:30"
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  totalCount | 总数  
|  totalPages | 总页数
|  currentSize | 每页条数
|  pageNum | 当前页码
|  realTimeTouristNum | 实时游客数
|  todayTouristCount | 今日累积游客数
|  lastTodayTouristCount | 去年今日游客数
|  compareLastTouristCount | 同比去年
|  compareLastExtendType | -1：代表下降，1：代表上升
|  beforeLastTodayTouristCount | 前年今日游客数
|  compareBeforeLastTouristCount | 同比前年（null代表前年没有数据）
|  compareBeforeLastExtendType | -1：代表下降，1：代表上升
|  responseTime | 采集时间


## 票务数据 ##
### 路径 ###
<pre>http://192.168.10.39:30800/keyScenic/ticketList </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* keyScenic:ticketList
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | int  | 当前页码  |  Y |
|  size | int  | 每页条数  |  Y |
|  condition.scenicName | string  | 景区名称  |  Y |
|  condition.startTime | long  | 统计开始时间  |  Y |
|  condition.endTime | long  | 统计结束时间  |  Y |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "totalCount": 2076,
        "totalPages": 208,
        "currentSize": 10,
        "pageNum": 1,
        "list": [
            {
                "onlineTicketCount": 1221,
                "offlineTicketCount": 518,
                "onlineCablewayCount": 2276,
                "onlineSightseeingCarCount": 1215,
                "responseTime": "2020-01-13 16:55:30"
            },
            {
                "onlineTicketCount": 1221,
                "offlineTicketCount": 518,
                "onlineCablewayCount": 2079,
                "onlineSightseeingCarCount": 1219,
                "responseTime": "2020-01-13 16:10:30"
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  totalCount | 总数  
|  totalPages | 总页数
|  currentSize | 每页条数
|  pageNum | 当前页码
|  onlineTicketCount | 线上销售门票总数
|  offlineTicketCount | 线下销售门票总数
|  onlineCablewayCount | 线上索道票总数
|  onlineSightseeingCarCount | 线上观光车票总数
|  responseTime | 采集时间
