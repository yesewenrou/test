package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.TouristSourceCountryData;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LHY
 * @date 2019/10/18 11:42
 */
@Repository
public interface TouristSourceCountryMapper extends ElasticsearchRepository<TouristSourceCountryData, String> {
}
