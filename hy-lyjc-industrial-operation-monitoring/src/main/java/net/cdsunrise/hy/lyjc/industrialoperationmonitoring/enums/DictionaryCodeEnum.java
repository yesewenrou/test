package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

/**
 * 数据字典枚举类
 * @author liuyin
 */
public enum DictionaryCodeEnum {

//    /** 所属区域*/
//    REGION,
//    /** 旅游厕所项目类型*/
//    TOILET_PROJECT_TYPE,
//    /** 旅游厕所建造性质*/
//    TOILET_CONSTRUCTION_NATURE,
//    /** 旅游厕所等级*/
//    TOILET_LEVEL,
//    /** 文物保护级别*/
//    CULTURAL_PROTECTION_LEVEL,
//    /** 学历*/
//    EDUCATION,
//    /** 性别*/
//    SEX,
//    /** 银行类型*/
//    BANK_TYPE,
//    /** 重点建设项目类型 */
//    BUILD_TYPE,
//    /** 住宿类别*/
//    ACCOMMODATION_CATEGORY,
//    /** 住宿星级*/
//    ACCOMMODATION_STAR,
//    /** 行业类型*/
//    INDUSTRY_TYPE,
//    /** 关联对象 */
//    RELATION_OBJECT,
//    /** 景区*/
//    SCENIC_AREA,
//    /** 监控类型 */
//    MONITOR_TYPE,
    /** 来源范围*/
    TOURISTS_SCOPE
    ;

    public String code(){
        return name().toUpperCase();
    }


}
