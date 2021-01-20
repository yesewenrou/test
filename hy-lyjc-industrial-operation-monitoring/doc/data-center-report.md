# 大数据中心报表导出模块 #
## 大数据中心报表导出 ##
### 路径 ###
<pre>/dataCenterMonthlyReport/export</pre>
### 请求方式 ###
*GET*
### 权限标识 ###
* dataCenterMonthlyReport:export
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
* `beginDate`: long, 开始日期的时间戳，毫秒，必传
* `endDate`: long, 结束日期的时间戳，毫秒，必传
#### 请求示例
### 返回结果 ###
<pre>
    文件流，浏览器直接下载
</pre>
```