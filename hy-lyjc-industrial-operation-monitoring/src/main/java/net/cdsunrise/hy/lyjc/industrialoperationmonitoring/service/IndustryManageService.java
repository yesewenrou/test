package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.IndustryManage;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.IndustryManageVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ResourceCondition;

/**
 * @author LHY
 */
public interface IndustryManageService {

    /**
     * 新增
     */
    Long add(IndustryManageVO industryManageVO);

    /**
     * 编辑
     */
    void update(IndustryManageVO industryManageVO);

    /**
     * 删除
     */
    void delete(Long id);

    /**
     * 根据主键查询
     */
    IndustryManage findById(Long id);

    /**
     * 搜索条件分页
     */
    Page<IndustryManage> list(PageRequest<ResourceCondition> pageRequest);

}
