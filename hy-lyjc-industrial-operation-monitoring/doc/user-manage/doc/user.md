# 平台用户管理模块 #
## 用户列表 ##
### 路径 ###
<pre>/um/user/list </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* um:user:list
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  page | int  | 当前页码  |  Y |
|  size | int  | 每页条数  |  Y |
|  userName | string  | 用户名  |  N |
|  roleId | int  | 角色ID  |  N |
|  companyId | int  | 公司ID  |  N |
|  departmentId | int  | 部门ID  |  N |
### 返回结果 ###
<pre>
{
  "code": "01000000",
  "message": "success",
  "success": true,
  "data": {
    "records": [
      {
        "id": 18,
        "userName": "刘颖",
        "phoneNum": "13565654678",
        "company": {
          "id": 13,
          "name": "眉山市文化旅游局"
        },
        "department": {
          "id": 1,
          "name": "人资办公室"
        },
        "roleList": [
          {
            "id": 1,
            "name": "超级管理员"
          }
        ]
      }
    ],
    "total": 1,
    "size": 100,
    "current": 1,
    "searchCount": true,
    "pages": 1
  }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  id | 用户ID 
|  userName | 用户名
|  phoneNum | 手机号
|  company | 单位
|  department | 部门
|  roleList | 角色列表


## 单位列表 ##
### 路径 ###
<pre>/um/company/list </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* um:company:list
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 返回结果 ###
<pre>
{
  "code": "01000000",
  "message": "success",
  "success": true,
  "data": [
    {
      "id": 13,
      "name": "眉山市文化旅游局"
    },
    {
      "id": 12,
      "name": "洪雅县交通运输管理局"
    }
  ]
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  id | 单位ID 
|  name | 单位名称


## 部门列表 ##
### 路径 ###
<pre>/um/department/findByCompanyId/id </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* um:department:findByCompanyId
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  id | int  | 单位ID  |  Y |
### 返回结果 ###
<pre>
{
  "code": "01000000",
  "message": "",
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "人资办公室"
    },
    {
      "id": 13,
      "name": "文化宣传部212"
    }
  ]
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  id | 部门ID 
|  name | 部门名称


## 用户对应角色列表 ##
### 路径 ###
<pre>/um/role/findByUserId/id </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* um:role:findByUserId
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  id | int  | 用户ID  |  Y |
### 返回结果 ###
<pre>
{
  "code": "01000000",
  "message": "success",
  "success": true,
  "data": [
    {
      "id": 5,
      "name": "大屏监察员3"
    },
    {
      "id": 2,
      "name": "大屏监察员"
    }
  ]
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  id | 角色ID 
|  name | 角色名称


## 用户分配角色 ##
### 路径 ###
<pre>/um/userRole/add </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* um:userRole:add
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  userId | int  | 用户ID  |  Y |
|  roleIdList | array  | 角色ID列表  |  Y |
### 返回结果 ###
<pre>
{
  "code": "01000000",
  "message": "success",
  "success": true,
  "data": 20
}
</pre>


## 退出登录 ##
### 路径 ###
<pre>/um/user/logout </pre>
### 请求方式 ###
* GET
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 返回结果 ###
<pre>
{
  "code": "01000000",
  "message": "success",
  "success": true,
  "data": 20
}
</pre>


## 修改密码 ##
### 路径 ###
<pre>/um/user/changePassword </pre>
### 请求方式 ###
* POST
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  oldPassword | string  | 旧密码  |  Y |
|  newPassword | string  | 新密码  |  Y |
### 返回结果 ###
<pre>
{
  "code": "01000000",
  "message": "success",
  "success": true,
  "data": 20
}
</pre>