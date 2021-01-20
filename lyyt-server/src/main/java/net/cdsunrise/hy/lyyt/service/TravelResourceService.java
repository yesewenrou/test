package net.cdsunrise.hy.lyyt.service;

import net.cdsunrise.hy.lyyt.entity.resp.TravelResourceResponse;


/**
 * 涉旅资源服务接口
 * @author LiuYin 2020/2/5
 */
public interface TravelResourceService {


    /**
     * 获取不同涉旅资源分类的数量统计
     * @return response
     */
    TravelResourceResponse getResourceTypeCount();


    /**
     * 涉旅行业数据分析
     * @return response
     */
    TravelResourceResponse getIndustryAnalysis();

}
