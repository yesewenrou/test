# 值班部门管理
## 目录
- [查询部门表](#查询值班表)
- [新增部门](#新增部门)
- [获取单个部门](获取单个部门)
- [更新部门](#更新部门)
- [删除部门](#删除部门)
- [获取单位](#获取单位)
- [根据单位获取部门](#根据单位获取部门)

## 查询值班部门表
### 请求地址
```
/duty-department/list
```

### 权限标识
- `duty-department:list`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
current | number | true | 当前页 |
size | number | true | 每页条数 |

### 请求示例
/duty-department/list

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | array | 返回数据 | 
&emsp;id | string | ID | 
&emsp;dutyInstitutions | string | 值班单位 | 
&emsp;departmentName | string | 值班部门 | 
&emsp;departmentLeader | string | 部门负责人 | 
&emsp;leaderContact|string|负责人电话
&emsp;gmtModified|string|更新时间

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
        "dutyInstitutions": "单位1",
        "departmentName": "部门1",
        "departmentLeader": null,
        "leaderContact": null,
        "gmtModified": 1610780162000
      }
    ],
    "total": 11,
    "size": 1,
    "current": 1,
    "orders": [],
    "searchCount": true,
    "pages": 11
  }
}
```

## 新增值班部门
### 请求地址
```
/duty-department:save
```

### 权限标识
- `duty-department:save`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
dutyInstitutions | string | true | 值班单位 | 
departmentName | string | true | 值班部门 |
departmentLeader | string | false | 部门负责人 |
leaderContact|string|false|负责人电话
### 请求示例
```json
{
  "dutyInstitutions":"单位3",
  "departmentName":"部门33",
  "departmentLeader":"张三",
  "leaderContact":"1369999999"
}
```

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | array | 返回数据 | id

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "更新成功",
    "success": true,
    "data": 1
}
```

## 获取单个值班部门
### 请求地址
```
/duty-department:get
```

### 权限标识
- `duty-department:get`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | Long | true | 值班单位 | 
### 请求示例
/duty-department/get?id=1

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 |
message | string | 返回描述 |
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | array | 返回数据 | 

### 返回参数示例
```json
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": {
    "id": 1,
    "dutyInstitutions": "单位1",
    "departmentName": "部门1",
    "departmentLeader": null,
    "leaderContact": null,
    "gmtModified": 1610780162000
  }
}
```

## 更新值班部门
### 请求地址
```
/duty-department:update
```

### 权限标识
- `duty-department:update`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | Long | true | ID |
dutyInstitutions | string | true | 值班单位 |
departmentName | string | true | 值班部门 |
departmentLeader | string | false | 部门负责人 |
leaderContact|string|false|负责人电话
### 请求示例
```json
{
  "id":3,
  "dutyInstitutions":"单位3",
  "departmentName":"部门33",
  "departmentLeader":"张三",
  "leaderContact":"1369999999"
}
```

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 |
message | string | 返回描述 |
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | array | 返回数据 | 

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "更新成功",
    "success": true,
    "data": 1
}
```

## 删除值班部门
### 请求地址
```
/duty-department:delete
```

### 权限标识
- `duty-department:delete`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | Long | true | 值班单位 |
### 请求示例
/duty-department/delete?id=1

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 |
message | string | 返回描述 |
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | array | 返回数据 |

### 返回参数示例
```json
{
  "code": "08010000",
  "message": "删除成功",
  "success": true,
  "data": null
}
```

## 获取单位列表
### 请求地址
```
/duty-department:getInstitutions
```

### 权限标识
- `duty-department:getInstitutions`

### 请求方式
- `GET`

### 请求参数说明
无

### 请求示例
/duty-department/getInstitutions

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 |
message | string | 返回描述 |
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | array | 返回数据 |

### 返回参数示例
```json
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": {
    "单位2": "单位2",
    "单位3": "单位3",
    "单位1": "单位1"
  }
}
```

## 根据单位获取部门
### 请求地址
```
/duty-department:getDepartmentByInstitutions
```

### 权限标识
- `duty-department:getDepartmentByInstitutions`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
institutions | String | true | 值班单位 |
### 请求示例
/duty-department/getDepartmentByInstitutions?institutions&=单位1

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 |
message | string | 返回描述 |
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | array | 返回数据 |

### 返回参数示例
```json
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": {
    "1": "部门1",
    "2": "部门2",
    "3": "部门3"
  }
}
```