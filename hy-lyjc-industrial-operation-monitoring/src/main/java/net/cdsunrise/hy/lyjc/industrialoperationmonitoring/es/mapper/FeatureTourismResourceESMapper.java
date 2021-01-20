package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.FeatureTourismResourceData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author sh
 * @date 2020-01-16 18:16
 */
public interface FeatureTourismResourceESMapper extends ElasticsearchRepository<FeatureTourismResourceData, String> {
}
