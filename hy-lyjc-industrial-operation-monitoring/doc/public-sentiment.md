# 舆情模块 #
## 舆情列表 ##
### 路径 ###
<pre>http://192.168.10.39:30800/publicSentiment/list </pre>
### 请求方式 ###
* POST
### 权限标识 ###
* publicSentiment:list
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  current | int  | 当前页码  |  Y |
|  size | int  | 每页条数  |  Y |
|  condition.title | string  | 标题  |  N |
|  condition.sentiment | int  | 舆情分类，1：正面，0：中性，-1：负面  |  N |
### 返回结果 ###
<pre>
{
  "code": "08010000",
  "message": "操作成功",
  "success": true,
  "data": {
    "totalCount": 55,
    "totalPages": 6,
    "currentSize": 10,
    "pageNum": 1,
    "list": [
      {
        "title": "【2019国庆假期洪雅旅游微博实时播报】国庆打卡槽渔滩",
        "author": "洪雅旅游",
        "sentiment": 1,
        "date": "10月03日 13:12",
        "content": "【2019国庆假期洪雅旅游微博实时播报】国庆打卡槽渔滩国旗同框庆华诞！​​​2眉山·洪雅县​",
        "addDate": "2019-10-05",
        "origin": "OPPO R9s",
        "url": "https://weibo.com/5708464307/I9M3cpoPV?refer_flag=1001030103_"
      }
    ]
  }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  totalCount | 总条数
|  totalPages | 总页数
|  pageNum | 当前页码
|  title | 标题 
|  author | 作者 
|  sentiment | 分类
|  content | 舆情详情  
|  addDate  | 采集时间 
|  origin | 文章来源 
|  url | 微博跳转链接 


## 最近7天舆情分析 ##
### 路径 ###
<pre>http://192.168.10.39:30800/publicSentiment/sevenPublicSentimentTrend </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* publicSentiment:sevenPublicSentimentTrend
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
        "sentimentAnalyze": [
            {
                "name": "正面",
                "value": 59
            },
            {
                "name": "中性",
                "value": 0
            },
            {
                "name": "负面",
                "value": 3
            }
        ],
        "trend": [
            {
                "name": "2019-11-13",
                "value": 0
            },
            {
                "name": "2019-11-14",
                "value": 0
            },
            {
                "name": "2019-11-15",
                "value": 0
            },
            {
                "name": "2019-11-16",
                "value": 0
            },
            {
                "name": "2019-11-17",
                "value": 0
            },
            {
                "name": "2019-11-18",
                "value": 62
            },
            {
                "name": "2019-11-19",
                "value": 0
            }
        ],
        "keyword": [
            {
                "name": "山",
                "value": 12
            },
            {
                "name": "4a",
                "value": 11
            },
            {
                "name": "景区",
                "value": 11
            },
            {
                "name": "十大",
                "value": 10
            },
            {
                "name": "南溪",
                "value": 10
            },
            {
                "name": "名山",
                "value": 10
            },
            {
                "name": "桂林",
                "value": 10
            },
            {
                "name": "桂林市",
                "value": 10
            },
            {
                "name": "级",
                "value": 10
            },
            {
                "name": "古镇",
                "value": 9
            }
        ]
    }
}
</pre>

### 返回参数说明 ###
| 参数名字 | 参数描述  
| :------------: | :------------: 
|  trend | 舆情趋势
|  sentimentAnalyze | 情感分析
|  keyword | 关键词



## 最近30天舆情分析 ##
### 路径 ###
<pre>http://192.168.10.39:30800/publicSentiment/thirtyPublicSentimentTrend </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* publicSentiment:thirtyPublicSentimentTrend
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 其余和7天参数一致 ###