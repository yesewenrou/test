# 接口管理服务
## 目录
- [查询接口管理列表](#查询接口管理列表)

## 查询接口管理列表
### 请求地址
```
/api-manage/list
```

### 权限标识
- `无`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
page | number | true | 当前页 | 
size | number | true | 每页条数 | 
dataSource | string | false | 数据来源 | 

### 请求示例
/api-manage/list?page=1&size=5&dataSource=test11

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
&emsp;&emsp;id | string | 主键ID | 
&emsp;&emsp;interfaceName | string | 接口名称 | 
&emsp;&emsp;interfaceDesc | string | 接口描述 | 
&emsp;&emsp;dataSource | string | 数据来源 | 
&emsp;&emsp;interfaceAddress | string | 接口地址 | 

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
                "interfaceName": "test",
                "interfaceDesc": "test1",
                "dataSource": "test11",
                "interfaceAddress": "test111"
            },
            {
                "id": 3,
                "interfaceName": "test2",
                "interfaceDesc": "test1",
                "dataSource": "test11",
                "interfaceAddress": "test111"
            }
        ],
        "total": 6,
        "size": 5,
        "current": 1,
        "orders": [],
        "searchCount": true,
        "pages": 2
    }
}
```