package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util;

import net.cdsunrise.common.utility.AssertUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.SpecialReport;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.StringLongVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.NumberUnitEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.SpecialReportWordData;
import net.cdsunrise.hy.lyjtglserverstarter.entity.vo.NameValue;
import org.apache.ibatis.javassist.compiler.ast.StringL;
import org.springframework.core.convert.converter.ConvertingComparator;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * TrafficReportUtil
 * 交通报告工具类
 *
 * @author LiuYin
 * @date 2020/5/12 9:35
 */
public final class TrafficReportUtil {

    /**
     * 道路拥堵top2
     */
    private static final String KEY_JAM_TOP_2 = "TA.heavyArterialTraffic";
    /**
     * 入城趋势
     */
    private static final String KEY_IN_CITY_FLOW_TREND = "TA.lineTrafficFlowTrend";
    /**
     * 入城总数
     */
    private static final String KEY_IN_CITY_FLOW_TOTAL = "TA.trafficFlow";
    /**
     * 进县域省内top5
     */
    private static final String KEY_IN_CITY_IN_PROVINCE_SOURCE_TOP_5 = "TA.top5TrafficSourceInProvince";
    /**
     * 进县域省外top5
     */
    private static final String KEY_IN_CITY_OUT_PROVINCE_SOURCE_TOP_5 = "TA.top5TrafficSourceOutsideProvince";


    private static final String[] KEYS = new String[]{KEY_JAM_TOP_2, KEY_IN_CITY_FLOW_TREND, KEY_IN_CITY_FLOW_TOTAL, KEY_IN_CITY_IN_PROVINCE_SOURCE_TOP_5, KEY_IN_CITY_OUT_PROVINCE_SOURCE_TOP_5};


    /**
     * 处理交通报告map
     * @param reportMap 报告map
     * @return 交通分析对象
     */
    public static SpecialReportWordData.TrafficAnalysis handleTrafficReportMap(Map<String, List<NameValue<?, ?>>> reportMap) {
        AssertUtil.notNull(reportMap, () -> new RuntimeException("reportMap is null"));

        final SpecialReportWordData.TrafficAnalysis analysis = new SpecialReportWordData.TrafficAnalysis();
        final Map<String, List<StringLongVO>> dataMap = createDataMap(reportMap);
        // 保留原始数据
        analysis.setData(dataMap);

        // 处理拥堵top2
        analysis.setHeavyArterialTraffic(handleJamMessage(dataMap.get(KEY_JAM_TOP_2)));

        // 处理趋势图
        analysis.setLineTrafficFlowTrend(dataMap.get(KEY_IN_CITY_FLOW_TREND).stream().sorted(Comparator.comparing(o -> DateUtil.stringToLocalDate(o.getName()))).collect(Collectors.toList()));

        // 处理总数
        final List<StringLongVO> total = dataMap.get(KEY_IN_CITY_FLOW_TOTAL);
        analysis.setTrafficFlow(total.isEmpty() ? "0" : NumberUnitEnum.getDescription(total.get(0).getValue()));

        // 处理进县域省内top5
        analysis.setTop5TrafficSourceInProvince(handleInCitySource(dataMap.get(KEY_IN_CITY_IN_PROVINCE_SOURCE_TOP_5)));
        // 处理进县域省外top5
        analysis.setTop5TrafficSourceOutsideProvince(handleInCitySource(dataMap.get(KEY_IN_CITY_OUT_PROVINCE_SOURCE_TOP_5)));

        return analysis;
    }


    /**
     * 处理进县域车辆来源数据
     * @param list list
     * @return string
     */
    private static String handleInCitySource(List<StringLongVO> list){
        if(Objects.isNull(list)){
            return "无";
        }
        StringBuilder sb = new StringBuilder();
        final int size = list.size();
        for(int i = 0; i < size ; i ++){
            final StringLongVO vo = list.get(i);
            sb.append(vo.getName()).append("（").append(NumberUnitEnum.getDescription(vo.getValue())).append("辆）");
            if( i < size -1){
                sb.append("，");
            }else{
                sb.append("。");
            }
        }
        return sb.toString();
    }


    /**
     * 处理拥堵信息
     * @param list list
     * @return string
     */
    private static String handleJamMessage(List<StringLongVO> list){
        if(Objects.isNull(list)){
            return "无";
        }

        StringBuilder sb = new StringBuilder();
        final int size = list.size();
        for(int i = 0; i < size ; i ++){
            final StringLongVO vo = list.get(i);
            sb.append(replaceBrackets(vo.getName())).append("（").append("累计拥堵").append(TimeDurationUtil.getTimeMessage(vo.getValue())).append("）");
            if( i < size -1){
                sb.append("，");
            }else{
                sb.append("。");
            }
        }
        return sb.toString();
    }

    /**
     * 把半角英文括号替换成半角中文括号
     * @param target 目标字符串
     * @return 替换后的字符串
     */
    private static String replaceBrackets(String target){
        if(Objects.isNull(target)){
            return null;
        }
        if(target.isEmpty()){
            return target;
        }
        return target.replace("(","（").replace(")","）");
    }


    /**
     * 创建数据map
     * @param reportMap 报告map
     * @return map
     */
    private static Map<String, List<StringLongVO>> createDataMap(Map<String, List<NameValue<?, ?>>> reportMap) {
        final HashMap<String, List<StringLongVO>> map = new HashMap<>(reportMap.size());
        for (String key : KEYS) {
            final List<NameValue<?, ?>> nameValues = reportMap.get(key);
            map.put(key, Objects.isNull(nameValues) ? new ArrayList<>(0) : toSortedList(nameValues));
        }
        return map;
    }

    /**
     * 转换成已经排序的list
     * @param nvs name value list
     * @return list
     */
    private static List<StringLongVO> toSortedList(List<NameValue<?, ?>> nvs) {
        return nvs.stream().map(TrafficReportUtil::convert).sorted(getComparator()).collect(Collectors.toList());
    }

    /**
     * 得到比较器
     * @return comparator
     */
    private static Comparator<StringLongVO> getComparator() {
        return (o1, o2) -> {
            final int compare = Long.compare(o2.getValue(), o1.getValue());
            return compare != 0 ? compare : o1.getName().compareTo(o2.getName());
        };
    }

    /**
     * 转换成string long
     * @param nv name value
     * @return string long
     */
    private static StringLongVO convert(NameValue<?, ?> nv) {
        final StringLongVO vo = new StringLongVO();
        vo.setName((String) nv.getName());
        final Object value = nv.getValue();
        if(value instanceof Integer){
            vo.setValue((long) (Integer) value);
        }else{
            vo.setValue((Long) value);
        }
        return vo;
    }

}
