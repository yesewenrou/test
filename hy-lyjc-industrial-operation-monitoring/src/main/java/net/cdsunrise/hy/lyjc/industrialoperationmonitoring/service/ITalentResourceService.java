package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TalentResourceVO;

/**
 * @author lijiafeng
 * @date 2020/1/17 11:15
 */
public interface ITalentResourceService {

    /**
     * 查询人才列表
     *
     * @param page            页数
     * @param size            每页条数
     * @param type            资源类型 数据字典 PUBLIC_RESOURCE_TYPE
     * @param name            名称
     * @param unitAndPosition 单位及职称
     * @return 结果
     */
    Result<PageResult<TalentResourceVO>> listTalentResources(int page, int size, String type, String name, String unitAndPosition);
}
