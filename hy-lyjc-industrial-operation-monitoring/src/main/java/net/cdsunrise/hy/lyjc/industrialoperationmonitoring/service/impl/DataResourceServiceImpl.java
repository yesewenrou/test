package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.common.utility.AssertUtil;
import net.cdsunrise.common.utility.StringUtil;
import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.DataResource;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.TouristLocalData;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.TouristSourceCityData;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.TouristSourceCountryData;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp.TouristRegionCompareResponse;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.resp.TouristRegionStatisticsResponse;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.TouristRegionCompareVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.TouristRegionStatisticsVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.DictionaryCodeEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.RegionTypeEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.TouristSourceFlagType;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.common.IndexWrapperFactory;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.index.IndexWrapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper.TouristLocalMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper.TouristSourceCityMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper.TouristSourceCountryMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.helper.DataDictionaryHelper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.helper.MsmAdministrativeAreaHelper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.DataResourceMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.DataResourceService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.*;
import net.cdsunrise.hy.record.starter.util.PageUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.*;
import org.elasticsearch.search.aggregations.bucket.MultiBucketsAggregation;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.InternalSum;
import org.elasticsearch.search.aggregations.metrics.sum.Sum;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.xml.ws.Holder;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author LHY
 */
@Service
public class DataResourceServiceImpl extends ServiceImpl<DataResourceMapper, DataResource> implements DataResourceService {

    @Autowired
    private DataResourceMapper dataResourceMapper;
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;
    @Autowired
    private TouristLocalMapper touristLocalMapper;
    @Autowired
    private TouristSourceCityMapper touristSourceCityMapper;
    @Autowired
    private TouristSourceCountryMapper touristSourceCountryMapper;

    @Override
    public void add(DataResource dataResource) {
        save(dataResource);
    }

    @Override
    public Integer checkDataResourceExist(String countryName, String cityName, String time) {
        QueryWrapper<DataResource> wrapper = new QueryWrapper<>();
        wrapper.eq("country_name", countryName)
                .eq("city_name", cityName)
                .eq("time", time);
        return dataResourceMapper.selectCount(wrapper);
    }

    @Override
    public Map<String, Object> list(PageRequest<DataResourceCondition> pageRequest) {
        Map<String, Object> resultMap = new HashMap<>(2);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        DataResourceCondition condition = pageRequest.getCondition();
        if (condition.getStartTime() != null && condition.getEndTime() != null) {
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").gte(DateUtil.format(new Date(condition.getStartTime().getTime()), DateUtil.PATTERN_YYYY_MM_DD)).lte(DateUtil.format(new Date(condition.getEndTime().getTime()), DateUtil.PATTERN_YYYY_MM_DD)));
        }
        boolQueryBuilder.must(QueryBuilders.termQuery("flag", "day"));
        if (!StringUtil.isEmpty(condition.getScenicName())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("scenicName", condition.getScenicName()));
        }
        int page = Integer.parseInt(String.valueOf(pageRequest.getCurrent())) - 1;
        int size = Integer.parseInt(String.valueOf(pageRequest.getSize()));
        if ("1".equals(condition.getCountryName())) {
            // 中国
            if (!StringUtil.isEmpty(condition.getProvName())) {
                boolQueryBuilder.must(QueryBuilders.termQuery("provName", condition.getProvName()));
            }
            if (!StringUtil.isEmpty(condition.getCityName())) {
                boolQueryBuilder.must(QueryBuilders.termQuery("cityName", condition.getCityName()));
            }
            // 合计
            setTotalPeopleNum("tourist_source_city", boolQueryBuilder, resultMap);
            // 分页
            org.springframework.data.domain.Page<TouristSourceCityData> sourceCityDataPage = touristSourceCityMapper.search(boolQueryBuilder, org.springframework.data.domain.PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "time", "peopleNum", "_id")));
            List<TouristSourceCityData> collect = sourceCityDataPage.getContent();
            PageResult<Object> pageResult = PageUtil.page(sourceCityDataPage, touristSourceCityData -> touristSourceCityData, collect);
            resultMap.put("pageResult", pageResult);
            return resultMap;
        } else {
            // 境外
            boolQueryBuilder.mustNot(QueryBuilders.termQuery("countryName", "中国"));
            // 合计
            setTotalPeopleNum("tourist_source_country", boolQueryBuilder, resultMap);
            // 分页
            org.springframework.data.domain.Page<TouristSourceCountryData> sourceCountryDataPage = touristSourceCountryMapper.search(boolQueryBuilder, org.springframework.data.domain.PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "time", "peopleNum", "_id")));
            List<TouristSourceCountryData> collect = sourceCountryDataPage.getContent();
            PageResult<Object> pageResult = PageUtil.page(sourceCountryDataPage, touristSourceCountryData -> touristSourceCountryData, collect);
            resultMap.put("pageResult", pageResult);
            return resultMap;
        }
    }

    private void setTotalPeopleNum(String indexName, BoolQueryBuilder boolQueryBuilder, Map<String, Object> resultMap) {
        SumAggregationBuilder peopleNumSumAggregationBuilder = AggregationBuilders.sum("peopleNum_sum")
                .field("peopleNum");
        TermsAggregationBuilder scenicNameTermsAggregationBuilder = AggregationBuilders.terms("scenicName_terms")
                .field("scenicName")
                .size(10)
                .subAggregation(peopleNumSumAggregationBuilder);
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices(indexName).withTypes("doc")
                .withQuery(boolQueryBuilder)
                .addAggregation(scenicNameTermsAggregationBuilder);
        elasticsearchTemplate.query(searchQueryBuilder.build(), response -> {
            Aggregations aggregations = response.getAggregations();
            StringTerms scenicNameTerms = aggregations.get("scenicName_terms");
            List<StringTerms.Bucket> buckets = scenicNameTerms.getBuckets();
            Map<String, Integer> peopleNumMap = buckets.stream().collect(Collectors.toMap(StringTerms.Bucket::getKeyAsString, item -> {
                InternalSum peopleNumSum = item.getAggregations().get("peopleNum_sum");
                return Double.valueOf(peopleNumSum.getValue()).intValue();
            }));
            // 洪雅县
            int hyTotalPeopleNum = peopleNumMap.getOrDefault("洪雅县", 0);
            resultMap.put("hyTotalPeopleNum", hyTotalPeopleNum);
            // 瓦屋山
            int wwsTotalPeopleNum = peopleNumMap.getOrDefault("瓦屋山", 0);
            resultMap.put("wwsTotalPeopleNum", wwsTotalPeopleNum);
            // 柳江古镇
            int ljgzTotalPeopleNum = peopleNumMap.getOrDefault("柳江古镇", 0);
            resultMap.put("ljgzTotalPeopleNum", ljgzTotalPeopleNum);
            // 七里坪
            int qlpTotalPeopleNum = peopleNumMap.getOrDefault("七里坪", 0);
            resultMap.put("qlpTotalPeopleNum", qlpTotalPeopleNum);
            // 玉屏山
            int ypsTotalPeopleNum = peopleNumMap.getOrDefault("玉屏山", 0);
            resultMap.put("ypsTotalPeopleNum", ypsTotalPeopleNum);
            // 槽渔滩
            int cytTotalPeopleNum = peopleNumMap.getOrDefault("槽渔滩", 0);
            resultMap.put("cytTotalPeopleNum", cytTotalPeopleNum);
            // 主城区
            int zcqTotalPeopleNum = peopleNumMap.getOrDefault("主城区", 0);
            resultMap.put("zcqTotalPeopleNum", zcqTotalPeopleNum);

            return null;
        });
    }

    @SuppressWarnings("all")
    @Override
    public Map statisticsData(DataResourceCondition condition) {
        Map resultMap = new HashMap();
        String startTime = DateUtil.format(new Date(condition.getStartTime().getTime()), DateUtil.PATTERN_YYYY_MM_DD);
        String endTime = DateUtil.format(new Date(condition.getEndTime().getTime()), DateUtil.PATTERN_YYYY_MM_DD);
        String lastStartTime = DateUtil.format(new Date(condition.getLastStartTime().getTime()), DateUtil.PATTERN_YYYY_MM_DD);
        String lastEndTime = DateUtil.format(new Date(condition.getLastEndTime().getTime()), DateUtil.PATTERN_YYYY_MM_DD);
        resultMap.put("peopleCount", commonQueryTouristNum(startTime, endTime, 0));
        // 省内
        Double peopleInnerProvince = commonQueryTouristNum(startTime, endTime, 1);
        // 省外
        Double peopleOuterProvince = commonQueryTouristNum(startTime, endTime, 2);
        resultMap.put("peopleInnerProvince", peopleInnerProvince);
        resultMap.put("peopleOuterProvince", peopleOuterProvince);
        // 去年省内和省外
        Double lastPeopleInnerProvince = commonQueryTouristNum(lastStartTime, lastEndTime, 1);
        Double lastPeopleOuterProvince = commonQueryTouristNum(lastStartTime, lastEndTime, 2);
        if (lastPeopleInnerProvince > 0 && lastPeopleOuterProvince > 0) {
            // 保留两位小数
            Double compareInnerProvince = new BigDecimal(peopleInnerProvince * 1.0 / lastPeopleInnerProvince)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
            Double compareOuterProvince = new BigDecimal(peopleOuterProvince * 1.0 / lastPeopleOuterProvince)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
            resultMap.put("compareInnerProvince", compareInnerProvince);
            resultMap.put("compareOuterProvince", compareOuterProvince);
        } else {
            resultMap.put("compareInnerProvince", "");
            resultMap.put("compareOuterProvince", "");
        }
        resultMap.put("tourismTrend", commonTouristTrend(startTime, endTime));
        resultMap.put("lastTourismTrend", commonTouristTrend(lastStartTime, lastEndTime));
        return resultMap;
    }

    private List<ChartVO> commonTouristTrend(String startTime, String endTime) {
        List<ChartVO> dataList = new ArrayList<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").gte(startTime).lte(endTime));
        boolQueryBuilder.must(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .must(QueryBuilders.termQuery("flag", "day"));
        Iterable<TouristLocalData> iterable = touristLocalMapper.search(boolQueryBuilder);
        iterable.forEach(touristLocalData -> {
            dataList.add(new ChartVO(touristLocalData.getTime(), touristLocalData.getPeopleNum()));
        });
        return buildMixData(dataList, startTime, endTime);
    }

    // 根据起始节假日，构建完整数据
    private List<ChartVO> buildMixData(List<ChartVO> list, String startTime, String endTime) {
        Map<String, Object> paramMap = new HashMap<>();
        List<String> dateList = DateUtil.getBetweenDate(startTime, endTime);
        List<ChartVO> dataList = new ArrayList<>();
        list.forEach(scenicTouristVO -> {
            paramMap.put(scenicTouristVO.getName(), scenicTouristVO.getValue());
        });
        for (String date : dateList) {
            ChartVO chartVO;
            if (paramMap.containsKey(date)) {
                chartVO = new ChartVO(date, Integer.parseInt(String.valueOf(paramMap.get(date))));
            } else {
                chartVO = new ChartVO(date, 0);
            }
            dataList.add(chartVO);
        }
        return dataList;
    }

    private Double commonQueryTouristNum(String startTime, String endTime, Integer scope) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").gte(startTime).lte(endTime));
        boolQueryBuilder.must(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .must(QueryBuilders.termQuery("flag", "day"));
        // 节假日游客接待总数
        SumAggregationBuilder aggregationBuilder = AggregationBuilders.sum("people_count").field("peopleNum");
        // scope=0，整个县域游客(包含境外游客)；scoe=1，省内游客；scope=2，省外游客
        if (scope == 0) {
            return sumAggregation("tourist_local_data", "doc", boolQueryBuilder, aggregationBuilder);
        } else if (scope == 1) {
            boolQueryBuilder.must(QueryBuilders.termQuery("provName", "四川"));
            return sumAggregation("tourist_source_prov", "doc", boolQueryBuilder, aggregationBuilder);
        } else {
            boolQueryBuilder.mustNot(QueryBuilders.termQuery("provName", "四川"));
            return sumAggregation("tourist_source_prov", "doc", boolQueryBuilder, aggregationBuilder);
        }
    }

    @SuppressWarnings("all")
    @Override
    public PageResult<DataResourceVO> conditionStatisticsData(PageRequest<DataResourceCondition> pageRequest) {
        DataResourceCondition condition = pageRequest.getCondition();
        Map<String, Object> resultMap = commonConditionStatisticsData(pageRequest, true);
        if ("1".equals(condition.getCountryName())) {
            // 中国
            org.springframework.data.domain.Page<TouristSourceCityData> sourceCityDataPage = (org.springframework.data.domain.Page<TouristSourceCityData>) resultMap.get("pageInfo");
            List<TouristSourceCityData> collect = sourceCityDataPage.getContent();
            return PageUtil.page(sourceCityDataPage, touristSourceCityData -> {
                DataResourceVO dataResourceVO = new DataResourceVO();
                dataResourceVO.setCount(touristSourceCityData.getPeopleNum());
                dataResourceVO.setCountryName(touristSourceCityData.getCountryName());
                dataResourceVO.setProvName(touristSourceCityData.getProvName());
                dataResourceVO.setCityName(touristSourceCityData.getCityName());
                dataResourceVO.setStatisticsTime(touristSourceCityData.getTime());
                return dataResourceVO;
            }, collect);
        } else {
            // 境外
            org.springframework.data.domain.Page<TouristSourceCountryData> sourceCountryDataPage = (org.springframework.data.domain.Page<TouristSourceCountryData>) resultMap.get("pageInfo");
            List<TouristSourceCountryData> collect = sourceCountryDataPage.getContent();
            return PageUtil.page(sourceCountryDataPage, touristSourceCountryData -> {
                DataResourceVO dataResourceVO = new DataResourceVO();
                dataResourceVO.setCount(touristSourceCountryData.getPeopleNum());
                dataResourceVO.setCountryName(touristSourceCountryData.getCountryName());
                dataResourceVO.setStatisticsTime(touristSourceCountryData.getTime());
                return dataResourceVO;
            }, collect);
        }
    }

    /**
     * 假日统计分析条件搜索+导出Excel，通用方法
     */
    private Map<String, Object> commonConditionStatisticsData(PageRequest<DataResourceCondition> pageRequest, Boolean pageFlag) {
        Map<String, Object> resultMap = new HashMap<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        DataResourceCondition condition = pageRequest.getCondition();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").gte(DateUtil.format(new Date(condition.getStartTime().getTime()), DateUtil.PATTERN_YYYY_MM_DD)).lte(DateUtil.format(new Date(condition.getEndTime().getTime()), DateUtil.PATTERN_YYYY_MM_DD)));
        boolQueryBuilder.must(QueryBuilders.termQuery("flag", "day"))
                .must(QueryBuilders.termQuery("scenicName", "洪雅县"));
        int page = Integer.parseInt(String.valueOf(pageRequest.getCurrent())) - 1;
        int size = Integer.parseInt(String.valueOf(pageRequest.getSize()));
        List<DataResourceVO> dataList = new ArrayList<>();
        if ("1".equals(condition.getCountryName())) {
            // 中国
            if (!StringUtil.isEmpty(condition.getProvName())) {
                boolQueryBuilder.must(QueryBuilders.termQuery("provName", condition.getProvName()));
            }
            if (!StringUtil.isEmpty(condition.getCityName())) {
                boolQueryBuilder.must(QueryBuilders.termQuery("cityName", condition.getCityName()));
            }
            if (pageFlag) {
                // 要分页
                org.springframework.data.domain.Page<TouristSourceCityData> sourceCityDataPage = touristSourceCityMapper.search(boolQueryBuilder, org.springframework.data.domain.PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "time")));
                resultMap.put("pageInfo", sourceCityDataPage);
            } else {
                Iterable<TouristSourceCityData> iterable = touristSourceCityMapper.search(boolQueryBuilder);
                iterable.forEach(touristSourceCityData -> {
                    DataResourceVO dataResourceVO = new DataResourceVO();
                    dataResourceVO.setCount(touristSourceCityData.getPeopleNum());
                    dataResourceVO.setCountryName(touristSourceCityData.getCountryName());
                    dataResourceVO.setProvName(touristSourceCityData.getProvName());
                    dataResourceVO.setCityName(touristSourceCityData.getCityName());
                    dataResourceVO.setStatisticsTime(touristSourceCityData.getTime());
                    dataList.add(dataResourceVO);
                });
                resultMap.put("dataList", dataList);
            }
        } else {
            // 境外
            boolQueryBuilder.mustNot(QueryBuilders.termQuery("countryName", "中国"));
            if (pageFlag) {
                // 要分页
                org.springframework.data.domain.Page<TouristSourceCountryData> sourceCountryDataPage = touristSourceCountryMapper.search(boolQueryBuilder, org.springframework.data.domain.PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "time")));
                resultMap.put("pageInfo", sourceCountryDataPage);
            } else {
                Iterable<TouristSourceCountryData> iterable = touristSourceCountryMapper.search(boolQueryBuilder);
                iterable.forEach(touristSourceCountryData -> {
                    DataResourceVO dataResourceVO = new DataResourceVO();
                    dataResourceVO.setCount(touristSourceCountryData.getPeopleNum());
                    dataResourceVO.setCountryName(touristSourceCountryData.getCountryName());
                    dataResourceVO.setStatisticsTime(touristSourceCountryData.getTime());
                    dataList.add(dataResourceVO);
                });
                resultMap.put("dataList", dataList);
            }
        }
        return resultMap;
    }

    @Override
    public List<DataResourceVO> historyConditionStatisticsData(DataResourceCondition condition) {
        List<DataResourceVO> dataList = new ArrayList<>();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.must(QueryBuilders.rangeQuery("time").gte(DateUtil.format(new Date(condition.getStartTime().getTime()), DateUtil.PATTERN_YYYY_MM_DD)).lte(DateUtil.format(new Date(condition.getEndTime().getTime()), DateUtil.PATTERN_YYYY_MM_DD)));
        if (!StringUtil.isEmpty(condition.getScenicName())) {
            boolQueryBuilder.must(QueryBuilders.termQuery("scenicName", condition.getScenicName()));
        }
        switch (condition.getFlag()) {
            case "month":
                boolQueryBuilder.must(QueryBuilders.termQuery("flag", "month"));
                break;
            case "day":
                boolQueryBuilder.must(QueryBuilders.termQuery("flag", "day"));
                break;
            default:
                boolQueryBuilder.must(QueryBuilders.termQuery("flag", "month"));
                AggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("people_count").field("peopleNum");
                TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                        .terms("people_agg")
                        .field("scenicName")
                        .order(BucketOrder.aggregation("people_count", false))
                        .subAggregation(sumAggregationBuilder);
                DateHistogramAggregationBuilder histogramAggregationBuilder = AggregationBuilders.dateHistogram("time_histogram")
                        .field("time")
                        .subAggregation(termsAggregationBuilder);
                histogramAggregationBuilder.dateHistogramInterval(DateHistogramInterval.YEAR).format("yyyy");
                NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices("tourist_local_data").withTypes("doc")
                        .withQuery(boolQueryBuilder).addAggregation(histogramAggregationBuilder).build();
                Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
                InternalDateHistogram dateHistogram = temp.get("time_histogram");
                // 获得所有的桶
                List<InternalDateHistogram.Bucket> buckets = dateHistogram.getBuckets();
                // 将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
                for (InternalDateHistogram.Bucket bucket : buckets) {
                    // 聚合日期列表
                    String time = bucket.getKeyAsString();
                    StringTerms stringTerms = (StringTerms) bucket.getAggregations().asMap().get("people_agg");
                    // 获得所有的桶
                    List<StringTerms.Bucket> aggBuckets = stringTerms.getBuckets();
                    // 将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
                    for (StringTerms.Bucket aggBucket : aggBuckets) {
                        String scenicName = aggBucket.getKeyAsString();
                        Sum sum = aggBucket.getAggregations().get("people_count");
                        dataList.add(new DataResourceVO(scenicName, new Double(sum.getValue()).intValue(), time));
                    }
                }
                return dataList;
        }
        // 最多展示1w数据，深度分页问题
        Page<TouristLocalData> page = touristLocalMapper.search(boolQueryBuilder, org.springframework.data.domain.PageRequest.of(0, 10000, Sort.by(Sort.Direction.DESC, "_id")));
        page.getContent().forEach(touristLocalData->{
            dataList.add(new DataResourceVO(touristLocalData.getScenicName(), touristLocalData.getPeopleNum(), touristLocalData.getTime()));
        });
        return dataList;
    }

    @SuppressWarnings("all")
    @Override
    public ResponseEntity<byte[]> export(PageRequest<DataResourceCondition> pageRequest) {
        String excelName = "产业运行监测假日统计分析游客数据";
        ClassPathResource classPathResource = new ClassPathResource("template/产业运行监测假日统计分析游客数据模板.xlsx");
        Map<String, Object> resultMap = commonConditionStatisticsData(pageRequest, false);
        try {
            InputStream inputStream = classPathResource.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            // 获取sheet
            Sheet sheet = workbook.getSheet(excelName);
            // 写入头部信息
            Row headRow = sheet.getRow(2);
            // 对象名称
            Cell summaryCell = headRow.getCell(1);
            summaryCell.setCellValue(excelName);
            // 时间范围
            String dateString = DateUtil.format(pageRequest.getCondition().getStartTime(), DateUtil.PATTERN_YYYY_MM_DD) + " - " +
                    DateUtil.format(pageRequest.getCondition().getEndTime(), DateUtil.PATTERN_YYYY_MM_DD);
            Cell dateStringCell = headRow.getCell(4);
            dateStringCell.setCellValue(dateString);
            // 写入每一行数据
            int startRowIndex = 5;
            int rowIndex = startRowIndex;
            int cellNumber = 5;
            List<DataResourceVO> dataResourceList = (List<DataResourceVO>) resultMap.get("dataList");
            if (!CollectionUtils.isEmpty(dataResourceList)) {
                Map<Integer, CellStyle> cellStyleMap = new HashMap<>(cellNumber);
                for (DataResourceVO dataResourceVO : dataResourceList) {
                    Row row;
                    if (rowIndex == startRowIndex) {
                        row = sheet.getRow(rowIndex);
                    } else {
                        // 创建新的行
                        row = sheet.createRow(rowIndex);
                    }
                    // 复制样式
                    for (int i = 0; i < cellNumber; i++) {
                        Cell cell;
                        if (rowIndex == 5) {
                            // 模板行，保存样式
                            cell = row.getCell(i);
                            cellStyleMap.put(i, cell.getCellStyle());
                        } else {
                            // 复制样式
                            cell = row.createCell(i);
                            cell.setCellStyle(cellStyleMap.get(i));
                        }
                    }
                    row.getCell(0).setCellValue(dataResourceVO.getCount());
                    row.getCell(1).setCellValue(dataResourceVO.getCountryName());
                    row.getCell(2).setCellValue(dataResourceVO.getProvName());
                    row.getCell(3).setCellValue(dataResourceVO.getCityName());
                    row.getCell(4).setCellValue(dataResourceVO.getStatisticsTime());
                    rowIndex++;
                }
            }
            workbook.write(byteArrayOutputStream);
            // 下载
            byte[] body = byteArrayOutputStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename("产业运行监测假日统计分析游客数据.xlsx", StandardCharsets.UTF_8)
                    .build();
            headers.setContentDisposition(contentDisposition);
            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 之所以export需要传递page和size，是因为之前用MySQL时，直接通过size=-1，去获取全部数据
     */
    @Override
    public ResponseEntity<byte[]> historyExport(PageRequest<DataResourceCondition> pageRequest) {
        String excelName = "产业运行监测历史数据统计游客接待数";
        ClassPathResource classPathResource = new ClassPathResource("template/产业运行监测历史数据统计游客接待数模板.xlsx");
        try {
            InputStream inputStream = classPathResource.getInputStream();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            // 获取sheet
            Sheet sheet = workbook.getSheet(excelName);
            // 写入头部信息
            Row headRow = sheet.getRow(2);
            // 时间范围
            String dateString = DateUtil.format(pageRequest.getCondition().getStartTime(), DateUtil.PATTERN_YYYY_MM_DD) + " - " +
                    DateUtil.format(pageRequest.getCondition().getEndTime(), DateUtil.PATTERN_YYYY_MM_DD);
            Cell dateStringCell = headRow.getCell(1);
            dateStringCell.setCellValue(dateString);
            // 写入每一行数据
            int startRowIndex = 5;
            int rowIndex = startRowIndex;
            int cellNumber = 3;
            List<DataResourceVO> dataResourceList = historyConditionStatisticsData(pageRequest.getCondition());
            if (!CollectionUtils.isEmpty(dataResourceList)) {
                Map<Integer, CellStyle> cellStyleMap = new HashMap<>(cellNumber);
                for (DataResourceVO dataResourceVO : dataResourceList) {
                    Row row;
                    if (rowIndex == startRowIndex) {
                        row = sheet.getRow(rowIndex);
                    } else {
                        // 创建新的行
                        row = sheet.createRow(rowIndex);
                    }
                    // 复制样式
                    for (int i = 0; i < cellNumber; i++) {
                        Cell cell;
                        if (rowIndex == 5) {
                            // 模板行，保存样式
                            cell = row.getCell(i);
                            cellStyleMap.put(i, cell.getCellStyle());
                        } else {
                            // 复制样式
                            cell = row.createCell(i);
                            cell.setCellStyle(cellStyleMap.get(i));
                        }
                    }
                    row.getCell(0).setCellValue(dataResourceVO.getCount());
                    row.getCell(1).setCellValue(dataResourceVO.getScenicName());
                    row.getCell(2).setCellValue(dataResourceVO.getStatisticsTime());
                    rowIndex++;
                }
            }
            workbook.write(byteArrayOutputStream);
            // 下载
            byte[] body = byteArrayOutputStream.toByteArray();
            HttpHeaders headers = new HttpHeaders();
            ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
                    .filename("产业运行监测历史数据统计游客接待数.xlsx", StandardCharsets.UTF_8)
                    .build();
            headers.setContentDisposition(contentDisposition);
            return new ResponseEntity<>(body, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("all")
    @Override
    public Map sourceCityTop(Integer year, Integer scope) {
        Map map = new HashMap();
        String start = "-01-01";
        String end = "-12-31";
        // 今年时间
        String startTime = year + start;
        String endTime = year + end;
        // 去年时间
        Integer lastYear = year - 1;
        String lastStartTime = lastYear + start;
        String lastEndTime = lastYear + end;
        map.put(year, commonSourceCityQuery(startTime, endTime, scope));
        map.put(lastYear, commonSourceCityQuery(lastStartTime, lastEndTime, scope));
        return map;
    }

    private List<ConsumptionVO> commonSourceCityQuery(String startTime, String endTime, Integer scope) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").gte(startTime).lte(endTime));
        boolQueryBuilder.must(QueryBuilders.termQuery("scenicName", "洪雅县"))
                .must(QueryBuilders.termQuery("flag", "month"));
        if (scope == 1) {
            // 省内
            boolQueryBuilder.must(QueryBuilders.termQuery("provName", "四川"));
            return aggregation("tourist_source_city", "doc", boolQueryBuilder, 10, "cityName", "peopleNum");
        } else if (scope == 2) {
            // 省外（从source_prov索引查询，省外显示省份）
            boolQueryBuilder.mustNot(QueryBuilders.termQuery("provName", "四川"));
            return aggregation("tourist_source_prov", "doc", boolQueryBuilder, 10, "provName", "peopleNum");
        }
        // 不限（从source_city索引查询，省外也只显示城市）
        return aggregation("tourist_source_city", "doc", boolQueryBuilder, 10, "cityName", "peopleNum");
    }

    private Double sumAggregation(String indexName, String indexType, BoolQueryBuilder boolQueryBuilder, SumAggregationBuilder aggregationBuilder) {
        NativeSearchQuery query = (new NativeSearchQueryBuilder()).withIndices(indexName).withTypes(indexType)
                .withQuery(boolQueryBuilder).addAggregation(aggregationBuilder).build();
        return elasticsearchTemplate.query(query, response -> {
            InternalSum sum = (InternalSum) response.getAggregations().asList().get(0);
            return sum.getValue();
        });
    }

    private List<ConsumptionVO> aggregation(String indexName, String indexType, BoolQueryBuilder boolQueryBuilder, Integer size, String field, String agg) {
        List<ConsumptionVO> dataList = new ArrayList<>();
        AggregationBuilder sumAggregationBuilder = AggregationBuilders.sum("people_count").field(agg);
        TermsAggregationBuilder termsAggregationBuilder = AggregationBuilders
                .terms("people_agg")
                .field(field)
                .order(BucketOrder.aggregation("people_count", false))
                .subAggregation(sumAggregationBuilder);
        if (size != null) {
            termsAggregationBuilder.size(size);
        }
        NativeSearchQuery query = new NativeSearchQueryBuilder().withIndices(indexName).withTypes(indexType)
                .withQuery(boolQueryBuilder).addAggregation(termsAggregationBuilder).build();
        Aggregations temp = elasticsearchTemplate.query(query, SearchResponse::getAggregations);
        StringTerms stringTerms = (StringTerms) temp.get("people_agg");
        // 获得所有的桶
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        // 将集合转换成迭代器遍历桶,当然如果你不删除buckets中的元素，直接foreach遍历就可以了
        for (StringTerms.Bucket bucket : buckets) {
            String name = bucket.getKeyAsString();
            Sum sum = bucket.getAggregations().get("people_count");
            dataList.add(new ConsumptionVO(name, sum.getValue()));
        }
        return dataList;
    }

    @SuppressWarnings("all")
    @Override
    public Map sourceCityTopByDay() {
        Map map = new HashMap();
        String yesterday = DateUtil.format(DateUtil.getTime("day", new Date(), -1), DateUtil.PATTERN_YYYY_MM_DD);
        List<ChartVO> innerList = dataResourceMapper.sourceCityTop(yesterday, yesterday, 1);
        List<ChartVO> outerList = dataResourceMapper.sourceCityTop(yesterday, yesterday, 2);
        map.put("innerProvince", innerList);
        map.put("outerProvince", outerList);
        return map;
    }

    @Override
    public TouristRegionStatisticsResponse touristRegionStatistics(Long beginDate, Long endDate, String touristsScopeCode) {
        checkDate(beginDate,endDate);
        checkTouristScopeCode(touristsScopeCode);

        // 把时间戳转换成日期
        // 开始日期（包含）
        final LocalDate begin = DateUtil.longToLocalDateTime(beginDate).toLocalDate();
        // 结束日期（不包含）
        final LocalDate end = DateUtil.longToLocalDateTime(endDate).plusDays(1).toLocalDate();

        final TouristRegionStatisticsResponse response = TouristRegionStatisticsResponse.createDefault(beginDate, endDate, touristsScopeCode,"眉山移动");
        if(begin.compareTo(end) > 0){
            response.setList(new ArrayList<>(0));
            return response;
        }

        String scenicName = DataDictionaryHelper.getNameByCode(touristsScopeCode);
        final List<TouristRegionStatisticsVO> list = doRegionStatisticsByDay(begin, end, scenicName);

        response.setList(list);
        if(!CollectionUtils.isEmpty(response.getList())){
            response.setTotal(list.stream().mapToLong(TouristRegionStatisticsVO::getPeopleNum).sum());
        }
        return response;
    }

    private static void checkDate(Long beginDate, Long endDate){
        // 参数校验
        AssertUtil.notNull(beginDate, () -> new ParamErrorException("begin date is null"));
        AssertUtil.notNull(endDate, () -> new ParamErrorException("end date is null"));
    }

    private static void checkRegionType(Integer regionType){
        AssertUtil.notNull(regionType, () -> new ParamErrorException("region type is null"));
        AssertUtil.isTrue(RegionTypeEnum.isTypeExist(regionType),() -> new ParamErrorException("region type is illegal by: " + regionType) );
    }

      /**
     * 游客来源对比统计分析（按天生成折线图）
     *
     * @param beginDate         开始日期时间戳
     * @param endDate           结束日期时间戳
     * @param touristsScopeCode 游客区域编码（数据字典）
     * @param fullName              区域名称
     * @return TouristRegionCompareResponse
     */
    @Override
    public TouristRegionCompareResponse touristRegionCompareStatistics(Long beginDate, Long endDate, String touristsScopeCode, String fullName) {
        checkTouristRegionCompareStatistics(beginDate, endDate, touristsScopeCode, fullName);
        // 开始日期（包含）,这里要减一天，是因为要算“较昨日环比”
        final LocalDate begin = DateUtil.longToLocalDateTime(beginDate).toLocalDate();
        // 结束日期（不包含）
        final LocalDate end = DateUtil.longToLocalDateTime(endDate).plusDays(1).toLocalDate();

        final TouristRegionCompareResponse resp = TouristRegionCompareResponse.createDefault();
        if(begin.compareTo(end) >= 0){
            return resp;
        }

        final String scenicName = DataDictionaryHelper.getNameByCode(touristsScopeCode);
        // 这里开始日期减少1天，是因为要查询“昨日”数据并对比，则第一天的对象需要其前一天的对象提供值
        final List<TouristRegionCompareVO> touristRegionCompareList = queryRegionData(begin.minusDays(1), end, scenicName, fullName);

        // 排序
        touristRegionCompareList.sort(Comparator.comparing(TouristRegionCompareVO::getTime));
        // 设置比率
        touristRegionCompareList.forEach(TouristRegionCompareVO::setRatio);

        resp.setList(touristRegionCompareList);
        resp.setTimeList(touristRegionCompareList.stream().map(TouristRegionCompareVO::getTime).collect(Collectors.toList()));
        return resp;
    }



    private List<TouristRegionCompareVO> queryRegionData(LocalDate begin, LocalDate end, String scenicName, String fullName) {
        final String[] split = fullName.split("_");

        // 当数组长度等于1，表示查询的是国家
        if(split.length <= 1){
            if("中国".equals(fullName)){
                return queryChainRegionData(begin, end, scenicName);
            }else{
                return queryOutsideRegionData(begin, end, scenicName, fullName);
            }
        }else{
            if(split.length == 2){
                String provName = split[1];
                final boolean isOutsideProvince = MsmAdministrativeAreaHelper.isOutsideProvince(provName);
                if(isOutsideProvince){
                    return queryOutsideRegionData(begin,end,scenicName,provName);
                }else{
                    return queryInsideRegionData(begin,end,scenicName,provName,null);
                }
            }else{
                return queryInsideRegionData(begin,end,scenicName, split[1],split[2]);
            }
        }
    }

    private List<TouristRegionCompareVO> queryInsideRegionData(LocalDate begin, LocalDate end, String scenicName, String provName,String cityName){
        final Map<String, Long> currentMap = queryRegionDataFromCity(begin, end, scenicName, provName, cityName);
        final Map<String, Long> lastYearMap = queryRegionDataFromCity(begin.minusYears(1), end.minusYears(1), scenicName, provName, cityName);
        return createCompareVOList(currentMap,lastYearMap);
    }

    private List<TouristRegionCompareVO> queryOutsideRegionData(LocalDate begin, LocalDate end, String scenicName, String countryName){
        final Map<String, Long> currentMap = queryRegionDataFromCountry(begin, end, scenicName, countryName);
        final Map<String, Long> lastYearMap = queryRegionDataFromCountry(begin.minusYears(1), end.minusYears(1), scenicName, countryName);
        return createCompareVOList(currentMap, lastYearMap);

    }



    private List<TouristRegionCompareVO> queryChainRegionData(LocalDate begin, LocalDate end, String scenicName){
        // 查询中国大陆当前日期范围的数据
        final Map<String, Long> currentInsideMap = queryRegionDataFromCity(begin, end, scenicName, null, null);
        // 查询中国台湾、香港、澳门当前日期范围的数据
        final Map<String, Long> currentOutSideMap = queryRegionDataFromCountry(begin, end, scenicName, MsmAdministrativeAreaHelper.getOutsideProvinceNames());
        final Map<String, Long> currentMap = mergeMap(currentInsideMap, currentOutSideMap);


        LocalDate lastYearBegin = begin.minusYears(1);
        LocalDate lastYearEnd = end.minusYears(1);

        // 查询中国大陆当前上一年日期范围的数据
        final Map<String, Long> lastYearInsideMap = queryRegionDataFromCity(lastYearBegin, lastYearEnd, scenicName, null, null);
        // 查询中国台湾、香港、澳门上一年日期范围的数据
        final Map<String, Long> lastYearOutSideMap = queryRegionDataFromCountry(lastYearBegin, lastYearEnd, scenicName, MsmAdministrativeAreaHelper.getOutsideProvinceNames());
        final Map<String, Long> lastYearMap = mergeMap(lastYearInsideMap, lastYearOutSideMap);

        return createCompareVOList(currentMap, lastYearMap);

    }

    private static List<TouristRegionCompareVO> createCompareVOList(Map<String, Long> currentMap, Map<String, Long> lastYearMap) {
        // 以currentMap为基准，生成vo列表
        if(CollectionUtils.isEmpty(currentMap)){
            return new ArrayList<>(0);
        }
        // 得到最小的key
        final String minKey =
                currentMap.keySet().stream().min(Comparator.naturalOrder()).orElseThrow(() -> new RuntimeException("not found min key from current map"));

        List<TouristRegionCompareVO> list = new ArrayList<>(currentMap.size());
        currentMap.forEach((k,v) -> {
            final LocalDate currentLocalDate = DateUtil.stringToLocalDate(k);
            final Long time = DateUtil.localDateToLong(currentLocalDate);
            // 设置当天
            final TouristRegionCompareVO vo = TouristRegionCompareVO.createZero(time);
            vo.setCurrentYearDayPeopleNum(v);

            // 设置去年当天
            final Long lastYearValue = lastYearMap.get(k);
            if(Objects.nonNull(lastYearValue)){
                vo.setLastYearDayPeopleNum(lastYearValue);
            }

            // 设置昨天
            String yesterdayKey = DateUtil.localDateToString(currentLocalDate.minusDays(1));
            final Long yesterdayValue = currentMap.get(yesterdayKey);
            if(Objects.nonNull(yesterdayValue)){
                vo.setCurrentYearYesterdayPeopleNum(yesterdayValue);
            }
            // 这里不要把最早的那一天放进来，因为它只是为了获取历史数据，不做展示
            if(!k.equals(minKey)){
                list.add(vo);
            }
        });
        return list;
    }


    private Map<String, Long> mergeMap(Map<String, Long> map1, Map<String, Long> map2){
        if(Objects.isNull(map1) && Objects.isNull(map2)){
            return new HashMap<>(0);
        }
        if(Objects.isNull(map1)){
            return map2;
        }
        if(Objects.isNull(map2)){
            return map1;
        }

        final HashSet<String> allKeySet = new HashSet<>(map1.size() + map2.size());
        allKeySet.addAll(map1.keySet());
        allKeySet.addAll(map2.keySet());
        Map<String, Long> newMap = new HashMap<>(allKeySet.size());

        allKeySet.forEach(k -> {
            Long v1 = map1.get(k);
            Long v2 = map2.get(k);
            newMap.put(k, nullToZero(v1) + nullToZero(v2));
        });
        return newMap;
    }


    private static Long nullToZero(Long v){
        return Objects.isNull(v) ? 0L : v;
    }




    @SuppressWarnings("DuplicatedCode")
    private Map<String, Long> queryRegionDataFromCity(LocalDate begin, LocalDate end, String scenicName, String provName, String cityName){
        final Map<String, Long> map = createDateMapWithDefaultValue(begin, end.minusDays(1));
        if(CollectionUtils.isEmpty(map)){
            return map;
        }

        final IndexWrapper<TouristSourceCityData> wrapper = IndexWrapperFactory.getWrapper(TouristSourceCityData.class);
        // 拼接bool查询
        final BoolQueryBuilder boolQueryBuilder = wrapper.boolQuery(wrapper.rangeQuery(TouristSourceCityData::getTime, DateUtil.localDateToString(begin), DateUtil.localDateToString(end)));
        boolQueryBuilder.must(wrapper.termQuery(TouristSourceCityData::getFlag, TouristSourceFlagType.DAY.getType()));
        boolQueryBuilder.must(wrapper.termQuery(TouristSourceCityData::getScenicName, scenicName));

        if(!StringUtils.isEmpty(provName)){
            boolQueryBuilder.must(wrapper.termQuery(TouristSourceCityData::getProvName, provName));
        }
        if(!StringUtils.isEmpty(cityName)){
            boolQueryBuilder.must(wrapper.termQuery(TouristSourceCityData::getCityName, cityName));
        }

        //拼接聚合查询
        final DateHistogramAggregationBuilder timeAggBuilder = wrapper.dateHistogramAgg(TouristSourceCityData::getTime, DateHistogramInterval.DAY, "yyyy-MM-dd");
        final SumAggregationBuilder peopleSumAggBuilder = wrapper.sumAgg(TouristSourceCityData::getPeopleNum);
        timeAggBuilder.subAggregation(peopleSumAggBuilder);

        //所有条件拼接
        final NativeSearchQueryBuilder nativeSearchQueryBuilder = wrapper.nativeSearchQueryBuilder(boolQueryBuilder, timeAggBuilder);

        final Aggregations aggregations = elasticsearchTemplate.query(nativeSearchQueryBuilder.build(), SearchResponse::getAggregations);

        if(Objects.nonNull(aggregations)){
            final InternalDateHistogram dateHistogram = aggregations.get(timeAggBuilder.getName());
            if(hasBuckets(dateHistogram)){
                dateHistogram.getBuckets().forEach(b -> {
                    final String time = b.getKeyAsString();
                    final Aggregations subAgg = b.getAggregations();
                    final InternalSum sum = subAgg.get(peopleSumAggBuilder.getName());
                    map.put(time, defaultZeroValueIfNull(sum));
                });
            }
        }
        return map;

    }



    @SuppressWarnings("DuplicatedCode")
    private Map<String, Long> queryRegionDataFromCountry(LocalDate begin, LocalDate end, String scenicName, String... countryNames){
        final Map<String, Long> map = createDateMapWithDefaultValue(begin, end.minusDays(1));
        if(CollectionUtils.isEmpty(map)){
            return map;
        }
        final IndexWrapper<TouristSourceCountryData> wrapper = IndexWrapperFactory.getWrapper(TouristSourceCountryData.class);
        // 拼接bool查询
        final BoolQueryBuilder boolQueryBuilder =
                wrapper.boolQuery(
                        wrapper.rangeQuery(TouristSourceCountryData::getTime, DateUtil.localDateToString(begin), DateUtil.localDateToString(end)),
                        wrapper.termQuery(TouristSourceCountryData::getFlag, TouristSourceFlagType.DAY.getType())
                );
        boolQueryBuilder.must(wrapper.termQuery(TouristSourceCountryData::getScenicName, scenicName));
        if(Objects.nonNull(countryNames) && countryNames.length > 0){
           boolQueryBuilder.must(
                   countryNames.length == 1
                   ? wrapper.termQuery(TouristSourceCountryData::getCountryName, countryNames[0])
                   : wrapper.termsQuery(TouristSourceCountryData::getCountryName, countryNames)
           );
        }


        //拼接聚合查询
        final DateHistogramAggregationBuilder timeAggBuilder =
                wrapper.dateHistogramAgg(TouristSourceCountryData::getTime, DateHistogramInterval.DAY, "yyyy-MM-dd");
        final SumAggregationBuilder peopleSumAggBuilder = wrapper.sumAgg(TouristSourceCountryData::getPeopleNum);
        timeAggBuilder.subAggregation(peopleSumAggBuilder);

        //所有条件拼接
        final NativeSearchQueryBuilder nativeSearchQueryBuilder = wrapper.nativeSearchQueryBuilder(boolQueryBuilder, timeAggBuilder);

        final Aggregations aggregations = elasticsearchTemplate.query(nativeSearchQueryBuilder.build(), SearchResponse::getAggregations);

        // TODO 可以重构
        if(Objects.nonNull(aggregations)){
            final InternalDateHistogram dateHistogram = aggregations.get(timeAggBuilder.getName());
            if(hasBuckets(dateHistogram)){
                dateHistogram.getBuckets().forEach(b -> {
                    final String time = b.getKeyAsString();
                    final Aggregations subAgg = b.getAggregations();
                    final InternalSum sum = subAgg.get(peopleSumAggBuilder.getName());
                    map.put(time, defaultZeroValueIfNull(sum));
                });
            }
        }
        return map;

    }


    private static Map<String,Long> createDateMapWithDefaultValue(LocalDate begin, LocalDate end){
        if(begin.compareTo(end) > 0){
            return new HashMap<>(0);
        }

        Map<String,Long> map = new LinkedHashMap<>();
        final Holder<LocalDate> current = new Holder<>(begin);
        do{
            map.put(DateUtil.localDateToString(current.value), 0L);
            current.value = current.value.plusDays(1);
        }while (current.value.compareTo(end) <= 0);
        return map;
    }





    private static void checkTouristRegionCompareStatistics(Long beginDate, Long endDate, String touristsScopeCode, String fullName){
        checkDate(beginDate,endDate);
//        checkTouristScopeCode(touristsScopeCode);
        AssertUtil.notEmpty(fullName, () -> new ParamErrorException("full name is empty"));
    }



    private List<TouristRegionStatisticsVO> doRegionStatisticsByDay(LocalDate begin, LocalDate end, String scenicName){
        final List<TouristRegionStatisticsVO> inSideList = doInsideRegionStatistics(begin, end, scenicName, TouristSourceFlagType.DAY);
        final List<TouristRegionStatisticsVO> outSideList = doOutSideRegionStatistics(begin, end, scenicName, TouristSourceFlagType.DAY);

        // 构建一个list，包含境内和境外数据
        List<TouristRegionStatisticsVO> list = new ArrayList<>(outSideList.size() + 1);
        final TouristRegionStatisticsVO china = TouristRegionStatisticsVO.createChina();
        china.getChildren().addAll(inSideList);
        list.add(china);

        if(!CollectionUtils.isEmpty(outSideList)){
            final Map<String, List<TouristRegionStatisticsVO>> motherOrOtherMap = outSideList.stream().collect(Collectors.groupingBy(v -> {
                        final boolean isChina = v.getRegionName().startsWith("中国");
                        return isChina ? "mother" : "other";
                    }
            ));
            final List<TouristRegionStatisticsVO> motherChildren = motherOrOtherMap.get("mother");
            if(!CollectionUtils.isEmpty(motherChildren)){
                china.getChildren().addAll(motherChildren);
            }
            final List<TouristRegionStatisticsVO> otherCountry = motherOrOtherMap.get("other");
            list.addAll(otherCountry);
        }

        china.countFromChildren();
        fillDefaultValue(china);

        list.sort((o1, o2) -> o2.getPeopleNum().compareTo(o1.getPeopleNum()));
        list.forEach(TouristRegionStatisticsVO::sortChildrenByDesc);
        list.forEach(TouristRegionStatisticsVO::addFullName);
        return list;
    }

    /**
     * 填充默认值
     * TODO 要优化代码
     * @param china 中国区域统计对象
     */
    private static void fillDefaultValue(TouristRegionStatisticsVO china) {
        // TODO 获取默认值的逻辑，可以优化到MsmAdministrativeAreaHelper这个工具类里面
        final Map<String, Set<String>> nameMap = MsmAdministrativeAreaHelper.getNameMap();
        final Map<String, List<TouristRegionStatisticsVO>> currentMap = china.getChildren().stream().collect(Collectors.toMap(TouristRegionStatisticsVO::getRegionName, TouristRegionStatisticsVO::getChildren));
        nameMap.forEach((provName, cities) -> {
            RegionTypeEnum typeEnum = MsmAdministrativeAreaHelper.isOutsideProvince(provName) ? RegionTypeEnum.OUTSIDE : RegionTypeEnum.INSIDE;
            // 如果当前有这个省，就去判断是否有城市
            if(currentMap.containsKey(provName)){
                // 当前的城市名称
                final Set<String> currentProvCitesNameSet = currentMap.get(provName).stream().map(TouristRegionStatisticsVO::getRegionName).collect(Collectors.toSet());
                cities.forEach(cityName -> {
                    if(!currentProvCitesNameSet.contains(cityName)){
                        final TouristRegionStatisticsVO addCity = TouristRegionStatisticsVO.createDefault(typeEnum);
                        addCity.setRegionName(cityName);
                        currentMap.get(provName).add(addCity);
                    }
                });
            // 如果当前没有这个省，就连省和城市一起添加
            }else{
                final TouristRegionStatisticsVO addProv = TouristRegionStatisticsVO.createDefault(typeEnum);
                addProv.setRegionName(provName);
                cities.forEach(cityName -> {
                    final TouristRegionStatisticsVO addCity = TouristRegionStatisticsVO.createDefault(typeEnum);
                    addCity.setRegionName(cityName);
                    addProv.getChildren().add(addCity);
                });
                china.getChildren().add(addProv);
            }
        });

    }


    private List<TouristRegionStatisticsVO>  doInsideRegionStatistics(LocalDate begin, LocalDate end, String scenicName, TouristSourceFlagType flagType){
        String beginString = DateUtil.localDateToString(begin);
        String endString = DateUtil.localDateToString(end);

        final IndexWrapper<TouristSourceCityData> wrapper = IndexWrapperFactory.getWrapper(TouristSourceCityData.class);
        // 构建bool
        final BoolQueryBuilder boolQueryBuilder = wrapper.boolQuery(wrapper.rangeQuery(TouristSourceCityData::getTime, beginString, endString), wrapper.termQuery(TouristSourceCityData::getScenicName, scenicName))
                .must(wrapper.termQuery(TouristSourceCityData::getFlag, flagType.getType()));

        // 构建聚合
        final TermsAggregationBuilder provTermAgg = wrapper.termsAgg(TouristSourceCityData::getProvName, 50);
        final TermsAggregationBuilder cityTermAgg = wrapper.termsAgg(TouristSourceCityData::getCityName, 100);
        final SumAggregationBuilder peopleNumSumAgg = wrapper.sumAgg(TouristSourceCityData::getPeopleNum);
        // 聚合层次排列
        provTermAgg.subAggregation(cityTermAgg.subAggregation(peopleNumSumAgg)).subAggregation(peopleNumSumAgg);
        // 最后拼接
        final NativeSearchQuery nativeSearchQuery = wrapper.nativeSearchQueryBuilder(boolQueryBuilder, provTermAgg).build();

        final Aggregations aggregations = elasticsearchTemplate.query(nativeSearchQuery, SearchResponse::getAggregations);

        // TODO 待优化
        List<TouristRegionStatisticsVO> list = new ArrayList<>();
        final StringTerms provTermsResult = aggregations.get(provTermAgg.getName());
        if(hasBuckets(provTermsResult)){
            final List<StringTerms.Bucket> buckets = provTermsResult.getBuckets();
            buckets.forEach(b -> {
                final TouristRegionStatisticsVO vo = TouristRegionStatisticsVO.createDefault(RegionTypeEnum.INSIDE);
                vo.setRegionName(b.getKeyAsString());
                final InternalAggregations subAggregations = (InternalAggregations) b.getAggregations();
                if(Objects.nonNull(subAggregations)){
                    final InternalSum provPeopleSum = subAggregations.get(peopleNumSumAgg.getName());
                    vo.setPeopleNum(defaultZeroValueIfNull(provPeopleSum));

                    final StringTerms cityTermsResult = subAggregations.get(cityTermAgg.getName());
                    if(hasBuckets(cityTermsResult)){
                        cityTermsResult.getBuckets().forEach(subB -> {
                            TouristRegionStatisticsVO subVo = TouristRegionStatisticsVO.createDefault(RegionTypeEnum.INSIDE);
                            subVo.setRegionName(subB.getKeyAsString());
                            final InternalAggregations subSubAggregations =  (InternalAggregations) subB.getAggregations();
                            if(Objects.nonNull(subSubAggregations)){
                                final InternalSum cityPeopleSum = subSubAggregations.get(peopleNumSumAgg.getName());
                                subVo.setPeopleNum(defaultZeroValueIfNull(cityPeopleSum));
                            }
                            vo.getChildren().add(subVo);
                        });
                    }
                }
                list.add(vo);
            });
        }
        return list;
    }


    private List<TouristRegionStatisticsVO>  doOutSideRegionStatistics(LocalDate begin, LocalDate end, String scenicName, TouristSourceFlagType flagType) {
        String beginString = DateUtil.localDateToString(begin);
        String endString = DateUtil.localDateToString(end);

        final IndexWrapper<TouristSourceCountryData> wrapper = IndexWrapperFactory.getWrapper(TouristSourceCountryData.class);
        // 构建bool
        final BoolQueryBuilder boolQueryBuilder = wrapper.boolQuery(wrapper.rangeQuery(TouristSourceCountryData::getTime, beginString, endString))
                .must(wrapper.termQuery(TouristSourceCountryData::getScenicName, scenicName))
                .must(wrapper.termQuery(TouristSourceCountryData::getFlag, flagType.getType()))
                .mustNot(wrapper.termQuery(TouristSourceCountryData::getCountryName, "中国"));
        // 构建聚合
        final TermsAggregationBuilder countryTermsAgg = wrapper.termsAgg(TouristSourceCountryData::getCountryName, 300);
        final SumAggregationBuilder peopleNumSumAgg = wrapper.sumAgg(TouristSourceCountryData::getPeopleNum);
        // 聚合层次排列
        countryTermsAgg.subAggregation(peopleNumSumAgg);

        //最后拼接
        final NativeSearchQueryBuilder nativeSearchQueryBuilder = wrapper.nativeSearchQueryBuilder(boolQueryBuilder, countryTermsAgg);

        final Aggregations aggregations = elasticsearchTemplate.query(nativeSearchQueryBuilder.build(), SearchResponse::getAggregations);

        List<TouristRegionStatisticsVO> list = new ArrayList<>();
        final StringTerms countryTermsResult = aggregations.get(countryTermsAgg.getName());

        if(hasBuckets(countryTermsResult)) {
            final List<StringTerms.Bucket> buckets = countryTermsResult.getBuckets();

            buckets.forEach(b -> {
                final TouristRegionStatisticsVO vo = TouristRegionStatisticsVO.createDefault(RegionTypeEnum.OUTSIDE);
                vo.setRegionName(b.getKeyAsString());
                final InternalAggregations subAggregations = (InternalAggregations) b.getAggregations();

                if (Objects.nonNull(subAggregations)) {
                    final InternalSum provPeopleSum = subAggregations.get(peopleNumSumAgg.getName());
                    vo.setPeopleNum(defaultZeroValueIfNull(provPeopleSum));
                }

                list.add(vo);
            });

        }
        return list;

    }





    private Long defaultZeroValueIfNull(Sum sum){
        return Objects.isNull(sum) ? 0L : doubleToDefaultLong(sum.getValue());
    }


    private Long doubleToDefaultLong(Double d){
        return Objects.isNull(d) ? 0L : d.longValue();
    }


    private boolean hasBuckets(MultiBucketsAggregation aggregation){
        return Objects.nonNull(aggregation) && !CollectionUtils.isEmpty(aggregation.getBuckets());
    }


    /**
     * 校验游客区域编码
     * @param touristScopeCode 游客区域编码
     */
    private static void checkTouristScopeCode(String touristScopeCode){
        DataDictionaryHelper.check(DictionaryCodeEnum.TOURISTS_SCOPE, touristScopeCode);
    }
}
