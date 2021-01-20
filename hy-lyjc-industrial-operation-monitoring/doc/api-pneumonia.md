# 疫情分析
## 目录
   - [外来人员](#外来人员)
   - [外来车辆](#外来车辆)
   - [外来车辆统计中的城市接口](#外来车辆统计中的城市接口)
### 外来人员
#### 请求地址
```
/pneumonia/peopleFromForeign
```
#### 权限标识
- `pneumonia:peopleFromForeign`
#### 请求方式
- `GET`

#### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :--- | :--- | :--- | :---
city | string | true | 城市名称 |  数据字典接口: /msm-admin-area/province/list
dateType | string | true | 日期类型 |   day month
begin | number |true | 开始时间 | 
end |number | true | 结束日期 | 

#### 响应参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | object | 返回数据 | 
whCount|number |武汉返乡人数|
hbCount|number|湖北返乡人数|
lineCharts| list|折线图
&emsp;date| string|日期
&emsp;currentYear|number| 今年人数
&emsp;lastYear| number|去年人数
&emsp;compareLastYear|number| 较去年同期
&emsp;compareLast|number| 较昨日丶上月环比

#### 响应示例
```
{
   {
       "code": "08010000",
       "message": "操作成功",
       "success": true,
       "data": {
           "whCount": 6536,
           "hbCount": 13456,
           "lineCharts": [
               {
                   "date": "2020-01",
                   "currentYear": 77,
                   "lastYear": 0,
                   "compareLastYear": null,
                   "compareLast": null
               },
               {
                   "date": "2020-02",
                   "currentYear": 0,
                   "lastYear": 0,
                   "compareLastYear": null,
                   "compareLast": -1.0
               }
           ]
       }
   }
}
```
### 外来车辆统计中的城市接口
#### 请求地址
```
/pneumonia/carCities
```
#### 权限标识
- `pneumonia:carCities`
#### 请求方式
- `GET`

#### 请求参数  
- `无`


#### 响应参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | object | 返回数据 | 
&emsp;provinceName|string |省名|
&emsp;cityNames|list|市名|

#### 响应示例
```
{
	"code": "00000000",
	"message": "处理成功",
	"success": true,
	"data": [{
		"provinceName": "河北",
		"cityNames": [
			"⽯家庄",
			"唐⼭",
			"秦皇岛",
			"邯郸",
			"邢台",
			"保定",
			"张家⼝",
			"承德",
			"沧州",
			"廊坊",
			"沧州",
			"衡⽔"
		]
	}]
}

```

### 外来车辆
#### 请求地址
```
/pneumonia/carFromForeign
```
#### 权限标识
- `pneumonia:carFromForeign`
#### 请求方式
- `GET`

#### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :--- | :--- | :--- | :---
province|string |true| 省名 |来源接口: [外来车辆统计中的城市接口](#外来车辆统计中的城市接口)
city | string | false | 城市名称 | 来源接口: [外来车辆统计中的城市接口](#外来车辆统计中的城市接口)
dateType | string | true | 日期类型 |   day month
begin | number |true | 开始时间 | 
end |number | true | 结束日期 | 

#### 响应参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | object | 返回数据 | 
whCount|number |武汉返乡人数|
hbCount|number|湖北返乡人数|
lineCharts| list|折线图
&emsp;date| string|日期
&emsp;currentYear|number| 今年人数
&emsp;lastYear| number|去年人数
&emsp;compareLastYear|number| 较去年同期
&emsp;compareLast|number| 较昨日丶上月环比

#### 响应示例
```
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "whCount": 664,
        "hbCount": 2390,
        "lineCharts": [
            {
                "date": "10-23",
                "currentYear": 971,
                "lastYear": 0,
                "compareLastYear": null,
                "compareLast": 383.0
            },
            {
                "date": "10-24",
                "currentYear": 249,
                "lastYear": 0,
                "compareLastYear": null,
                "compareLast": -74.0
            }
        ]
    }
}
```