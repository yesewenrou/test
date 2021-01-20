package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo;

import lombok.Data;
import lombok.val;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * TouristRegionCompareVO
 * 游客数区域比较对象
 * @author LiuYin
 * @date 2020/3/27 13:59
 */
@Data
public class TouristRegionCompareVO {

    /** 时间戳*/
    private Long time;
    /** 本年当日人数*/
    private Long currentYearDayPeopleNum;
    /** 去年当日人数*/
    private Long lastYearDayPeopleNum;
    /** 本年昨日人数*/
    private Long currentYearYesterdayPeopleNum;
    /** 比上年同期*/
    private Double compareLastYear;
    /** 比昨日*/
    private Double compareLastDay;


    /**
     * 创建默认零值的对象
     * @param time 时间戳
     * @return 比较对象
     */
    public static TouristRegionCompareVO createZero(Long time){
        final TouristRegionCompareVO vo = new TouristRegionCompareVO();
        vo.setTime(time);
        vo.setCurrentYearDayPeopleNum(0L);
        vo.setLastYearDayPeopleNum(0L);
        return vo;
    }

    /**
     * 设置比率
     */
    public void setRatio(){
        setCompareLastDayValue();
        setCompareLastYearValue();
    }


    /**
     * 设置今年与去年当日的比率
     */
    private void setCompareLastYearValue(){
        compareLastYear = getRatio(currentYearDayPeopleNum, lastYearDayPeopleNum);
    }

    /**
     * 设置今年当天与昨天的比率
     */
    private void setCompareLastDayValue(){
        compareLastDay = getRatio(currentYearDayPeopleNum, currentYearYesterdayPeopleNum);
    }

    /**
     * 获得两个值的比率（保留2位小数)
     * <p>(值1 - 值2) / 值2</p>
     * @param o1 值1
     * @param o2 值2
     * @return 比率
     */
    private Double getRatio(Long o1, Long o2){
        long o1Value = Objects.isNull(o1) ? 0L : o1;
        long o2Value = Objects.isNull(o2) ? 0L : o2;
        if(o1Value == o2Value){
            return 0D;
        }
        if(o2Value == 0){
            return 1D;
        }
        if(o1Value == 0){
            return -1D;
        }
        final double v = (o1Value - o2Value + 0D) / (o2Value + 0D);
        if(0D < v && v < 0.001D){
            return 0.001D;
        }
        if(0D > v && -0.001D < v){
            return -0.001D;
        }
        return BigDecimal.valueOf(v).setScale(3, RoundingMode.HALF_UP).doubleValue();

    }

}
