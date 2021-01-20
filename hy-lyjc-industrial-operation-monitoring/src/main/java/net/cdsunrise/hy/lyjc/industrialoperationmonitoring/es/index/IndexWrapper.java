package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.index;


import lombok.Data;
import net.cdsunrise.common.utility.function.PropertyFunction;
import net.cdsunrise.common.utility.utils.PropertyLambdaUtil;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.aggregations.AbstractAggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.sum.SumAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.tophits.TopHitsAggregationBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * IndexWrapper
 *
 * @author LiuYin
 * @date 2020/3/24 17:41
 */
public class IndexWrapper<T> {

    private static final String DEFAULT_DOC_TYPE = "_doc";

    private static final String DEFAULT_KEY_WORD = ".keyword";

    private static final String TERMS_AGG_PREFIX = "terms_";
    private static final String SUM_AGG_PREFIX = "sum_";
    private static final String TOP_HIT_AGG_PREFIX = "top_hit_";
    private static final String DATE_HISTOGRAM_AGG_PREFIX = "date_histogram_";

    private Map<String, DocumentField> FIELD_MAP;

    private Class<T> clazz;

    private String indexName;

    private String documentType;

    public IndexWrapper(Class<T> clazz) {
        load(clazz);
    }

    private void load(Class<T> clazz){
        Objects.requireNonNull(clazz, "clazz is null");
        if(Objects.nonNull(getClazz())){
            throw new RuntimeException("class is already load is this object");
        }
        setClazz(clazz);

        final Document annotation = clazz.getAnnotation(Document.class);
        boolean noAnnotation = Objects.isNull(annotation);

        setIndexName(noAnnotation || isBlank(annotation.indexName()) ? clazz.getSimpleName() : annotation.indexName().trim());
        setDocumentType(noAnnotation || isBlank(annotation.type()) ? getDefaultDocType() : annotation.type().trim());

        final Field[] declaredFields = clazz.getDeclaredFields();
        if(declaredFields.length == 0){
            throw new RuntimeException("at least one field exists");
        }
        loadFields(declaredFields);
    }


    private void loadFields(Field[] fields){
        Map<String, DocumentField> map = new HashMap<>(fields.length);
        for (Field field : fields) {
            if(!isStatistic(field)){
                final DocumentField documentField = createDocumentField(field);
                map.putIfAbsent(documentField.getName(), documentField);
            }
        }
        FIELD_MAP = Collections.unmodifiableMap(map);
    }

    private static boolean isStatistic(Field field){
       return Modifier.isStatic(field.getModifiers());
    }


    private static DocumentField createDocumentField(Field field){
        final DocumentField documentField = new DocumentField();
        final org.springframework.data.elasticsearch.annotations.Field annotation = field.getAnnotation(org.springframework.data.elasticsearch.annotations.Field.class);

        documentField.setName(field.getName());
        documentField.setType(Objects.nonNull(annotation) ? annotation.type() : FieldType.Auto);
        documentField.setKeyword(FieldType.Keyword.equals(documentField.getType()));
        return documentField;
    }


    @Data
    private static class DocumentField{
        private String name;
        private FieldType type;
        private boolean isKeyword;
    }


    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public String getIndexName() {
        return indexName;
    }

    public Map<String, DocumentField> getFieldMap() {
        return FIELD_MAP;
    }

    public static String getDefaultDocType() {
        return DEFAULT_DOC_TYPE;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    /**
     * 设置索引名称
     * @param indexName 索引名称
     **/
    public void setIndexName(String indexName) {
        if(isBlank(indexName)){
            throw new RuntimeException("index name is null or blank");
        }
        if(Objects.nonNull(this.indexName)){
            throw new RuntimeException("index name is already set by [" + indexName + "], can not be modified");
        }
        this.indexName = indexName.trim();
    }

    /**
     * 词条查询
     * @param propertyFunction 字段get方法
     * @param v 词条的值
     * @param <V> 泛型
     * @return TermQueryBuilder
     */
    public <V> TermQueryBuilder termQuery(PropertyFunction<T, V> propertyFunction, V v){
        return QueryBuilders.termQuery(getKeywordName(propertyFunction), v);
    }

    /**
     * 多词条查询
     * @param propertyFunction 字段get方法
     * @param vs 多个值
     * @param <V> 泛型
     * @return TermsQueryBuilder
     */
    public <V> TermsQueryBuilder termsQuery(PropertyFunction<T,V> propertyFunction, V... vs){
        return QueryBuilders.termsQuery(getKeywordName(propertyFunction), vs);
    }


    /**
     * 范围查询，默认左闭右开
     * @param propertyFunction 字段get方法
     * @param from 大于等于
     * @param to 小于
     * @param <V> 泛型
     * @return RangeQueryBuilder
     */
    public <V> RangeQueryBuilder rangeQuery(PropertyFunction<T,V> propertyFunction, V from, V to){
        final RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery(getFieldName(propertyFunction));
        rangeQueryBuilder.gte(from).lt(to);
        return rangeQueryBuilder;
    }


    public <V> TermsAggregationBuilder termsAgg(PropertyFunction<T,V> propertyFunction, int size){
        final String keywordName = getKeywordName(propertyFunction);
        return AggregationBuilders.terms(termsAggName(keywordName)).field(keywordName).size(size);
    }

    public <V> SumAggregationBuilder sumAgg(PropertyFunction<T, V> propertyFunction){
        final String fieldName = getFieldName(propertyFunction);
        return AggregationBuilders.sum(sumAggName(fieldName)).field(fieldName);
    }

    public <V> TopHitsAggregationBuilder topHitsAgg(PropertyFunction<T, V> propertyFunction, int size, SortOrder order){
        final String fieldName = getFieldName(propertyFunction);
        return AggregationBuilders.topHits(topHitAggName(fieldName)).size(size).sort(fieldName, order);
    }

    public <V> DateHistogramAggregationBuilder dateHistogramAgg(PropertyFunction<T, V> propertyFunction, DateHistogramInterval interval, String format){
        final String fieldName = getFieldName(propertyFunction);
        return AggregationBuilders.dateHistogram(dateHistogramAggName(fieldName)).field(fieldName).dateHistogramInterval(interval).format(format);
    }



    public BoolQueryBuilder boolQuery(QueryBuilder filter){
        return boolQuery(filter, null, null, null);
    }

    public BoolQueryBuilder boolQuery(QueryBuilder filter, QueryBuilder must){
        return boolQuery(filter, must, null, null);
    }

    public BoolQueryBuilder boolQuery(QueryBuilder filter, QueryBuilder must, QueryBuilder mustNot, QueryBuilder should){
        final BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        runIfNotNull(filter, () -> boolQueryBuilder.filter(filter));
        runIfNotNull(must, () -> boolQueryBuilder.must(must));
        runIfNotNull(mustNot, () -> boolQueryBuilder.mustNot(mustNot));
        runIfNotNull(should, () -> boolQueryBuilder.should(should));
        return boolQueryBuilder;
    }

    public NativeSearchQueryBuilder nativeSearchQueryBuilder(QueryBuilder query, AbstractAggregationBuilder aggBuilder){
        final NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        nativeSearchQueryBuilder.withIndices(getIndexName());
        runIfNotNull(query, () -> nativeSearchQueryBuilder.withQuery(query));
        runIfNotNull(aggBuilder, () -> nativeSearchQueryBuilder.addAggregation(aggBuilder));
        return nativeSearchQueryBuilder;
    }


    private static void runIfNotNull(Object o, Runnable run){
        if(Objects.nonNull(o)){
            run.run();
        }
    }


    private static boolean isBlank(String str){
        return Objects.isNull(str) || str.trim().isEmpty();
    }


    private String getFieldName(PropertyFunction<T, ?> propertyFunction){
        if(Objects.isNull(propertyFunction)){
           throw new RuntimeException("property function is null");
        }
        return PropertyLambdaUtil.getPropertyNameList(propertyFunction).get(0);

    }

    private String getKeywordName(PropertyFunction<T, ?> propertyFunction){
        final String fieldName = getFieldName(propertyFunction);
        final DocumentField documentField = getFieldMap().get(fieldName);
        return documentField.isKeyword() ? fieldName : (fieldName + DEFAULT_KEY_WORD);
    }


    public static String termsAggName(String fieldName){
        return TERMS_AGG_PREFIX + fieldName;
    }

    public static String sumAggName(String fieldName){
        return SUM_AGG_PREFIX + fieldName;
    }

    public static String topHitAggName(String fieldName){
        return TOP_HIT_AGG_PREFIX + fieldName;
    }

    public static String dateHistogramAggName(String fieldName){
        return DATE_HISTOGRAM_AGG_PREFIX + fieldName;
    }



}
