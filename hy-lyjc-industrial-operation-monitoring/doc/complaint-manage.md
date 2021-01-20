# 投诉管理模块 #
## 新增投诉 ##
### 路径 ###
<pre>http://192.168.10.39:30800/complaintManage/add </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* complaintManage:add
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  complaintObject | string  | 被投诉方  |  Y |
|  complainant | string  | 投诉人  |  Y |
|  sex | string  | 性别  |  N |
|  mobile | string  | 手机号  |  Y |
|  content | string  | 投诉内容  |  Y |
|  type | string  | 类型code  |  Y |
|  channel | string  | 投诉来源  |  Y |
|  complaintTime | long  | 投诉时间  |  Y |
|  certificate | string  | 投诉凭证  |  N |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": 1
}
</pre>


## 投诉单详情 ##
### 路径 ###
<pre>http://192.168.10.39:30800/complaintManage/findById/{id} </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* complaintManage:findById
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  id | int  | 投诉单ID  |  Y |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": {
    "handleResult": {
      "id": null,
      "complaintId": null,
      "complaintObjectFullname": "瓦屋山土豪餐饮部",
      "industryType": "010001",
      "rejectReason": null,
      "assignee": null,
      "assigneeTime": null,
      "handler": "testAdmin",
      "handleTime": 1566442346000,
      "handlerResult": "must handle",
      "createTime": null,
      "updateTime": null
    },
    "complaintManage": {
      "id": 1,
      "complaintNumber": "T201908211566453155543",
      "complaintObject": "瓦屋山景区投资管理公司",
      "complainant": "李华",
      "sex": "",
      "mobile": "15634561111",
      "content": "根据国家旅游局发布的旅游法：第三十五条　旅行社不得以不合理的低价组织旅游活动，诱骗旅游者，并通过安排购物或者另行付费旅游项目获取回扣等不正当利益。旅行社组织、接待旅游者，不得指定具体购物场所，不得安排另行付费旅游项目。但是，经双方协商一致或者旅游者要求，且不影响其他旅游",
      "certificate": "",
      "type": "消费服务",
      "channel": "畅游洪雅APP",
      "complaintTime": 1566374907000,
      "status": 4,
      "createTime": 1566375333000,
      "updateTime": 1566453140000
    }
  }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  complaintManage | 投诉详情
|  id | ID 
|  complaintNumber | 投诉编号
|  complaintObject | 被投诉方
|  complainant | 投诉人
|  sex | 性别 
|  mobile | 手机号 
|  content | 投诉内容
|  type | 类型code  
|  channel  | 投诉来源 
|  complaintTime | 投诉时间  
|  certificate | 投诉凭证
|  status | 投诉单状态
|  handleResult | 处理结果详情
|  complaintObjectFullname | 被投诉方全称 
|  industryType | 投诉行业
|  rejectReason | 不受理原因
|  assignee | 初审处理人
|  assigneeTime | 初审处理时间
|  handler | 最终处理人
|  handleTime | 最终处理时间
|  handlerResult | 处理结果



## 投诉分类列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/complaintManage/typeList </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* complaintManage:typeList
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
      "id": 76,
      "code": "011001",
      "name": "公共服务",
      "orderNumber": 1,
      "parentCode": "COMPLAINT_TYPE",
      "children": []
    },
    {
      "id": 77,
      "code": "011002",
      "name": "消费服务",
      "orderNumber": 2,
      "parentCode": "COMPLAINT_TYPE",
      "children": []
    }
  ]
}
</pre>



## 投诉渠道列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/complaintManage/channelList </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* complaintManage:channelList
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
      "id": 83,
      "code": "012001",
      "name": "畅游洪雅APP",
      "orderNumber": 1,
      "parentCode": "COMPLAINT_CHANNEL",
      "children": []
    },
    {
      "id": 84,
      "code": "012002",
      "name": "公众号",
      "orderNumber": 2,
      "parentCode": "COMPLAINT_CHANNEL",
      "children": []
    }
  ]
}
</pre>



## 投诉列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/complaintManage/list </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* complaintManage:list
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | int  | 当前页码  |  Y |
|  size | int  | 每页条数  |  Y |
|  condition.complaintNumber | string  | 投诉编号  |  N |
|  condition.type | string  | 投诉分类  |  N |
|  condition.status | int  | 投诉单状态，1：未处理，2：受理中，3：不受理，4：已完成  |  N |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": {
    "records": [
      {
        "id": 1,
        "complaintNumber": "T201908211566453155543",
        "complaintObject": "瓦屋山景区投资管理公司",
        "complainant": "李华",
        "sex": "",
        "mobile": "15634561111",
        "content": "根据国家旅游局发布的旅游法：第三十五条　旅行社不得以不合理的低价组织旅游活动，诱骗旅游者，并通过安排购物或者另行付费旅游项目获取回扣等不正当利益。旅行社组织、接待旅游者，不得指定具体购物场所，不得安排另行付费旅游项目。但是，经双方协商一致或者旅游者要求，且不影响其他旅游",
        "certificate": "",
        "type": "消费服务",
        "channel": "畅游洪雅APP",
        "complaintTime": 1566374907000,
        "status": 4,
        "createTime": 1566375333000,
        "updateTime": 1566453140000
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
| :------------: | :------------: 
|  complaintNumber | 投诉编号
|  complaintObject | 被投诉方
|  complainant | 投诉人
|  sex | 性别 
|  mobile | 手机号 
|  content | 投诉内容
|  type | 类型code  
|  channel  | 投诉来源 
|  complaintTime | 投诉时间  
|  certificate | 投诉凭证
|  status | 投诉单状态


## 投诉单数字统计详情 ##
### 路径 ###
<pre>http://192.168.10.39:30800/complaintManage/statisticsComplaint </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* complaintManage:statisticsComplaint
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
  "data": {
    "finishCount": 1,
    "notHandleCount": 4,
    "allCount": 5,
    "beingCount": 0,
    "rejectCount": 0
  }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  allCount | 总投诉 
|  finishCount | 已完成
|  beingCount | 受理中
|  rejectCount | 不受理
|  notHandleCount | 未处理


## 投诉单图表统计详情 ##
### 路径 ###
<pre>http://192.168.10.39:30800/complaintManage/chartComplaint </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* complaintManage:chartComplaint
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
  "data": {
    "complaintChannel": [
      {
        "name": "畅游洪雅APP",
        "value": 3
      },
      {
        "name": "公众号",
        "value": 0
      },
      {
        "name": "旅游资讯网",
        "value": 1
      },
      {
        "name": "12301",
        "value": 1
      }
    ],
    "complaintIndustryType": [
      {
        "name": "景区",
        "value": 1
      },
      {
        "name": "住宿",
        "value": 0
      },
      {
        "name": "餐饮",
        "value": 0
      },
      {
        "name": "购物",
        "value": 0
      },
      {
        "name": "娱乐",
        "value": 0
      }
    ],
    "complaintType": [
      {
        "name": "公共服务",
        "value": 0
      },
      {
        "name": "消费服务",
        "value": 3
      },
      {
        "name": "商品质量",
        "value": 1
      },
      {
        "name": "商品价格",
        "value": 1
      },
      {
        "name": "环境卫生",
        "value": 0
      },
      {
        "name": "其他",
        "value": 0
      }
    ]
  }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  complaintChannel | 投诉渠道 
|  complaintIndustryType | 投诉行业
|  complaintType | 投诉分类



## 按条件统计投诉列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/complaintManage/conditionStatisticsComplaint </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* complaintManage:conditionStatisticsComplaint
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | int  | 当前页码  |  Y |
|  size | int  | 每页条数  |  Y |
|  condition.flag | string  | 统计类型，天：day，月：month，年：year  |  Y |
|  condition.startTime | long  | 开始时间  |  Y |
|  condition.endTime | long  | 结束时间  |  Y |
|  condition.industryType | string  |  投诉行业分类 |  N |
|  condition.type | string  | 投诉分类  |  N |
|  condition.channel | string  | 投诉渠道  |  N |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": {
    "records": [
      {
        "count": 1,
        "industryType": null,
        "type": "商品质量",
        "channel": "12301",
        "statisticsTime":"2019-08-19"
      },
      {
        "count": 1,
        "industryType": null,
        "type": "消费服务",
        "channel": "畅游洪雅APP",
        "statisticsTime":"2019-08-19"
      }
    ],
    "total": 5,
    "size": 3,
    "current": 2,
    "orders": [],
    "searchCount": true,
    "pages": 2
  }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  total | 投诉总条数
|  industryType | 投诉行业分类
|  type | 投诉分类 
|  channel | 投诉渠道 
|  count | 投诉数量
|  statisticsTime | 投诉时间



## 导出Excel ##
### 路径 ###
<pre>http://192.168.10.39:30800/complaintManage/export </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* complaintManage:export
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | int  | 当前页码  |  Y |
|  size | int  | 每页条数（导出不分页，直接传-1）  |  Y |
|  condition.flag | string  | 统计类型，天：day，月：month，年：year  |  Y |
|  condition.startTime | long  | 开始时间  |  Y |
|  condition.endTime | long  | 结束时间  |  Y |
|  condition.industryType | string  |  投诉行业分类 |  N |
|  condition.type | string  | 投诉分类  |  N |
|  condition.channel | string  | 投诉渠道  |  N |



## 查询商户列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/complaintManage/queryMerchant?merchantName=巧面馆 </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* complaintManage:queryMerchant
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  merchantName | string  | 商户名  |  Y |
|  size | int  | 列表长度，默认是5  |  N |
### 返回结果 ###
<pre>
{
  "code": "06010000",
  "message": "操作成功",
  "success": true,
  "data": [
    {
      "id": 23,
      "merchantName": "巧面馆444"
    },
    {
      "id": 24,
      "merchantName": "巧面馆430"
    },
    {
      "id": 25,
      "merchantName": "巧面馆405"
    },
    {
      "id": 28,
      "merchantName": "巧面馆694"
    }
  ]
}
</pre>
### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  id | 商户ID
|  merchantName | 商户名