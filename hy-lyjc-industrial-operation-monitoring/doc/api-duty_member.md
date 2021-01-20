# 值班人员管理
## 目录
- [查询值班人员表](#查询人员表)
- [新增值班人员](#新增值班人员)
- [获取单个值班人员](获取单个值班人员)
- [更新值班人员](#更新值班人员)
- [删除值班人员](#删除值班人员)
- [根据值班部门获取值班人员](#根据单位获取值班人员)
- [根据值班人员获取值班人员电话](#根据值班人员获取值班人员电话)

## 查询值班人员表
### 请求地址
```
/duty-member/list
```

### 权限标识
- `duty-member:list`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
current | number | true | 当前页 |
size | number | true | 每页条数 |

### 请求示例
/duty-member/list

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | array | 返回数据 | 
&emsp;id | string | ID | 
&emsp;dutyInstitutions | string | 值班单位 | 
&emsp;dutyDepartment | string | 值班部门 |
&emsp;departmentId | string | 值班部门ID |
&emsp;dutyPerson | string | 值班人员 | 
&emsp;dutyContact|string|联系电话
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
        "dutyDepartment": "部门1",
        "departmentId": 1,
        "dutyPerson": "张三",
        "dutyContact": "13693556666",
        "gmtCreate": 1610783369000,
        "gmtModified": 1610955825000
      }
    ],
    "total": 9,
    "size": 1,
    "current": 1,
    "orders": [],
    "searchCount": true,
    "pages": 9
  }
}
```

## 新增值班人员
### 请求地址
```
/duty-member:save
```

### 权限标识
- `duty-member:save`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
departmentId | string | true | 值班部门ID |
dutyPerson | string | false | 部门负责人 |
dutyContact|string|false|负责人电话
### 请求示例
```json
{
  "departmentId":3,
  "dutyPerson":"测试11",
  "dutyContact":13333333889
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
  "message": "操作成功",
  "success": true,
  "data": 10
}
```

## 获取单个值班人员
### 请求地址
```
/duty-member:get
```

### 权限标识
- `duty-member:get`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | Long | true | 值班单位 | 
### 请求示例
/duty-member/get?id=1

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
    "id": 10,
    "dutyInstitutions": "单位1",
    "dutyDepartment": "部门3",
    "departmentId": 3,
    "dutyPerson": "测试11",
    "dutyContact": "13333333889",
    "gmtCreate": 1611019699000,
    "gmtModified": 1611019699000
  }
}
```

## 更新值班人员
### 请求地址
```
/duty-member:update
```

### 权限标识
- `duty-member:update`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | Long | true | ID |
departmentId | string | true | 值班部门ID |
dutyPerson | string | true | 值班人員 |
dutyContact|string|true|值班人电话
### 请求示例
```json
{
  "departmentId": 1,
  "dutyPerson": "测试118",
  "dutyContact": 13333333889,
  "id": 10
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
  "data": null
}
```

## 删除值班人员
### 请求地址
```
/duty-member:delete
```

### 权限标识
- `duty-member:delete`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | Long | true | 值班单位 |
### 请求示例
/duty-member/delete?id=1

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

## 根据部门获取人员
### 请求地址
```
/duty-member:getMemberByDepartment
```

### 权限标识
- `duty-member:getMemberByDepartment`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
institutions | String | true | 值班单位 |
### 请求示例
/duty-member/getMemberByDepartment?departmentId=2

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
    "3": "王五",
    "4": "王五66",
    "5": "zhoaliu"
  }
}
```

## 根据值班人员获取值班人员电话
### 请求地址
```
/duty-member:getContactByMember
```

### 权限标识
- `duty-member:getContactByMember`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
memberId | Long | true | 值班人员ID |
### 请求示例
/duty-member/getContactByMember?memberId=10

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
    "10": "13333333889"
  }
}
```