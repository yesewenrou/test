# 游客热力图模块 #
## 县域游客数据统计（右侧顶部两个数据展示） ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismHot/countyStatistical </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* tourismHot:countyStatistical
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
        "maxPeopleNum": 11246.0,
        "realTimePeopleNum": 17257
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  realTimePeopleNum |  县域实时游客数
|  maxPeopleNum | 县域昨日高峰游客数


## 当天县域游客趋势 ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismHot/countyTourismTrend </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* tourismHot:countyTourismTrend
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
            "name": "00",
            "value": 7698
        },
        {
            "name": "23",
            "value": 0
        }
    ]
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  name |  小时
|  value | 人数


## 当天景区游客趋势 ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismHot/scenicTourismTrend </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* tourismHot:scenicTourismTrend
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
        "玉屏山": [
            {
                "name": "00",
                "value": 422
            },
            {
                "name": "23",
                "value": 0
            }
        ],
        "七里坪": [
            {
                "name": "00",
                "value": 1357
            },
            {
                "name": "23",
                "value": 0
            }
        ],
        "柳江古镇": [
            {
                "name": "00",
                "value": 7428
            },
            {
                "name": "23",
                "value": 0
            }
        ],
        "瓦屋山": [
            {
                "name": "00",
                "value": 740
            },
            {
                "name": "23",
                "value": 0
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  name |  小时
|  value | 人数