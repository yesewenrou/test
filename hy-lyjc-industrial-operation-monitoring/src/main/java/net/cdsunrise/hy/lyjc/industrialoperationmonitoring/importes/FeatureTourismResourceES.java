package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.importes;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.FeatureTourismResource;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.FeatureTourismResourceData;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper.FeatureTourismResourceESMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.FeatureTourismResourceMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sh
 * @date 2020-01-16 18:08
 */
@Slf4j
@Component
public class FeatureTourismResourceES {
    private final FeatureTourismResourceMapper featureTourismResourceMapper;
    private final FeatureTourismResourceESMapper featureTourismResourceESMapper;

    @Autowired
    public FeatureTourismResourceES(FeatureTourismResourceMapper featureTourismResourceMapper, FeatureTourismResourceESMapper featureTourismResourceESMapper) {
        this.featureTourismResourceMapper = featureTourismResourceMapper;
        this.featureTourismResourceESMapper = featureTourismResourceESMapper;
    }

//    @PostConstruct
    public void importFeatureTourism() {
        log.info("[FeatureTourismResourceES] start import feature tourism resource data...");
        List<FeatureTourismResource> resources = featureTourismResourceMapper.selectList(null);
        if (resources != null && resources.size() > 0) {
            List<FeatureTourismResourceData> resourceData = resources.stream().map(r -> {
                FeatureTourismResourceData data = new FeatureTourismResourceData();
                BeanUtils.copyProperties(r, data);
                data.setId(r.getId().toString());
                data.setUpdateTime(DateUtil.format(r.getUpdateTime(), DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS));
                return data;
            }).collect(Collectors.toList());
            featureTourismResourceESMapper.saveAll(resourceData);
        }
        log.info("[FeatureTourismResourceES] end import feature tourism resource data...");
    }
}
