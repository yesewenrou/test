package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.CountyIndustryConsumption;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LHY
 * @date 2019/12/2 9:53
 */
@Repository
public interface CountyIndustryConsumptionMapper extends ElasticsearchRepository<CountyIndustryConsumption,String>{
}
