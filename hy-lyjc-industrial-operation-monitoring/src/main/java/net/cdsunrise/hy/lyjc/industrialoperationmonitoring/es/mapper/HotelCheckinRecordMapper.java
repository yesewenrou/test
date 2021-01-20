package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.HotelCheckinRecord;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LHY
 * @date 2019/10/30 16:14
 */
@Repository
public interface HotelCheckinRecordMapper extends ElasticsearchRepository<HotelCheckinRecord, String> {
}
