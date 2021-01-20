package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.HotelPassengerReception;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LHY
 * @date 2019/11/18 17:38
 */
@Repository
public interface HotelPassengerReceptionMapper extends ElasticsearchRepository<HotelPassengerReception, String> {
}
