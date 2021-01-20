# 应急资源管理
## 目录
- [新增应急资源](#新增应急资源)
- [删除应急资源](#删除应急资源)
- [更新应急资源](#更新应急资源)
- [查询单个应急资源](#查询单个应急资源)
- [查询应急资源列表](#查询应急资源列表)

## 新增应急资源
### 请求地址
```
/emergency-resource/save
```

### 权限标识
- `emergency-resource:save`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
name | string | true | 资源名称 | 
type | string | true | 资源类型编码 | 数据字典EMERGENCY_RESOURCE_TYPE
inventory | number | true | 资源数量 | 支持小数
unit | string | true | 资源数量单位 | 

### 请求示例
```json
{
    "id": 1,
    "name": "D",
    "type": "013004",
    "inventory": "12",
    "unit": "袋"
}
```

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | number | 返回数据 | 资源ID

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": 6
}
```

## 删除应急资源
### 请求地址
```
/emergency-resource/delete
```

### 权限标识
- `emergency-resource:delete`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | number | true | 资源ID | 

### 请求示例
/emergency-resource/delete?id=6

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

## 更新应急资源
### 请求地址
```
/emergency-resource/update
```

### 权限标识
- `emergency-resource:update`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | string | true | 资源ID | 
name | string | true | 资源名称 | 
type | string | true | 资源类型编码 | 数据字典EMERGENCY_RESOURCE_TYPE
inventory | number | true | 资源数量 | 支持小数
unit | string | true | 资源数量单位 | 

### 请求示例
```json
{
    "id": 1,
    "name": "A",
    "type": "013001",
    "inventory": "12",
    "unit": "个"
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

## 查询单个应急资源
### 请求地址
```
/emergency-resource/get
```

### 权限标识
- `emergency-resource:get`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | number | true | 资源ID | 

### 请求示例
/emergency-resource/get?id=1

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | string | 返回数据 | 
&emsp;id | string | 资源ID | 
&emsp;name | string | 资源名称 | 
&emsp;type | string | 资源类型编码 | 
&emsp;typeName | string | 资源类型名称 | 
&emsp;inventory | number | 资源数量 | 
&emsp;unit | string | 资源数量单位 | 
&emsp;gmtModified | number | 更新时间 | 时间戳

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "id": 1,
        "name": "A",
        "type": "013001",
        "typeName": "防护用品",
        "inventory": 12.00,
        "unit": "个",
        "gmtModified": 1574407344000
    }
}
```

## 查询应急资源列表
### 请求地址
```
/emergency-resource/list
```

### 权限标识
- `emergency-resource:list`

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
&emsp;name | string | false | 资源名称 | 模糊匹配
&emsp;type | string | false | 资源类型编码 | 数据字典，精确匹配

#### 可排序字段
- name 资源名称
- type 资源类型编码
- inventory 资源数量
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
        "name": "A",
        "type": "013001"
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
&emsp;&emsp;id | string | 资源ID | 
&emsp;&emsp;name | string | 资源名称 | 
&emsp;&emsp;type | string | 资源类型编码 | 
&emsp;&emsp;typeName | string | 资源类型名称 | 
&emsp;&emsp;inventory | number | 资源数量 | 
&emsp;&emsp;unit | string | 资源数量单位 | 
&emsp;&emsp;gmtModified | number | 更新时间 | 时间戳


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
                "name": "A",
                "type": "013001",
                "typeName": "防护用品",
                "inventory": 12.00,
                "unit": "个",
                "gmtModified": 1574407344000
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