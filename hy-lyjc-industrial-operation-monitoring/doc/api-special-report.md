# 专题报告
## 目录
- [添加专题报告](#添加专题报告)
- [删除专题报告](#删除专题报告)
- [查询专题报告列表](#查询专题报告列表)

## 添加专题报告
### 请求地址
```
/special-report
```

### 权限标识
- `special-report:add`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
name | string | true | 报告名称 | 
type | string | true | 报告类型 | 数据字典 SPECIAL_REPORT_TYPE
attachmentName | true | 附件名称 | 
attachmentUrl | true | 附件地址 | 

### 请求示例
```json
{
    "name": "测试报告1000",
    "type": "031002",
    "attachmentName": "测试报告031002",
    "attachmentUrl": "http://xxx"
}
```

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | number | 报告ID | 

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": 35
}
```

## 删除专题报告
### 请求地址
```
/special-report
```

### 权限标识
- `special-report:delete`

### 请求方式
- `DELETE`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | number | true | 报告ID | 

### 请求示例
```
/special-report?id=35
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
    "message": "操作成功",
    "success": true,
    "data": null
}
```

## 查询专题报告列表
### 请求地址
```
/special-report/list
```

### 权限标识
- `special-report:list`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
page | number | false | 当前页 | 默认1
size | number | false | 每页条数 | 默认10
name | string | false | 资源名称 | 模糊匹配
type | string | false | 资源类型编码 | 数据字典 SPECIAL_REPORT_TYPE
startTime | number | false | 开始时间 | 时间戳
endTime | number | false | 结束时间 | 时间戳

### 请求示例
```
/special-report/list?page=1&size=-1&startTime=1579248571000&endTime=1579248571000
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
&emsp;current | number | 当前页数 | 
&emsp;size | number | 每页条数 | 
&emsp;records | array | 查询结果 | 
&emsp;&emsp;id | number | ID | 
&emsp;&emsp;name | string | 名称 | 
&emsp;&emsp;type | string | 类型 | 
&emsp;&emsp;typeName | string | 类型名称 | 
&emsp;&emsp;attachmentName | string | 附件名称 | 
&emsp;&emsp;attachmentUrl | string | 附件地址 | 
&emsp;&emsp;reportTime | string | 生成日期 | 

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "records": [
            {
                "id": 4,
                "name": "测试报告4",
                "type": "031004",
                "typeName": "季报",
                "attachmentName": "xxx",
                "attachmentUrl": "http://xxx",
                "reportTime": 1579249016000
            }
        ],
        "total": 33,
        "size": 1,
        "current": 4,
        "orders": [],
        "searchCount": true,
        "pages": 33
    }
}
```