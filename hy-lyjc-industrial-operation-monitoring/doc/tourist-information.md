
## 旅游资讯统计月 ##
### 路径 ###
<pre> /tourist/information/statistics/month </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08000000",
    "message": "success",
    "success": true,
    "data": [
        {
            "dateString": "2020-02-22",
            "total": 0
        },
        {
            "dateString": "2020-02-23",
            "total": 0
        },
        {
            "dateString": "2020-02-24",
            "total": 0
        },
        {
            "dateString": "2020-02-25",
            "total": 0
        },
        {
            "dateString": "2020-02-26",
            "total": 0
        },
        {
            "dateString": "2020-02-27",
            "total": 0
        },
        {
            "dateString": "2020-02-28",
            "total": 0
        },
        {
            "dateString": "2020-02-29",
            "total": 0
        },
        {
            "dateString": "2020-03-01",
            "total": 0
        },
        {
            "dateString": "2020-03-02",
            "total": 0
        },
        {
            "dateString": "2020-03-03",
            "total": 0
        },
        {
            "dateString": "2020-03-04",
            "total": 0
        },
        {
            "dateString": "2020-03-05",
            "total": 0
        },
        {
            "dateString": "2020-03-06",
            "total": 0
        },
        {
            "dateString": "2020-03-07",
            "total": 0
        },
        {
            "dateString": "2020-03-08",
            "total": 6
        },
        {
            "dateString": "2020-03-09",
            "total": 0
        },
        {
            "dateString": "2020-03-10",
            "total": 0
        },
        {
            "dateString": "2020-03-11",
            "total": 0
        },
        {
            "dateString": "2020-03-12",
            "total": 0
        },
        {
            "dateString": "2020-03-13",
            "total": 0
        },
        {
            "dateString": "2020-03-14",
            "total": 0
        },
        {
            "dateString": "2020-03-15",
            "total": 0
        },
        {
            "dateString": "2020-03-16",
            "total": 0
        },
        {
            "dateString": "2020-03-17",
            "total": 0
        },
        {
            "dateString": "2020-03-18",
            "total": 0
        },
        {
            "dateString": "2020-03-19",
            "total": 0
        },
        {
            "dateString": "2020-03-20",
            "total": 0
        },
        {
            "dateString": "2020-03-21",
            "total": 0
        },
        {
            "dateString": "2020-03-22",
            "total": 0
        },
        {
            "dateString": "2020-03-23",
            "total": 2
        },
        {
            "dateString": "2020-03-24",
            "total": 0
        }
    ]
}
</pre>



## 旅游资讯统计年 ##
### 路径 ###
<pre> /tourist/information/statistics/year </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08000000",
    "message": "success",
    "success": true,
    "data": [
        {
            "dateString": "2019-04",
            "total": 0
        },
        {
            "dateString": "2019-05",
            "total": 0
        },
        {
            "dateString": "2019-06",
            "total": 0
        },
        {
            "dateString": "2019-07",
            "total": 0
        },
        {
            "dateString": "2019-08",
            "total": 0
        },
        {
            "dateString": "2019-09",
            "total": 0
        },
        {
            "dateString": "2019-10",
            "total": 0
        },
        {
            "dateString": "2019-11",
            "total": 0
        },
        {
            "dateString": "2019-12",
            "total": 0
        },
        {
            "dateString": "2020-01",
            "total": 0
        },
        {
            "dateString": "2020-02",
            "total": 0
        },
        {
            "dateString": "2020-03",
            "total": 9
        }
    ]
}
</pre>





## 旅游消费数据消费汇总 ##
### 路径 ###
<pre> /tourismConsumption/statistics </pre>
### 请求方式 ###
* POST
### 请求参数 ###
```js
{
	"beginDate":"1269679973000",
	"endDate":"1585299173000"
}
```
### 返回结果 ###
```js
{
    "code": "08000000",
    "message": "success",
    "success": true,
    "data": [
        {
            "cbdName": "县城区域商圈",//范围
            "transAt": "1089387756.62",//消费金额
            "transNum": 5755560,//消费笔数
            "acctNum": 1455501,//消费人次
            "transAtAvg": "748.46",//人均消费金额
            "transNumAvg": 3//人均消费笔数
        },
        {
            "cbdName": "七里坪商圈",
            "transAt": "1264447097.80",
            "transNum": 4345656,
            "acctNum": 912897,
            "transAtAvg": "1385.09",
            "transNumAvg": 4
        },
        {
            "cbdName": "柳江商圈",
            "transAt": "104329887.24",
            "transNum": 252900,
            "acctNum": 81336,
            "transAtAvg": "1282.70",
            "transNumAvg": 3
        },
        {
            "cbdName": "瓦屋山商圈",
            "transAt": "125250121.92",
            "transNum": 153020,
            "acctNum": 99945,
            "transAtAvg": "1253.19",
            "transNumAvg": 1
        },
        {
            "cbdName": "槽渔滩商圈",
            "transAt": "265358.22",
            "transNum": 7928,
            "acctNum": 1467,
            "transAtAvg": "180.88",
            "transNumAvg": 5
        }
    ]
}
```










## 旅游消费数据消费趋势 ##
### 路径 ###
<pre> /tourismConsumption/trend </pre>
### 请求方式 ###
* POST
### 请求参数 ###
```js
{
  "cbdName":""//范围,null则是不选择范围
	"beginDate":"1269679973000",
	"endDate":"1585299173000"
}
```
### 返回结果 ###
```js
{
    "code": "08000000",
    "message": "success",
    "success": true,
    "data": {
        "thisYearList": [//今年
            {
                "time": 1584806400000,//时间
                "transAtThisYear": "1717232.10",//今年消费金额
                "transAtLastYear": "5498744.82",//去年消费金额
                "compareLastYear": -220,//较去年同期
                "compareYesterday": null//较昨日环比
            },
            {
                "time": 1584892800000,
                "transAtThisYear": "1552312.20",
                "transAtLastYear": "5235718.24",
                "compareLastYear": -237,
                "compareYesterday": null
            },
            {
                "time": 1584979200000,
                "transAtThisYear": "1580969.12",
                "transAtLastYear": "6927391.20",
                "compareLastYear": -338,
                "compareYesterday": null
            }
        ],
        "lastYearList": [
            {
                "time": 1553184000000,
                "transAtThisYear": "5498744.82",
                "transAtLastYear": null,
                "compareLastYear": null,
                "compareYesterday": null
            },
            {
                "time": 1553270400000,
                "transAtThisYear": "5235718.24",
                "transAtLastYear": null,
                "compareLastYear": null,
                "compareYesterday": null
            },
            {
                "time": 1553356800000,
                "transAtThisYear": "6927391.20",
                "transAtLastYear": null,
                "compareLastYear": null,
                "compareYesterday": null
            },
            {
                "time": 1553443200000,
                "transAtThisYear": "4429420.98",
                "transAtLastYear": null,
                "compareLastYear": null,
                "compareYesterday": null
            },
            {
                "time": 1553529600000,
                "transAtThisYear": "3696166.48",
                "transAtLastYear": null,
                "compareLastYear": null,
                "compareYesterday": null
            }
        ]
    }
}
```




## 业态分布 ##
### 路径 ###
<pre> /tourismConsumption/industry </pre>
### 请求方式 ###
* POST
### 请求参数 ###
```js
{
	"beginDate":"1269679973000",
	"endDate":"1585299173000"
}
```
### 返回结果 ###
```js
{
    "code": "08000000",
    "message": "success",
    "success": true,
    "data": {
        "totalTransAt": null,
        "list": [
            {
                "type": "住宿",//行业
                "transAt": "1942502759.80",//游客消费金额
                "transNum": 6738568,//游客消费笔数
                "acctNum": 1604446,//游客消费人次
                "transAtRatio": "100.00",//游客消费额金额贡献度
                "avgTransAt": "1210.70",//人均消费金额
                "avgTransNum": "4.20"//人均消费笔数
            },
            {
                "type": "其他",
                "transAt": "4790053.94",
                "transNum": 120164,
                "acctNum": 23141,
                "transAtRatio": "13.20",
                "avgTransAt": "206.99",
                "avgTransNum": "5.19"
            },
            {
                "type": "购物",
                "transAt": "518130660.74",
                "transNum": 2845060,
                "acctNum": 738817,
                "transAtRatio": "15.10",
                "avgTransAt": "701.30",
                "avgTransNum": "3.85"
            },
            {
                "type": "餐饮",
                "transAt": "61573554.30",
                "transNum": 752124,
                "acctNum": 164155,
                "transAtRatio": "26.89",
                "avgTransAt": "375.09",
                "avgTransNum": "4.58"
            },
            {
                "type": "交通",
                "transAt": "586484.54",
                "transNum": 11736,
                "acctNum": 4706,
                "transAtRatio": "31.46",
                "avgTransAt": "124.62",
                "avgTransNum": "2.49"
            },
            {
                "type": "娱乐",
                "transAt": "76237063.82",
                "transNum": 109036,
                "acctNum": 35539,
                "transAtRatio": "18.50",
                "avgTransAt": "2145.17",
                "avgTransNum": "3.07"
            },
            {
                "type": "景区",
                "transAt": "204723.60",
                "transNum": 14740,
                "acctNum": 2930,
                "transAtRatio": "81.52",
                "avgTransAt": "69.87",
                "avgTransNum": "5.03"
            },
            {
                "type": "旅游综合服务",
                "transAt": "456.00",
                "transNum": 8,
                "acctNum": 2,
                "transAtRatio": "61.54",
                "avgTransAt": "228.00",
                "avgTransNum": "4.00"
            }
        ]
    }
}
```

## 消费来源地 ##
### 路径 ###
<pre> /tourismConsumption/source </pre>
### 请求方式 ###
* POST
### 请求参数 ###
```js
{
	"beginDate":"1582992000000",
	"endDate":"1585584000000"
}
```
### 返回结果 ###
```js
{
    "code": "08000000",
    "message": "success",
    "success": true,
    "data": {
        "totalTransAt": 46382964.77, // 总消费
        "list": [
            {
                "sourceId": "CHINA", // 来源地ID
                "sourceName": "中国", // 来源地名称
                "level": "COUNTRY", // 来源地级别 国COUNTRY、省PROVINCE、市CITY
                "transAt": 46382964.77,// 消费金额 元
                "transNum": 377947, // 消费笔数
                "acctNum": 85249, // 消费人次
                "avgTransAt": 544.09, // 人均消费金额 元
                "avgTransNum": 4.43, // 人均消费笔数
                "children": [ // 下级数据
                    {
                        "sourceId": "510000",
                        "sourceName": "四川省",
                        "level": "PROVINCE",
                        "transAt": 20231851.09,
                        "transNum": 149389,
                        "acctNum": 34533,
                        "avgTransAt": 585.87,
                        "avgTransNum": 4.33,
                        "children": [
                            {
                                "sourceId": "510100",
                                "sourceName": "成都市",
                                "level": "CITY",
                                "transAt": 20231851.09,
                                "transNum": 149389,
                                "acctNum": 34533,
                                "avgTransAt": 585.87,
                                "avgTransNum": 4.33,
                                "children": null
                            },
                            {
                                "sourceId": "510300",
                                "sourceName": "自贡市",
                                "level": "CITY",
                                "transAt": 20231851.09,
                                "transNum": 149389,
                                "acctNum": 34533,
                                "avgTransAt": 585.87,
                                "avgTransNum": 4.33,
                                "children": null
                            }
                        ]
                    }
                ]
            }
        ]
    }
}
```

## 消费来源地消费趋势 ##
### 路径 ###
<pre> /tourismConsumption/source/trend </pre>
### 请求方式 ###
* POST
### 请求参数 ###
```js
{
	"sourceId":"510100",
	"level":"CITY",
	"beginDate":"1582992000000",
	"endDate":"1585584000000"
}
```
### 返回结果 ###
```js
{
    "code": "08000000",
    "message": "success",
    "success": true,
    "data": [
        {
            "date": 1582992000000, // 日期时间戳
            "transAt": 289887.80, // 消费金额
            "lastTransAt": 491204.56, // 昨日消费金额
            "periodOnPeriod": -0.4098, // 昨日环比
            "sameTimeLastYear": 2022105.11, // 去年同期消费金额
            "yearOnYear": -0.8566 // 去年同比
        },
        {
            "date": 1583078400000,
            "transAt": 225085.57,
            "lastTransAt": 289887.80,
            "periodOnPeriod": -0.2235,
            "sameTimeLastYear": 2845448.73,
            "yearOnYear": -0.9209
        }
    ]
}
```
