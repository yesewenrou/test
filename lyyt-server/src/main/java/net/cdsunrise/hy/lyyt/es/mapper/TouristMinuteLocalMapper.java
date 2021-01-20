package net.cdsunrise.hy.lyyt.es.mapper;

import net.cdsunrise.hy.lyyt.es.entity.TouristMinuteLocalData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LHY
 * @date 2019/10/18 10:25
 */
@Repository
public interface TouristMinuteLocalMapper extends ElasticsearchRepository<TouristMinuteLocalData, String> {
}
