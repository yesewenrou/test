package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.HotelTouristSource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LHY
 * @date 2019/11/19 14:06
 */
@Repository
public interface HotelTouristSourceMapper extends ElasticsearchRepository<HotelTouristSource, String> {
}
