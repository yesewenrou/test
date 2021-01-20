# 旅游消费分析
## 目录
- [查询旅游消费分析](#查询旅游消费分析)
- [查询旅游消费来源地分析](#查询旅游消费来源地分析)
- [查询旅游消费行业分析](#查询旅游消费行业分析)
- [查询旅游消费商圈分析](#查询旅游消费商圈分析)

## 查询旅游消费分析
### 请求地址
```
/tourism-consumption/analyze
```

### 权限标识
- `tourism-consumption:analyze`

### 请求方式
- `GET`

### 请求参数说明
无

### 请求示例
/tourism-consumption/analyze

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | object | 返回数据 | 
&emsp;currentMonth | object | 当月 | 
&emsp;&emsp;transAt | number | 当月消费 | 
&emsp;&emsp;sameTimeLastYear | number | 去年同期消费 | 
&emsp;&emsp;yearOnYear | number | 同比去年 | 展示百分比时需要*100
&emsp;lastMonth | object | 上月 | 
&emsp;&emsp;transAt | number | 上月消费 | 
&emsp;&emsp;sameTimeLastYear | number | 去年同期消费 | 
&emsp;&emsp;yearOnYear | number | 同比去年 | 展示百分比时需要*100
&emsp;currentYear | object | 当年 | 
&emsp;&emsp;transAt | number | 当年消费 | 
&emsp;&emsp;sameTimeLastYear | number | 去年同期消费 | 
&emsp;&emsp;yearOnYear | number | 同比去年 | 展示百分比时需要*100

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "currentMonth": {
            "transAt": 2468694.10,
            "sameTimeLastYear": 11785557.12,
            "yearOnYear": -0.7905
        },
        "lastMonth": {
            "transAt": 29025613.92,
            "sameTimeLastYear": 239014027.62,
            "yearOnYear": -0.8786
        },
        "currentYear": {
            "transAt": 213232805.38,
            "sameTimeLastYear": 556714705.22,
            "yearOnYear": -0.6170
        }
    }
}
```

## 查询旅游消费来源地分析
### 请求地址
```
/tourism-consumption/analyze/source
```

### 权限标识
- `tourism-consumption:analyze:source`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
type | String | true | 查询类型 | DAY-天  MONTH-月
beginDate | number | true | 开始日期 | 时间戳
endDate | number | true | 结束日期 | 时间戳

### 请求示例
/tourism-consumption/analyze/source?type=MONTH&beginDate=1546272000000&endDate=1582992000000

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | object | 返回数据 | 
&emsp;totalTransAt | number | 消费总金额 | 
&emsp;innerProvTransAt | number | 省内消费总金额 | 
&emsp;outerProvTransAt | number | 省外消费总金额 | 
&emsp;innerProvList | array | 省内城市消费TOP10 | 
&emsp;&emsp;source | string | 来源地 | 
&emsp;&emsp;transAt | number | 消费金额 | 
&emsp;&emsp;sameTimeLastYear | number | 去年同期消费金额 | 
&emsp;&emsp;yearOnYear | number | 去年同比 | 展示百分比时需要*100
&emsp;&emsp;proportion | number | 占总消费比例 | 展示百分比时需要*100
&emsp;&emsp;acctNum | number | 消费人次 | 
&emsp;&emsp;perAcctTransAt | number | 人均消费 | 
&emsp;outerProvList | array | 省外省份消费TOP10 | 
&emsp;&emsp;source | string | 来源地 | 
&emsp;&emsp;transAt | number | 消费金额 | 
&emsp;&emsp;sameTimeLastYear | number | 去年同期消费金额 | 
&emsp;&emsp;yearOnYear | number | 去年同比 | 展示百分比时需要*100
&emsp;&emsp;proportion | number | 占总消费比例 | 展示百分比时需要*100
&emsp;&emsp;acctNum | number | 消费人次 | 
&emsp;&emsp;perAcctTransAt | number | 人均消费 | 

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "totalTransAt": 2570951723.85,
        "innerProvTransAt": 1379944261.12,
        "innerProvList": [
            {
                "source": "成都市",
                "transAt": 925042657.62,
                "sameTimeLastYear": 261802535.57,
                "yearOnYear": 2.5334,
                "proportion": 0.6703,
                "acctNum": 871349,
                "perAcctTransAt": 1061.62
            }
        ],
        "outerProvTransAt": 1191007462.73,
        "outerProvList": [
            {
                "source": "广东省",
                "transAt": 191463599.02,
                "sameTimeLastYear": 56414126.94,
                "yearOnYear": 2.3939,
                "proportion": 0.1608,
                "acctNum": 208996,
                "perAcctTransAt": 916.11
            }
        ]
    }
}
```

## 查询旅游消费行业分析
### 请求地址
```
/tourism-consumption/analyze/industry
```

### 权限标识
- `tourism-consumption:analyze:industry`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
type | String | true | 查询类型 | DAY-天  MONTH-月
beginDate | number | true | 开始日期 | 时间戳
endDate | number | true | 结束日期 | 时间戳

### 请求示例
/tourism-consumption/analyze/industry?type=MONTH&beginDate=1546272000000&endDate=1582992000000

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | array | 返回数据 | 
&emsp;industry | string | 行业名称 | 
&emsp;transAt | number | 消费金额 | 
&emsp;sameTimeLastYear | number | 去年同期消费金额 | 
&emsp;yearOnYear | number | 去年同比 | 展示百分比时需要*100
&emsp;transAtRatio | number | 游客贡献度 | 展示百分比时需要*100
&emsp;acctNum | number | 消费人次 | 
&emsp;perAcctTransAt | number | 人均消费 | 


### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "industry": "住宿",
            "transAt": 1925710588.06,
            "sameTimeLastYear": 504942958.00,
            "yearOnYear": 2.8137,
            "transAtRatio": 1.0000,
            "acctNum": 1578103,
            "perAcctTransAt": 1220.27
        },
        {
            "industry": "购物",
            "transAt": 503437957.04,
            "sameTimeLastYear": 147016811.04,
            "yearOnYear": 2.4244,
            "transAtRatio": 0.1578,
            "acctNum": 706299,
            "perAcctTransAt": 712.78
        },
        {
            "industry": "娱乐",
            "transAt": 75411631.10,
            "sameTimeLastYear": 24681374.26,
            "yearOnYear": 2.0554,
            "transAtRatio": 0.2091,
            "acctNum": 34998,
            "perAcctTransAt": 2154.74
        },
        {
            "industry": "餐饮",
            "transAt": 61140733.16,
            "sameTimeLastYear": 21884351.36,
            "yearOnYear": 1.7938,
            "transAtRatio": 0.2637,
            "acctNum": 161835,
            "perAcctTransAt": 377.80
        },
        {
            "industry": "其他",
            "transAt": 5251146.48,
            "sameTimeLastYear": 948850.18,
            "yearOnYear": 4.5342,
            "transAtRatio": 0.1367,
            "acctNum": 28695,
            "perAcctTransAt": 183.00
        }
    ]
}
```

## 查询旅游消费商圈分析
### 请求地址
```
/tourism-consumption/analyze/business-circle
```

### 权限标识
- `tourism-consumption:analyze:business-circle`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
type | String | true | 查询类型 | DAY-天  MONTH-月
cbdName | String | true | 商圈名称 | 
beginDate | number | true | 开始日期 | 时间戳
endDate | number | true | 结束日期 | 时间戳

### 请求示例
/tourism-consumption/analyze/business-circle?type=MONTH&beginDate=1546272000000&endDate=1582992000000&cbdName=柳江商圈

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | object | 返回数据 | 
&emsp;totalTransAt | number | 总消费 | 单位：元
&emsp;merchantNum | number | 涉旅商户数 | 
&emsp;list | array | 消费数据 | 
&emsp;&emsp;date | string | 日期 | 时间戳
&emsp;&emsp;transAt | number | 消费金额 | 
&emsp;&emsp;lastTransAt | number | 昨日或上月金额 | 
&emsp;&emsp;periodOnPeriod | number | 环比 | 展示百分比时需要*100
&emsp;&emsp;sameTimeLastYear | number | 去年同期消费金额 | 
&emsp;&emsp;yearOnYear | number | 去年同比 | 展示百分比时需要*100

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "totalTransAt": 5761290.92,
        "merchantNum": 1334,
        "list": [
            {
                "date": 1577808000000,
                "transAt": 5708414.10,
                "lastTransAt": 7876896.64,
                "periodOnPeriod": -0.2753,
                "sameTimeLastYear": 14293556.70,
                "yearOnYear": -0.6006
            },
            {
                "date": 1580486400000,
                "transAt": 6765.60,
                "lastTransAt": 5708414.10,
                "periodOnPeriod": -0.9988,
                "sameTimeLastYear": 13076683.58,
                "yearOnYear": -0.9995
            },
            {
                "date": 1582992000000,
                "transAt": 46111.22,
                "lastTransAt": 6765.60,
                "periodOnPeriod": 5.8155,
                "sameTimeLastYear": 3314421.20,
                "yearOnYear": -0.9861
            }
        ]
    }
}
```