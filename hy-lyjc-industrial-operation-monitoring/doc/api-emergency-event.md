# 应急事件管理
## 目录
- [新增应急事件](#新增应急事件)
- [编辑应急事件](#编辑应急事件)
- [审核应急事件](#审核应急事件)
- [查询所有可以指派的用户](#查询所有可以指派的用户)
- [指派应急事件](#指派应急事件)
- [反馈应急事件](#反馈应急事件)
- [结案应急事件](#结案应急事件)
- [查询单个应急事件](#查询单个应急事件)
- [查询应急事件列表](#查询应急事件列表)
- [删除应急事件](#删除应急事件)

## 新增应急事件
### 请求地址
```
/emergency-event/save
```

### 权限标识
- `emergency-event:save`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
eventName | string | true | 事件名称 | 
eventType | string | true | 事件类型 | 数据字典EMERGENCY_EVENT_TYPE
eventLevel | string | true | 事件等级 | 数据字典EMERGENCY_EVENT_LEVEL
eventAddress | string | true | 事件地址 | 
eventContent | string | true | 事件内容 | 
eventTime | number | true | 事发时间 | 时间戳
contact | string | true | 联系方式 | 
scenePhotos | array | false | 现场照片 | 
&emsp;fileName | string | true | 文件名 | 
&emsp;fileUrl | string | true | 文件地址 | 
attachments | array | false | 资料文件 | 
&emsp;fileName | string | true | 文件名 | 
&emsp;fileUrl | string | true | 文件地址 | 

### 请求示例
```json
{
    "eventName": "测试事件",
    "eventType": "014001",
    "eventLevel": "015001",
    "eventAddress": "四川省洪雅县瓦屋山",
    "eventContent": "突发事件",
    "eventTime": 1574697600000,
    "contact": "136580000",
    "scenePhotos": [
        {
            "fileName": "scenePhotos测试文件",
            "fileUrl": "http://xxx"
        }
    ],
    "attachments": [
        {
            "fileName": "attachments测试文件",
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
data | number | 返回数据 | 事件ID

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": 1
}
```

## 编辑应急事件
### 请求地址
```
/emergency-event/edit
```

### 权限标识
- `emergency-event:edit`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | number | true | 事件名ID | 
eventName | string | true | 事件名称 | 
eventType | string | true | 事件类型 | 数据字典EMERGENCY_EVENT_TYPE
eventLevel | string | true | 事件等级 | 数据字典EMERGENCY_EVENT_LEVEL
eventAddress | string | true | 事件地址 | 
eventContent | string | true | 事件内容 | 
eventTime | number | true | 事发时间 | 时间戳
contact | string | true | 联系方式 | 
scenePhotos | array | false | 现场照片 | 
&emsp;fileName | string | true | 文件名 | 
&emsp;fileUrl | string | true | 文件地址 | 
attachments | array | false | 资料文件 | 
&emsp;fileName | string | true | 文件名 | 
&emsp;fileUrl | string | true | 文件地址 | 

### 请求示例
```json
{
    "id": 1,
    "eventName": "测试事件",
    "eventType": "014001",
    "eventLevel": "015001",
    "eventAddress": "四川省洪雅县瓦屋山",
    "eventContent": "突发事件",
    "eventTime": 1574697600000,
    "contact": "136580000",
    "scenePhotos": [
        {
            "fileName": "scenePhotos测试文件",
            "fileUrl": "http://xxx"
        }
    ],
    "attachments": [
        {
            "fileName": "attachments测试文件",
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
data | number | 返回数据 | 事件ID

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": null
}
```

## 审核应急事件
### 请求地址
```
/emergency-event/check
```

### 权限标识
- `emergency-event:check`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | number | true | 事件ID | 
checkFlag | boolean | true | 审核结果 | true-通过，false-未通过
checkContent | string | true | 原因描述 | 

### 请求示例
```json
{
    "id": 1,
    "checkFlag": true,
    "checkContent": "审核通过"
}
```

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | object | 返回数据 | 

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "审核成功",
    "success": true,
    "data": null
}
```

## 查询所有可以指派的用户
### 请求地址
```
/emergency-event/assign-user/listAll
```

### 权限标识
- `emergency-event:assign-user:listAll`

### 请求方式
- `GET`

### 请求参数说明
无

### 请求示例
/emergency-event/assign-user/listAll

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | array | 返回数据 | 
&emsp;id | number | 用户ID | 
&emsp;userName | string | 用户名称 | 

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "id": 78,
            "userName": "wuqian"
        }
    ]
}
```

## 指派应急事件
### 请求地址
```
/emergency-event/assign
```

### 权限标识
- `emergency-event:assign`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | number | true | 事件ID | 
assignedUserId | number | true | 被指派人员ID | 

### 请求示例
```json
{
    "id": 1,
    "assignedUserId": 78
}
```

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | object | 返回数据 | 

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "指派成功",
    "success": true,
    "data": null
}
```

## 反馈应急事件
### 请求地址
```
/emergency-event/feedback
```

### 权限标识
- `emergency-event:feedback`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | number | true | 事件ID | 
feedbackContent | string | true | 反馈内容 | 
feedbackPhotos | array | false | 反馈现场照片 | 
&emsp;fileName | string | true | 文件名 | 
&emsp;fileUrl | string | true | 文件地址 | 

### 请求示例
```json
{
    "id": 1,
    "feedbackContent": "解决啦",
    "feedbackPhotos": [
        {
            "fileName": "feedbackPhotos测试文件",
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
data | object | 返回数据 | 

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "反馈成功",
    "success": true,
    "data": null
}
```

## 结案应急事件
### 请求地址
```
/emergency-event/close
```

### 权限标识
- `emergency-event:close`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | number | true | 事件ID | 
closeContent | string | true | 结案原因 | 
closeAttachments | array | false | 结案附件 | 
&emsp;fileName | string | true | 文件名 | 
&emsp;fileUrl | string | true | 文件地址 | 
closeImages | array | false | 结案图片 | 
&emsp;fileName | string | true | 文件名 | 
&emsp;fileUrl | string | true | 文件地址 | 

### 请求示例
```json
{
    "id": 1,
    "closeContent": "结案啦",
    "closeAttachments": [
        {
            "fileName": "closeAttachments测试文件",
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
data | object | 返回数据 | 

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "结案成功",
    "success": true,
    "data": null
}
```

## 查询单个应急事件
### 请求地址
```
/emergency-event/get
```

### 权限标识
- `emergency-event:get`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | number | true | 事件ID | 

### 请求示例
/emergency-event/get?id=1

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | string | 返回数据 | 
&emsp;id | string | 事件ID | 
&emsp;eventName | string | 事件名称 | 
&emsp;eventType | string | 事件类型 | 数据字典EMERGENCY_EVENT_TYPE
&emsp;eventTypeDesc | string | 事件类型描述 | 
&emsp;eventLevel | string | 事件等级 | 数据字典EMERGENCY_EVENT_LEVEL
&emsp;eventLevelDesc | string | 事件等级描述 | 
&emsp;eventAddress | string | 事件地址 | 
&emsp;eventContent | string | 事件内容 | 
&emsp;eventTime | number | 事件时间 | 时间戳
&emsp;contact | string | 事件联系人 | 
&emsp;scenePhotos | array | 现场照片 | 
&emsp;&emsp;id | number | 主键ID | 
&emsp;&emsp;fileName | string | 文件名称 | 
&emsp;&emsp;fileUrl | string | 文件地址 | 
&emsp;attachments | array | 资料文件 | 
&emsp;&emsp;id | number | 主键ID | 
&emsp;&emsp;fileName | string | 文件名称 | 
&emsp;&emsp;fileUrl | string | 文件地址 | 
&emsp;eventStatus | string | 事件状态 | WAIT_CHECK-待审核、WAIT_DEAL-待处理、WAIT_CLOSE-待结案、CLOSED-已结案、NO_PASS-未通过
&emsp;eventStatusDesc | string | 事件状态描述 | WAIT_CHECK-待审核、WAIT_DEAL-待处理、WAIT_CLOSE-待结案、CLOSED-已结案、NO_PASS-未通过
&emsp;checkStatus | string | 审核状态 | PASS-通过、NO_PASS-未通过
&emsp;checkStatusDesc | string | 审核状态描述 | PASS-通过、NO_PASS-未通过
&emsp;checkContent | string | 审核原因描述 | 
&emsp;checkUserId | number | 审核人ID | 
&emsp;checkUserName | string | 审核人名称 | 
&emsp;checkTime | number | 审核人时间 | 时间戳
&emsp;assignedUserId | number | 被指派人ID | 
&emsp;assignedUserName | string | 被指派人名称 | 
&emsp;assignTime | number | 指派时间 | 时间戳
&emsp;feedbackContent | string | 反馈内容 | 
&emsp;feedbackTime | number | 反馈时间 | 时间戳
&emsp;feedbackPhotos | array | 反馈现场照片 | 
&emsp;&emsp;id | number | 主键ID | 
&emsp;&emsp;fileName | string | 文件名称 | 
&emsp;&emsp;fileUrl | string | 文件地址 | 
&emsp;closeContent | string | 结案原因 | 
&emsp;closeTime | number | 结案时间 | 时间戳
&emsp;closeAttachments | array | 结案附件 | 
&emsp;&emsp;id | number | 主键ID | 
&emsp;&emsp;fileName | string | 文件名称 | 
&emsp;&emsp;fileUrl | string | 文件地址 | 
&emsp;gmtCreate | number | 创建时间、上报时间 | 时间戳
&emsp;closeImages | array | 结案图片 | （内部属性同结案附件）
&emsp;autoCreated | boolean | 是否是自动创建 | true:自动创建, false:手动创建
&emsp;assignerUserId | number | 指派人id | 
&emsp;assignerUserName | boolean | 指派人姓名 | 
&emsp;closeUserId | number | 结案人id | 
&emsp;closeUserName | boolean | 结案人用户名 |
&emsp;autoCreated | boolean | 是否是自动创建 | 

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "id": 1,
        "eventName": "测试事件",
        "eventType": "014001",
        "eventTypeDesc": "自然灾害",
        "eventLevel": "015001",
        "eventLevelDesc": "一般",
        "eventAddress": "四川省洪雅县瓦屋山",
        "eventContent": "突发事件",
        "eventTime": 1574697600000,
        "contact": "136580000",
        "scenePhotos": [
            {
                "id": 1,
                "fileName": "scenePhotos测试文件",
                "fileUrl": "http://xxx"
            }
        ],
        "attachments": [
            {
                "id": 2,
                "fileName": "attachments测试文件",
                "fileUrl": "http://xxx"
            }
        ],
        "eventStatus": "CLOSED",
        "eventStatusDesc": "已结案",
        "checkStatus": "PASS",
        "checkStatusDesc": "通过",
        "checkContent": "审核通过",
        "checkUserId": 3,
        "checkUserName": "admin",
        "checkTime": 1574921724000,
        "assignedUserId": 78,
        "assignedUserName": "wuqian",
        "assignTime": 1574922627000,
        "feedbackContent": "解决啦",
        "feedbackTime": 1574922958000,
        "feedbackPhotos": [
            {
                "id": 4,
                "fileName": "feedbackPhotos测试文件1",
                "fileUrl": "http://xxx"
            }
        ],
        "closeContent": "结案啦",
        "closeTime": 1574923066000,
        "closeAttachments": [
            {
                "id": 5,
                "fileName": "closeAttachments测试文件",
                "fileUrl": "http://xxx"
            }
        ],
        "gmtCreate": 1574921082000
    }
}
```

## 查询应急事件列表
### 请求地址
```
/emergency-event/list
```

### 权限标识
- `emergency-event:list`

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
&emsp;eventName | string | false | 事件名称 | 模糊匹配
&emsp;eventType | string | false | 事件类型 | 数据字典，精确匹配
&emsp;eventLevel | string | false | 事件等级 | 数据字典，精确匹配
&emsp;eventStatus | string | false | 事件状态 | 精确匹配
&emsp;assignedUserId | number | false | 被指派的人员 | 精确匹配

#### 可排序字段
- event_time 事件日期
- event_status 事件状态
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
            "column": "event_status",
            "asc": true
        }
    ],
    "condition": {
        "eventName": "事件",
        "eventType": "014001",
        "eventLevel": "015001",
        "eventStatus": "CLOSED",
        "assignedUserId": 78
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
&emsp;&emsp;id | string | 事件ID | 
&emsp;&emsp;eventName | string | 事件名称 | 
&emsp;&emsp;eventType | string | 事件类型 | 数据字典EMERGENCY_EVENT_TYPE
&emsp;&emsp;eventTypeDesc | string | 事件类型描述 | 
&emsp;&emsp;eventLevel | string | 事件等级 | 数据字典EMERGENCY_EVENT_LEVEL
&emsp;&emsp;eventLevelDesc | string | 事件等级描述 | 
&emsp;&emsp;eventAddress | string | 事件地址 | 
&emsp;&emsp;eventContent | string | 事件内容 | 
&emsp;&emsp;eventTime | number | 事件时间 | 时间戳
&emsp;&emsp;contact | string | 事件联系人 | 
&emsp;&emsp;scenePhotos | array | 现场照片 | 
&emsp;&emsp;&emsp;id | number | 主键ID | 
&emsp;&emsp;&emsp;fileName | string | 文件名称 | 
&emsp;&emsp;&emsp;fileUrl | string | 文件地址 | 
&emsp;&emsp;attachments | array | 资料文件 | 
&emsp;&emsp;&emsp;id | number | 主键ID | 
&emsp;&emsp;&emsp;fileName | string | 文件名称 | 
&emsp;&emsp;&emsp;fileUrl | string | 文件地址 | 
&emsp;&emsp;eventStatus | string | 事件状态 | WAIT_CHECK-待审核、WAIT_DEAL-待处理、WAIT_CLOSE-待结案、CLOSED-已结案、NO_PASS-未通过
&emsp;&emsp;eventStatusDesc | string | 事件状态描述 | WAIT_CHECK-待审核、WAIT_DEAL-待处理、WAIT_CLOSE-待结案、CLOSED-已结案、NO_PASS-未通过
&emsp;&emsp;checkStatus | string | 审核状态 | PASS-通过、NO_PASS-未通过
&emsp;&emsp;checkStatusDesc | string | 审核状态描述 | PASS-通过、NO_PASS-未通过
&emsp;&emsp;checkContent | string | 审核原因描述 | 
&emsp;&emsp;checkUserId | number | 审核人ID | 
&emsp;&emsp;checkUserName | string | 审核人名称 | 
&emsp;&emsp;checkTime | number | 审核人时间 | 时间戳
&emsp;&emsp;assignedUserId | number | 被指派人ID | 
&emsp;&emsp;assignedUserName | string | 被指派人名称 | 
&emsp;&emsp;assignTime | number | 指派时间 | 时间戳
&emsp;&emsp;feedbackContent | string | 反馈内容 | 
&emsp;&emsp;feedbackTime | number | 反馈时间 | 时间戳
&emsp;&emsp;feedbackPhotos | array | 反馈现场照片 | 
&emsp;&emsp;&emsp;id | number | 主键ID | 
&emsp;&emsp;&emsp;fileName | string | 文件名称 | 
&emsp;&emsp;&emsp;fileUrl | string | 文件地址 | 
&emsp;&emsp;closeContent | string | 结案原因 | 
&emsp;&emsp;closeTime | number | 结案时间 | 时间戳
&emsp;&emsp;closeAttachments | array | 结案附件 | 
&emsp;&emsp;&emsp;id | number | 主键ID | 
&emsp;&emsp;&emsp;fileName | string | 文件名称 | 
&emsp;&emsp;&emsp;fileUrl | string | 文件地址 | 
&emsp;&emsp;gmtCreate | number | 创建时间、上报时间 | 时间戳
&emsp;&emsp;autoCreated | Boolean | 是否是自动创建的 | true:自动创建, false:手动创建

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
                "eventName": "测试事件",
                "eventType": "014001",
                "eventTypeDesc": "自然灾害",
                "eventLevel": "015001",
                "eventLevelDesc": "一般",
                "eventAddress": "四川省洪雅县瓦屋山",
                "eventContent": "突发事件",
                "eventTime": 1574697600000,
                "contact": "136580000",
                "scenePhotos": [
                    {
                        "id": 1,
                        "fileName": "scenePhotos测试文件",
                        "fileUrl": "http://xxx"
                    }
                ],
                "attachments": [
                    {
                        "id": 2,
                        "fileName": "attachments测试文件",
                        "fileUrl": "http://xxx"
                    }
                ],
                "eventStatus": "CLOSED",
                "eventStatusDesc": "已结案",
                "checkStatus": "PASS",
                "checkStatusDesc": "通过",
                "checkContent": "审核通过",
                "checkUserId": 3,
                "checkUserName": "admin",
                "checkTime": 1574921724000,
                "assignedUserId": 78,
                "assignedUserName": "wuqian",
                "assignTime": 1574922627000,
                "feedbackContent": "解决啦",
                "feedbackTime": 1574922958000,
                "feedbackPhotos": [
                    {
                        "id": 4,
                        "fileName": "feedbackPhotos测试文件1",
                        "fileUrl": "http://xxx"
                    }
                ],
                "closeContent": "结案啦",
                "closeTime": 1574923066000,
                "closeAttachments": [
                    {
                        "id": 5,
                        "fileName": "closeAttachments测试文件",
                        "fileUrl": "http://xxx"
                    }
                ],
                "gmtCreate": 1574921082000
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

## 删除应急事件
### 请求地址
```
/emergency-event/delete
```

### 权限标识
- `emergency-event:delete`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | number | true | 事件ID | 

### 请求示例
/emergency-event/delete?id=1

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
    "message": "操作成功",
    "success": true,
    "data": null
}
```