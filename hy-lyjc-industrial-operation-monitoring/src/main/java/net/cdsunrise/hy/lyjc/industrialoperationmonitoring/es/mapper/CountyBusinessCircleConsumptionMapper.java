package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.CountyBusinessCircleConsumption;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LHY
 * @date 2019/11/28 14:04
 */
@Repository
public interface CountyBusinessCircleConsumptionMapper extends ElasticsearchRepository<CountyBusinessCircleConsumption,String> {
}
