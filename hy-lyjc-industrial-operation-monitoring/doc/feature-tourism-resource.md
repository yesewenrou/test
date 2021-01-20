# 特色旅游资源模块 #
## 新增 ##
### 路径 ###
<pre>http://192.168.10.39:30800/featureResource/add </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* featureResource:add
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  resourceName | string  | 资源名称  |  Y |
|  resourceTypeCode | string  | 资源类型编码  |  Y |
|  resourceTypeName | string  | 资源类型名称  |  Y |
|  regionalCode | string  | 所属区域编码  |  Y |
|  regionalName | string  | 所属区域名称  |  Y |
|  addressDetail | string  | 详细地址  |  Y |
|  featureLabelIds | string  | 特色标签id 多个用英文逗号隔开 |  Y | 
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
<pre>http://192.168.10.39:30800/featureResource/update </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* featureResource:update
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  id | number  | 主键ID  |  Y |
|  resourceName | string  | 资源名称  |  N |
|  resourceTypeCode | string  | 资源类型编码  |  N |
|  resourceTypeName | string  | 资源类型名称  |  N |
|  regionalCode | string  | 所属区域编码  |  N |
|  regionalName | string  | 所属区域名称  |  N |
|  addressDetail | string  | 详细地址  |  N |
|  featureLabelIds | string  | 特色标签  | N |
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
<pre>http://192.168.10.39:30800/featureResource/delete/{id} </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* featureResource:delete
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
<pre>http://192.168.10.39:30800/featureResource/list </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* featureResource:list
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | int  | 当前页码  |  Y |
|  size | int  | 每页条数  |  Y |
|  condition.resourceName | string  | 资源名称  |  N |
|  condition.resourceTypeCode | string  | 资源类型编码  |  N |
|  condition.regionalCode | string  | 所属区域编码  |  N |
|  condition.featureLabel | string  | 特色标签  |  N |
### 请求参数示例 ###
<pre>
{
    "current": 1,
    "size": 10,
    "condition": {
        "resourceName": "七里坪",
        "resourceTypeCode": "029003",
        "regionalCode": "001012",
        "featureLabel": "5"
    }
}
</pre>
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "records": [
            {
                "id": 182,
                "resourceName": "洪雅县七里坪购物中心",
                "resourceTypeCode": "029003",
                "resourceTypeName": "购物场所",
                "regionalCode": "001012",
                "regionalName": "七里坪镇",
                "addressDetail": "四川省眉山市洪雅县高庙镇七里村2组",
                "featureLabelIds": "5",
                "featureLabelNames": "购得称心",
                "updateBy": 3,
                "updateName": "admin",
                "updateTime": 1579087024000
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
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------ | :------------ 
| code | string | 返回码 |
| message | string | 返回描述 |
| success | boolean | 是否成功 | 用于前端直接判断请求是否成功
| data | object | 返回数据 | 
| records | array | 诚信指标列表 | 
|  &emsp;&emsp;id | 主键ID  |  
|  &emsp;&emsp;resourceName | 资源名称  |
|  &emsp;&emsp;resourceTypeCode | 资源类型编码  |
|  &emsp;&emsp;resourceTypeName | 资源类型名称  |
|  &emsp;&emsp;regionalCode | 所属区域编码  |
|  &emsp;&emsp;regionalName | 所属区域名称  |
|  &emsp;&emsp;addressDetail | 详细地址  |
|  &emsp;&emsp;featureLabelIds | 特色标签  | 
|  &emsp;&emsp;featureLabelNames | 特色标签名称  | 
|  &emsp;&emsp;updateBy  | 更新人ID |
|  &emsp;&emsp;updateName  | 更新人名称 |
|  &emsp;&emsp;updateTime  | 更新时间 |