package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.utils.JsonUtils;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req.DataCenterMonthlyReportReq;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.ScenicReportVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.StringLongVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.DataCenterReportService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.ISpecialReportService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.WordGeneratorUtils;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.KeyAndValue;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.SpecialReportWordData;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TourismConsumptionIndustryAnalyzeVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TourismConsumptionSourceAnalyzeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.GenerateChartUtils.generate;

/**
 * @author zbk
 * @date 2020/5/14 14:12
 */
@SuppressWarnings("all")
@Slf4j
@Service
public class DataCenterReportServiceImpl implements DataCenterReportService {
    @Value("${chart.phantomjs.path}")
    private String binDir;
    @Value("${chart.phantomjs.temp}")
    private String workDir;

    @Autowired
    ISpecialReportService specialReportService;

    public static String PIC1 = "{title:[{text:'{name|'+'县域客流量'+'}\\n{val|'+allPassenger+'}',top:'center',left:'center',textStyle:{rich:{name:{fontSize:24,fontWeight:'normal',color:'#666666',padding:[10,0]},val:{fontSize:32,fontWeight:'bold',color:'#333333',}}}}],\"legend\":{bottom:55,\"orient\":\"vertical\",\"bottom\":\"center\",\"icon\":\"circle\",\"left\":\"75%\",\"data\":[\"省内\",\"省外\",\"境外\"],\"itemGap\":25,\"selectedMode\":false,\"textStyle\":{\"color\":\"#607A8D\",\"rich\":{\"a\":{},\"b\":{\"align\":\"right\"},\"c\":{\"align\":\"right\"}}}},series:[{type:'pie',center:['50%','50%'],radius:['60%','75%'],clockwise:true,avoidLabelOverlap:true,hoverOffset:15,itemStyle:{normal:{color:function(params){var colorList=[\"#40A9FF\",\"#F2B50E\",\"#F655AA\",\"#9254DE\",\"#597EF7\",\"#13C2C2\",\"#6CBA52\",\"#097C25\"];return colorList[params.dataIndex];}}},label:{show:true,position:'outside',formatter:'{a|{b}：{d}%}\\n{hr|}',rich:{hr:{backgroundColor:'t',borderRadius:3,width:3,height:3,padding:[3,3,0,-12]},a:{padding:[-30,15,-20,15]}}},labelLine:{normal:{length:20,length2:30,lineStyle:{width:1}}},data:[{name:\"省内\",value:provinceInner},{name:\"省外\",value:provinceOuter},{name:\"境外\",value:countryOuter}],}]}";
    public static String PIC2 = "{\"color\":[\"#38A0FF\",\"#4CCA73\"],\"tooltip\":{\"trigger\":\"axis\",\"axisPointer\":{\"type\":\"shadow\"}},\"legend\":{\"bottom\":\"bottom\"},\"grid\":{\"left\":\"3%\",\"right\":\"4%\",\"bottom\":\"15%\",\"containLabel\":true},\"xAxis\":{\"type\":\"value\"},\"yAxis\":{\"name\":\"单位：人次\",\"type\":\"category\",\"data\":barNameArray},\"series\":[{\"type\":\"bar\",\"data\":barValueArray,\"itemStyle\":{\"normal\":{\"label\":{\"show\":true,\"position\":\"right\"}}}}]}";
    public static String PIC3 = "{\"color\":[\"#38A0FF\",\"#4CCA73\"],\"tooltip\":{\"trigger\":\"axis\",\"axisPointer\":{\"type\":\"shadow\"}},\"legend\":{\"bottom\":\"bottom\"},\"grid\":{\"left\":\"3%\",\"right\":\"4%\",\"bottom\":\"15%\",\"containLabel\":true},\"xAxis\":{\"type\":\"value\"},\"yAxis\":{\"name\":\"单位：人次\",\"type\":\"category\",\"data\":barNameArray},\"series\":[{\"type\":\"bar\",\"data\":barValueArray,\"itemStyle\":{\"normal\":{\"label\":{\"show\":true,\"position\":\"right\"}}}}]}";
    public static String PIC4 = "{\"color\":[\"#FFD045\",\"#6600FF\",\"#597EF7\",\"#00BE8A\",\"#1FA8F8\",\"#F759AB\",\"#FF9933\"],\"title\":{\"show\":true,\"link\":\"\",\"target\":\"self\",\"text\":\"单位:人\",\"x\":\"left\",\"padding\":[5,0,20,8],\"textStyle\":{\"fontSize\":12,\"fontWeight\":\"normal\",\"fontFamily\":\"Microsoft Yahei\"}},\"tooltip\":{\"trigger\":\"axis\",\"padding\":[10,10,10,10],\"axisPointer\":{\"type\":\"shadow\"}},\"legend\":{\"padding\":[15,0,0,0],\"itemGap\":5,\"itemWidth\":25,\"itemHeight\":12,\"width\":\"auto\",\"show\":true,\"left\":\"auto\",\"right\":\"50\",\"bottom\":\"auto\",\"top\":\"0\",\"data\":[\"洪雅县\",\"主城区\",\"柳江古镇\",\"七里坪\",\"瓦屋山\",\"玉屏山\",\"槽渔滩\"],\"textStyle\":{\"color\":\"#607A8D\",\"fontSize\":14,\"fontFamily\":\"Microsoft Yahei\"},\"botom\":20},\"grid\":{\"top\":\"13%\",\"left\":10,\"right\":\"5%\",\"bottom\":15,\"containLabel\":true},\"xAxis\":[{\"name\":\"\",\"type\":\"category\",\"boundaryGap\":false,\"data\":lineNameArray,\"axisLine\":{\"lineStyle\":{\"color\":\"#E9EEF2\"}},\"axisLabel\":{\"color\":\"#88A3B4\",\"textStyle\":{\"fontSize\":12,\"fontFamily\":\"Microsoft Yahei\"}},\"axisTick\":{\"show\":false},\"splitLine\":{\"lineStyle\":{\"color\":\"#DDE0E5\"}}}],\"yAxis\":[{\"type\":\"value\",\"data\":[],\"name\":\"\",\"axisLine\":{\"lineStyle\":{\"color\":\"#E9EEF2\"}},\"axisTick\":{\"show\":false},\"axisLabel\":{\"color\":\"#88A3B4\",\"textStyle\":{\"fontSize\":12,\"fontFamily\":\"Microsoft Yahei\"}},\"splitLine\":{\"lineStyle\":{\"color\":\"#F7F9FA\"}}},{\"type\":\"value\",\"name\":\"\",\"data\":[],\"axisLine\":{\"show\":false,\"lineStyle\":{\"color\":\"#E9EEF2\"}},\"axisLabel\":{\"show\":false,\"color\":\"#88A3B4\",\"textStyle\":{\"fontSize\":12,\"fontFamily\":\"Microsoft Yahei\"}},\"splitLine\":{\"show\":false,\"lineStyle\":{\"color\":\"#F7F9FA\"}}}],\"series\":[{\"type\":\"line\",\"symbolSize\":6,\"name\":\"洪雅县\",\"data\":hongYaXian,\"areaStyle\":{\"normal\":{\"color\":{\"type\":\"linear\",\"x\":0,\"y\":0,\"x2\":0,\"y2\":1,\"colorStops\":[{\"offset\":0,\"color\":\"#FFD045\"},{\"offset\":1,\"color\":\"white\"}],\"globalCoord\":false},\"opacity\":0.2}}},{\"type\":\"line\",\"symbolSize\":6,\"name\":\"主城区\",\"data\":zhuChengQu,\"areaStyle\":{\"normal\":{\"color\":{\"type\":\"linear\",\"x\":0,\"y\":0,\"x2\":0,\"y2\":1,\"colorStops\":[{\"offset\":0,\"color\":\"#6600FF\"},{\"offset\":1,\"color\":\"white\"}],\"globalCoord\":false},\"opacity\":0.2}}},{\"type\":\"line\",\"symbolSize\":6,\"name\":\"柳江古镇\",\"data\":liuJiangGuZhen,\"areaStyle\":{\"normal\":{\"color\":{\"type\":\"linear\",\"x\":0,\"y\":0,\"x2\":0,\"y2\":1,\"colorStops\":[{\"offset\":0,\"color\":\"#597EF7\"},{\"offset\":1,\"color\":\"white\"}],\"globalCoord\":false},\"opacity\":0.2}}},{\"type\":\"line\",\"symbolSize\":6,\"name\":\"七里坪\",\"data\":qiLiPing,\"areaStyle\":{\"normal\":{\"color\":{\"type\":\"linear\",\"x\":0,\"y\":0,\"x2\":0,\"y2\":1,\"colorStops\":[{\"offset\":0,\"color\":\"#00BE8A\"},{\"offset\":1,\"color\":\"white\"}],\"globalCoord\":false},\"opacity\":0.2}}},{\"type\":\"line\",\"symbolSize\":6,\"name\":\"瓦屋山\",\"data\":waWuShan,\"areaStyle\":{\"normal\":{\"color\":{\"type\":\"linear\",\"x\":0,\"y\":0,\"x2\":0,\"y2\":1,\"colorStops\":[{\"offset\":0,\"color\":\"#1FA8F8\"},{\"offset\":1,\"color\":\"white\"}],\"globalCoord\":false},\"opacity\":0.2}}},{\"type\":\"line\",\"symbolSize\":6,\"name\":\"玉屏山\",\"data\":yuPingShan,\"areaStyle\":{\"normal\":{\"color\":{\"type\":\"linear\",\"x\":0,\"y\":0,\"x2\":0,\"y2\":1,\"colorStops\":[{\"offset\":0,\"color\":\"#F759AB\"},{\"offset\":1,\"color\":\"white\"}],\"globalCoord\":false},\"opacity\":0.2}}},{\"type\":\"line\",\"symbolSize\":6,\"name\":\"槽渔滩\",\"data\":caoYuTan,\"areaStyle\":{\"normal\":{\"color\":{\"type\":\"linear\",\"x\":0,\"y\":0,\"x2\":0,\"y2\":1,\"colorStops\":[{\"offset\":0,\"color\":\"#FF9933\"},{\"offset\":1,\"color\":\"white\"}],\"globalCoord\":false},\"opacity\":0.2}}}]}";
    public static String PIC5 = "{\"legend\":{\"bottom\":55,\"orient\":\"vertical\",\"left\":\"75%\",\"top\":\"center\",\"icon\":\"circle\",\"data\":pieNameArray,\"itemGap\":25,\"itemWidth\":8,\"itemHeight\":8,\"selectedMode\":false,\"textStyle\":{\"color\":\"#607A8D\",\"rich\":{\"a\":{},\"b\":{\"align\":\"right\"},\"c\":{\"align\":\"right\"}}}},series:[{type:'pie',center:['40%','50%'],radius:['0%','75%'],clockwise:true,avoidLabelOverlap:true,hoverOffset:15,itemStyle:{normal:{color:function(params){var colorList=[\"#13C2C2\",\"#40A9FF\",\"#597EF7\",\"#9254DE\",\"#F759AB\"];return colorList[params.dataIndex];}}},label:{show:true,position:'outside',formatter:'{a|{b}：{d}%}\\n{hr|}',rich:{hr:{backgroundColor:'t',borderRadius:3,width:3,height:3,padding:[3,3,0,-12]},a:{padding:[-30,15,-20,15]}}},labelLine:{normal:{length:20,length2:30,lineStyle:{width:1}}},\"data\":pieValueArray}]}";
    public static String PIC6 = "{\"legend\":{\"bottom\":15,\"icon\":\"circle\",\"itemGap\":25,\"itemWidth\":8,\"itemHeight\":8,\"selectedMode\":false,\"data\":pieNameArray},series:[{type:'pie',center:['50%','50%'],radius:['0%','75%'],clockwise:true,avoidLabelOverlap:true,hoverOffset:15,itemStyle:{normal:{color:function(params){var colorList=[\"#13C2C2\",\"#40A9FF\"];return colorList[params.dataIndex];}}},label:{show:true,position:'outside',formatter:'{a|{b}：{d}%}\\n{hr|}',rich:{hr:{backgroundColor:'t',borderRadius:3,width:3,height:3,padding:[3,3,0,-12]},a:{padding:[-30,15,-20,15]}}},labelLine:{normal:{length:20,length2:30,lineStyle:{width:1}}},\"data\":pieValueArray}]}";
    public static String PIC7 = "{\"color\":[\"#38A0FF\",\"#4CCA73\"],\"tooltip\":{\"trigger\":\"axis\",\"axisPointer\":{\"type\":\"shadow\"}},\"legend\":{\"bottom\":\"bottom\"},\"grid\":{\"left\":\"3%\",\"right\":\"4%\",\"bottom\":\"15%\",\"containLabel\":true},\"xAxis\":{\"type\":\"value\"},\"yAxis\":{\"name\":\"单位：万元\",\"type\":\"category\",\"data\":barNameArray},\"series\":[{\"type\":\"bar\",\"data\":barValueArray,\"itemStyle\":{\"normal\":{\"label\":{\"show\":true,\"position\":\"right\"}}}}]}";
    public static String PIC8 = "{\"color\":[\"#38A0FF\",\"#4CCA73\"],\"tooltip\":{\"trigger\":\"axis\",\"axisPointer\":{\"type\":\"shadow\"}},\"legend\":{\"bottom\":\"bottom\"},\"grid\":{\"left\":\"3%\",\"right\":\"4%\",\"bottom\":\"15%\",\"containLabel\":true},\"xAxis\":{\"type\":\"value\"},\"yAxis\":{\"name\":\"单位：万元\",\"type\":\"category\",\"data\":barNameArray},\"series\":[{\"type\":\"bar\",\"data\":barValueArray,\"itemStyle\":{\"normal\":{\"label\":{\"show\":true,\"position\":\"right\"}}}}]}";
    public static String PIC9 = "{\"noDataLoadingOption\":{\"text\":\"暂无数据\",\"effect\":\"bubble\",\"effectOption\":{\"effect\":{\"n\":0}}},\"grid\":{\"top\":\"40px\",\"bottom\":\"60px\",\"right\":\"40px\",\"left\":\"80px\",\"containLabel\":false},\"xAxis\":[{\"axisTick\":{\"alignWithLabel\":true},\"axisLabel\":{\"textStyle\":{\"color\":\"rgba(85, 85, 85, 1)\"}},\"axisLine\":{\"lineStyle\":{\"color\":\"#BBBBBB\"}},\"type\":\"category\",\"data\":lineNameArray}],\"yAxis\":[{\"type\":\"value\",\"axisLabel\":{\"textStyle\":{\"color\":\"rgba(85, 85, 85, 1)\"}},\"axisLine\":{\"lineStyle\":{\"color\":\"#BBBBBB\"}},\"splitLine\":{\"show\":true,\"lineStyle\":{\"type\":\"dashed\",\"color\":\"rgba(238,238,238,1)\"}},\"name\":\"\"}],\"series\":[{\"name\":\"车流量\",\"type\":\"line\",\"data\":lineValueArray,\"lineStyle\":{\"color\":\"rgba(0, 190, 138, 1)\"},\"color\":\"rgba(0, 190, 138, 1)\",\"symbol\":\"emptyCircle\",\"symbolSize\":8,\"areaStyle\":{\"normal\":{\"color\":{\"x\":0,\"y\":0,\"x2\":0,\"y2\":1,\"type\":\"linear\",\"global\":false,\"colorStops\":[{\"offset\":0,\"color\":\"rgba(0, 190, 138, 1)\"},{\"offset\":1,\"color\":\"rgba(0, 190, 138, 0)\"}]}}}}]}";
    public static String PIC10 = "{\"tooltip\":{\"trigger\":\"axis\"},\"grid\":{\"left\":\"2%\",\"right\":\"2%\",\"top\":\"5%\",\"bottom\":\"5%\",\"containLabel\":true},\"xAxis\":[{\"type\":\"category\",\"boundaryGap\":true,\"data\":lineNameArray,\"axisLabel\":{\"show\":true,\"textStyle\":{\"color\":\"#666666\",\"fontSize\":12}},\"axisLine\":{\"lineStyle\":{\"color\":\"#666666\",\"width\":0.5}},\"axisTick\":{\"show\":false}}],\"yAxis\":[{\"type\":\"value\",\"axisLine\":{\"show\":false},\"axisLabel\":{\"show\":true,\"textStyle\":{\"color\":\"#666666\"}},\"splitLine\":{\"show\":true,\"lineStyle\":{\"type\":\"dashed\"}},\"axisTick\":{\"show\":false}}],\"series\":[{\"name\":\"当日累计接待\",\"type\":\"line\",\"stack\":100,\"color\":\"#33ADFC\",\"symbolSize\":6,\"data\":lineValueArray}]}";
    public static String PIC11 = "{\"color\":[\"#13C2C2\"],\"title\":{\"show\":false,\"link\":\"\",\"target\":\"self\",\"text\":\"\",\"x\":\"left\",\"padding\":[25,0,0,5],\"textStyle\":{\"fontSize\":12,\"fontWeight\":\"normal\",\"fontFamily\":\"Microsoft Yahei\"}},\"tooltip\":{\"trigger\":\"axis\",\"padding\":[10,10,10,10],\"axisPointer\":{\"type\":\"shadow\"}},\"legend\":{\"padding\":[15,0,0,0],\"itemGap\":5,\"itemWidth\":25,\"itemHeight\":12,\"width\":\"auto\",\"show\":true,\"left\":\"auto\",\"right\":\"50\",\"bottom\":\"auto\",\"top\":\"0\",\"data\":[],\"textStyle\":{\"color\":\"#607A8D\",\"fontSize\":14,\"fontFamily\":\"Microsoft Yahei\"}},\"grid\":{\"top\":15,\"left\":10,\"right\":\"5%\",\"bottom\":15,\"containLabel\":true},\"xAxis\":[{\"name\":\"\",\"type\":\"category\",\"boundaryGap\":true,\"data\":barNameArray,\"axisLine\":{\"lineStyle\":{\"color\":\"#E9EEF2\"}},\"axisLabel\":{\"color\":\"#88A3B4\",\"textStyle\":{\"fontSize\":12,\"fontFamily\":\"Microsoft Yahei\"}},\"axisTick\":{\"show\":false},\"splitLine\":{\"lineStyle\":{\"color\":\"#DDE0E5\"}}}],\"yAxis\":[{\"type\":\"value\",\"data\":[],\"name\":\"\",\"axisLine\":{\"lineStyle\":{\"color\":\"#E9EEF2\"}},\"axisTick\":{\"show\":false},\"axisLabel\":{\"color\":\"#88A3B4\",\"textStyle\":{\"fontSize\":12,\"fontFamily\":\"Microsoft Yahei\"}},\"splitLine\":{\"lineStyle\":{\"color\":\"#F7F9FA\"}}},{\"type\":\"value\",\"name\":\"\",\"data\":[],\"axisLine\":{\"show\":false,\"lineStyle\":{\"color\":\"#E9EEF2\"}},\"axisLabel\":{\"show\":false,\"color\":\"#88A3B4\",\"textStyle\":{\"fontSize\":12,\"fontFamily\":\"Microsoft Yahei\"}},\"splitLine\":{\"show\":false,\"lineStyle\":{\"color\":\"#F7F9FA\"}}}],\"series\":[{\"type\":\"bar\",\"barMaxWidth\":20,\"data\":barValueArray}]}";
    public static String PIC12 = "{\"legend\":{\"bottom\":55,\"orient\":\"vertical\",\"left\":\"75%\",\"top\":\"center\",\"icon\":\"circle\",\"data\":pieNameArray,\"itemGap\":25,\"itemWidth\":8,\"itemHeight\":8,\"selectedMode\":false,\"textStyle\":{\"color\":\"#607A8D\",\"rich\":{\"a\":{},\"b\":{\"align\":\"right\"},\"c\":{\"align\":\"right\"}}}},series:[{type:'pie',center:['50%','50%'],radius:['0%','75%'],clockwise:true,avoidLabelOverlap:true,hoverOffset:15,itemStyle:{normal:{color:function(params){var colorList=[\"#40A9FF\",\"#F2B50E\",\"#F655AA\",\"#9254DE\",\"#597EF7\",\"#13C2C2\",\"#6CBA52\",\"#097C25\"];return colorList[params.dataIndex];}}},label:{show:true,position:'outside',formatter:'{a|{b}：{d}%}\\n{hr|}',rich:{hr:{backgroundColor:'t',borderRadius:3,width:3,height:3,padding:[3,3,0,-12]},a:{padding:[-30,15,-20,15]}}},labelLine:{normal:{length:20,length2:30,lineStyle:{width:1}}},data:pieValueArray}]}";


    @Override
    public SpecialReportWordData queryReportData(DataCenterMonthlyReportReq reportReq) {
        SpecialReportWordData data = specialReportService.getSpecialReportWordData(reportReq);
        log.info("=============={},{}",reportReq.toString(),data.getHCA().toString());
        return data;
    }

    @Override
    public File export(DataCenterMonthlyReportReq reportReq) {
        SpecialReportWordData data = specialReportService.getSpecialReportWordData(reportReq);
        log.info("=============={},{}",reportReq.toString(),data.getHCA().toString());
        /**
         * 1.准备数据生成echarts饼图
         */
        String nullListJson = "[]";
        // 替换县域游客来源饼图数据
        ScenicReportVO.PassengerFlowAnalysis PFA = data.getPFA();
        ScenicReportVO.PiePassengerSource pie = PFA.getPiePassengerSource();
        int provinceInner = pie.getProvinceInner();
        int provinceOuter = pie.getProvinceOuter();
        int countryOuter = pie.getCountryOuter();
        String pic1Options = PIC1.replace("allPassenger", (provinceInner + provinceOuter + countryOuter) + "")
                .replace("provinceInner", provinceInner + "")
                .replace("provinceOuter", provinceOuter + "")
                .replace("countryOuter", countryOuter + "");

        // 替换省内游客柱状图数据
        List<ScenicReportVO.LineChartVO> barPassengerInProvince = PFA.getBarPassengerInProvince();
        String pic2Options;
        if (CollectionUtil.isNotEmpty(barPassengerInProvince)) {
            List<String> nameList = barPassengerInProvince.stream().map(e -> e.getName()).collect(Collectors.toList());
            List<Integer> valueList = barPassengerInProvince.stream().map(e -> e.getValue()).collect(Collectors.toList());
            Collections.reverse(nameList);
            Collections.reverse(valueList);
            pic2Options = PIC2.replace("barNameArray", JsonUtils.toJsonString(nameList))
                    .replace("barValueArray", JsonUtils.toJsonString(valueList));
        } else {
            pic2Options = PIC2.replace("barNameArray", nullListJson)
                    .replace("barValueArray", nullListJson);
        }

        // 替换省外游客柱状图数据
        List<ScenicReportVO.LineChartVO> barPassengerOutsideProvince = PFA.getBarPassengerOutsideProvince();
        String pic3Options;
        if (CollectionUtil.isNotEmpty(barPassengerOutsideProvince)) {
            List<String> name2List = barPassengerOutsideProvince.stream().map(e -> e.getName()).collect(Collectors.toList());
            List<Integer> value2List = barPassengerOutsideProvince.stream().map(e -> e.getValue()).collect(Collectors.toList());
            Collections.reverse(name2List);
            Collections.reverse(value2List);
            pic3Options = PIC3.replace("barNameArray", JsonUtils.toJsonString(name2List))
                    .replace("barValueArray", JsonUtils.toJsonString(value2List));
        } else {
            pic3Options = PIC3.replace("barNameArray", nullListJson)
                    .replace("barValueArray", nullListJson);
        }


        // 客流趋势及景区热度
        ScenicReportVO.LinePassengerFlowByScenic linePassengerFlowByScenic = PFA.getLinePassengerFlowByScenic();
        List<ScenicReportVO.LineChartVO> hongYaXian = linePassengerFlowByScenic.getHongYaXian();
        List<ScenicReportVO.LineChartVO> zhuChengQu = linePassengerFlowByScenic.getZhuChengQu();
        List<ScenicReportVO.LineChartVO> liuJiangGuZhen = linePassengerFlowByScenic.getLiuJiangGuZhen();
        List<ScenicReportVO.LineChartVO> qiLiPing = linePassengerFlowByScenic.getQiLiPing();
        List<ScenicReportVO.LineChartVO> waWuShan = linePassengerFlowByScenic.getWaWuShan();
        List<ScenicReportVO.LineChartVO> yuPingShan = linePassengerFlowByScenic.getYuPingShan();
        List<ScenicReportVO.LineChartVO> caoYuTan = linePassengerFlowByScenic.getCaoYuTan();
        String pic4Options = PIC4;
        if (CollectionUtil.isNotEmpty(hongYaXian)) {
            pic4Options = pic4Options.replace("lineNameArray", JsonUtils.toJsonString(hongYaXian.stream().map(e -> e.getName()).collect(Collectors.toList())))
                    .replace("hongYaXian", JsonUtils.toJsonString(hongYaXian.stream().map(e -> e.getValue()).collect(Collectors.toList())));
        } else {
            pic4Options = pic4Options.replace("lineNameArray", nullListJson).replace("hongYaXian",nullListJson);
        }
        if (CollectionUtil.isNotEmpty(zhuChengQu)) {
            pic4Options = pic4Options.replace("zhuChengQu", JsonUtils.toJsonString(zhuChengQu.stream().map(e -> e.getValue()).collect(Collectors.toList())));
        } else {
            pic4Options = pic4Options.replace("zhuChengQu", nullListJson);
        }
        if (CollectionUtil.isNotEmpty(liuJiangGuZhen)) {
            pic4Options = pic4Options.replace("liuJiangGuZhen", JsonUtils.toJsonString(liuJiangGuZhen.stream().map(e -> e.getValue()).collect(Collectors.toList())));
        } else {
            pic4Options = pic4Options.replace("liuJiangGuZhen", nullListJson);
        }
        if (CollectionUtil.isNotEmpty(qiLiPing)) {
            pic4Options = pic4Options.replace("qiLiPing", JsonUtils.toJsonString(qiLiPing.stream().map(e -> e.getValue()).collect(Collectors.toList())));
        }else{
            pic4Options = pic4Options.replace("qiLiPing", nullListJson);
        }
        if (CollectionUtil.isNotEmpty(waWuShan)) {
            pic4Options = pic4Options.replace("waWuShan", JsonUtils.toJsonString(waWuShan.stream().map(e -> e.getValue()).collect(Collectors.toList())));
        } else {
            pic4Options = pic4Options.replace("waWuShan", nullListJson);
        }
        if (CollectionUtil.isNotEmpty(yuPingShan)) {
            pic4Options = pic4Options.replace("yuPingShan", JsonUtils.toJsonString(yuPingShan.stream().map(e -> e.getValue()).collect(Collectors.toList())));
        } else {
            pic4Options = pic4Options.replace("yuPingShan", nullListJson);
        }
        if (CollectionUtil.isNotEmpty(caoYuTan)) {
            pic4Options = pic4Options.replace("caoYuTan", JsonUtils.toJsonString(caoYuTan.stream().map(e -> e.getValue()).collect(Collectors.toList())));
        } else {
            pic4Options = pic4Options.replace("caoYuTan", nullListJson);
        }

        // 行业消费
        SpecialReportWordData.TourismConsumptionAnalysis tca = data.getTCA();
        List<TourismConsumptionIndustryAnalyzeVO> pieBusinessConsumption = tca.getIndustryData();
        String pic5Options;
        if (CollectionUtil.isNotEmpty(pieBusinessConsumption)) {
            List<SpecialReportWordData.CommonNameValue> pieBusinessConsumptionList = pieBusinessConsumption.stream().map(e -> new SpecialReportWordData.CommonNameValue(e.getIndustry(), e.getTransAt().toString())).collect(Collectors.toList());
            List<String> tcaPieNameList = pieBusinessConsumptionList.stream().map(e -> e.getName()).collect(Collectors.toList());
            Collections.reverse(pieBusinessConsumptionList);
            pic5Options = PIC5.replace("pieNameArray", JsonUtils.toJsonString(tcaPieNameList))
                    .replace("pieValueArray", JsonUtils.toJsonString(pieBusinessConsumptionList));
        } else {
            pic5Options = PIC5.replace("pieNameArray", nullListJson)
                    .replace("pieValueArray", nullListJson);
        }

        // 游客消费来源地
        TourismConsumptionSourceAnalyzeVO sourceData = tca.getSourceData();
        List<String> pieSourceNameList = Arrays.asList("省内", "省外");
        String innerProvTransAt = sourceData.getInnerProvTransAt() == null ? "0" : sourceData.getInnerProvTransAt().toString();
        String outerProvTransAt = sourceData.getOuterProvTransAt() == null ? "0" : sourceData.getOuterProvTransAt().toString();
        List<SpecialReportWordData.CommonNameValue> pieSourceList = Arrays.asList(
                new SpecialReportWordData.CommonNameValue("省内", innerProvTransAt),
                new SpecialReportWordData.CommonNameValue("省外", outerProvTransAt)
        );
        String pic6Options = PIC6.replace("pieNameArray", JsonUtils.toJsonString(pieSourceNameList))
                .replace("pieValueArray", JsonUtils.toJsonString(pieSourceList));

        // 省内游客消费柱状图
        List<TourismConsumptionSourceAnalyzeVO.TransInfo> innerProvList = sourceData.getInnerProvList();
        String pic7Options;
        BigDecimal wan = new BigDecimal("10000");
        if (CollectionUtil.isNotEmpty(innerProvList)) {
            List<String> barInnerProvNameList = innerProvList.stream().map(e -> e.getSource()).collect(Collectors.toList());
            List<String> barInnerProvValueList = innerProvList.stream().map(e -> e.getTransAt().divide(wan, 2, BigDecimal.ROUND_HALF_UP).toString()).collect(Collectors.toList());
            Collections.reverse(barInnerProvNameList);
            Collections.reverse(barInnerProvValueList);
            pic7Options = PIC7.replace("barNameArray", JsonUtils.toJsonString(barInnerProvNameList))
                    .replace("barValueArray", JsonUtils.toJsonString(barInnerProvValueList));
        } else {
            pic7Options = PIC7.replace("barNameArray", nullListJson)
                    .replace("barValueArray", nullListJson);
        }


        // 省外游客消费柱状图
        List<TourismConsumptionSourceAnalyzeVO.TransInfo> outerProvList = sourceData.getOuterProvList();
        String pic8Options;
        if (CollectionUtil.isNotEmpty(outerProvList)) {
            List<String> barOuterProvNameList = outerProvList.stream().map(e -> e.getSource()).collect(Collectors.toList());
            List<String> barOuterProvValueList = outerProvList.stream().map(e -> e.getTransAt().divide(wan, 2, BigDecimal.ROUND_HALF_UP).toString()).collect(Collectors.toList());
            Collections.reverse(barOuterProvNameList);
            Collections.reverse(barOuterProvValueList);
            pic8Options = PIC8.replace("barNameArray", JsonUtils.toJsonString(barOuterProvNameList))
                    .replace("barValueArray", JsonUtils.toJsonString(barOuterProvValueList));
        } else {
            pic8Options = PIC8.replace("barNameArray", nullListJson)
                    .replace("barValueArray", nullListJson);
        }

        // 车流趋势图
        SpecialReportWordData.TrafficAnalysis ta = data.getTA();
        List<StringLongVO> lineTrafficFlowTrend = ta.getLineTrafficFlowTrend();
        String pic9Options;
        if (CollectionUtil.isNotEmpty(lineTrafficFlowTrend)) {
            List<String> lineTrendNameList = lineTrafficFlowTrend.stream().map(e -> e.getName()).collect(Collectors.toList());
            List<Long> lineTrendValueList = lineTrafficFlowTrend.stream().map(e -> e.getValue()).collect(Collectors.toList());
            pic9Options = PIC9.replace("lineNameArray", JsonUtils.toJsonString(lineTrendNameList))
                    .replace("lineValueArray", JsonUtils.toJsonString(lineTrendValueList));
        } else {
            pic9Options = PIC9.replace("lineNameArray", nullListJson)
                    .replace("lineValueArray", nullListJson);
        }

        // 酒店累计接待折线图
        SpecialReportWordData.HotelAnalysis hca = data.getHCA();
        List<KeyAndValue<String, Integer>> lineCheckInAmount = hca.getLineCheckInAmount();
        String pic10Options;
        if (CollectionUtil.isNotEmpty(lineCheckInAmount)) {
            List<String> lineKeyList = lineCheckInAmount.stream().map(e -> e.getKey()).collect(Collectors.toList());
            List<Integer> lineValueList = lineCheckInAmount.stream().map(e -> e.getValue()).collect(Collectors.toList());
            pic10Options = PIC10.replace("lineNameArray", JsonUtils.toJsonString(lineKeyList))
                    .replace("lineValueArray", JsonUtils.toJsonString(lineValueList));
        } else {
            pic10Options = PIC10.replace("lineNameArray", nullListJson)
                    .replace("lineValueArray", nullListJson);
        }

        // 酒店入住年龄分布图
        List<KeyAndValue<String, Integer>> barCheckInAgeDistribution = hca.getBarCheckInAgeDistribution();
        String pic11Options;
        if (CollectionUtil.isNotEmpty(barCheckInAgeDistribution)) {
            List<String> barNameList = barCheckInAgeDistribution.stream().map(e -> e.getKey()).collect(Collectors.toList());
            List<Integer> barValueList = barCheckInAgeDistribution.stream().map(e -> e.getValue()).collect(Collectors.toList());
            pic11Options = PIC11.replace("barNameArray", JsonUtils.toJsonString(barNameList))
                    .replace("barValueArray", JsonUtils.toJsonString(barValueList));
        } else {
            pic11Options = PIC11.replace("barNameArray", nullListJson)
                    .replace("barValueArray", nullListJson);
        }

        // 酒店入住过夜数占比饼图
        List<KeyAndValue<String, Integer>> pieDataStayOvernight = hca.getPieDataStayOvernight();
        String pic12Options;
        if (CollectionUtil.isNotEmpty(pieDataStayOvernight)) {
            List<SpecialReportWordData.CommonNameValue> pieDataStayOvernightList = pieDataStayOvernight.stream().map(e -> new SpecialReportWordData.CommonNameValue(e.getKey(), e.getValue().toString())).collect(Collectors.toList());
            List<String> pieDataStayOvernightNameList = pieDataStayOvernight.stream().map(e -> e.getKey()).collect(Collectors.toList());
            pic12Options = PIC12.replace("pieNameArray", JsonUtils.toJsonString(pieDataStayOvernightNameList))
                    .replace("pieValueArray", JsonUtils.toJsonString(pieDataStayOvernightList));
        } else {
            pic12Options = PIC12.replace("pieNameArray", nullListJson)
                    .replace("pieValueArray", nullListJson);
        }

        /**
         * 清空工作目录、准备模板数据、渲染模板
         */
        FileUtil.del(workDir + File.separator + "temp");
        Map<String, Object> datas = new HashMap(32);
        String filePath = workDir + File.separator + "大数据中心报告.doc";
        datas.put("template", "test");
        datas.put("data", data);
        datas.put("pic1", Base64.encode(new File(generate(binDir,workDir, pic1Options))));
        datas.put("pic2", Base64.encode(new File(generate(binDir,workDir, pic2Options))));
        datas.put("pic3", Base64.encode(new File(generate(binDir,workDir, pic3Options))));
        datas.put("pic4", Base64.encode(new File(generate(binDir,workDir, pic4Options))));
        datas.put("pic5", Base64.encode(new File(generate(binDir,workDir, pic5Options))));
        datas.put("pic6", Base64.encode(new File(generate(binDir,workDir, pic6Options))));
        datas.put("pic7", Base64.encode(new File(generate(binDir,workDir, pic7Options))));
        datas.put("pic8", Base64.encode(new File(generate(binDir,workDir, pic8Options))));
        datas.put("pic9", Base64.encode(new File(generate(binDir,workDir, pic9Options))));
        datas.put("pic10", Base64.encode(new File(generate(binDir,workDir, pic10Options))));
        datas.put("pic11", Base64.encode(new File(generate(binDir,workDir, pic11Options))));
        datas.put("pic12", Base64.encode(new File(generate(binDir,workDir, pic12Options))));
        return WordGeneratorUtils.createDoc(WordGeneratorUtils.DATA_CENTER_REPORT, filePath, datas);
    }
}
