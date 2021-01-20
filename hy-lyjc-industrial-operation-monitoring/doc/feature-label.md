# 特色标签模块 #
## 新增 ##
### 路径 ###
<pre>http://192.168.10.39:30800/featureLabel/add </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* featureLabel:add
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  labelName | string  | 标签名称  |  Y |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": true
}
</pre>

## 更新 ##
### 路径 ###
<pre>http://192.168.10.39:30800/featureLabel/update </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* featureLabel:update
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  id | number  | 主键ID  |  Y |
|  labelName | string  | 标签名称  |  Y |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": true
}
</pre>

## 删除 ##
### 路径 ###
<pre>http://192.168.10.39:30800/featureLabel/delete/{id} </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* featureLabel:delete
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  id | number  | 主键ID  |  Y |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": true
}
</pre>

## 列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/featureLabel/list </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* featureLabel:list
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | int  | 当前页码  |  Y |
|  size | int  | 每页条数  |  Y |
|  resourceName | string  | 资源名称  |  N |
|  resourceTypeCode | string  | 资源类型编码  |  N |
|  regionalCode | string  | 所属区域编码  |  N |
|  featureLabel | string  | 特色标签  |  N |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "id": 1,
            "labelName": "特色住宿",
            "updateBy": 3,
            "updateName": "admin",
            "updateTime": 1579159761000
        },
        {
            "id": 2,
            "labelName": "住的舒心",
            "updateBy": 3,
            "updateName": "admin",
            "updateTime": 1579159761000
        },
        {
            "id": 3,
            "labelName": "特色美食",
            "updateBy": 3,
            "updateName": "admin",
            "updateTime": 1579159761000
        },
        {
            "id": 4,
            "labelName": "吃的放心",
            "updateBy": 3,
            "updateName": "admin",
            "updateTime": 1579159761000
        },
        {
            "id": 5,
            "labelName": "购得称心",
            "updateBy": 3,
            "updateName": "admin",
            "updateTime": 1579159761000
        },
        {
            "id": 6,
            "labelName": "特色商品",
            "updateBy": 3,
            "updateName": "admin",
            "updateTime": 1579159761000
        }
    ]
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  | 备注
| :------------ | :------------ | :------------ 
| code | string | 返回码 |
| message | string | 返回描述 |
| success | boolean | 是否成功 | 
| data | object | 返回数据 | 
| &emsp;total | number | 总条数 |
| &emsp;pages | number | 总页数 |
| &emsp;current | number | 当前页 |
| &emsp;size | number | 每页条数 |
| &emsp;records | array | 查询结果 | 
|  &emsp;&emsp;id | 主键ID  |  
|  &emsp;&emsp;labelName | 标签名称  |
|  &emsp;&emsp;updateBy  | 更新人ID |
|  &emsp;&emsp;updateName  | 更新人名称 |
|  &emsp;&emsp;updateTime  | 更新时间 |

## 标签列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/featureLabel/labelList </pre>
### 请求方式 ###
* GET
### 权限标识 ###
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |

### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "id": 1,
            "labelName": "特色住宿"
        },
        {
            "id": 2,
            "labelName": "住的舒心"
        },
        {
            "id": 3,
            "labelName": "特色美食"
        },
        {
            "id": 4,
            "labelName": "吃的放心"
        },
        {
            "id": 5,
            "labelName": "购得称心"
        },
        {
            "id": 6,
            "labelName": "特色商品"
        }
    ]
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  | 备注
| :------------ | :------------ | :------------ 
| code | string | 返回码 |
| message | string | 返回描述 |
| success | boolean | 是否成功 | 
| data | object | 返回数据 | 
|  &emsp;id | 主键ID  |  
|  &emsp;labelName | 标签名称  |