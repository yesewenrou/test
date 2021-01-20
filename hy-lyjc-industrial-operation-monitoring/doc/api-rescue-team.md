# 救援队伍管理

## 目录

- [查询救援队伍类型列表](#查询救援队伍类型列表)
- [新增救援队伍](#新增救援队伍)
- [删除救援队伍](#删除救援队伍)
- [更新救援队伍](#更新救援队伍)
- [查询单个救援队伍](#查询单个救援队伍)
- [查询救援队伍列表](#查询救援队伍列表)

## 查询救援队伍类型列表

### 请求地址

```
/rescue-team/type/list
```

### 权限标识

- 无

### 请求方式

- `GET`

### 请求参数说明

无

### 请求示例

无

### 返回参数说明

名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 |
message | string | 返回描述 |
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | array | 返回数据 |
&emsp;type | number | 类型 |
&emsp;typeDesc | number | 类型描述 |

### 返回参数示例

```json
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": [
    {
      "type": 1,
      "typeDesc": "道路救援"
    },
    {
      "type": 2,
      "typeDesc": "消防救援"
    },
    {
      "type": 3,
      "typeDesc": "医疗救援"
    },
    {
      "type": 4,
      "typeDesc": "自然灾害救援"
    },
    {
      "type": 5,
      "typeDesc": "综合性救援"
    },
    {
      "type": 99,
      "typeDesc": "其他"
    }
  ]
}
```

## 新增救援队伍

### 请求地址

```
/rescue-team
```

### 权限标识

- 无

### 请求方式

- `POST`

### 请求参数说明

名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
name | string | true | 救援队伍名称 |
type | number | true | 救援队伍类型 |
org | string | true | 隶属单位 |
principal | string | true | 负责人 |
principalPhone | string | true | 负责人电话 |
teamPhone | string | true | 队伍电话 |

### 请求示例

```json
{
  "name": "名称1",
  "type": 1,
  "org": "隶属单位1",
  "principal": "负责人1",
  "principalPhone": "负责人电话1",
  "teamPhone": "队伍电话1"
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
  "data": 1
}
```

## 删除救援队伍

### 请求地址

```
/rescue-team
```

### 权限标识

- 无

### 请求方式

- `DELETE`

### 请求参数说明

名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | number | true | 救援队伍ID |

### 请求示例

/rescue-team/6

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

## 更新救援队伍

### 请求地址

```
/rescue-team
```

### 权限标识

- 无

### 请求方式

- `PUT`

### 请求参数说明

名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | string | true | 救援队伍ID |
name | string | true | 救援队伍名称 |
type | number | true | 救援队伍类型 |
org | string | true | 隶属单位 |
principal | string | true | 负责人 |
principalPhone | string | true | 负责人电话 |
teamPhone | string | true | 队伍电话 |

### 请求示例

```json
{
  "id": 1,
  "name": "名称1改",
  "type": 1,
  "org": "隶属单位1",
  "principal": "负责人1",
  "principalPhone": "负责人电话1",
  "teamPhone": "队伍电话1"
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
  "message": "操作成功",
  "success": true,
  "data": null
}
```

## 查询单个救援队伍

### 请求地址

```
/rescue-team/1
```

### 权限标识

- 无

### 请求方式

- `GET`

### 请求参数说明

名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
id | number | true | 救援队伍ID |

### 请求示例

/rescue-team/1

### 返回参数说明

名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 |
message | string | 返回描述 |
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | string | 返回数据 |
&emsp;id | string | true | 救援队伍ID |
&emsp;name | string | true | 救援队伍名称 |
&emsp;type | number | true | 救援队伍类型 |
&emsp;org | string | true | 隶属单位 |
&emsp;principal | string | true | 负责人 |
&emsp;principalPhone | string | true | 负责人电话 |
&emsp;teamPhone | string | true | 队伍电话 |

### 返回参数示例

```json
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": {
    "id": 1,
    "name": "名称1改",
    "type": 1,
    "typeDesc": "道路救援",
    "org": "隶属单位1",
    "principal": "负责人1",
    "principalPhone": "负责人电话1",
    "teamPhone": "队伍电话1"
  }
}
```

## 查询救援队伍列表

### 请求地址

```
/rescue-team/page
```

### 权限标识

- 无

### 请求方式

- `GET`

### 请求参数说明

名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
current | number | true | 当前页 |
size | number | true | 每页条数 |
name | string | false | 救援队伍名称 | 模糊匹配
type | number | false | 救援队伍类型 |

### 请求示例

/rescue-team/page?current=1&size=10&name=名称&type=2

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
&emsp;&emsp;id | string | true | 救援队伍ID |
&emsp;&emsp;name | string | true | 救援队伍名称 |
&emsp;&emsp;type | number | true | 救援队伍类型 |
&emsp;&emsp;org | string | true | 隶属单位 |
&emsp;&emsp;principal | string | true | 负责人 |
&emsp;&emsp;principalPhone | string | true | 负责人电话 |
&emsp;&emsp;teamPhone | string | true | 队伍电话 |

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
        "name": "名称2",
        "type": 2,
        "typeDesc": "消防救援",
        "org": "隶属单位2",
        "principal": "负责人2",
        "principalPhone": "负责人电话2",
        "teamPhone": "队伍电话2"
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
