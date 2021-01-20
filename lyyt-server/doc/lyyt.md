# 旅游一张图模块 #
## 景区游客分析 ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/touristAnalyze </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "touristDataList": [
            {
                "name": "2019-09",
                "value": 32.45
            },
            {
                "name": "2019-10",
                "value": 42.17
            }
        ],
        "touristRank": [
            {
                "name": "玉屏山",
                "value": 43156.0
            }
        ],
        "lastTouristDataList": [
            {
                "name": "2018-01",
                "value": 0.0
            },
            {
                "name": "2018-02",
                "value": 0.0
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  touristRank | 景区客流排行 
|  touristDataList | 今年县域游客接待走势 
|  lastTouristDataList | 去年县域游客接待走势



## 游客来源地TOP10 ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/touristSourceCity </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "outerProvince": [
            {
                "name": "重庆市",
                "value": 143121.0
            },
            {
                "name": "上海市",
                "value": 34005.0
            }
        ],
        "touristCount": [
            {
                "name": "洪雅县",
                "value": 746244.0
            }
        ],
        "innerProvince": [
            {
                "name": "眉山市",
                "value": 876226.0
            },
            {
                "name": "成都市",
                "value": 600090.0
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  touristCount | 年度县域总游客 
|  innerProvince | 省内top 
|  outerProvince | 省外top


## 酒店入住分析 ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/hotelAnalyze </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "touristSourceTop": [
            {
                "name": "眉山市",
                "value": 700.0
            },
            {
                "name": "成都市",
                "value": 220.0
            },
            {
                "name": "重庆市",
                "value": 180.0
            },
            {
                "name": "内江市",
                "value": 80.0
            },
            {
                "name": "自贡市",
                "value": 3.0
            }
        ],
        "cumulativeReception": 1183.0,
        "hotelReceptionTop": [
            {
                "name": "4xxxxxxxxxxxxx",
                "value": 888.0
            },
            {
                "name": "6xxxxxxxxxxxxx",
                "value": 196.0
            },
            {
                "name": "1xxxxxxxxxxxxx",
                "value": 165.0
            },
            {
                "name": "3xxxxxxxxxxxxx",
                "value": 65.0
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  touristSourceTop | 入住游客来源地top
|  cumulativeReception | 当年累计入住人数 
|  hotelReceptionTop | 酒店接待排名top


## 酒店画像（省内省外游客） ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/hotelTouristSource </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "outer": [
            {
                "name": "重庆市",
                "value": 12871.0
            },
            {
                "name": "河南省",
                "value": 10123.0
            },
            {
                "name": "湖南省",
                "value": 6206.0
            },
            {
                "name": "湖北省",
                "value": 6063.0
            },
            {
                "name": "山东省",
                "value": 6023.0
            },
            {
                "name": "贵州省",
                "value": 5893.0
            },
            {
                "name": "陕西省",
                "value": 5837.0
            },
            {
                "name": "甘肃省",
                "value": 5505.0
            },
            {
                "name": "云南省",
                "value": 5324.0
            },
            {
                "name": "河北省",
                "value": 5049.0
            }
        ],
        "inner": [
            {
                "name": "成都市",
                "value": 156183.0
            },
            {
                "name": "乐山市",
                "value": 149366.0
            },
            {
                "name": "眉山地区",
                "value": 147305.0
            },
            {
                "name": "内江市",
                "value": 45782.0
            },
            {
                "name": "德阳市",
                "value": 33741.0
            },
            {
                "name": "绵阳市",
                "value": 24530.0
            },
            {
                "name": "自贡市",
                "value": 22581.0
            },
            {
                "name": "达川地区",
                "value": 20316.0
            },
            {
                "name": "泸州市",
                "value": 16847.0
            },
            {
                "name": "重庆市",
                "value": 16769.0
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  inner | 省内
|  outer | 省外 


## 游客画像分析 -> 过夜天数 ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/stayOvernight </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "dataList": [
            {
                "name": "1天",
                "value": 88461.0
            },
            {
                "name": "未过夜",
                "value": 17589.0
            },
            {
                "name": "2天",
                "value": 10268.0
            },
            {
                "name": "3天",
                "value": 3291.0
            },
            {
                "name": "4天及以上",
                "value": 3958.0
            }
        ],
        "avgOvernight": 1.4
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  avgOvernight | 平均过夜天数
|  dataList | 列表 


## 游客画像分析 -> 游客年龄 ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/tourismAge </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "name": "50",
            "value": 0.0
        },
        {
            "name": "60",
            "value": 0.0
        },
        {
            "name": "70",
            "value": 0.0
        },
        {
            "name": "80",
            "value": 0.0
        },
        {
            "name": "90",
            "value": 0.0
        },
        {
            "name": "00",
            "value": 0.0
        }
    ]
}
</pre>


## 游客画像分析 -> 游客性别 ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/tourismSex </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "femaleCount": 0.0,
        "maleCount": 0.0
    }
}
</pre>
### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  maleCount | 男性数目 
|  femaleCount | 女性数目


## 县域旅游收入分析 -> 各景区旅游收入占比 ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/businessCircleTourismConsumption </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "name": "柳江古镇商圈",
            "value": 80005.28
        },
        {
            "name": "七里坪商圈",
            "value": 72005.28
        },
        {
            "name": "主城区商圈",
            "value": 40001.76
        },
        {
            "name": "瓦屋山商圈",
            "value": 32001.76
        },
        {
            "name": "玉屏山商圈",
            "value": 17777.76
        }
    ]
}
</pre>


## 洪雅旅游一张图 -> 县域旅游收入分析 -> 县域收入分析 ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/tourismConsumptionAnalyze </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "name": "2019-01",
            "value": 56005.28
        },
        {
            "name": "2019-02",
            "value": 0.0
        },
        {
            "name": "2019-11",
            "value": 0.0
        },
        {
            "name": "2019-12",
            "value": 177814.08
        }
    ]
}
</pre>


## 游客画像分析 -> 消费行为分析 ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/industryConsumptionType </pre>
### 请求方式 ###
* GET
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


## 游客画像分析 -> 客源地人均消费 ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/perConsumptionCity </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "transAtYear": 233819.36,
        "cityConsumption": [
            {
                "name": "自贡市",
                "value": 107.5
            },
            {
                "name": "泸州市",
                "value": 90.0
            },
            {
                "name": "雅安市",
                "value": 90.0
            },
            {
                "name": "绵阳市",
                "value": 60.0
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  transAtYear | 年度旅游总收入 
|  cityConsumption | 客源地人均消费


# 产业运行监测模块 #
## 游客人数实时监测 ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/touristMonitor </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "yesterdayPeopleNum": 19370,
        "realTimeList": [
            {
                "id": null,
                "scenicId": "2830028",
                "scenicName": "瓦屋山",
                "peopleNum": 7022,
                "memberType": 0,
                "time": null,
                "datasource": "眉山移动",
                "flag": "minute"
            },
            {
                "id": null,
                "scenicId": "2830029",
                "scenicName": "七里坪",
                "peopleNum": 1853,
                "memberType": 0,
                "time": null,
                "datasource": "眉山移动",
                "flag": "minute"
            },
            {
                "id": null,
                "scenicId": "511423",
                "scenicName": "洪雅县",
                "peopleNum": 16738,
                "memberType": 0,
                "time": null,
                "datasource": "眉山移动",
                "flag": "minute"
            },
            {
                "id": null,
                "scenicId": "2830027",
                "scenicName": "玉屏山",
                "peopleNum": 714,
                "memberType": 0,
                "time": null,
                "datasource": "眉山移动",
                "flag": "minute"
            },
            {
                "id": null,
                "scenicId": "2830030",
                "scenicName": "柳江古镇",
                "peopleNum": 14272,
                "memberType": 0,
                "time": null,
                "datasource": "眉山移动",
                "flag": "minute"
            }
        ],
        "maxPeopleNum": 8782.0,
        "todayTotalPeopleNum": 19672,
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  realTimeList | 游客实时人数 
|  yesterdayPeopleNum | 昨日县域客流量
|  maxPeopleNum | 今日县域高峰游客数
|  todayTotalPeopleNum | 今日县域累计游客数


## 游客来源地top10（统计昨日数据） ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/latestTouristSourceCity </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "outerProvince": [
            {
                "id": null,
                "scenicName": "洪雅县",
                "countryName": "中国",
                "provName": "重庆",
                "cityName": "重庆市",
                "peopleNum": 1771,
                "time": "2019-11-06",
                "datasource": "眉山移动",
                "flag": "day"
            },
            {
                "id": null,
                "scenicName": "洪雅县",
                "countryName": "中国",
                "provName": "上海",
                "cityName": "上海市",
                "peopleNum": 1073,
                "time": "2019-11-06",
                "datasource": "眉山移动",
                "flag": "day"
            }
        ],
        "innerProvince": [
            {
                "id": null,
                "scenicName": "洪雅县",
                "countryName": "中国",
                "provName": "四川",
                "cityName": "眉山市",
                "peopleNum": 161881,
                "time": "2019-11-06",
                "datasource": "眉山移动",
                "flag": "day"
            },
            {
                "id": null,
                "scenicName": "洪雅县",
                "countryName": "中国",
                "provName": "四川",
                "cityName": "成都市",
                "peopleNum": 18168,
                "time": "2019-11-06",
                "datasource": "眉山移动",
                "flag": "day"
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  innerProvince | 省内top 
|  outerProvince | 省外top


## 住宿接待 ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/hotelPassengerReception </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "bedNumCount": 1090.0,
        "hotelCount": 4,
        "realTimeCheckInCount": 1338.0,
        "estimateOccupancy": "100%"
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  hotelCount | 酒店总数 
|  bedNumCount | 床位数
|  realTimeCheckInCount | 实时入住人数
|  estimateOccupancy | 估算入住率


## 旅游收入趋势+上周收入总分析 ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/tourismConsumptionTrend </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "businessCircleList": [
            {
                "name": "瓦屋山商圈",
                "value": 0.0
            },
            {
                "name": "七里坪商圈",
                "value": 42003.52
            },
            {
                "name": "柳江古镇商圈",
                "value": 16000.88
            }
        ],
        "tourismConsumptionTrend": [
            {
                "name": "2019-11-25",
                "value": 0.0
            },
            {
                "name": "2019-12-07",
                "value": 24001.76
            },
            {
                "name": "2019-12-08",
                "value": 14001.76
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  businessCircleList | 上周商圈收入分析 
|  tourismConsumptionTrend | 半个月旅游收入趋势图


## 近30天舆情 ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/publicSentimentKeyword </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "name": "洪雅",
            "value": 206.0
        },
        {
            "name": "疫情",
            "value": 127.0
        },
        {
            "name": "眉山",
            "value": 71.0
        },
        {
            "name": "肺炎",
            "value": 68.0
        },
        {
            "name": "洪雅县",
            "value": 64.0
        },
        {
            "name": "新型",
            "value": 59.0
        },
        {
            "name": "眉山市",
            "value": 42.0
        },
        {
            "name": "致敬",
            "value": 34.0
        }
    ]
}
</pre>


# 节假日专题模块 #
## 获取年度法定节假日 ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/holiday </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "key": "元旦节",
            "value": "YDJ"
        },
        {
            "key": "春节",
            "value": "CJ"
        },
        {
            "key": "清明节",
            "value": "QMJ"
        },
        {
            "key": "劳动节",
            "value": "LDJ"
        },
        {
            "key": "端午节",
            "value": "DWJ"
        },
        {
            "key": "中秋节",
            "value": "ZQJ"
        },
        {
            "key": "国庆节",
            "value": "GQJ"
        }
    ]
}
</pre>


## 景区游客接待分析（按节日统计） ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/holidayScenicTourist?holiday=YDJ </pre>
### 请求方式 ###
* GET
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  holiday | 枚举  | 节日名称大写，即holiday接口返回的value  |  Y |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "touristCount": [
            {
                "name": "洪雅县",
                "value": 499889.0
            }
        ],
        "scenicList": [
            {
                "name": "玉屏山",
                "value": 32456.0
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  startDate | 节日开始时间 
|  endDate | 节日结束时间 


## 游客来源地TOP10（按节日统计） ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/holidayTouristSourceCity?holiday=YDJ </pre>
### 请求方式 ###
* GET
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  holiday | 枚举  | 节日名称大写，即holiday接口返回的value  |  Y |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "sourceCityTop": [
            {
                "name": "眉山市",
                "value": 448276.0
            },
            {
                "name": "成都市",
                "value": 347401.0
            },
            {
                "name": "乐山市",
                "value": 264648.0
            }
        ]
    }
}
</pre>


## 年龄和性别（按节日统计） ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/holidayTouristAgeSex?holiday=YDJ </pre>
### 请求方式 ###
* GET
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  holiday | 枚举  | 节日名称大写，即holiday接口返回的value  |  Y |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "ageChart": [
            {
                "name": "50",
                "value": 0.0
            },
            {
                "name": "60",
                "value": 0.0
            },
            {
                "name": "70",
                "value": 2.0
            },
            {
                "name": "80",
                "value": 3.0
            },
            {
                "name": "90",
                "value": 3.0
            },
            {
                "name": "00",
                "value": 0.0
            },
            {
                "name": "其他",
                "value": 0.0
            }
        ],
        "compareFemale": "",
        "peopleCount": 5012786.0,
        "maleCount": 7.0,
        "compareMale": "",
        "femaleCount": 1.0
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  ageChart | 年龄扇形图 
|  peopleCount | 节假日男女总人数
|  maleCount | 男性比例（需根据总人数+比例，得到估算男性人数） 
|  femaleCount | 女性比例 
|  compareMale | 男性同去年变化 
|  compareFemale | 女性同去年变化 


## 旅游收入分析（按节日统计） ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/holidayTourismConsumption?holiday=YDJ </pre>
### 请求方式 ###
* GET
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  holiday | 枚举  | 节日名称大写，即holiday接口返回的value  |  Y |
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
        "compareTransAt": "12.0%",
        "industryConsumption": [
            {
                "name": "餐饮",
                "value": 0.0
            },
            {
                "name": "酒店",
                "value": 0.0
            },
            {
                "name": "购物",
                "value": 0.0
            },
            {
                "name": "娱乐",
                "value": 0.0
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  consumptionTrend | 今年消费趋势图 
|  lastConsumptionTrend | 去年消费趋势图
|  transAtTotal | 旅游总收入 
|  compareTransAt | 同比变化 
|  industryConsumption | 各行业占比  


## 住宿接待分析（按节日统计） ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/holidayPassengerReception?holiday=YDJ </pre>
### 请求方式 ###
* GET
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  holiday | 枚举  | 节日名称大写，即holiday接口返回的value  |  Y |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "lastHotelAreaReception": [
            {
                "name": "洪川镇",
                "value": 0.0
            },
            {
                "name": "东岳镇",
                "value": 0.0
            },
            {
                "name": "将军镇",
                "value": 0.0
            },
            {
                "name": "瓦屋山镇",
                "value": 0.0
            },
            {
                "name": "七里坪镇",
                "value": 0.0
            },
            {
                "name": "中山镇",
                "value": 0.0
            },
            {
                "name": "余坪镇",
                "value": 0.0
            },
            {
                "name": "高庙镇",
                "value": 0.0
            },
            {
                "name": "柳江镇",
                "value": 0.0
            },
            {
                "name": "槽渔滩镇",
                "value": 0.0
            },
            {
                "name": "止戈镇",
                "value": 0.0
            }
        ],
        "hotelReceptionTop": [],
        "passengerReception": 0.0,
        "hotelAreaReception": [
            {
                "name": "洪川镇",
                "value": 0.0
            },
            {
                "name": "东岳镇",
                "value": 0.0
            },
            {
                "name": "将军镇",
                "value": 0.0
            },
            {
                "name": "瓦屋山镇",
                "value": 0.0
            },
            {
                "name": "七里坪镇",
                "value": 0.0
            },
            {
                "name": "中山镇",
                "value": 0.0
            },
            {
                "name": "余坪镇",
                "value": 0.0
            },
            {
                "name": "高庙镇",
                "value": 0.0
            },
            {
                "name": "柳江镇",
                "value": 0.0
            },
            {
                "name": "槽渔滩镇",
                "value": 0.0
            },
            {
                "name": "止戈镇",
                "value": 0.0
            }
        ],
        "compareTransAt": ""
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  hotelReceptionTop | 热门酒店排行 
|  passengerReception | 累计入住人数 
|  compareTransAt | 同比变化  
|  hotelAreaReception | 今年区域接待 
|  lastHotelAreaReception | 去年区域接待  


# 旅游商户模块 #
## 旅游商户专题 -> 收入类型分析 ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/industryConsumptionAnalyze </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "lastIndustryConsumption": [],
        "industryConsumption": [
            {
                "name": "住宿",
                "value": 1.4299654778E8
            },
            {
                "name": "其他",
                "value": 301026.28
            },
            {
                "name": "购物",
                "value": 4.497895958E7
            },
            {
                "name": "餐饮",
                "value": 3013669.26
            },
            {
                "name": "交通",
                "value": 44325.78
            },
            {
                "name": "娱乐",
                "value": 3207251.5
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  industryConsumption | 今年行业收入 
|  lastIndustryConsumption | 去年行业收入


## 旅游商户专题 -> 各区域月度营收 ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/businessCircleConsumptionMonth </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "startTime": "2020-01-01",
        "endTime": "2020-01-31",
        "month": "1",
        "year": "2020",
        "transAtTotal": 3.921897548E7,
        "businessCircleConsumption": [
            {
                "name": "七里坪商圈",
                "value": 9724426.72
            },
            {
                "name": "县城区域商圈",
                "value": 2.740659152E7
            },
            {
                "name": "柳江商圈",
                "value": 447003.54
            },
            {
                "name": "瓦屋山商圈",
                "value": 1256017.26
            },
            {
                "name": "槽渔滩商圈",
                "value": 500.0
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  transAtTotal | 县域当月营收 
|  businessCircleConsumption | 各商圈营收
|  year | 年
|  month | 月


## 旅游商户专题 -> 各区域年度消费总金额，人均消费 ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/businessCircleConsumptionYear </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "name": "七里坪商圈",
            "transAt": 7.88922925E7,
            "acctNum": 49893.0,
            "perConsumption": 1581.23
        },
        {
            "name": "县城区域商圈",
            "transAt": 9.596837932E7,
            "acctNum": 120528.0,
            "perConsumption": 796.23
        },
        {
            "name": "柳江商圈",
            "transAt": 1.32609699E7,
            "acctNum": 5929.0,
            "perConsumption": 2236.63
        },
        {
            "name": "槽渔滩商圈",
            "transAt": 28111.46,
            "acctNum": 236.0,
            "perConsumption": 119.12
        },
        {
            "name": "瓦屋山商圈",
            "transAt": 6236092.8,
            "acctNum": 4139.0,
            "perConsumption": 1506.67
        }
    ]
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  name | 商圈名 
|  transAt | 总金额
|  acctNum | 消费人次
|  perConsumption | 人均消费


## 旅游商户专题 -> 本周商户营收走势 ##
### 路径 ###
<pre>http://192.168.10.39:30250/industrialMonitor/businessConsumptionTrend </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "weekData": [
            {
                "name": "2019-10-25",
                "value": 4656773.1
            },
            {
                "name": "2019-10-26",
                "value": 4147579.04
            },
            {
                "name": "2019-10-27",
                "value": 5132714.28
            },
            {
                "name": "2019-10-28",
                "value": 1.273518378E7
            },
            {
                "name": "2019-10-29",
                "value": 3208429.98
            },
            {
                "name": "2019-10-30",
                "value": 1658977.14
            },
            {
                "name": "2019-10-31",
                "value": 5875240.9
            }
        ],
        "weekBusinessConsumption": 3.741489822E7,
        "lastWeekData": [
            {
                "name": "2019-10-18",
                "value": 5783558.4
            },
            {
                "name": "2019-10-19",
                "value": 9641534.32
            },
            {
                "name": "2019-10-20",
                "value": 3452713.68
            },
            {
                "name": "2019-10-21",
                "value": 3571362.86
            },
            {
                "name": "2019-10-22",
                "value": 6500818.24
            },
            {
                "name": "2019-10-23",
                "value": 2960552.38
            },
            {
                "name": "2019-10-24",
                "value": 3386685.68
            }
        ],
        "lastWeekBusinessConsumption": 3.529722556E7,
        "compareTransAt": 6.0
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  weekData | 本周收入走势 
|  lastWeekData | 上周收入走势
|  weekBusinessConsumption | 本周营收总额
|  lastWeekBusinessConsumption | 上周营收总额
|  compareTransAt | 涨跌幅度

## 旅游商户专题 -> 商户红黑榜 ##
### 路径 ###
<pre>http://192.168.10.39:30250/lysh/credit/trend/list </pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "00000000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "merchantName": "洪雅县七里坪购物中心",
            "trendType": 1
        },
        {
            "merchantName": "洪雅邻你商贸有限公司",
            "trendType": 1
        },
        {
            "merchantName": "洪雅县华生温泉酒店",
            "trendType": 1
        },
        {
            "merchantName": "洪雅县半岛度假酒店有限责任公司",
            "trendType": 1
        },
        {
            "merchantName": "洪雅县罗粮饭店",
            "trendType": 1
        },
        {
            "merchantName": "眉山岷江东湖饭店洪雅瓦屋山居分公司",
            "trendType": 1
        },
        {
            "merchantName": "洪雅县李家大院",
            "trendType": 1
        },
        {
            "merchantName": "洪雅县东林尚品酒楼",
            "trendType": 1
        },
        {
            "merchantName": "洪雅县德元楼餐饮有限公司",
            "trendType": 1
        },
        {
            "merchantName": "洪雅七里坪温泉酒店管理有限公司",
            "trendType": 1
        },
        {
            "merchantName": "四川新希望乳业有限公司",
            "trendType": 1
        },
        {
            "merchantName": "洪雅县高庙古镇酒业有限公司",
            "trendType": 1
        },
        {
            "merchantName": "洪雅县雅康影业有限公司",
            "trendType": 1
        },
        {
            "merchantName": "洪雅发展投资控股有限责任公司",
            "trendType": 1
        },
        {
            "merchantName": "四川洪雅七里坪半山旅游开发有限公司",
            "trendType": 1
        },
        {
            "merchantName": "四川省洪雅县洪州大酒店",
            "trendType": 1
        },
        {
            "merchantName": "洪雅县豪庭酒店有限公司",
            "trendType": 1
        },
        {
            "merchantName": "四川信和文化旅游有限公司",
            "trendType": 1
        }
    ]
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  merchantName | 商户名称
|  trendType | 1:红榜 0:黑榜

## 旅游商户专题 -> 商户巡查合格率 ##
### 路径 ###
<pre>http://192.168.10.39:30250/lysh/patrol/statistic</pre>
### 请求方式 ###
* GET
### 返回结果 ###
<pre>
{
    "code": "00000000",
    "message": "操作成功",
    "success": true,
    "data": {
        "percentage": "75.00"
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  percentage | 合格率

## 旅游监测->预警待确认统计
### 路径 ###
```text
/lyjc/unconfirmedWarning
```
### 请求方式 ###
* GET
### 返回结果 ###
```text
{
    "code": "00000000",
    "message": "处理成功",
    "success": true,
    "data": [
        {
            "code": "035001",
            "count": 22
        },
        {
            "code": "035002",
            "count": 0
        },
        {
            "code": "035003",
            "count": 0
        }
    ]
}
```
### 返回参数说明
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  035001 | 客流量预警
|  035002 | 停车位预警
|  035003 | 交通拥堵预警