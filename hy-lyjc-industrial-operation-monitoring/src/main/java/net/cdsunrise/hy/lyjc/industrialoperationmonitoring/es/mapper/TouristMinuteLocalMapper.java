package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.TouristMinuteLocalData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LHY
 * @date 2019/10/18 10:25
 */
@Repository
public interface TouristMinuteLocalMapper extends ElasticsearchRepository<TouristMinuteLocalData, String> {
}
