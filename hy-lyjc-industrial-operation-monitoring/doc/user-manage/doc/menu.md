# 菜单模块 #
## 新增菜单 ##
### 路径 ###
<pre>/um/menu/add </pre>
### 请求方式 ###
* POST
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 参数说明 ###
| 参数名字  | 参数类型  | 参数描述  | 是否必须  |
| :------------: | :------------: | :------------: | :------------: |
|  name | string  | 菜单名  |  Y |
|  path | string  | 路径  |  N |
|  parentId | int  | 父级菜单  |  Y |
|  moduleType | string  | 菜单类型：page，button  |  Y |
|  icon | string  | 图标  |  N |
### 返回结果 ###
<pre>
{
  "code": "01000000",
  "message": "",
  "success": true,
  "data": null
}
</pre>


## 全部菜单树 ##
### 路径 ###
<pre>/um/menu/show </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* um:menu:show
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 返回结果 ###
<pre>
{
  "code": "01000000",
  "message": "success",
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "首页",
      "path": "/",
      "parentId": 0,
      "moduleType": "page",
      "icon": "icon-nav_statistical_n",
      "children": null
    },
    {
      "id": 2,
      "name": "停车热力图系统",
      "path": "/parkingHeatMap",
      "parentId": 0,
      "moduleType": "page",
      "icon": "icon-relitu",
      "children": [
        {
          "id": 3,
          "name": "饱和度分析",
          "path": "/parkingHeatMap/saturationAnalysis",
          "parentId": 2,
          "moduleType": "page",
          "icon": "",
          "children": null
        }
      ]
    },
    {
      "id": 5,
      "name": "停车统计",
      "path": "/parkingStatistics",
      "parentId": 0,
      "moduleType": "page",
      "icon": "icon-tongjituxing",
      "children": [
        {
          "id": 6,
          "name": "实时停车数据",
          "path": "/parkingStatistics/realTimeParkingData",
          "parentId": 5,
          "moduleType": "page",
          "icon": "",
          "children": null
        }
      ]
    },
    {
      "id": 7,
      "name": "资源管理系统",
      "path": "/resourceMag",
      "parentId": 0,
      "moduleType": "page",
      "icon": "icon-ziyuan",
      "children": [
        {
          "id": 9,
          "name": "停车场信息管理",
          "path": "/resourceMag/parkingLotInfo",
          "parentId": 7,
          "moduleType": "page",
          "icon": "",
          "children": [
            {
              "id": 10,
              "name": "新增",
              "path": "",
              "parentId": 9,
              "moduleType": "button",
              "icon": "",
              "children": null
            }
          ]
        }
      ]
    },
    {
      "id": 14,
      "name": "系统设置",
      "path": "/systemSettings",
      "parentId": 0,
      "moduleType": "page",
      "icon": "icon-shezhi",
      "children": [
        {
          "id": 15,
          "name": "角色管理",
          "path": "/systemSettings/roleMag",
          "parentId": 14,
          "moduleType": "page",
          "icon": null,
          "children": null
        }
      ]
    }
  ]
</pre>


## 当前用户菜单树 ##
### 路径 ###
<pre>/um/menu/findByUserId </pre>
### 请求方式 ###
* GET
### 权限标识 ###
* um:menu:findByUserId
### 全局请求头 ###
| 请求头名字  | 值  | 是否必须  
| :------------: | :------------: | :------------: 
|  token | 登录成功返回token  | 是  
### 返回结果 ###
<pre>
{
  "code": "01000000",
  "message": "success",
  "success": true,
  "data": [
    {
      "id": 1,
      "name": "首页",
      "path": "/",
      "parentId": 0,
      "moduleType": "page",
      "icon": "icon-nav_statistical_n",
      "children": null
    },
    {
      "id": 2,
      "name": "停车热力图系统",
      "path": "/parkingHeatMap",
      "parentId": 0,
      "moduleType": "page",
      "icon": "icon-relitu",
      "children": [
        {
          "id": 3,
          "name": "饱和度分析",
          "path": "/parkingHeatMap/saturationAnalysis",
          "parentId": 2,
          "moduleType": "page",
          "icon": "",
          "children": null
        }
      ]
    },
    {
      "id": 5,
      "name": "停车统计",
      "path": "/parkingStatistics",
      "parentId": 0,
      "moduleType": "page",
      "icon": "icon-tongjituxing",
      "children": [
        {
          "id": 6,
          "name": "实时停车数据",
          "path": "/parkingStatistics/realTimeParkingData",
          "parentId": 5,
          "moduleType": "page",
          "icon": "",
          "children": null
        }
      ]
    },
    {
      "id": 7,
      "name": "资源管理系统",
      "path": "/resourceMag",
      "parentId": 0,
      "moduleType": "page",
      "icon": "icon-ziyuan",
      "children": [
        {
          "id": 9,
          "name": "停车场信息管理",
          "path": "/resourceMag/parkingLotInfo",
          "parentId": 7,
          "moduleType": "page",
          "icon": "",
          "children": [
            {
              "id": 10,
              "name": "新增",
              "path": "",
              "parentId": 9,
              "moduleType": "button",
              "icon": "",
              "children": null
            }
          ]
        }
      ]
    },
    {
      "id": 14,
      "name": "系统设置",
      "path": "/systemSettings",
      "parentId": 0,
      "moduleType": "page",
      "icon": "icon-shezhi",
      "children": [
        {
          "id": 15,
          "name": "角色管理",
          "path": "/systemSettings/roleMag",
          "parentId": 14,
          "moduleType": "page",
          "icon": null,
          "children": null
        }
      ]
    }
  ]
</pre>


