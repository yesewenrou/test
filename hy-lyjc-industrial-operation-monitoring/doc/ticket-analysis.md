# 景区游客数据

## 客流分析
### 路径 ###
<pre>/touristTicketAnalysis/tourist </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* 无
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  

### 请求参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  beginDate | int  | 开始时间   |  Y |
|  endDate | int  | 结束时间  |  Y |
|  scenicName | string | 景区名称 | Y |


### 返回参数说明 ###
| 参数名字 | 参数描述  | 额外说明
| :------------| :------------ |:---
|  dateRangeTotalTourist | 客流量合计 |
|  touristsLineChartVO | 折线图 |
|  touristCompareVO | [游客对比统计](#游客对比统计说明) |[点击见详情](#游客对比统计说明)

#### 游客对比统计说明  
| 参数名字 | 参数描述   | 额外说明
| :------------| :------------ |:---
|currentTimeTourists| 当前游客实时数|
|today|[今日对比](#对比信息说明)|[点击见详情](#对比信息说明)
|currentMonth|[当月对比](#对比信息说明)|[点击见详情](#对比信息说明)
|currentYear|[当年对比](#对比信息说明) |[点击见详情](#对比信息说明)

##### 对比信息说明
| 参数名字 | 参数描述  | 额外说明
| :------------| :------------|:------------
|currentTotalTourists|当前累计客流量|
|lastYearTotalTourists|去年同期客流量|
|compareLastExtendType|同比扩展类型| 大于0,向上箭头、小于0 向下箭头。具体值为 compareLastCount
|compareLastCount|游客总数同比去年| 百分比(不包含百分号)

### 返回结果 ###
```
{
    "code": "16000000",
    "message": "",
    "success": true,
    "data": {
        "dateRangeTotalTourist": 43739,
        "touristsLineChartVO": [
            {
                "day": "01月06日",
                "tourists": 584
            },
            {
                "day": "01月25日",
                "tourists": 1109
            }
        ],
        "touristCompareVO": {
            "currentTimeTourists": 0,
            "today": {
                "currentTotalTourists": 0,
                "lastYearTotalTourists": 67275,
                "compareLastExtendType": -1,
                "compareLastCount": 100.0
            },
            "currentMonth": {
                "currentTotalTourists": 0,
                "lastYearTotalTourists": 165487,
                "compareLastExtendType": -1,
                "compareLastCount": 100.0
            },
            "currentYear": {
                "currentTotalTourists": 43739,
                "lastYearTotalTourists": 214307,
                "compareLastExtendType": -1,
                "compareLastCount": 79.6
            }
        }
    }
}
</pre>
```






## 票务分析
### 路径 ###
<pre>/touristTicketAnalysis/ticket </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* 无
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  

### 请求参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  beginDate | int  | 开始时间   |  Y |
|  endDate | int  | 结束时间  |  Y |
|  scenicName | string | 景区名称 | Y |


### 返回参数说明 ###
| 参数名字 | 参数描述  | 额外说明
| :------------| :------------ |:---
|totalTickets| 门票合计
|sightseeingCarTickets|观光车合计
|cablewayTickets|索道合计
|ticketLineChart|门票折线图
|sightseeingCarLineChart|观光车折线图
|cablewayLineChart|索道折线图
|ticketVO|[门票统计](#门票统计详细说明)| [点击见详情](#门票统计详细说明)
|cablewayVO|[索道统计](#索道统计详细说明)|[点击见详情](#索道统计详细说明)
|sightseeingCarVO|[观光车统计](#索道统计详细说明)| [点击见详情](#索道统计详细说明)、同索道统计

#### 门票统计详细说明
| 参数名字 | 参数描述  | 额外说明
| :------------| :------------ |:---
|currentDay| [今日累计](#门票统计累计详情说明) | [点击见详情](#门票统计累计详情说明)
|currentMonth| [当月累计](#门票统计累计详情说明) |[点击见详情](#门票统计累计详情说明)
|currentYear| [当年累计](#门票统计累计详情说明) | [点击见详情](#门票统计累计详情说明)

##### 门票统计累计详情说明
| 参数名字 | 参数描述  | 额外说明
| :------------| :------------ |:---
|onlineTickets|线上累计
|offlineTickets|线下累计
|total|合计
#### 索道统计详细说明
| 参数名字 | 参数描述  | 额外说明
| :------------| :------------ |:---
|dayTickets| 今日累计
|monthTickets|当月累计
|yearTickets|当年累计
### 返回结果示例
```
{
    "code": "16000000",
    "message": "",
    "success": true,
    "data": {
        "totalTickets": 43739,
        "sightseeingCarTickets": 31209,
        "cablewayTickets": 52565,
        "ticketLineChart": [
            {
                "day": "01月06日",
                "tickets": 584
            }
        ],
        "sightseeingCarLineChart": [
            {
                "day": "01月06日",
                "tickets": 333
            }
          
        ],
        "cablewayLineChart": [
            {
                "day": "01月06日",
                "tickets": 637
            }
         ],
        "ticketVO": {
            "currentDay": {
                "onlineTickets": 0,
                "offlineTickets": 0,
                "total": 0
            },
            "currentMonth": {
                "onlineTickets": 0,
                "offlineTickets": 0,
                "total": 0
            },
            "currentYear": {
                "onlineTickets": 31209,
                "offlineTickets": 12530,
                "total": 43739
            }
        },
        "cablewayVO": {
            "dayTickets": 0,
            "monthTickets": 0,
            "yearTickets": 52565
        },
        "sightseeingCarVO": {
            "dayTickets": 0,
            "monthTickets": 0,
            "yearTickets": 31209
        }
    }
}
```

