package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.tool.service.impl;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.tool.entity.TouristSourceProvData;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.tool.service.ITouristSourceProvService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.tool.vo.TouristSourceProvExportRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author lijiafeng
 * @date 2020/1/19 13:03
 */
@Service
public class TouristSourceProvServiceImpl implements ITouristSourceProvService {

    private ElasticsearchTemplate elasticsearchTemplate;

    public TouristSourceProvServiceImpl(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public String exportDay(TouristSourceProvExportRequest request) {
        Date startDate = request.getStartDate();
        Date endDate = request.getEndDate();
        String startDateString = DateUtil.format(startDate, DateUtil.PATTERN_YYYY_MM_DD);
        String endDateString = DateUtil.format(endDate, DateUtil.PATTERN_YYYY_MM_DD);
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.rangeQuery("time").gte(startDateString).lte(endDateString))
                .must(QueryBuilders.termQuery("flag", "day"));
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder();
        searchQueryBuilder.withIndices("tourist_source_prov").withTypes("doc")
                .withQuery(queryBuilder)
                .withPageable(PageRequest.of(0, 10000))
                .withSort(SortBuilders.fieldSort("time").order(SortOrder.ASC));

        List<TouristSourceProvData> list = elasticsearchTemplate.queryForList(searchQueryBuilder.build(), TouristSourceProvData.class);
        StringBuilder stringBuilder = new StringBuilder("景区,国家,省份,人数,日期");
        list.forEach(touristSourceProvData -> {
            stringBuilder.append(System.lineSeparator())
                    .append(touristSourceProvData.getScenicName()).append(",")
                    .append(touristSourceProvData.getCountryName()).append(",")
                    .append(touristSourceProvData.getProvName()).append(",")
                    .append(touristSourceProvData.getPeopleNum()).append(",")
                    .append(touristSourceProvData.getTime());
        });
        return stringBuilder.toString();
    }
}
