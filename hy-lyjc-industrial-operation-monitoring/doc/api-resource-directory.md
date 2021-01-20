# 旅游资源目录
## 目录
- [涉旅资源目录](#涉旅资源目录)
- [公共资源目录](#公共资源目录)
- [旅游从业人才](#旅游从业人才)

## 涉旅资源目录
### 请求地址
```
/resource-directory/travelRelatedResources
```

### 权限标识
- `resource-directory:travelRelatedResources`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
page | number | false | 当前页 | 默认1
size | number | false | 每页条数 | 默认10
type | string | true | 资源类型编码 | 数据字典 NEW_TOURISM_RESOURCE_TYPE
name | string | false | 资源名称 | 模糊匹配
region | string | false | 区域编码 | 数据字典 REGION

### 请求示例
```
/resource-directory/travelRelatedResources?page=1&size=10&type=029001
```

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | object | 返回数据 | 
&emsp;totalCount | number | 总条数 | 
&emsp;totalPages | number | 总页数 | 
&emsp;pageNum | number | 当前页数 | 
&emsp;currentSize | number | 当前页条数 | 
&emsp;list | array | 查询结果 | 
&emsp;&emsp;id | number | ID | 
&emsp;&emsp;name | string | 资源名称 | 
&emsp;&emsp;region | string | 所属区域名称 | 
&emsp;&emsp;type | string | 资源类型 | 
&emsp;&emsp;address | string | 地址 | 
&emsp;&emsp;phoneNumber | string | 联系电话 | 

### 返回参数示例
```json
{
    "code": "00000000",
    "message": "处理成功",
    "success": true,
    "data": {
        "totalCount": 17,
        "totalPages": 17,
        "currentSize": 1,
        "pageNum": 1,
        "list": [
            {
                "id": 1,
                "name": "柳江古镇景区",
                "region": "柳江镇",
                "type": "精品景区",
                "address": "眉山市洪雅县明月南街",
                "phoneNumber": ""
            }
        ]
    }
}
```

## 公共资源目录
### 请求地址
```
/resource-directory/publicResources
```

### 权限标识
- `resource-directory:publicResources`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
page | number | false | 当前页 | 默认1
size | number | false | 每页条数 | 默认10
type | string | true | 资源类型编码 | 数据字典 PUBLIC_RESOURCE_TYPE
name | string | false | 资源名称 | 模糊匹配
region | string | false | 区域编码 | 数据字典 REGION

### 请求示例
```
/resource-directory/publicResources?page=1&size=10&type=009001
```

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | object | 返回数据 | 
&emsp;totalCount | number | 总条数 | 
&emsp;totalPages | number | 总页数 | 
&emsp;pageNum | number | 当前页数 | 
&emsp;currentSize | number | 当前页条数 | 
&emsp;list | array | 查询结果 | 
&emsp;&emsp;id | number | ID | 
&emsp;&emsp;name | string | 资源名称 | 
&emsp;&emsp;region | string | 所属区域名称 | 
&emsp;&emsp;type | string | 资源类型 | 
&emsp;&emsp;address | string | 地址 | 
&emsp;&emsp;phoneNumber | string | 联系电话 | 

### 返回参数示例
```json
{
    "code": "00000000",
    "message": "处理成功",
    "success": true,
    "data": {
        "totalCount": 171,
        "totalPages": 171,
        "currentSize": 1,
        "pageNum": 1,
        "list": [
            {
                "id": 171,
                "name": "123",
                "region": "洪川镇",
                "type": "药房药店",
                "address": "123123",
                "phoneNumber": "123123"
            }
        ]
    }
}
```

## 旅游从业人才
### 请求地址
```
/resource-directory/talentResources
```

### 权限标识
- `resource-directory:talentResources`

### 请求方式
- `GET`

### 请求参数说明
名称 | 类型 | 必填 | 描述 | 备注
:--- | :---: | :---: | :--- | :---
page | number | false | 当前页 | 默认1
size | number | false | 每页条数 | 默认10
type | string | true | 资源类型编码 | 数据字典 TALENT_TYPE
name | string | false | 资源名称 | 模糊匹配
unitAndPosition | string | false | 单位及职务 | 模糊匹配

### 请求示例
```
/resource-directory/talentResources?page=1&size=10&type=029001
```

### 返回参数说明
名称 | 类型 | 描述 | 备注
:--- | :---: | :--- | :---
code | string | 返回码 | 
message | string | 返回描述 | 
success | boolean | 是否成功 | 用于前端直接判断请求是否成功
data | object | 返回数据 | 
&emsp;totalCount | number | 总条数 | 
&emsp;totalPages | number | 总页数 | 
&emsp;pageNum | number | 当前页数 | 
&emsp;currentSize | number | 当前页条数 | 
&emsp;list | array | 查询结果 | 
&emsp;&emsp;id | number | ID | 
&emsp;&emsp;name | string | 资源名称 | 
&emsp;&emsp;type | string | 资源类型 | 
&emsp;&emsp;sex | string | 性别 | 
&emsp;&emsp;education | string | 学历 | 
&emsp;&emsp;phoneNumber | string | 联系电话 | 
&emsp;&emsp;unitAndPosition | string | 单位及职务 | 

### 返回参数示例
```json
{
    "code": "00000000",
    "message": "处理成功",
    "success": true,
    "data": {
        "totalCount": 31,
        "totalPages": 31,
        "currentSize": 1,
        "pageNum": 1,
        "list": [
            {
                "id": 37,
                "name": "_(:з」∠)_",
                "type": "中英文讲解员",
                "sex": null,
                "education": "专科",
                "phoneNumber": "",
                "unitAndPosition": "参数"
            }
        ]
    }
}
```