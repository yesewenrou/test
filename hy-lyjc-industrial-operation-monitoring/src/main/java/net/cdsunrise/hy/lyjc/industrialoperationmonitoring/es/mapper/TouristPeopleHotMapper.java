package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.TouristPeopleHotData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author lijiafeng
 * @date 2019/11/29 10:58
 */
@Repository
public interface TouristPeopleHotMapper extends ElasticsearchRepository<TouristPeopleHotData, String> {
}
