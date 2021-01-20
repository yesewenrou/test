package net.cdsunrise.hy.lyyt.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import net.cdsunrise.common.utility.utils.JsonUtils;
import net.cdsunrise.hy.lyyt.consts.RedisKeyConsts;
import net.cdsunrise.hy.lyyt.domain.vo.ResourceCountVO;
import net.cdsunrise.hy.lyyt.entity.resp.MerchantTypeCountResponse;
import net.cdsunrise.hy.lyyt.entity.resp.TravelResourceResponse;
import net.cdsunrise.hy.lyyt.entity.vo.KeyCountVO;
import net.cdsunrise.hy.lyyt.enums.DataSubTypeEnum;
import net.cdsunrise.hy.lyyt.service.TravelResourceService;
import net.cdsunrise.hy.lyyt.utils.ConvertUtil;
import net.cdsunrise.hy.lyyt.utils.DataSubTypeUtil;
import net.cdsunrise.hy.lyyt.utils.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 涉旅资源分类统计服务实现
 * @author LiuYin 2020/2/5
 */
@Service
public class TravelResourceServiceImpl implements TravelResourceService {

    /**
     * 基础数据需要的分类
     */
    private static final Set<String> BASE_TYPE_FIELD =
            Stream.of(DataSubTypeEnum.PHARMACY,DataSubTypeEnum.TOURIST_TOILET,DataSubTypeEnum.TRAVEL_AGENCY,DataSubTypeEnum.GAS_STATION)
                    .map(DataSubTypeEnum::getType)
                    .collect(Collectors.toSet());

    /**
     * 行业分析需要的分类
     */
    private static final Set<String> INDUSTRY_TYPE_FIELD =
            Stream.of(DataSubTypeEnum.BOUTIQUE_SCENIC,DataSubTypeEnum.PHARMACY,DataSubTypeEnum.TOURIST_TOILET)
            .map(DataSubTypeEnum::getType)
            .collect(Collectors.toSet());;

    /**
     * 获取不同涉旅资源分类的数量统计
     *
     * @return response
     */
    @Override
    public TravelResourceResponse getResourceTypeCount(){
        return getCountByFilter(BASE_TYPE_FIELD::contains);
    }

    /**
     * 涉旅行业数据分析
     *
     * @return list
     */
    @Override
    public TravelResourceResponse getIndustryAnalysis() {
        return getCountByFilter(INDUSTRY_TYPE_FIELD::contains);
    }

    /**
     * 通过过滤来获取结果
     * @param predicate 过滤器
     * @return respnse
     */
    private TravelResourceResponse getCountByFilter(Predicate<String> predicate) {
        // 目前统计来源来自redis的两个不同的key
        // 1、得到商户的分类
        final Map<String, Long> merchantTypeCount = getMerchantTypeCount();
        // 2、得到非商户的分类
        final Map<String,Long> otherTypeCount = getNotMerchantTypeCount(predicate);

        // 根据2021年1月11日需求，添加旅游从业人员（导游）数量的统计
        // 3、得到中英文讲解员（导游）的数量
        final Map<String, Long> guideCountMap = getGuideCount();

        final TravelResourceResponse response = new TravelResourceResponse();
        response.setList(getListFormMaps(Stream.of(merchantTypeCount, otherTypeCount, guideCountMap).collect(Collectors.toList())));
        response.setTotal(response.getList().stream().mapToLong(KeyCountVO::getCount).sum());

        return response;
    }

    /**
     * 合并多个map并得到list
     * @param maps map列表
     * @return list
     */
    private List<KeyCountVO> getListFormMaps(List<Map<String,Long>> maps){
        Map<String,Long> map = new HashMap<>();
        for (Map<String, Long> stringLongMap : maps) {
            map.putAll(stringLongMap);
        }
        if(CollectionUtils.isEmpty(map)){
            return new ArrayList<>(0);
        }
        return map.entrySet().stream().map(entry -> {
                final KeyCountVO vo = new KeyCountVO();
                vo.setKey(entry.getKey());
                vo.setCount(entry.getValue().intValue());
                return vo;
            }).collect(Collectors.toList());
    }




    /**
     * 得到非商户中的分类
     * @param predicate 过滤key的bool表达
     * @return map
     */
    private Map<String,Long> getNotMerchantTypeCount(Predicate<String> predicate){
        final Map<String, String> entries = RedisUtil.hashOperations().entries(RedisKeyConsts.LYDSJZX_STATISTICS_SUB_TYPE_COUNT);
        if(CollectionUtils.isEmpty(entries)){
            return new HashMap<>(0);
        }
        final Map<String, Long> map = new HashMap<>(entries.size());
        entries.forEach((k,v) ->{
            if(Objects.isNull(predicate) || predicate.test(k)){
                map.put(k, Long.parseLong(v));
            }
        });
        return map;
    }


    /**
     * 获取导游数量
     * @return
     */
    private Map<String, Long> getGuideCount(){
        // 导游数量其实就是中英文讲解员数量
        final String jsonString = RedisUtil.hashOperations().get(RedisKeyConsts.LYDSJZX_STATISTICS_SUB_TYPE_COUNT, "chineseEnglishGuide");
        final HashMap<String, Long> map = new HashMap<>(2);
        if(StringUtils.isEmpty(jsonString)){
            map.put("chineseEnglishGuide", 0L);
        }else{
            final ResourceCountVO resourceCountVO = JsonUtils.toObject(jsonString, new TypeReference<ResourceCountVO>() {
            });
            map.put("chineseEnglishGuide", resourceCountVO.getCount().longValue());
        }
        return map;
    }


    /**
     * 得到商户中的分类
     * @return map
     */
    private Map<String,Long> getMerchantTypeCount(){
        final List<String> values = RedisUtil.hashOperations().values(RedisKeyConsts.MERCHANT_BUSINESS_COUNT);
        if(CollectionUtils.isEmpty(values)){
            return new HashMap<>(0);
        }else{
            // 数据是按照商圈组合的，每个商圈都包含不同的商业分类，这里展开后，将按照不同类型进行重新分组统计
            final List<MerchantTypeCountResponse.Count> countList = values.stream().map(json -> JsonUtils.toObject(json, new TypeReference<MerchantTypeCountResponse>() {
            })).flatMap(mt -> mt.getCounts().stream()).collect(Collectors.toList());

            final Map<String, List<MerchantTypeCountResponse.Count>> groupByNameMap = countList.stream().collect(Collectors.groupingBy(MerchantTypeCountResponse.Count::getMerchantTypeName));
            return ConvertUtil.convertMap(groupByNameMap, DataSubTypeUtil::getTypeByName, (list) -> list.stream().mapToLong(MerchantTypeCountResponse.Count::getCount).sum());
        }
    }
}
