package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.PublicResource;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ResourceCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ResourceVO;

/**
 * @author LHY
 */
public interface PublicResourceService {

    /**
     * 新增
     */
    Long add(ResourceVO resourceVO);

    /**
     * 编辑
     */
    void update(ResourceVO resourceVO);

    /**
     * 删除
     */
    void delete(Long id);

    /**
     * 根据主键查询
     */
    PublicResource findById(Long id);

    /**
     * 搜索条件分页
     */
    Page<PublicResource> list(PageRequest<ResourceCondition> pageRequest);

}
