# 自动预警
- [待确认预警列表查询](#待确认预警列表查询)
- [待确认预警申请发布](#待确认预警申请发布)
- [待确认预警申请发布/发短信](#待确认预警申请发布)
- [预警忽略](#预警忽略)
- [查看详情](#查看详情)
- [立即发布](#立即发布)
- [选择节目库发布](#选择节目库发布)
- [已通过短信发送的人](#已通过短信发送的人)
- [预警按状态统计](#按状态统计)
- [根据应急事件id查询预警详情](#根据应急事件id查询预警详情)
- [](#)
- [](#)

# 查看状态码说明

```text

    
     * 预警类型 
     
        035001, 客流量预警
        035002, 停车位预警
        035003, 交通拥堵预警

    
     * 处理类型  1 申请发布 2 发送短信
        1 : 申请发布
        2 : 发送短信
        3 : 正式发布
        4 : 忽略申请
        5 : 自动过期


    
     * 预警状态
     
        033001 , "待确认"),
        033002 , "已忽略"),
        033003 , "已申请/待发布"),
        033004 , "已过期"),
        033005 , "已发布");
        033006 ,  忽略申请)                 

```

## 待确认预警列表查询
```
待确认预警和待信息发都用此接口

待信息发布需要传状态为待发布过来
```
### 请求路径
```text
/autoWarning/list
```
### 权限标识
- `autoWarning:list`

### 请求方式
- `POST`

### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------ | :------------ | :------------ 
|  token | 登录成功返回token  | 是 

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---| :--- | :--- | :---
type| string|N|预警类型| [查看状态码说明](#查看状态码说明)
object|string |N |预警对象
status|list<string> |N |状态| [查看状态码说明](#查看状态码说明)
begin|timestamp|Y|
end| timestamp|Y|
page|number|Y|
size|number|Y|
orderBy|array|N| 按什么排序,不传时默认按预警时间倒叙|  传入值可以为: update_time 等等
asc| boolean| N| 是否是升序排列, true:代表升序,  false 代表降序

### 请求示例
```json
{
	"type":"035001",
	"object":"",
	"status":"",
	"begin":1582992256000,
	"end":1583597056000,
	"page":2,
	"size":1
}

```
### 响应示例
```json
{
    "code": "08000000",
    "message": "success",
    "success": true,
    "data": {
        "records": [
            {
                "id": 2,
                "object": "瓦屋山",
                "type": "035001",
                "status": "033001",
                "warningTime": 1583596609000,
                "createTime": 1583596609000,
                "updateTime": 1583596609000
            }
        ],
        "total": 2,
        "size": 1,
        "pages": 2
    }
}
```

## 待确认预警申请发布
- `发短信 也使用此接口`
### 请求路径
```text
/autoWarning/requestPublic
```
### 权限
- `autoWarning:requestPublic`
### 请求方式
- `POST`
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------ | :------------ | :------------ 
|  token | 登录成功返回token  | 是 
### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---| :--- | :--- | :---
id|number|Y|预警id|
content| string|Y|内容
handleType|number|请求类型|  1 申请发布 2 发送短信 | [查看状态码说明](#查看状态码说明)
receivers|list|Y|接收者| 诱导屏或者联系人
&emsp;id| number| Y|诱导屏id或者联系人id
&emsp;name|string|Y|诱导屏名称或者联系人名称
attaches|list| N |附件列表
&emsp;name|string|N|附件名称
&emsp;url|string|N|附件地址
### 请求示例
```text
{
    "id": 1,
    "receivers": [
        {
            "id": 310,
            "name": "方云龙"
        },
        {
            "id": 311,
            "name": "李云龙"
        }
    ],
    "content": "just a test",
    "handleType": 1,
    "attaches": [
        {
            "name": "附件1",
            "url": "111111"
        },
        {
            "name": "附件2",
            "url": "22222"
        }
    ]
}
```
### 返回示例
```text
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": "success"
}
```
## 预警忽略
### 请求地址
```text
/autoWarning/ignore
```

### 权限
- `autoWarning:ignore`

### 请求方式
- `GET`
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------ | :------------ | :------------ 
|  token | 登录成功返回token  | 是 
### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---| :--- | :--- | :---
id|number|Y|预警id|
statusCode|string|Y| 忽略 033002,   忽略申请:033006  |[查看状态码说明](#查看状态码说明)
### 响应示例
```text
{
    "code": "08010000",
    "message": "成功",
    "success": true,
    "data": null
}
```

## 查看详情
### 请求路径
```text
/autoWarning/queryRecordDetail
```
### 权限
- `autoWarning:queryRecordDetail`

### 请求方式
- `GET`
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------ | :------------ | :------------ 
|  token | 登录成功返回token  | 是 
### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---| :--- | :--- | :---
id|number|Y|预警id|

### 查看详情响应参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | object | 返回数据 | 
&emsp; type | String| 预警类型|
&emsp; scenicDetail | object| 景区预警明细
&emsp;&emsp;scenicDetailId|number|景区预警详情id
&emsp;&emsp;scenicName|string|景区名称
&emsp;&emsp;warningTime|timestamp|预警时间
&emsp;&emsp;peopleNum|number|景区人数
&emsp;trafficDetail|object|[交通预警明细](#交通预警明细)
&emsp;parkingCarDetail|object| 停车预警明细
&emsp;record|Object|操作记录
&emsp;&emsp;warningId|number|预警id
&emsp;&emsp;updateTime|timestamp|处理时间|
&emsp;&emsp;handler|string|处理人名称
&emsp;&emsp;handleType|number|用于区分是短信还是诱导屏
&emsp;&emsp;content|string|内容
&emsp;&emsp;programContent|string|信息屏内容
&emsp;&emsp;receiver|String|接收者名字组合。多个会以逗号连接
&emsp;&emsp;programName|string | 节目名称
&emsp;&emsp;messageChannel|String|推送渠道
&emsp;&emsp;attaches|list|附件列表
&emsp;&emsp;&emsp;name|string|附件名称
&emsp;&emsp;&emsp;url|String|附件地址
#### 交通预警明细
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
roadName|string|路段名称
totalMileage|string|总里程
roadDesc|String|路段描述
warningReason|string|预警原因
congestionIndex|string |拥堵指数
averageSpeed|string|平均速度
warningTime|timestamp|预警时间
### 响应示例
```text
{
    "code": "08000000",
    "message": "success",
    "success": true,
    "data": {
        "scenicDetail": {
            "peopleNum": 200
        },
        "trafficDetail": null,
        "parkingCarDetail": null,
        "records": [
            {
                "warningId": 1,
                "updateTime": 1583607271000,
                "handler": "",
                "handleType": 1,
                "content": "just a test",
                "receiver": "方云龙,李云龙",
                "programName": "交通拥堵",
                "messageChannel": "",
                "attaches": [
                    {
                        "name": "附件2",
                        "url": "22222"
                    },
                    {
                        "name": "附件1",
                        "url": "111111"
                    }
                ]
            }
        ]
    }
}
```

## 立即发布
`发布即时信息`
### 路径 ###
<pre>/autoWarning/publish</pre>

### 请求方式 ###
* POST
### 权限标识 ###
* autoWarning:publish

### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------ | :------------ | :------------ 
|  token | 登录成功返回token  | 是 

### 请求参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------ | :------------ | :------------ | :------------ |
|  subject | 信息发布主题 |  string  |   Y |
|  programId | 节目ID |  long  |  N |
|  programName | 节目名称 |  string  |  N |
|  inductionScreens | 诱导屏名称 |   List  | N |
|  pushChannels | 消息推送渠道 |  List  |  N |
|  content | 消息推送内容 |  string  |  N |
### 信息屏inductionScreens参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  id | long  | 诱导屏ID  |  Y |
|  guidanceDisplayName | string  | 诱导屏名称  |  Y |
|  deviceId | string  | 设备ID、序列号  |  Y |
|  mac  | string | mac地址 | Y |
### 信息发布参数校验说明 ###
1. programId、programName、inductionScreens、pushChannels、content不能同时为空；
2. programId、programName、inductionScreens 如果一个不为空，则另外两个不能为空；
3. pushChannels不为空，则content不能为空；

### 请求参数示例 ###
<pre>
{
    "subject": "欢迎**领导视察工作",
    "programId": 1,
    "programName": "欢迎上级检查",
    "inductionScreens": [
        {
            "id": 1,
            "guidanceDisplayName": "PSP-1",
            "deviceId": "PSP10000a",
            "mac": " 02:e2:30:c0:2f:73"
        },
        {
            "id": 2,
            "guidanceDisplayName": "PSP-2",
            "deviceId": "PSP10000b",
            "mac": " 02:e2:30:c0:2f:74"
        }
    ],
    "pushChannels": [
        {
            "channel": "微信公众号"
        },
        {
            "channel": "畅游洪雅APP"
        },
        {
            "channel": "咨询网"
        }
    ],
    "content": "欢迎**领导视察工作，~~~~~~~~~~~~~"
}
</pre>
### 返回结果 ###
<pre>
{
    "code": "04000000",
    "message": "操作成功",
    "success": true,
    "data": null
}
</pre>

## 选择节目库发布
`选择节目库`
### 路径 ###
<pre>/autoWarning/publishProgram</pre>

### 请求方式 ###
* POST
### 权限标识 ###
* autoWarning:publishProgram

### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------ | :------------ | :------------ 
|  token | 登录成功返回token  | 是 

### 请求参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------ | :------------ | :------------ | :------------ |
|  subject | 信息发布主题 |  string  |   Y |
|  programId | 节目ID |  long  |  N |
|  programName | 节目名称 |  string  |  N |
|  inductionScreens | 诱导屏名称 |   List  | N |
|  pushChannels | 消息推送渠道 |  List  |  N |
|  content | 消息推送内容 |  string  |  N |
|  startTime | 消息有效开始时间 |  number  |  N |
|  endTime | 消息有截止时间 |  number  |  N |

### 信息屏inductionScreens参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  id | long  | 诱导屏ID  |  Y |
|  guidanceDisplayName | string  | 诱导屏名称  |  Y |
|  deviceId | string  | 设备ID、序列号  |  Y |
|  mac  | string | mac地址 | Y |
### 信息发布参数校验说明 ###
1. programId、programName、inductionScreens、pushChannels、content不能同时为空；
2. programId、programName、inductionScreens 如果一个不为空，则另外两个不能为空；
3. pushChannels不为空，则content不能为空；

### 请求参数示例 ###
<pre>
{
    "subject": "欢迎**领导视察工作",
    "programId": 1,
    "programName": "欢迎上级检查",
    "inductionScreens": [
        {
            "id": 1,
            "guidanceDisplayName": "PSP-1",
            "deviceId": "PSP10000a",
            "mac": " 02:e2:30:c0:2f:73"
        },
        {
            "id": 2,
            "guidanceDisplayName": "PSP-2",
            "deviceId": "PSP10000b",
            "mac": " 02:e2:30:c0:2f:74"
        }
    ],
    "pushChannels": [
        {
            "channel": "微信公众号"
        },
        {
            "channel": "畅游洪雅APP"
        },
        {
            "channel": "咨询网"
        }
    ],
    "content": "欢迎**领导视察工作，~~~~~~~~~~~~~"
}
</pre>
### 返回结果 ###
<pre>
{
    "code": "04000000",
    "message": "操作成功",
    "success": true,
    "data": null
}
</pre>


## 已通过短信发送的人

### 请求路径
```
/autoWarning/querySentContact
```
### 权限
- `autoWarning:querySentContact`
### 请求方式
- `GET`
### 请求参数说明
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------ | :------------ | :------------ | :------------ |
|  id | 预警ID |  number  |   Y |
### 响应示例
```text
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": "方云龙, 李云龙"
}
```


## 按状态统计
### 请求路径
```
/autoWarning/statistics
```
### 权限
- `autoWarning:statistics`
### 请求方式
- `GET`
### 请求参数说明
- `无`
### 返回参数说明
[查看状态码说明](#查看状态码说明)

### 响应示例
```text
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "count": 7,
            "status": "033003"
        },
        {
            "count": 7,
            "status": "033005"
        },
        {
            "count": 2,
            "status": "033002"
        },
        {
            "count": 2,
            "status": "033006"
        },
        {
            "count": 73,
            "status": "033004"
        },
        {
            "count": 3,
            "status": "033001"
        }
    ]
}
```


## 根据应急事件id查询预警详情
### 请求路径
```
/autoWarning/queryRecordDetailByEventId
```
### 权限
- `autoWarning:queryRecordDetailByEventId`
### 请求方式
- `GET`
### 请求参数说明
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------ | :------------ | :------------ | :------------ |
|  eventId | 应急事件id |  number  |   Y |
### 返回参数说明
-  [同查看详情响应参数说明](#查看详情响应参数说明)


