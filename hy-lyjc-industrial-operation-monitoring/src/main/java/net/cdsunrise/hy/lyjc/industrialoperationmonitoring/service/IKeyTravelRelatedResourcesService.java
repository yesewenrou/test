package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.KeyTravelRelatedResourcesCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.KeyTravelRelatedResourcesVO;

/**
 * @author lijiafeng
 * @date 2020/3/8 16:23
 */
public interface IKeyTravelRelatedResourcesService {

    /**
     * 分页查询重点涉旅资源
     *
     * @param condition 条件
     * @return 结果
     */
    PageResult<KeyTravelRelatedResourcesVO> page(KeyTravelRelatedResourcesCondition condition);
}
