# 景区游客承载量配置
## 目录
- [查询所有景区承载量配置](#查询所有景区承载量配置)
- [更新单个景区承载量配置](#更新单个景区承载量配置)
- [联系人编辑](#预警联系人编辑)
- [联系人删除](#预警联系人删除)
- [联系人列表](#预警联系人列表)

## 查询所有景区承载量配置
### 请求地址
```
/scenic-tourist-capacity-config/listAll
```

### 权限标识
- `scenic-tourist-capacity-config:listAll`

### 请求方式
- `GET`

### 请求参数说明
无

### 请求示例
/scenic-tourist-capacity-config/listAll

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | array | 返回数据 | 
&emsp;scenicCode | string | 景区编码 | 
&emsp;scenicName | string | 景区名称 | 
&emsp;comfortableCapacity|number |舒适|
&emsp;warningCapacity | number | 预警人数 | 
&emsp;lessComfortableCapacity | number | 较舒适 | 
&emsp;ordinaryCapacity | number | 一般 | 
&emsp;saturationCapacity | number | 饱和承载量丶较拥挤容量 | 
&emsp;overloadCapacity | number | 超负荷承载量丶拥挤容量 | 

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
                    "scenicCode": "002006",
                    "scenicName": "主城区",
                    "warningCapacity": null,
                    "comfortableCapacity": null,
                    "lessComfortableCapacity": null,
                    "ordinaryCapacity": null,
                    "saturationCapacity": 500,
                    "overloadCapacity": 1000
        }
    ]
}
```

## 更新单个景区承载量配置
### 请求地址
```
/scenic-tourist-capacity-config/update
```

### 权限标识
- `scenic-tourist-capacity-config:update`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
scenicCode | string | true | 景区编码 | 
warningCapacity|number | true | 预警人数 | 大于0 
comfortableCapacity|number | true | 舒适 | 大于0
lessComfortableCapacity|number | true | 较舒适 | 大于0 且大于 comfortableCapacity
ordinaryCapacity|number | true | 一般 | 大于0, 且大于 lessComfortableCapacity
saturationCapacity | number | true | 饱和承载量丶较拥挤容量 |  大于0, 且大于 ordinaryCapacity
overloadCapacity | number | true | 超负荷承载量丶拥挤容量 | 大于0且大于saturationCapacity

### 请求示例
```json
{
	"scenicCode":"002004",
	"warningCapacity":200,
	"comfortableCapacity":100,
	"lessComfortableCapacity":200,
	"ordinaryCapacity":500,
	"saturationCapacity":800,
	"overloadCapacity": 1000
}
```

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | array | 返回数据 | 无

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "更新配置成功",
    "success": true,
    "data": null
}
```

## 预警联系人编辑
- 包含新增和修改

### 请求地址
```text
/smsContact/edit
```
### 权限标识
- `smsContact:edit`

### 请求方式
- `POST`

### 请求参数说明 
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
name|string|Y|姓名|
phone|String|Y|手机号
scenicAuto|boolean|Y|景区是否自动发送. <br>true 自动, false 非自动
trafficAuto|boolean |Y|交通是否自动发送.<br>true 自动, false 非自动
carAuto|boolean|Y|停车是否自动发送.<br>true 自动, false 非自动


###  请求示例
```text
{
	"name":"李",
	"phone":"13658037118",
	"autoSend":true
}
```

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data |number | 返回数据 | id

### 返回示例
```text
{
    "code": "08000000",
    "message": "success",
    "success": true,
    "data": 3
}
```


## 预警联系人删除
### 请求地址
```text
/smsContact
```

### 权限标识
- `smsContact:delete`

### 请求方式
- `DELETE`

### 请求参数说明 
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | number | Y | id

###  请求示例
```text
/smsContact?id=2
```
### 返回示例
```text
{
    "code": "08000000",
    "message": "success",
    "success": true,
    "data": null
}
```

## 预警联系人列表
### 请求地址 
```text
/smsContact/list
```
### 权限标识
- `smsContact:list`
### 请求方式
- `GET`
### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
page | number | Y | 页数 |  页数必传
size | number | N | 记录数 | 负数 和 null 查询所有记录


### 响应说明
名称 | 类型 | 描述 | 备注
:--- | :--- | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data |object | 返回数据 | 
&emsp;total |number| 总记录数
&emsp;records| list |记录
&emsp;&emsp;id | number|id
&emsp;&emsp;name | String|名称
&emsp;&emsp;phone |string| 手机号
&emsp;&emsp;autoSend | boolean | 是否自动发送| true 自动发送 , false 非自动发送
&emsp;&emsp;createTime | timestamp | 创建时间
&emsp;&emsp;updateTime | timestamp |修改时间

### 请求示例
```text
/smsContact/list?page=1&size=
```

### 响应示例
```text
{
    "code": "08000000",
    "message": "success",
    "success": true,
    "data": {
        "records": [
            {
                "id": 1,
                "name": "方云龙",
                "phone": "13658031111",
                "autoSend": false,
                "createTime": 1583572162000,
                "updateTime": 1583572162000
            }
        ],
        "total": 0,
        "size": -1,
        "current": 1,
        "orders": [],
        "searchCount": true,
        "pages": 0
    }
}
```


