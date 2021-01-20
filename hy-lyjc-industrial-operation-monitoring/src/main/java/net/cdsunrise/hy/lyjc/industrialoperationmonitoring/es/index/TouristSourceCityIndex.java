package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.index;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.common.BaseIndex;

/**
 * 索引枚举
 */
public enum TouristSourceCityIndex implements BaseIndex {

    city_Name("cityName", true),
    country_name("countryName",true),
    datasource("datasource",true),
    flag("flag",true),
    id("id",true),
    peopleNum("peopleNum",true),
    provName("provName",true),
    scenicName("scenicName",true),
    time("time",false),
    ;

    /** 索引名称*/
    private String fieldName;
    /** 是否是keyword类型*/
    private boolean isKeyword;


    /** 索引名称*/
    static final String INDEX_NAME = "tourist_source_city";

    TouristSourceCityIndex(String fieldName, boolean isKeyword) {
        this.fieldName = fieldName;
        this.isKeyword = isKeyword;
    }

    /**
     * 索引名称
     */
    @Override
    public String indexName() {
        return INDEX_NAME;
    }

    /**
     * 字段名称
     */
    @Override
    public String filedName() {
        return fieldName;
    }

    /**
     * 字段是否是keyword
     */
    @Override
    public boolean isKeyword() {
        return isKeyword;
    }
}
