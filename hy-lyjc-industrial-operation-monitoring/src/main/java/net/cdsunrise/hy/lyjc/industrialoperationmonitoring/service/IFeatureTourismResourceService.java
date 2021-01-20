package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.FeatureTourismResource;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.FeatureTourismResourceCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.FeatureTourismResourceUpdateVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.FeatureTourismResourceVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;

/**
 * @author Binke Zhang
 * @date 2019/9/30 13:44
 */
public interface IFeatureTourismResourceService extends IService<FeatureTourismResource> {
    /**
     * 保存
     *
     * @param vo
     * @return
     */
    boolean save(FeatureTourismResourceVO vo);

    /**
     * 更新
     *
     * @param vo
     * @return
     */
    boolean update(FeatureTourismResourceUpdateVO vo);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    boolean delete(Long id);

    /**
     * 列表
     *
     * @param pageRequest
     * @return
     */
    IPage<FeatureTourismResource> list(PageRequest<FeatureTourismResourceCondition> pageRequest);

    /**
     * 列表
     *
     * @param pageRequest
     * @return
     */
    IPage<FeatureTourismResource> page(PageRequest<FeatureTourismResourceCondition> pageRequest);
}
