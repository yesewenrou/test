# 行业管理模块 #
## 新增行业 ##
### 路径 ###
<pre>http://192.168.10.39:30800/industryManage/add </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* industryManage:add
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  name | string  | 行业名称  |  Y |
|  area | string  | 区域code  |  Y |
|  operatingSubject | string  | 经营主体  |  Y |
|  location | string  | 地理位置  |  Y |
|  type | string  | 类型code  |  Y |
|  longitude | string  | 经度  |  N |
|  latitude | string  | 纬度  |  N |
|  servicePhone | string  | 服务电话  |  Y |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": 1
}
</pre>


## 更新行业 ##
### 路径 ###
<pre>http://192.168.10.39:30800/industryManage/update </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* industryManage:update
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  id | int  | 主键  |  Y |
|  name | string  | 行业名称  |  Y |
|  area | string  | 区域code  |  Y |
|  operatingSubject | string  | 经营主体  |  Y |
|  location | string  | 地理位置  |  Y |
|  type | string  | 类型code  |  Y |
|  longitude | string  | 经度  |  N |
|  latitude | string  | 纬度  |  N |
|  servicePhone | string  | 服务电话  |  Y |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": 1
}
</pre>


## 行业详情 ##
### 路径 ###
<pre>http://192.168.10.39:30800/industryManage/findById/{id} </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* industryManage:findById
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  id | int  | 行业ID  |  Y |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": {
    "id": 1,
    "name": "POI",
    "area": "2",
    "operatingSubject": "四川科技公司",
    "location": "lop",
    "type": "1",
    "longitude": 29.75,
    "latitude": 86.78,
    "servicePhone": "028-39102883",
    "createTime": 1566357559000,
    "updateTime": 1566467956000
  }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  id | ID 
|  name | 行业名称
|  area | 所属区域
|  operatingSubject | 经营主体
|  location | 地理位置
|  type | 行业类型
|  longitude | 经度
|  latitude | 纬度
|  servicePhone | 服务电话


## 删除行业 ##
### 路径 ###
<pre>http://192.168.10.39:30800/industryManage/delete/1 </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* industryManage:delete
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  id | int  | ID  |  Y |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": {
  }
}
</pre>


## 行业列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/industryManage/list </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* industryManage:list
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | int  | 当前页码  |  Y |
|  size | int  | 每页条数  |  Y |
|  condition.name | string  | 行业名称  |  N |
|  condition.type | string  | 行业类型  |  N |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "records": [
      {
        "id": 3,
        "name": "wto",
        "area": "中保镇",
        "operatingSubject": "MFC公司",
        "location": "lop",
        "type": "购物",
        "longitude": 29.75,
        "latitude": 86.78,
        "servicePhone": "028-39102883",
        "createTime": 1566357693000,
        "updateTime": 1566357809000
      }
    ],
    "total": 1,
    "size": 2,
    "current": 1,
    "orders": [],
    "searchCount": true,
    "pages": 1
  }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  id | ID 
|  name | 行业名称
|  area | 所属区域
|  operatingSubject | 经营主体
|  location | 地理位置
|  type | 行业类型
|  longitude | 经度
|  latitude | 纬度
|  servicePhone | 服务电话


## 行业类型列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/industryManage/industryTypeList </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* industryManage:industryTypeList
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": [
    {
      "id": 70,
      "code": "010001",
      "name": "景区",
      "orderNumber": 1,
      "parentCode": "INDUSTRY_TYPE",
      "children": []
    },
    {
      "id": 71,
      "code": "010002",
      "name": "住宿",
      "orderNumber": 2,
      "parentCode": "INDUSTRY_TYPE",
      "children": []
    }
  ]
}
</pre>