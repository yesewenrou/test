# 处理结果模块 #
## 同意受理 ##
### 路径 ###
<pre>http://192.168.10.39:30800/handleResult/agreeHandle/{complaintId} </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* handleResult:agreeHandle
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  complaintId | long  | 投诉单ID  |  Y |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": 1
}
</pre>


## 拒绝受理 ##
### 路径 ###
<pre>http://192.168.10.39:30800/handleResult/disagreeHandle </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* handleResult:disagreeHandle
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  complaintId | long  | 投诉单ID  |  Y |
|  rejectReason | string | 拒绝原因 | Y |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": 1
}
</pre>


## 完成受理 ##
### 路径 ###
<pre>http://192.168.10.39:30800/handleResult/finishHandle </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* handleResult:finishHandle
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  complaintId | long  | 投诉单ID  |  Y |
|  complaintObjectFullname | string | 被投诉方全称 | Y |
|  industryType | string | 投诉行业分类 | Y |
|  handlerResult | string | 处理结果 | Y |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": 1
}
</pre>