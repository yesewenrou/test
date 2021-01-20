package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.common;

/**
 * @author LiuYin
 * @InterfaceName BaseIndex
 * @description
 * @date 2020/3/24 16:54
 */
public interface BaseIndex {

    /** 默认的keyword名称*/
    String DEFAULT_KEY_WORD_SUFFIX = ".keyword" ;

    /** 索引名称*/
    String indexName();

    /** 字段名称*/
    String filedName();

    /** 字段是否是keyword*/
    boolean isKeyword();

    /**
     * 获取字段keyword名称
     * @return keyword name
     */
    default String keyword(){
        return isKeyword() ? filedName() : filedName() + DEFAULT_KEY_WORD_SUFFIX;
    }

    /**
     * 获取keyword名称
     * @param subFieldName 子字段（keyword）字段名称
     * @return keyword name
     */
    default String keyword(String subFieldName){
        return filedName() + "." + subFieldName;
    }

}
