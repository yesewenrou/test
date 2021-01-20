package net.cdsunrise.hy.lyyt.service;

import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;

import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

/**
 * 调用服务
 * @author YQ on 2019/11/15.
 */
public interface TransferService {
    /**
     * 正常调用
     * @param dataTypes 调用的模快
     */
    void normalTransfer(Set<DataTypeEnum> dataTypes);

    /**
     * 异常调用
     */
    void errorTransfer();

    /**
     * 普通调用
     * @param dataType 模块
     * @param date 时间
     * @return 计数
     */
    Long normal(DataTypeEnum dataType, Date date);

    /**
     * 普通调用统计
     * @param dataType 模块
     * @return 计数
     */
    Long normalTotal(DataTypeEnum dataType);

    /**
     * 异常调用
     * @param date 时间
     * @return 计数
     */
    Long error(Date date);

    /**
     * 错误调用
     * @return 计数
     */
    Long errorTotal();

    /**
     * 初始化某一天的数据
     * @param toLocalDate toLocalDate
     */
    void initDayCount(LocalDate toLocalDate);
}
