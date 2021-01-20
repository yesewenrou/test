package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.NewPublicResourceVO;

/**
 * @author lijiafeng
 * @date 2020/1/16 21:00
 */
public interface INewPublicResourceService {

    /**
     * 查询公共资源列表
     *
     * @param page   页数
     * @param size   每页条数
     * @param type   资源类型 数据字典 PUBLIC_RESOURCE_TYPE
     * @param name   名称
     * @param region 区域编码
     * @return 结果
     */
    Result<PageResult<NewPublicResourceVO>> listNewPublicResources(int page, int size, String type, String name, String region);

    /**
     * 查询公共资源详情
     *
     * @param type 资源类型 数据字典 PUBLIC_RESOURCE_TYPE
     * @param id   资源ID
     * @return 结果
     */
    Result<?> getNewPublicResource(String type, Long id);
}
