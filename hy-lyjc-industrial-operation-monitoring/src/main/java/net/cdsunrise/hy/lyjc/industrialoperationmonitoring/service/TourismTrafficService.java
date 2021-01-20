package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import org.springframework.http.ResponseEntity;

/**
 * @author LHY
 */
public interface TourismTrafficService {

    /**
     * 带搜索条件导出Excel
     */
    ResponseEntity<byte[]> export(Long beginTime, Long endTime, String provinceName, String cityName);

    /**
     * 历史统计带搜索条件导出Excel
     */
    ResponseEntity<byte[]> historyExport(Long beginTime, Long endTime, Integer graininess, String provinceName, String cityName);
}
