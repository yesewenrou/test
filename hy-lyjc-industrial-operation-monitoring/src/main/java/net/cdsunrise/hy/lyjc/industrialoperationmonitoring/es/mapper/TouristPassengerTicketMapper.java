package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.TouristPassengerTicket;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author LHY
 * @date 2020/1/2 16:35
 */
@Repository
public interface TouristPassengerTicketMapper extends ElasticsearchRepository<TouristPassengerTicket, String> {

    /**
     * query
     * @param scenicName scenicName
     * @param pageable pageable
     * @return TouristPassengerTicket
     */
    List<TouristPassengerTicket> findByScenicNameOrderByResponseTimeDesc(String scenicName, Pageable pageable);



}
