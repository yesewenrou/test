# 游客实时数据
## 目录
- [查询游客实时数据列表](#查询游客实时数据列表)

## 查询游客实时数据列表
### 请求地址
```
/tourist/newest/list
```

### 权限标识
无

### 请求方式
- `GET`

### 请求参数说明
无

### 请求示例
/tourist/newest/list

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | array | 返回数据 | 
&emsp;id | number | ID | 
&emsp;scenicId | string | 景区ID | 
&emsp;scenicName | string | 景区名称 | 
&emsp;peopleNum | number | 游客数量 | 

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "id": 1,
            "scenicId": "511423",
            "scenicName": "洪雅县",
            "peopleNum": 29268
        },
        {
            "id": 2,
            "scenicId": "2830027",
            "scenicName": "玉屏山",
            "peopleNum": 760
        }
    ]
}
```