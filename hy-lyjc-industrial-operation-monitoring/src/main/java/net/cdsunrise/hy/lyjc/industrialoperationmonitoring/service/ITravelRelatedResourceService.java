package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TravelRelatedResourceVO;

/**
 * @author lijiafeng
 * @date 2020/1/16 11:37
 */
public interface ITravelRelatedResourceService {

    /**
     * 查询涉旅资源列表
     *
     * @param page   页数
     * @param size   每页条数
     * @param type   资源类型 数据字典NEW_TOURISM_RESOURCE_TYPE
     * @param name   名称
     * @param region 区域编码
     * @return 结果
     */
    Result<PageResult<TravelRelatedResourceVO>> listTravelRelatedResources(int page, int size, String type, String name, String region);

    /**
     * 查询涉旅资源详情
     *
     * @param type 资源类型 数据字典NEW_TOURISM_RESOURCE_TYPE
     * @param id   资源ID
     * @return 结果
     */
    Result<?> getTravelRelatedResource(String type, Long id);
}
