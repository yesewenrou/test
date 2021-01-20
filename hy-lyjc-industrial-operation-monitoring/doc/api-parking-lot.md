# 停车场车位信息
## 目录
 - [停车位状态监测](#停车位状态监测)

## 停车位状态监测
### 请求地址
```
/parking-lot/parking-space/status
```

### 权限标识
- `parking-lot:parking-space:status`

### 请求方式
- `GET`

### 请求参数说明
无

### 请求示例
/parking-lot/parking-space/status

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | array | 返回数据 | 
&emsp;parkingSpaceNumber | number | 车位总数 | 
&emsp;inUseNumber | number | 已停 | 
&emsp;remainingNumber | number | 剩余 | 
&emsp;inUseDegree | number | 已停占比 | 
&emsp;fullNumber | number | 爆满停车场数 | 
&emsp;summaryParkingSpaceRealtimeList | array | 景区停车场状态 | 
&emsp;&emsp;summaryCode | string | 景区编码 | 
&emsp;&emsp;summary | string | 景区名称 | 
&emsp;&emsp;parkingSpaceNumber | number | 车位总数 | 
&emsp;&emsp;inUseNumber | number | 已停 | 
&emsp;&emsp;remainingNumber | number | 剩余 | 
&emsp;&emsp;inUseDegree | number | 已停占比 | 
&emsp;&emsp;saturationLevelCode | string | 饱和度编码 | 
&emsp;&emsp;saturationLevel | string | 饱和度描述 | 

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "parkingSpaceNumber": 1470,
        "inUseNumber": 591,
        "remainingNumber": 879,
        "inUseDegree": 0.4,
        "fullNumber": 1,
        "summaryParkingSpaceRealtimeList": [
            {
                "summaryCode": "002001",
                "summary": "瓦屋山",
                "parkingLotNumber": 1,
                "parkingSpaceNumber": 100,
                "inUseNumber": 100,
                "remainingNumber": 0,
                "inUseDegree": 1.0,
                "saturationLevelCode": "003",
                "saturationLevel": "无余位"
            },
            {
                "summaryCode": "002002",
                "summary": "七里坪",
                "parkingLotNumber": 4,
                "parkingSpaceNumber": 470,
                "inUseNumber": 141,
                "remainingNumber": 329,
                "inUseDegree": 0.2999,
                "saturationLevelCode": "000",
                "saturationLevel": "充足"
            },
            {
                "summaryCode": "002003",
                "summary": "柳江古镇",
                "parkingLotNumber": 1,
                "parkingSpaceNumber": 200,
                "inUseNumber": 80,
                "remainingNumber": 120,
                "inUseDegree": 0.4,
                "saturationLevelCode": "001",
                "saturationLevel": "饱和"
            },
            {
                "summaryCode": "002004",
                "summary": "槽渔滩",
                "parkingLotNumber": 2,
                "parkingSpaceNumber": 400,
                "inUseNumber": 260,
                "remainingNumber": 140,
                "inUseDegree": 0.65,
                "saturationLevelCode": "001",
                "saturationLevel": "饱和"
            },
            {
                "summaryCode": "002005",
                "summary": "玉屏山",
                "parkingLotNumber": 1,
                "parkingSpaceNumber": 300,
                "inUseNumber": 10,
                "remainingNumber": 290,
                "inUseDegree": 0.0333,
                "saturationLevelCode": "000",
                "saturationLevel": "充足"
            }
        ]
    }
}
```