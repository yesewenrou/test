package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.CountyConsumption;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author LHY
 * @date 2019/11/28 11:41
 */
@Repository
public interface CountyConsumptionMapper extends ElasticsearchRepository<CountyConsumption,String> {
}
