package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.FeatureLabel;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.OperationEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.FeatureLabelMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IFeatureLabelService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.FeatureLabelUpdateVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.FeatureLabelVO;
import net.cdsunrise.hy.record.starter.service.RecordService;
import net.cdsunrise.hy.sso.starter.common.CustomContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Binke Zhang
 * @date 2019/11/13 16:14
 */
@Service
@Slf4j
public class FeatureLabelServiceImpl extends ServiceImpl<FeatureLabelMapper, FeatureLabel> implements IFeatureLabelService {

    private final FeatureLabelMapper featureLabelMapper;
    private final RecordService recordService;

    @Autowired
    public FeatureLabelServiceImpl(FeatureLabelMapper featureLabelMapper, RecordService recordService) {
        this.featureLabelMapper = featureLabelMapper;
        this.recordService = recordService;
    }

    @Override
    public boolean save(FeatureLabelVO vo) {
        FeatureLabel featureLabel = new FeatureLabel();
        BeanUtils.copyProperties(vo,featureLabel);
        boolean success = featureLabelMapper.insert(featureLabel) > 0;
        if(success){
            try {
                recordService.insert(OperationEnum.FEATURE_LABEL,featureLabel, CustomContext.getTokenInfo().getUser().getId());
            } catch (Exception e) {}
        }
        return success;
    }

    @Override
    public boolean update(FeatureLabelUpdateVO vo) {
        FeatureLabel old = featureLabelMapper.selectById(vo.getId());
        FeatureLabel present = new FeatureLabel();
        BeanUtils.copyProperties(old,present);
        BeanUtils.copyProperties(vo,present);
        boolean success = featureLabelMapper.updateById(present) > 0;
        if(success){
            try {
                recordService.update(OperationEnum.FEATURE_LABEL,old,present, CustomContext.getTokenInfo().getUser().getId());
            } catch (Exception e) {}
        }
        return success;
    }

    @Override
    public boolean delete(Long id) {
        FeatureLabel old = featureLabelMapper.selectById(id);
        boolean success = featureLabelMapper.deleteById(id) > 0;
        if(success){
            try {
                recordService.delete(OperationEnum.FEATURE_LABEL,old, CustomContext.getTokenInfo().getUser().getId());
            } catch (Exception e) {}
        }
        return success;
    }

    @Override
    public String getLabelNames(List<Long> ids){
        List<FeatureLabel> labels = featureLabelMapper.selectBatchIds(ids);
        return labels.stream().map(e -> e.getLabelName()).collect(Collectors.joining(","));
    }
}
