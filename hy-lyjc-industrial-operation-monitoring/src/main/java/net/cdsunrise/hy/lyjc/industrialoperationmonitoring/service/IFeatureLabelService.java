package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.FeatureLabel;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.FeatureLabelUpdateVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.FeatureLabelVO;

import java.util.List;

/**
 * @author Binke Zhang
 * @date 2019/9/30 13:44
 */
public interface IFeatureLabelService extends IService<FeatureLabel> {
    /**
     * 保存
     * @param vo
     * @return
     */
    boolean save(FeatureLabelVO vo);
    /**
     * 更新
     * @param vo
     * @return
     */
    boolean update(FeatureLabelUpdateVO vo);

    /**
     * 删除
     * @param id
     * @return
     */
    boolean delete(Long id);

    /**
     * 获取标签名称
     * @param ids
     * @return
     */
    String getLabelNames(List<Long> ids);
}
