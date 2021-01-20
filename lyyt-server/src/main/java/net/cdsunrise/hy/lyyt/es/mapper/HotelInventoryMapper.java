package net.cdsunrise.hy.lyyt.es.mapper;

import net.cdsunrise.hy.lyyt.es.entity.HotelInventory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LHY
 * @date 2020/2/7 10:23
 */
@Repository
public interface HotelInventoryMapper extends ElasticsearchRepository<HotelInventory, String> {
}
