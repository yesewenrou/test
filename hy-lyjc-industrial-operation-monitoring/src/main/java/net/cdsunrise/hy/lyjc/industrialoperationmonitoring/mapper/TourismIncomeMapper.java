package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.TourismIncome;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TourismIncomeCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.TourismIncomeVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Author: LHY
 * @Date: 2019/9/17 17:29
 */
@Repository
public interface TourismIncomeMapper extends BaseMapper<TourismIncome>{

    /**今年节假日游客接待人数趋势**/
    @Select({
            "<script>",
            "SELECT DATE_FORMAT(time,'%Y-%m-%d') as statistics_time,SUM(income) as count FROM hy_tourism_income",
            "where time BETWEEN #{startTime} AND #{endTime}",
            "GROUP BY statistics_time",
            "</script>"
    })
    List<TourismIncomeVO> incomeTrend(@Param("startTime") Timestamp startTime, @Param("endTime") Timestamp endTime);

    /**去年节假日游客接待人数趋势**/
    @Select({
            "<script>",
            "SELECT DATE_FORMAT(time,'%Y-%m-%d') as statistics_time,SUM(income) as count FROM hy_tourism_income",
            "where time BETWEEN #{lastStartTime} AND #{lastEndTime}",
            "GROUP BY statistics_time",
            "</script>"
    })
    List<TourismIncomeVO> lastIncomeTrend(@Param("lastStartTime") Timestamp lastStartTime, @Param("lastEndTime") Timestamp lastEndTime);

    @Select({
            "<script>",
            "SELECT income_source,scenic_name,DATE_FORMAT(time,'%Y-%m-%d') as statistics_time,SUM(income) as count FROM hy_tourism_income",
            "where time BETWEEN #{condition.startTime} AND #{condition.endTime}",
            "<if test='condition.incomeSource!=null and condition.incomeSource.length>0'>",
            "and income_source=#{condition.incomeSource}",
            "</if>",
            "<if test='condition.scenicName!=null and condition.scenicName.length>0'>",
            "and scenic_name=#{condition.scenicName}",
            "</if>",
            "GROUP BY income_source,scenic_name,statistics_time",
            "<if test='size>0'>ORDER BY statistics_time DESC LIMIT #{startIndex},#{size}</if>",
            "</script>"
    })
    List<TourismIncomeVO> statisticsList(@Param("startIndex") Long startIndex, @Param("size") Long size,
                                        @Param("condition") TourismIncomeCondition condition);

    /**历史数据统计列表**/
    @Select({
            "<script>",
            "<if test='condition.flag == \"day\"'>",
            "SELECT income_source,scenic_name,DATE_FORMAT(time,'%Y-%m-%d') as statistics_time,SUM(income) as count",
            "</if>",
            "<if test='condition.flag == \"month\"'>",
            "SELECT income_source,scenic_name,DATE_FORMAT(time,'%Y-%m') as statistics_time,SUM(income) as count",
            "</if>",
            "<if test='condition.flag == \"year\"'>",
            "SELECT income_source,scenic_name,DATE_FORMAT(time,'%Y') as statistics_time,SUM(income) as count",
            "</if>",
            "FROM hy_tourism_income where time BETWEEN #{condition.startTime} AND #{condition.endTime}",
            "<if test='condition.scenicName!=null and condition.scenicName.length>0'>",
            "and scenic_name like \"%\"#{condition.scenicName}\"%\"",
            "</if>",
            "<if test='condition.incomeSource!=null and condition.incomeSource.length>0'>",
            "and income_source like \"%\"#{condition.incomeSource}\"%\"",
            "</if>",
            "GROUP BY income_source,scenic_name,statistics_time",
            "<if test='size>0'>ORDER BY statistics_time DESC LIMIT #{startIndex},#{size}</if>",
            "</script>"
    })
    List<TourismIncomeVO> historyStatisticsList(@Param("startIndex") Long startIndex, @Param("size") Long size,
                                               @Param("condition") TourismIncomeCondition condition);
}
