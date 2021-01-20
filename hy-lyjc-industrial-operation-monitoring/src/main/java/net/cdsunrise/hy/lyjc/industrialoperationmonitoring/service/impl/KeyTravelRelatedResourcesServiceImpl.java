package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.DataDictionaryVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.KeyTravelRelatedResources;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper.KeyTravelRelatedResourcesMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IKeyTravelRelatedResourcesService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.PageUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.KeyTravelRelatedResourcesCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.KeyTravelRelatedResourcesVO;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lijiafeng
 * @date 2020/3/8 16:38
 */
@Service
public class KeyTravelRelatedResourcesServiceImpl implements IKeyTravelRelatedResourcesService {

    private KeyTravelRelatedResourcesMapper keyTravelRelatedResourcesMapper;
    private DataDictionaryFeignClient dataDictionaryFeignClient;

    public KeyTravelRelatedResourcesServiceImpl(KeyTravelRelatedResourcesMapper keyTravelRelatedResourcesMapper, DataDictionaryFeignClient dataDictionaryFeignClient) {
        this.keyTravelRelatedResourcesMapper = keyTravelRelatedResourcesMapper;
        this.dataDictionaryFeignClient = dataDictionaryFeignClient;
    }

    @Override
    public PageResult<KeyTravelRelatedResourcesVO> page(KeyTravelRelatedResourcesCondition condition) {
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        if (StringUtils.hasText(condition.getName())) {
            queryBuilder.must(QueryBuilders.regexpQuery("name", ".*" + condition.getName() + ".*"));
        }
        if (StringUtils.hasText(condition.getMainType())) {
            queryBuilder.must(QueryBuilders.termsQuery("mainType", condition.getMainType()));
        }
        Page<KeyTravelRelatedResources> page = keyTravelRelatedResourcesMapper.search(queryBuilder, PageRequest.of(condition.getPage() - 1, condition.getSize()));

        Map<String, String> dataDictionaryMap = getDataDictionaryMap();
        return PageUtil.convertPage(page, keyTravelRelatedResources -> {
            KeyTravelRelatedResourcesVO keyTravelRelatedResourcesVO = new KeyTravelRelatedResourcesVO();
            BeanUtils.copyProperties(keyTravelRelatedResources, keyTravelRelatedResourcesVO);
            keyTravelRelatedResourcesVO.setMainTypeName(dataDictionaryMap.get(keyTravelRelatedResources.getMainType()));
            keyTravelRelatedResourcesVO.setRegionName(dataDictionaryMap.get(keyTravelRelatedResources.getRegion()));

            return keyTravelRelatedResourcesVO;
        });
    }

    private Map<String, String> getDataDictionaryMap() {
        String[] codes = new String[]{"REGION", "KEY_TRAVEL_RELATED_RESOURCES"};
        Result<Map<String, DataDictionaryVO>> res = dataDictionaryFeignClient.getByCodes(codes);
        Map<String, String> dataMap = new HashMap<>(16);
        Map<String, DataDictionaryVO> data = res.getData();
        for (DataDictionaryVO dataDictionaryVO : data.values()) {
            Map<String, String> map = dataDictionaryVO.getChildren().stream().collect(Collectors.toMap(DataDictionaryVO::getCode, DataDictionaryVO::getName));
            dataMap.putAll(map);
        }

        return dataMap;
    }
}
