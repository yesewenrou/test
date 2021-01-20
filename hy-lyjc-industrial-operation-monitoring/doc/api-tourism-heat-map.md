# 游客热力图-地图
## 目录
- [查询县域游客热力数据](#查询县域游客热力数据)
- [查询景区运营状态](#查询景区运营状态)

## 查询县域游客热力数据
### 请求地址
```
/tourist-heat-map/listAll
```

### 权限标识
- `tourist-heat-map:listAll`

### 请求方式
- `GET`

### 请求参数说明
无

### 请求示例
/tourist-heat-map/listAll

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | number | 返回数据 | 
&emsp;id | string | ID | 
&emsp;peopleNum | number | 游客数 | 
&emsp;lng | string | 经度 | 
&emsp;lat | string | 纬度 | 

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "id": "9yz1tW4BSMQ7-Lx8xXkW",
            "peopleNum": 37,
            "lng": "102.9541723",
            "lat": "29.58119666"
        },
        {
            "id": "-Sz1tW4BSMQ7-Lx8xXkW",
            "peopleNum": 10,
            "lng": "102.9569062",
            "lat": "29.66080969"
        },
        {
            "id": "-yz1tW4BSMQ7-Lx8xXkW",
            "peopleNum": 62,
            "lng": "102.9611683",
            "lat": "29.67520859"
        }
    ]
}
```

## 查询景区运营状态
### 请求地址
```
/tourist-heat-map/scenic-status/listAll
```

### 权限标识
- `tourist-heat-map:scenic-status:listAll`

### 请求方式
- `GET`

### 请求参数说明
无

### 请求示例
/tourist-heat-map/scenic-status/listAll

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | number | 返回数据 | 
&emsp;scenicId | string | 景区编码 | 
&emsp;scenicName | string | 景区名称 | 
&emsp;peopleNum | number | 当前游客数 | 
&emsp;overloadCapacity | number | 高峰承载量丶最大承载量 | 
&emsp;scenicStatus | string | 运营状态 | COMFORTABLE - 舒适, LESS_COMFORTABLE - 较舒适, NORMAL- 一般, SATURATED - 饱和运营丶较拥挤, OVERLOAD-超负荷丶拥挤
&emsp;scenicStatusDesc | string | 运营状态描述 | COMFORTABLE - 舒适, LESS_COMFORTABLE - 较舒适, NORMAL- 一般, SATURATED - 饱和运营丶较拥挤, OVERLOAD-超负荷丶拥挤
&emsp;comfortablePercent | number | 舒适度百分比  | 
&emsp;peopleInPark | number | 新增园内游客数字段 |

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": [
        {
            "scenicId": "002001",
            "scenicName": "瓦屋山",
            "peopleNum": 7387,
            "overloadCapacity": 222,
            "scenicStatus": "OVERLOAD",
            "scenicStatusDesc": "超负荷"
        },
        {
            "scenicId": "002002",
            "scenicName": "七里坪",
            "peopleNum": 2146,
            "overloadCapacity": 999,
            "scenicStatus": "OVERLOAD",
            "scenicStatusDesc": "超负荷"
        },
        {
            "scenicId": "002003",
            "scenicName": "柳江古镇",
            "peopleNum": 14552,
            "overloadCapacity": 1000,
            "scenicStatus": "OVERLOAD",
            "scenicStatusDesc": "超负荷"
        },
        {
            "scenicId": "002005",
            "scenicName": "玉屏山",
            "peopleNum": 877,
            "overloadCapacity": 1000,
            "scenicStatus": "SATURATED",
            "scenicStatusDesc": "饱和运营"
        }
    ]
}
```