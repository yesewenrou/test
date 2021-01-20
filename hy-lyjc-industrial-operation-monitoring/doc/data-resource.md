# 数据资源模块 #
## 数据资源列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/dataResource/list </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* dataResource:list
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | int  | 当前页码  |  Y |
|  size | int  | 每页条数  |  Y |
|  condition.scenicName | string  | 景区名称  |  N |
|  condition.countryName | string  | 国家  |  N |
|  condition.provName | string  | 省份  |  N |
|  condition.cityName | string  | 城市  |  N |
|  condition.startTime | long  | 统计开始时间  |  N |
|  condition.endTime | long  | 统计结束时间  |  N |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "hyTotalPeopleNum": 67038,
        "qlpTotalPeopleNum": 7944,
        "ljgzTotalPeopleNum": 51866,
        "cytTotalPeopleNum": 0,
        "wwsTotalPeopleNum": 15084,
        "ypsTotalPeopleNum": 5178，
        "pageResult": {
            "totalCount": 1000,
            "totalPages": 1000,
            "currentSize": 1,
            "pageNum": 1,
            "list": [
                {
                    "scenicName": "洪雅县",
                    "countryName": "中国",
                    "provName": "新疆",
                    "cityName": "伊犁哈萨克自治州",
                    "peopleNum": 8,
                    "time": "2020-02-01",
                    "datasource": "眉山移动",
                    "flag": "day"
                }
            ]
        }
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  hyTotalPeopleNum | 洪雅县人数合计  
|  qlpTotalPeopleNum | 七里坪人数合计  
|  ljgzTotalPeopleNum | 柳江古镇人数合计  
|  cytTotalPeopleNum | 槽渔滩人数合计  
|  wwsTotalPeopleNum | 瓦屋山人数合计  
|  ypsTotalPeopleNum | 玉屏山人数合计  
|  scenicName | 景区名称  
|  countryName | 国家
|  provName | 省份
|  cityName | 城市
|  peopleNum | 游客人数
|  time | 统计时间
|  createTime | 更新时间
|  datasource | 数据来源



## 景区列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/dataResource/scenicList </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* dataResource:scenicList
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
      "id": 18,
      "code": "002001",
      "name": "瓦屋山",
      "orderNumber": 1,
      "parentCode": "SCENIC_AREA",
      "children": []
    },
    {
      "id": 19,
      "code": "002002",
      "name": "七里坪",
      "orderNumber": 2,
      "parentCode": "SCENIC_AREA",
      "children": []
    }
  ]
}
</pre>


## 省市列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/dataResource/provinceList </pre>
### 请求方式 ###
* GET
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
	"data": [{
			"provId": "110000",
			"provName": "北京",
			"cities": [{
				"cityId": "10",
				"cityName": "北京市"
			}]
		},
		{
			"provId": "120000",
			"provName": "天津",
			"cities": [{
				"cityId": "22",
				"cityName": "天津市"
			}]
		}
	]
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  provId | 省份ID  
|  provName | 省份名
|  cityId | 城市ID
|  cityName | 城市名


## 游客来源地TOP10 ##
### 路径 ###
<pre>http://192.168.10.39:30800/dataResource/sourceCityTop </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* dataResource:sourceCityTop
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  year | int  | 年份  |  Y |
|  scope | int  | 1：省内，2：省外  |  N |
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
        "name": "眉山市",
        "value": 8873
      },
      {
        "name": "成都市",
        "value": 742
      },
      {
        "name": "乐山市",
        "value": 366
      },
      {
        "name": "重庆市",
        "value": 97
      },
      {
        "name": "深圳市",
        "value": 34
      },
      {
        "name": "上海市",
        "value": 34
      },
      {
        "name": "凉山彝族自治州",
        "value": 33
      },
      {
        "name": "德阳市",
        "value": 30
      },
      {
        "name": "宜宾市",
        "value": 30
      },
      {
        "name": "雅安市",
        "value": 30
      }
    ]
  }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  name | 城市名  
|  value | 游客总人数


## 游客来源地TOP10按天统计 ##
### 路径 ###
<pre>http://192.168.10.39:30800/dataResource/sourceCityTopByDay </pre>
### 请求方式 ###
* GET
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
    "outerProvince": [
      {
        "name": "重庆市",
        "value": 46
      },
      {
        "name": "上海市",
        "value": 40
      }
    ],
    "innerProvince": [
      {
        "name": "眉山市",
        "value": 9012
      },
      {
        "name": "成都市",
        "value": 814
      }
    ]
  }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  name | 城市名  
|  value | 游客总人数



## 游客来源区域统计 ##
### 路径 ###
<pre>/dataResource/tourist/region/statistics </pre>
### 请求方式 ###
*GET*
### 权限标识 ###
* 暂时无定义
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
* `beginDate`: long, 开始日期的时间戳，毫秒，必传
* `endDate`: long, 结束日期的时间戳，毫秒，必传
* `touristsScopeCode`: string, 游客范围数据字典code，必传。数据字典关键字：`TOURISTS_SCOPE`，如下：
    * "027001"：洪雅县
    * "027002": 瓦屋山
    * "027003": 柳江古镇
    * "027004": 玉屏山
    * "027005": 七里坪
    * "027006": 槽渔滩
#### 请求示例
> /dataResource/tourist/region/statistics?beginDate=1577808000000&endDate=1577808000000&touristsScopeCode=027001
### 返回结果 ###
#### 关于区域类型的分类（regionType)
* 1: 境内，表示中国大陆
* 2：境外，表示中国台湾、中国香港、中国澳门，及其他国家
* 3：中国，表示中国，包含台湾、香港、澳门在内。

```json
// json中新增了fullName字段，表示该区域的全称
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "total": 37225, // 游客总数
        "from": "眉山移动", // 数据来源
        "beginDate": 1577808000000,
        "endDate": 1577808000000,
        "touristsScopeCode": "027001", // 游客区域
        "list": [
            {
                "regionName": "中国", //区域名称
                "peopleNum": 37181, // 区域人数
                "fullName": "中国", // 区域全称
                "regionType": 3, // 区域类型，见“关于区域类型的分类”
                "children": [ // 子区域
                    {
                        "regionName": "四川",
                        "peopleNum": 33318,
                        "fullName": "中国_四川", // 区域全称
                        "regionType": 1,
                        "children": [
                            {
                                "regionName": "眉山市",
                                "peopleNum": 17951,
                                "fullName": "中国_四川_眉山市", // 区域全称
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "成都市",
                                "peopleNum": 7392,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "乐山市",
                                "peopleNum": 4937,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "雅安市",
                                "peopleNum": 899,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "绵阳市",
                                "peopleNum": 262,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "德阳市",
                                "peopleNum": 253,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "内江市",
                                "peopleNum": 234,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "宜宾市",
                                "peopleNum": 181,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "凉山彝族自治州",
                                "peopleNum": 176,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "自贡市",
                                "peopleNum": 174,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "泸州市",
                                "peopleNum": 121,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "遂宁市",
                                "peopleNum": 118,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "资阳市",
                                "peopleNum": 116,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "甘孜藏族自治州",
                                "peopleNum": 101,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "南充市",
                                "peopleNum": 91,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "攀枝花市",
                                "peopleNum": 72,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "广元市",
                                "peopleNum": 59,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "达州市",
                                "peopleNum": 57,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "广安市",
                                "peopleNum": 52,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "巴中市",
                                "peopleNum": 46,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "阿坝藏族羌族自治州",
                                "peopleNum": 26,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            }
                        ]
                    },
                    {
                        "regionName": "广东",
                        "peopleNum": 641,
                        "regionType": 1,
                        "children": [
                            {
                                "regionName": "深圳市",
                                "peopleNum": 371,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "广州市",
                                "peopleNum": 72,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "东莞市",
                                "peopleNum": 62,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "佛山市",
                                "peopleNum": 32,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "惠州市",
                                "peopleNum": 16,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "揭阳市",
                                "peopleNum": 16,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "中山市",
                                "peopleNum": 12,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "汕头市",
                                "peopleNum": 11,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "江门市",
                                "peopleNum": 10,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "湛江市",
                                "peopleNum": 7,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "珠海市",
                                "peopleNum": 6,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "河源市",
                                "peopleNum": 4,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "潮州市",
                                "peopleNum": 4,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "茂名市",
                                "peopleNum": 3,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "阳江市",
                                "peopleNum": 3,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "韶关市",
                                "peopleNum": 3,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "云浮市",
                                "peopleNum": 2,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "梅州市",
                                "peopleNum": 2,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "汕尾市",
                                "peopleNum": 2,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "清远市",
                                "peopleNum": 2,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            },
                            {
                                "regionName": "肇庆市",
                                "peopleNum": 1,
                                "regionType": 1,
                                "children": [
                                    
                                ]
                            }
                        ]
                    },
                    {
                        "regionName": "中国台湾",
                        "peopleNum": 3,
                        "regionType": 2,
                        "children": [
                            
                        ]
                    }
                ]
            },
            {
                "regionName": "泰国",
                "peopleNum": 6,
                "regionType": 2,
                "children": [
                    
                ]
            },
            {
                "regionName": "马来西亚",
                "peopleNum": 6,
                "regionType": 2,
                "children": [
                    
                ]
            }
        ]
    }
}
```





## 游客来源区域对比折线图 ##
### 路径 ###
<pre>/dataResource/tourist/region/compare </pre>
### 请求方式 ###
*GET*
### 权限标识 ###
* 暂时无定义
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
* `beginDate`: long, 开始日期的时间戳，毫秒，必传
* `endDate`: long, 结束日期的时间戳，毫秒，必传
* `touristsScopeCode`: string, 游客范围数据字典code，必传。数据字典关键字：`TOURISTS_SCOPE`，如下：
    * "027001"：洪雅县
    * "027002": 瓦屋山
    * "027003": 柳江古镇
    * "027004": 玉屏山
    * "027005": 七里坪
    * "027006": 槽渔滩
* `fullName`: string，在区域统计数据中返回了，例如`中国_四川_成都市`，必传。    
#### 请求示例
> /dataResource/tourist/region/compare?beginDate=1577850178000&endDate=1578008178000&touristsScopeCode=027001&fullName=中国_四川_成都市
### 返回结果 ###
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "list": [ // 数据列表，按照时间从早到近排序
            {
                "time": 1577808000000, // 时间
                "currentYearDayPeopleNum": 7392, // 当年当日人数
                "lastYearDayPeopleNum": 0, // 上一年当日人数
                "currentYearYesterdayPeopleNum": 6855, // 当年昨日人数
                "compareLastYear": 1.0, // 与去年相比的增长，1表示100% 
                "compareLastDay": 0.078 // 与昨日相比的增长， 0.078表示上升7.8%
            },
            {
                "time": 1577894400000,
                "currentYearDayPeopleNum": 5960,
                "lastYearDayPeopleNum": 0,
                "currentYearYesterdayPeopleNum": 7392,
                "compareLastYear": 1.0,
                "compareLastDay": -0.001 // 表示下降0.1%
            },
            {
                "time": 1577980800000,
                "currentYearDayPeopleNum": 6390,
                "lastYearDayPeopleNum": 0,
                "currentYearYesterdayPeopleNum": 5960,
                "compareLastYear": 1.0,
                "compareLastDay": 0.072
            }
        ],
        "timeList": [ // 按照时间从早到近排序的时间列表，用以做折线图的x轴
            1577808000000,
            1577894400000,
            1577980800000
        ]
    }
}
```