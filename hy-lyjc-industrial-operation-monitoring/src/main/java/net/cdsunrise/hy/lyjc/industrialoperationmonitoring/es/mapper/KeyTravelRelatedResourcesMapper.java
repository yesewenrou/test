package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.KeyTravelRelatedResources;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lijiafeng
 * @date 2020/3/8 16:22
 */
@Repository
public interface KeyTravelRelatedResourcesMapper extends ElasticsearchRepository<KeyTravelRelatedResources, String> {
}
