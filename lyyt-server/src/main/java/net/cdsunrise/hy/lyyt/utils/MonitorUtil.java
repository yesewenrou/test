package net.cdsunrise.hy.lyyt.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import net.cdsunrise.common.utility.utils.JsonUtils;
import net.cdsunrise.hy.lyyt.domain.dto.MonitorSiteDTO;
import net.cdsunrise.hy.lyyt.domain.dto.TrafficFlowConfigDTO;
import net.cdsunrise.hy.lyyt.entity.vo.MonitorSiteVO;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 监控点工具类
 *
 * @author YQ on 2019/12/21.
 */
public class MonitorUtil {
    private static final String LYJTGL_MONITOR_BASE_INFO = "LYJTGL_RESOURCE_MONITOR";


    /**
     * 进县城的卡口编号
     */
    private static final Set<String> IN_CITY_MONITOR_CODE = Collections.unmodifiableSet(initInCityMonitorCode());


    /**
     * 获取所有监控点map
     *
     * @return map
     */
    public static Map<String, MonitorSiteVO> getMonitorMap() {
        Map<String, String> entries = RedisUtil.hashOperations().entries(LYJTGL_MONITOR_BASE_INFO);
        return entries.entrySet().stream()
                .map(e -> JsonUtils.toObject(e.getValue(), new TypeReference<MonitorSiteVO>() {
                }))
                .sorted(Comparator.comparing(MonitorSiteVO::getMonitorSiteId))
                .collect(Collectors.toMap(MonitorSiteVO::getMonitorSiteId, v -> v, (e1, e2) -> e1, LinkedHashMap::new));

    }

    /**
     * 获取所有进县城的监控点编码
     *
     * @return set
     */
    public static Set<String> getInCityMonitorCodeSet() {
        return IN_CITY_MONITOR_CODE;
    }


    /**
     * 初始化进县城的卡口编号
     *
     * @return set
     */
    private static Set<String> initInCityMonitorCode() {
        String[] s = {"KK-01", "KK-02", "KK-03", "KK-04", "KK-05", "KK-06", "KK-07",
                "KK-09", "KK-11"};
        return Arrays.stream(s).collect(Collectors.toSet());
    }




}
