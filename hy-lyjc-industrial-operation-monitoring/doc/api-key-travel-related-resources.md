# 重点涉旅资源
## 目录
- [查询重点涉旅资源列表](#查询重点涉旅资源列表)

## 查询重点涉旅资源列表
### 请求地址
```
/key-travel-related-resources/page
```

### 权限标识
- `key-travel-related-resources:page`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
page | number | true | 当前页 | 
size | number | true | 每页条数 | 
name | string | false | 名称 | 
mainType | string | false | 资源类型 | 数据字典 KEY_TRAVEL_RELATED_RESOURCES

### 请求示例
/key-travel-related-resources/page?page=1&size=10&name=曲沿&mainType=032001

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | object | 返回数据 | 
&emsp;totalCount | number | 总条数 | 
&emsp;totalPages | number | 总页数 | 
&emsp;currentSize | number | 当前条数 | 
&emsp;pageNum | number | 当前页数 | 
&emsp;list | array | 列表 | 
&emsp;&emsp;id | string | 主键 | 
&emsp;&emsp;name | string | 名称 | 
&emsp;&emsp;mainType | string | 资源类型 | 
&emsp;&emsp;mainTypeName | string | 资源类型名称 | 
&emsp;&emsp;subType | string | 资源细类 | 
&emsp;&emsp;region | string | 区域编码 | 
&emsp;&emsp;regionName | string | 区域名称 | 
&emsp;&emsp;starLevel | string | 星级 | 
&emsp;&emsp;department | string | 评定部门 | 
&emsp;&emsp;address | string | 地址 | 
&emsp;&emsp;principal | string | 负责人 | 
&emsp;&emsp;phone | string | 联系电话 | 
&emsp;&emsp;symbol | string | 评定时间或文号 | 
&emsp;&emsp;link | string | 关联对象 游客范围或酒店公安编码 | 

### 返回参数示例
```json
{
    "code": "08010000",
    "message": "操作成功",
    "success": true,
    "data": {
        "totalCount": 1,
        "totalPages": 1,
        "currentSize": 10,
        "pageNum": 0,
        "list": [
            {
                "id": "zm4xuXABSMQ7-Lx8vi10",
                "name": "洪雅县曲沿村生态家园",
                "mainType": "032001",
                "mainTypeName": "精品景区",
                "subType": "国家农业旅游示范点",
                "region": "001001",
                "regionName": "洪川镇",
                "starLevel": "",
                "department": "国家旅游局",
                "address": "眉山市洪雅县一零六省道",
                "principal": "",
                "phone": "",
                "symbol": "2005.11.22",
                "link": ""
            }
        ]
    }
}
```