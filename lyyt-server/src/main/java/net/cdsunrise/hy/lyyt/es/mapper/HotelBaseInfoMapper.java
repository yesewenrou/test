package net.cdsunrise.hy.lyyt.es.mapper;

import net.cdsunrise.hy.lyyt.es.entity.HotelBaseInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LHY
 * @date 2019/10/30 14:29
 */
@Repository
public interface HotelBaseInfoMapper extends ElasticsearchRepository<HotelBaseInfo, String> {
}
