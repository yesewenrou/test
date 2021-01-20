# 旅游资源模块 #
## 新增旅游资源 ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismResource/add </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* tourismResource:add
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  name | string  | 资源名称  |  Y |
|  area | string  | 区域code  |  Y |
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


## 更新旅游资源 ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismResource/update </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* tourismResource:update
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  id | int  | 主键  |  Y |
|  name | string  | 资源名称  |  Y |
|  area | string  | 区域code  |  Y |
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


## 资源详情 ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismResource/findById/6 </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* tourismResource:findById
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  id | int  | 资源ID  |  Y |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": {
    "id": 6,
    "name": "bubble",
    "area": "002003",
    "location": "lop",
    "type": "008008",
    "longitude": 29.75,
    "latitude": 86.78,
    "servicePhone": "028-39102883",
    "createTime": 1566268535000,
    "updateTime": 1566284560000
  }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  id | 资源ID 
|  name | 资源名称
|  area | 所属区域
|  location | 地理位置
|  type | 资源类型
|  longitude | 经度
|  latitude | 纬度
|  servicePhone | 服务电话


## 删除资源 ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismResource/delete/6 </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* tourismResource:delete
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  id | int  | 资源ID  |  Y |
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


## 资源列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismResource/list </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* tourismResource:list
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | int  | 当前页码  |  Y |
|  size | int  | 每页条数  |  Y |
|  condition.name | string  | 资源名称  |  N |
|  condition.type | string  | 资源类型  |  N |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": {
    "records": [
      {
        "id": 8,
        "name": "pkp",
        "area": "槽渔滩",
        "location": "lop",
        "type": "购物场所",
        "longitude": 29.75,
        "latitude": 86.78,
        "servicePhone": "028-39102883",
        "createTime": 1566285459000,
        "updateTime": 1566290845000
      },
      {
        "id": 2,
        "name": "pk",
        "area": "七里坪镇",
        "location": "lop",
        "type": "汽车服务公司",
        "longitude": 21.75,
        "latitude": 56.78,
        "servicePhone": "028-39102883",
        "createTime": 1566266193000,
        "updateTime": 1566284551000
      }
    ],
    "total": 3,
    "size": 2,
    "current": 1,
    "orders": [],
    "searchCount": true,
    "pages": 2
  }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  id | 资源ID 
|  name | 资源名称
|  area | 所属区域
|  location | 地理位置
|  type | 资源类型
|  longitude | 经度
|  latitude | 纬度
|  servicePhone | 服务电话


## 区域列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismResource/areaList </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* tourismResource:areaList
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
      "id": 2,
      "code": "001001",
      "name": "洪川镇",
      "orderNumber": 1,
      "parentCode": "REGION",
      "children": []
    },
    {
      "id": 3,
      "code": "001002",
      "name": "止戈镇",
      "orderNumber": 2,
      "parentCode": "REGION",
      "children": []
    }
  ]
}
</pre>


## 资源类型列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/tourismResource/resourceTypeList </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* tourismResource:resourceTypeList
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
      "id": 51,
      "code": "008001",
      "name": "景区",
      "orderNumber": 1,
      "parentCode": "TOURISM_RESOURCE_TYPE",
      "children": []
    },
    {
      "id": 52,
      "code": "008002",
      "name": "旅行社",
      "orderNumber": 2,
      "parentCode": "TOURISM_RESOURCE_TYPE",
      "children": []
    }
  ]
}
</pre>