# 文化活动 #
## 新增 ##
### 路径 ###
<pre>http://192.168.10.39:30800/cultural/add </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* cultural:add
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  culturalName | string  | 文化活动名称  |  Y |
|  culturalTime | number  | 文化活动时间  |  Y |
|  coverPhotoUrl | string  | 封面照片url  |  N |
|  fileUrls | string  | 附件urls  |  Y |
|  fileNames | string  | 附件名称  |  Y |
### 请求示例 ###
<pre>
{
	"culturalName":"活动1",
	"culturalTime":1585207669000,
	"coverPhotoUrl":"http://123.com",
	"fileUrls":["http://123.com","http://1234.com"],
	"fileNames":["文件1","文件2"]
}
</pre>
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": 4
}
</pre>

## 删除 ##
### 路径 ###
<pre>http://192.168.10.39:30800/cultural/{id} </pre>
### 请求方式 ###
* DELETE
### 权限标识 ###
* cultural:delete
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
    "data": null
}
</pre>

## 分页查询 ##
### 路径 ###
<pre>http://192.168.10.39:30800/cultural/list </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* cultural:list
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  culturalName | string  | 文化活动名称  |  N |
|  beginTime | number  | 开始时间  |  N |
|  endTime | number  | 结束时间  |  N |
|  page | number  | 页码  |  N |
|  size | number  | 条数  |  N |
### 返回结果 ###
<pre>
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "totalCount": 3,
        "totalPages": 2,
        "currentSize": 2,
        "pageNum": 1,
        "list": [
            {
                "id": 2,
                "culturalName": "活动1",
                "culturalTime": 1585207669000,
                "coverPhotoUrl": "",
                "fileUrls": [
                    "http://123.com"
                ],
                "fileNames": [
                    "文件1"
                ]
            },
            ...
        ]
    }
}
</pre>