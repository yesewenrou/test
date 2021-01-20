package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.HotelReportService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.HotelTrendReportVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.KeyAndValue;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TouristPortraitVO;
import net.cdsunrise.hy.lyyx.precisionmarketing.autoconfigure.feign.HotelFeignClient;
import net.cdsunrise.hy.lyyx.precisionmarketing.autoconfigure.feign.vo.NameAndValueVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * @author LHY
 * @date 2020/5/9 16:42
 */
@Service
public class HotelReportServiceImpl implements HotelReportService {

    private final static String TYPE = "DAY";
    private final static String MALE_TYPE = "男性";
    private final static String FEMALE_TYPE = "女性";

    private final HotelFeignClient hotelFeignClient;

    public HotelReportServiceImpl(HotelFeignClient hotelFeignClient) {
        this.hotelFeignClient = hotelFeignClient;
    }

    @Override
    @SuppressWarnings("all")
    public HotelTrendReportVO hotelRecption(LocalDate beginDate,LocalDate endDate) {
        HotelTrendReportVO reportVO = new HotelTrendReportVO();
        Long begin = transfer(beginDate);
        Long end = transfer(endDate);
        Result<Map<String, Object>> trendResult = hotelFeignClient.receptionTrendReport(begin, end);
        if (trendResult.getSuccess()) {
            Map<String, Object> map = trendResult.getData();
            Double cumulativeReception = (Double) map.get("cumulativeReception");
            reportVO.setCheckInAmount(getPercent(cumulativeReception.intValue(),10000,2,false).doubleValue());
            List<NameAndValueVO> lineCheckInAmount = (List<NameAndValueVO>) map.get("trendList");
            reportVO.setLineCheckInAmount(magicTransfer(lineCheckInAmount));
        }
        Result<List<NameAndValueVO>> rankResult = hotelFeignClient.hotelReceptionRank(begin, end);
        if (rankResult.getSuccess()) {
            List<NameAndValueVO> list = rankResult.getData();
            List<KeyAndValue<String, Integer>> tempList = new ArrayList<>();
            list.forEach(valueVO -> {
                Double value = (Double) valueVO.getValue();
                tempList.add(new KeyAndValue<String, Integer>(valueVO.getName(), value.intValue()));
            });
            reportVO.setTop5CheckInAmount(getTopString(tempList, 5, "%s（累计接待%d人次）、", true));
        }
        return reportVO;
    }

    @Override
    @SuppressWarnings("all")
    public TouristPortraitVO touristPortrait(LocalDate beginDate,LocalDate endDate) {
        TouristPortraitVO touristPortraitVO = new TouristPortraitVO();
        Long begin = transfer(beginDate);
        Long end = transfer(endDate);
        Result<Map<String, Object>> sexAndAgeResult = hotelFeignClient.sexAndAge(begin, end, TYPE);
        if (sexAndAgeResult.getSuccess()) {
            Map<String, Object> map = sexAndAgeResult.getData();
            // 性别
            Double male = (Double) map.get("maleCount");
            Double female = (Double) map.get("femaleCount");
            String gender;
            Double genderRatio;
            Double count = male + female;
            if (male > female) {
                gender = MALE_TYPE;
                genderRatio = getPercent(male.intValue(), count.intValue(),4,true).doubleValue();
            } else {
                gender = FEMALE_TYPE;
                genderRatio = getPercent(female.intValue(), count.intValue(),4,true).doubleValue();
            }
            touristPortraitVO.setGender(gender);
            touristPortraitVO.setGenderRatio(genderRatio);
            // 年龄
            List<NameAndValueVO> ageList = (List<NameAndValueVO>) map.get("ageChart");
            String[] ageText = {"50","60","70","80","90","00"};
            List<KeyAndValue<String, Integer>> tempList = magicTransfer(ageList);
            touristPortraitVO.setBarCheckInAgeDistribution(buildData(tempList,ageText));
            // Collections排序降序
            Collections.sort(tempList, new Comparator<KeyAndValue<String, Integer>>() {
                @Override
                public int compare(KeyAndValue<String, Integer> o1, KeyAndValue<String, Integer> o2) {
                    return o2.getValue().compareTo(o1.getValue());
                }
            });
            touristPortraitVO.setTop3AgeDistribution(getTopString(tempList, 3, "%s后（占比%s），", false));
            // 过夜数
            Result<List<NameAndValueVO>> overnightResult = hotelFeignClient.stayOvernight(begin, end, TYPE);
            if (overnightResult.getSuccess()){
                List<NameAndValueVO> list = overnightResult.getData();
                List<KeyAndValue<String, Integer>> diyList = new ArrayList<>();
                list.forEach(valueVO -> {
                    Double value = (Double) valueVO.getValue();
                    diyList.add(new KeyAndValue<String, Integer>(valueVO.getName(), value.intValue()));
                });
                String[] overnightText = {"未过夜","1天","2天","3天","4天及以上"};
                touristPortraitVO.setPieDataStayOvernight(buildData(diyList,overnightText));
                // Collections排序降序
                Collections.sort(diyList, new Comparator<KeyAndValue<String, Integer>>() {
                    @Override
                    public int compare(KeyAndValue<String, Integer> o1, KeyAndValue<String, Integer> o2) {
                        return o2.getValue().compareTo(o1.getValue());
                    }
                });
                touristPortraitVO.setTop3StayOvernight(getTopOvernightString(diyList));
            }
        }
        return touristPortraitVO;
    }

    private Long transfer(LocalDate localDate) {
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDate.atStartOfDay().atZone(zone).toInstant();
        return instant.toEpochMilli();
    }

    @SuppressWarnings("all")
    private List<KeyAndValue<String, Integer>> magicTransfer(List<NameAndValueVO> list) {
        List<KeyAndValue<String, Integer>> tempList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object object = list.get(i);
            Map tempMap = (Map) object;
            String name = (String) tempMap.get("name");
            Double value = (Double) tempMap.get("value");
            tempList.add(new KeyAndValue<String, Integer>(name, value.intValue()));
        }
        return tempList;
    }

    private List<KeyAndValue<String, Integer>> buildData(List<KeyAndValue<String, Integer>> list, String[] strArr) {
        Map<String, Object> paramMap = new HashMap<>();
        List<KeyAndValue<String, Integer>> dataList = new ArrayList<>();
        list.forEach(keyAndValue -> {
            paramMap.put(keyAndValue.getKey(), keyAndValue.getValue());
        });
        for (String string : strArr) {
            KeyAndValue<String, Integer> keyAndValue;
            if (paramMap.containsKey(string)) {
                keyAndValue = new KeyAndValue<>(string, Integer.parseInt(String.valueOf(paramMap.get(string))));
            } else {
                keyAndValue = new KeyAndValue<>(string, 0);
            }
            dataList.add(keyAndValue);
        }
        return dataList;
    }

    /**
     * TOP数据组成字符串
     *
     * @param list                              TOP数据
     * @param size                              取前几位
     * @param text                              渲染文字模板
     * @param flag：true，代表酒店入住渲染；false，代表游客画像渲染
     * @return 结果
     */
    private String getTopString(List<KeyAndValue<String, Integer>> list, int size, String text, boolean flag) {
        StringBuilder builder = new StringBuilder();
        int sum = list.stream().mapToInt(KeyAndValue::getValue).sum();
        for (int i = 0; i < list.size() && i < size; i++) {
            KeyAndValue<String, Integer> keyAndValue = list.get(i);
            if (flag) {
                builder.append(String.format(text, keyAndValue.getKey(), keyAndValue.getValue()));
            } else {
                builder.append(String.format(text, keyAndValue.getKey(), getPercentString(keyAndValue.getValue(), sum)));
            }
        }
        // 最后、替换为。
        int length = builder.length();
        if (length > 0) {
            builder.replace(length - 1, length, "。");
        }

        return builder.toString();
    }

    private String getTopOvernightString(List<KeyAndValue<String, Integer>> list) {
        StringBuilder builder = new StringBuilder();
        int sum = list.stream().mapToInt(KeyAndValue::getValue).sum();
        for (int i = 0; i < list.size() && i < 3; i++) {
            KeyAndValue<String, Integer> keyAndValue = list.get(i);
            String text;
            if (i==0){
                text = "%s的最多（占比%s），";
            }else if (i==1){
                text = "其次是%s（占比%s），";
            }else{
                text = "第三是过夜数为%s的（占比%s），";
            }
            builder.append(String.format(text, keyAndValue.getKey(), getPercentString(keyAndValue.getValue(), sum)));
        }
        // 最后、替换为。
        int length = builder.length();
        if (length > 0) {
            builder.replace(length - 1, length, "。");
        }
        return builder.toString();
    }

    /**
     *
     * @param divider 除数
     * @param dividend 被除数
     * @param num 保留几位小数
     * @param flag 标记是否乘以100
     * @return
     */
    private BigDecimal getPercent(Integer divider, Integer dividend,int num,boolean flag) {
        if (dividend > 0){
            BigDecimal bg = new BigDecimal(String.valueOf(divider));
            BigDecimal percent = bg.divide(new BigDecimal(String.valueOf(dividend)), num, BigDecimal.ROUND_HALF_UP);
            return flag?percent.multiply(new BigDecimal(String.valueOf(100))):percent;
        }else{
            return new BigDecimal(0);
        }
    }

    private String getPercentString(Integer divider, Integer dividend) {
        if (dividend > 0){
            BigDecimal bg = new BigDecimal(String.valueOf(divider));
            BigDecimal percent = bg.divide(new BigDecimal(String.valueOf(dividend)), 4, BigDecimal.ROUND_HALF_UP);
            return percent.multiply(new BigDecimal(String.valueOf(100))).doubleValue() + "%";
        }else{
            return null;
        }
    }
}
