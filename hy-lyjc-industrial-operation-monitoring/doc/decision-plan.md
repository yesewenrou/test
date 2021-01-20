## 决策方案

## 新增 ##
### 路径 ###
<pre>/decision_plan </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* decision_plan:add
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  name | string  | 名称  |  Y |
|  eventType | string  | 事件类型编码  |  Y |
|  eventLevel | string  | 事件等级编码  |  Y |
|  attaches | list  | 附件列表  |  N |
|  &nbsp;&nbsp;attachName | string  | 附件名称  |  Y |
|  &nbsp;&nbsp;attachUrl | string  | 附件url  |  Y |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": 1
}
</pre>

## 修改 ##
### 路径 ###
<pre>/decision_plan </pre>
### 请求方式 ###
* PUT
### 权限标识 ###
* decision_plan:update
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  id | number  | id  |  Y |
|  name | string  | 名称  |  Y |
|  eventType | string  | 事件类型编码  |  Y |
|  eventLevel | string  | 事件等级编码  |  Y |
|  attaches | list  | 附件列表  |  N |
|  &nbsp;&nbsp;attachName | string  | 附件名称  |  Y |
|  &nbsp;&nbsp;attachUrl | string  | 附件url  |  Y |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": 1
}
</pre>

## 删除 ##
### 路径 ###
<pre>/decision_plan/{id} </pre>
### 请求方式 ###
* DELETE
### 权限标识 ###
* decision_plan:delete
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  id | number  | id  |  Y |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": 1
}
</pre>


## 详情 ##
### 路径 ###
<pre>/decision_plan/{id} </pre>
### 请求方式 ###
* GET
### 权限标识 ###
**无**
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 请求参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  id | number  | id  |  Y |

### 返回参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  id | number  | id  |  Y |
|  name | string  | 名称  |  Y |
|  eventType | string  | 事件类型编码  |  Y |
|  eventTypeName | string  | 事件类型名称  |  Y |
|  eventLevel | string  | 事件等级编码  |  Y |
|  eventLevelName | string  | 事件等级名称  |  Y |
|  gmtCreate | string  | 创建时间  |  Y |
|  gmtModified | string  | 修改时间  |  Y |
|  attaches | list  | 附件列表  |  N |
|  &nbsp;&nbsp;attachName | string  | 附件名称  |  Y |
|  &nbsp;&nbsp;attachUrl | string  | 附件url  |  Y |

### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": 1
}
</pre>

## 分页查询 ##
### 路径 ###
<pre>/decision_plan/list </pre>
### 请求方式 ###
* GET
### 权限标识 ###
**无**
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 请求参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | number  | 查询页  |  Y |
|  size | number  | 查询记录数  |  Y |

### 返回参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  id | number  | id  |  Y |
|  name | string  | 名称  |  Y |
|  eventType | string  | 事件类型编码  |  Y |
|  eventTypeName | string  | 事件类型名称  |  Y |
|  eventLevel | string  | 事件等级编码  |  Y |
|  eventLevelName | string  | 事件等级名称  |  Y |
|  gmtCreate | string  | 创建时间  |  Y |
|  gmtModified | string  | 修改时间  |  Y |
|  attaches | list  | 附件列表  |  N |
|  &nbsp;&nbsp;attachName | string  | 附件名称  |  Y |
|  &nbsp;&nbsp;attachUrl | string  | 附件url  |  Y |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": 1
}
</pre>