# 应急演练管理
## 目录
- [新增应急演练](#新增应急演练)
- [删除应急演练](#删除应急演练)
- [更新应急演练](#更新应急演练)
- [查询单个应急演练](#查询单个应急演练)
- [查询应急演练列表](#查询应急演练列表)

## 新增应急演练
### 请求地址
```
/emergency-drill/save
```

### 权限标识
- `emergency-drill:save`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
drillTitle | string | true | 演练标题 | 
drillDate | number | true | 演练日期 | 时间戳
department | string | true | 参与部门 | 
eventType | string | true | 事件类型 | 数据字典EMERGENCY_EVENT_TYPE
eventLevel | string | true | 事件等级 | 数据字典EMERGENCY_EVENT_LEVEL
attachments | array | true | 演练附件 | 
&emsp;fileName | string | true | 文件名 | 
&emsp;fileUrl | string | true | 文件地址 | 

### 请求示例
```json
{
    "drillTitle": "测试演练",
    "drillDate": 1574697600000,
    "department": "部门A, 部门B",
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
data | number | 返回数据 | 演练ID

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": 1
}
```

## 删除应急演练
### 请求地址
```
/emergency-drill/delete
```

### 权限标识
- `emergency-drill:delete`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | number | true | 演练ID | 

### 请求示例
/emergency-drill/delete?id=1

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

## 更新应急演练
### 请求地址
```
/emergency-drill/update
```

### 权限标识
- `emergency-drill:update`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | number | true | 演练ID | 
drillTitle | string | true | 演练标题 | 
drillDate | number | true | 演练日期 | 时间戳
department | string | true | 参与部门 | 
eventType | string | true | 事件类型 | 数据字典EMERGENCY_EVENT_TYPE
eventLevel | string | true | 事件等级 | 数据字典EMERGENCY_EVENT_LEVEL
attachments | array | true | 演练附件 | 
&emsp;fileName | string | true | 文件名 | 
&emsp;fileUrl | string | true | 文件地址 | 

### 请求示例
```json
{
    "id": 1,
    "drillTitle": "测试演练1",
    "drillDate": 1574697600000,
    "department": "部门A",
    "eventType": "014001",
    "eventLevel": "015001",
    "attachments": [
        {
            "fileName": "测试文件1",
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

## 查询单个应急演练
### 请求地址
```
/emergency-drill/get
```

### 权限标识
- `emergency-drill:get`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | number | true | 演练ID | 

### 请求示例
/emergency-drill/get?id=2

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | string | 返回数据 | 
&emsp;id | string | 演练ID | 
&emsp;drillTitle | string | 演练标题 | 
&emsp;drillDate | number | 演练日期 | 时间戳
&emsp;department | string | 参与部门 | 
&emsp;eventType | string | 事件类型 | 
&emsp;eventTypeDesc | string | 事件类型描述 | 
&emsp;eventLevel | string | 事件等级 | 
&emsp;eventLevelDesc | string | 事件等级描述 | 
&emsp;gmtCreate | number | 创建时间、上报时间 | 时间戳
&emsp;attachments | array | 演练附件 | 
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
        "id": 1,
        "drillTitle": "测试演练1",
        "drillDate": 1574697600000,
        "department": "部门A",
        "eventType": "014001",
        "eventTypeDesc": "自然灾害",
        "eventLevel": "015001",
        "eventLevelDesc": "一般",
        "gmtCreate": 1574760114000,
        "attachments": [
            {
                "id": 5,
                "fileName": "测试文件1",
                "fileUrl": "http://xxx1"
            }
        ]
    }
}
```

## 查询应急演练列表
### 请求地址
```
/emergency-drill/list
```

### 权限标识
- `emergency-drill:list`

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
&emsp;drillTitle | string | false | 演练标题 | 模糊匹配
&emsp;startDrillDate | number | false | 演练时间范围开始 | 大于等于
&emsp;endDrillDate | number | false | 演练时间范围结束 | 小于等于
&emsp;eventType | string | false | 事件类型 | 数据字典，精确匹配
&emsp;eventLevel | string | false | 事件等级 | 数据字典，精确匹配

#### 可排序字段
- drill_date 演练日期
- event_type 事件类型编码
- event_level 事件等级编码
- gmt_create 创建时间、上报时间

### 请求示例
```json
{
    "current": 1,
    "size": 10,
    "orderItemList": [
        {
            "column": "gmt_create",
            "asc": false
        }
    ],
    "condition": {
        "drillTitle": "演练",
        "startDrillDate": 1574697600000,
        "endDrillDate": 1574697600000,
        "eventType": "014001",
        "eventLevel": "015001"
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
&emsp;&emsp;id | string | 演练ID | 
&emsp;&emsp;drillTitle | string | 演练标题 | 
&emsp;&emsp;drillDate | number | 演练日期 | 时间戳
&emsp;&emsp;department | string | 参与部门 | 
&emsp;&emsp;eventType | string | 事件类型 | 
&emsp;&emsp;eventTypeDesc | string | 事件类型描述 | 
&emsp;&emsp;eventLevel | string | 事件等级 | 
&emsp;&emsp;eventLevelDesc | string | 事件等级描述 | 
&emsp;&emsp;gmtCreate | number | 创建时间、上报时间 | 时间戳
&emsp;&emsp;attachments | array | 演练附件 | 
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
                "id": 1,
                "drillTitle": "测试演练1",
                "drillDate": 1574697600000,
                "department": "部门A",
                "eventType": "014001",
                "eventTypeDesc": "自然灾害",
                "eventLevel": "015001",
                "eventLevelDesc": "一般",
                "gmtCreate": 1574760114000,
                "attachments": [
                    {
                        "id": 5,
                        "fileName": "测试文件1",
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