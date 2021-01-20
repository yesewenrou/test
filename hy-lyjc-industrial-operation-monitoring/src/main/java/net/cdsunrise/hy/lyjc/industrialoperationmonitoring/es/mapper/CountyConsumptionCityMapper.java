package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.CountyConsumptionCity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LHY
 * @date 2019/11/29 11:09
 */
@Repository
public interface CountyConsumptionCityMapper extends ElasticsearchRepository<CountyConsumptionCity,String>{
}
