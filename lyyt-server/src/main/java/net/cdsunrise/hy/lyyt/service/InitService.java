package net.cdsunrise.hy.lyyt.service;

/**
 * @author liuyin
 */
public interface InitService {

    /**
     * 初始化数据类型
     * @return 数据类型条数
     */
    Long initDataType();

    /**
     * 初始化数据来源
     * @return
     */
    Long initDataSource();


    /**
     * 初始化数据库类型
     */
    Long initDataBaseType();
}
