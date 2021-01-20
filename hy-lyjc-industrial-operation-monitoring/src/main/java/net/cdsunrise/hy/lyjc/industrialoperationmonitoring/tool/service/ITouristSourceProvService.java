package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.tool.service;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.tool.vo.TouristSourceProvExportRequest;

/**
 * @author lijiafeng
 * @date 2020/1/19 12:59
 */
public interface ITouristSourceProvService {

    /**
     * 导出天数据
     *
     * @param request 请求
     * @return 结果
     */
    String exportDay(TouristSourceProvExportRequest request);
}
