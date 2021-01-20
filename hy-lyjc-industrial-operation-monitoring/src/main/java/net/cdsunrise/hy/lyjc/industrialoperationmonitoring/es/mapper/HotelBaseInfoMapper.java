package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.HotelBaseInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LHY
 * @date 2019/11/13 17:25
 */
@Repository
public interface HotelBaseInfoMapper extends ElasticsearchRepository<HotelBaseInfo,String> {
}
