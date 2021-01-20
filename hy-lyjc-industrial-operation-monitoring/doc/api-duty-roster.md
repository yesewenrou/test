# 值班管理
## 目录
- [查询值班表](#查询值班表)
- [获取单天值班表](#获取单天值班表)
- [更新某天值班信息](#更新某天值班信息)

## 查询值班表
### 请求地址
```
/duty-roster/list
```

### 权限标识
- `duty-roster:list`

### 请求方式
- `POST`

### 请求参数说明
### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
current | number | true | 当前页 |
size | number | true | 每页条数 |

### 请求示例
/duty-roster/list

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | array | 返回数据 | 
&emsp;dutyTime | string | 值班时间 | MONDAY ~ SUNDAY
&emsp;dutyTimeDesc | string | 值班时间描述 | 星期一 ~ 星期日
&emsp;dutyPerson | string | 值班人员 |
&emsp;memberId | string | 值班人员ID |
&emsp;contact | string | 值班人员电话 |
&emsp;dutyInstitutions | 值班单位 |
&emsp;dutyDepartment | 值班部门 |
&emsp;departmentLeader|string|值班领导
&emsp;leaderContact | string |领导联系方式 |

### 返回参数示例
```json
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": [
    {
      "dutyTime": "MONDAY",
      "dutyTimeDesc": "星期一",
      "dutyPerson": "测试118",
      "contact": "13333333889",
      "memberId": 10,
      "dutyLeader": null,
      "dutyInstitutions": "单位1",
      "dutyDepartment": "部门1",
      "departmentLeader": null,
      "leaderContact": null
    }
  ]
}
```

## 获取单天值班表
### 请求地址
```
/duty-roster:get
```

### 权限标识
- `duty-roster:get`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
dutyTime | String | true | 值班时间 | MONDAY ~ SUNDAY
### 请求示例
/duty-roster/get?dutyTime=MONDAY

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
    "dutyTime": "MONDAY",
    "dutyTimeDesc": null,
    "dutyPerson": "测试118",
    "contact": "13333333889",
    "memberId": 10,
    "dutyLeader": null,
    "dutyInstitutions": "单位3",
    "dutyDepartment": "部门35",
    "departmentLeader": null,
    "leaderContact": null
  }
}
```

## 更新某天值班信息
### 请求地址
```
/duty-roster/update
```

### 权限标识
- `duty-roster:update`

### 请求方式
- `POST`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
dutyTime | string | true | 值班时间 | MONDAY ~ SUNDAY
memberId | string | true | 值班人员ID |
### 请求示例
```json
{
  "dutyTime": "TUESDAY",
  "memberId": 5
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
  "message": "更新成功",
  "success": true,
  "data": null
}
```