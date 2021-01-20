# 应急预案管理
## 目录
- [新增应急预案](#新增应急预案)
- [删除应急预案](#删除应急预案)
- [更新应急预案](#更新应急预案)
- [查询单个应急预案](#查询单个应急预案)
- [查询应急预案列表](#查询应急预案列表)

## 新增应急预案
### 请求地址
```
/emergency-plan/save
```

### 权限标识
- `emergency-plan:save`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
planName | string | true | 预案名称 | 
eventType | string | true | 事件类型 | 数据字典EMERGENCY_EVENT_TYPE
eventLevel | string | true | 事件等级 | 数据字典EMERGENCY_EVENT_LEVEL
attachments | array | true | 预案附件 | 
&emsp;fileName | string | true | 文件名 | 
&emsp;fileUrl | string | true | 文件地址 | 

### 请求示例
```json
{
    "planName": "测试预案2",
    "eventType": "014001",
    "eventLevel": "015001",
    "attachments": [
        {
            "fileName": "测试文件",
            "fileUrl": "http://xxx"
        }
    ]
}
```

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | number | 返回数据 | 预案ID

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": 3
}
```

## 删除应急预案
### 请求地址
```
/emergency-plan/delete
```

### 权限标识
- `emergency-plan:delete`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | number | true | 预案ID | 

### 请求示例
/emergency-plan/delete?id=6

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | string | 返回数据 | 

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "删除成功",
    "success": true,
    "data": null
}
```

## 更新应急预案
### 请求地址
```
/emergency-plan/update
```

### 权限标识
- `emergency-plan:update`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
planName | string | true | 预案名称 | 
eventType | string | true | 事件类型 | 数据字典EMERGENCY_EVENT_TYPE
eventLevel | string | true | 事件等级 | 数据字典EMERGENCY_EVENT_LEVEL
attachments | array | true | 预案附件 | 
&emsp;fileName | string | true | 文件名 | 
&emsp;fileUrl | string | true | 文件地址 | 

### 请求示例
```json
{
    "id": 3,
    "planName": "测试预案",
    "eventType": "014002",
    "eventLevel": "015002",
    "attachments": [
        {
            "fileName": "测试文1件",
            "fileUrl": "http://xxx1"
        }
    ]
}
```

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | string | 返回数据 | 

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "更新成功",
    "success": true,
    "data": null
}
```

## 查询单个应急预案
### 请求地址
```
/emergency-plan/get
```

### 权限标识
- `emergency-plan:get`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | number | true | 预案ID | 

### 请求示例
/emergency-plan/get?id=2

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | string | 返回数据 | 
&emsp;id | string | 预案ID | 
&emsp;planName | string | 预案名称 | 
&emsp;eventType | string | 事件类型 | 
&emsp;eventTypeDesc | string | 事件类型描述 | 
&emsp;eventLevel | string | 事件等级 | 
&emsp;eventLevelDesc | string | 事件等级描述 | 
&emsp;modifierId | number | 修改人ID | 
&emsp;modifierName | string | 修改人名称 | 
&emsp;gmtModified | number | 更新时间 | 时间戳
&emsp;attachments | array | 预案附件 | 
&emsp;&emsp;id | number | 主键ID | 
&emsp;&emsp;fileName | string | 文件名称 | 
&emsp;&emsp;fileUrl | string | 文件地址 | 

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "id": 2,
        "planName": "测试预案",
        "eventType": "014002",
        "eventTypeDesc": "事故灾难",
        "eventLevel": "015002",
        "eventLevelDesc": "较大",
        "modifierId": 3,
        "modifierName": "admin",
        "gmtModified": 1574739477000,
        "attachments": [
            {
                "id": 2,
                "fileName": "测试文1件",
                "fileUrl": "http://xxx1"
            }
        ]
    }
}
```

## 查询应急预案列表
### 请求地址
```
/emergency-plan/list
```

### 权限标识
- `emergency-plan:list`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
pageNum | number | true | 当前页 | 
pageSize | number | true | 每页条数 | 
orderItemList | array | false | 排序字段列表 | 
&emsp;column | string | true | 排序字段 | 
&emsp;asc | boolean | true | 是否正序 | true：正序，false：倒序
condition | object | false | 筛选条件 | 
&emsp;eventType | string | false | 事件类型 | 数据字典，精确匹配
&emsp;eventLevel | string | false | 事件等级 | 数据字典，精确匹配

#### 可排序字段
- event_type 事件类型编码
- event_level 事件等级编码
- gmt_modified 更新时间

### 请求示例
```json
{
    "current": 1,
    "size": 10,
    "orderItemList": [
        {
            "column": "gmt_modified",
            "asc": false
        }
    ],
    "condition": {
        "eventType": "014002",
        "eventLevel": "015002"
    }
}
```

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | object | 返回数据 | 
&emsp;total | number | 总条数 | 
&emsp;pages | number | 总页数 | 
&emsp;records | array | 查询结果 | 
&emsp;&emsp;id | string | 预案ID | 
&emsp;&emsp;planName | string | 预案名称 | 
&emsp;&emsp;eventType | string | 事件类型 | 
&emsp;&emsp;eventTypeDesc | string | 事件类型描述 | 
&emsp;&emsp;eventLevel | string | 事件等级 | 
&emsp;&emsp;eventLevelDesc | string | 事件等级描述 | 
&emsp;&emsp;modifierId | number | 修改人ID | 
&emsp;&emsp;modifierName | string | 修改人名称 | 
&emsp;&emsp;gmtModified | number | 更新时间 | 时间戳
&emsp;&emsp;attachments | array | 预案附件 | 
&emsp;&emsp;&emsp;id | number | 主键ID | 
&emsp;&emsp;&emsp;fileName | string | 文件名称 | 
&emsp;&emsp;&emsp;fileUrl | string | 文件地址 | 


### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "records": [
            {
                "id": 2,
                "planName": "测试预案",
                "eventType": "014002",
                "eventTypeDesc": "事故灾难",
                "eventLevel": "015002",
                "eventLevelDesc": "较大",
                "modifierId": 3,
                "modifierName": "admin",
                "gmtModified": 1574739477000,
                "attachments": [
                    {
                        "id": 2,
                        "fileName": "测试文1件",
                        "fileUrl": "http://xxx1"
                    }
                ]
            }
        ],
        "total": 1,
        "size": 10,
        "current": 1,
        "orders": [],
        "searchCount": true,
        "pages": 1
    }
}
```