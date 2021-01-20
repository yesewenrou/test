package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.DataResource;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ChartVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DataResourceCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DataResourceVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author LHY
 */
@Repository
public interface DataResourceMapper extends BaseMapper<DataResource>{

    @Select({
            "<script>",
            "SELECT country_name,prov_name,city_name,DATE_FORMAT(time,'%Y-%m-%d') as statistics_time,SUM(people_num) as count FROM hy_data_resource",
            "where time BETWEEN #{condition.startTime} AND #{condition.endTime}",
            "<if test='condition.countryName == \"1\"'>",
            "and country_name='中国'",
            "</if>",
            "<if test='condition.countryName == \"2\"'>",
            "and country_name!='中国'",
            "</if>",
            "<if test='condition.provName!=null and condition.provName.length>0'>",
            "and prov_name=#{condition.provName}",
            "</if>",
            "<if test='condition.cityName!=null and condition.cityName.length>0'>",
            "and city_name=#{condition.cityName}",
            "</if>",
            "GROUP BY country_name,prov_name,city_name,statistics_time",
            "<if test='size>0'>ORDER BY statistics_time DESC LIMIT #{startIndex},#{size}</if>",
            "</script>"
    })
    List<DataResourceVO> statisticsList(@Param("startIndex") Long startIndex, @Param("size") Long size,
                                        @Param("condition") DataResourceCondition condition);

    /**今年节假日游客接待人数趋势**/
    @Select({
            "<script>",
            "SELECT DATE_FORMAT(time,'%Y-%m-%d') as statistics_time,SUM(people_num) as count FROM hy_data_resource",
            "where time BETWEEN #{startTime} AND #{endTime}",
            "GROUP BY statistics_time",
            "</script>"
    })
    List<DataResourceVO> tourismTrend(@Param("startTime") Timestamp startTime, @Param("endTime") Timestamp endTime);

    /**去年节假日游客接待人数趋势**/
    @Select({
            "<script>",
            "SELECT DATE_FORMAT(time,'%Y-%m-%d') as statistics_time,SUM(people_num) as count FROM hy_data_resource",
            "where time BETWEEN #{lastStartTime} AND #{lastEndTime}",
            "GROUP BY statistics_time",
            "</script>"
    })
    List<DataResourceVO> lastTourismTrend(@Param("lastStartTime") Timestamp lastStartTime, @Param("lastEndTime") Timestamp lastEndTime);

    /**历史数据统计列表**/
    @Select({
            "<script>",
            "<if test='condition.flag == \"day\"'>",
            "SELECT scenic_name,DATE_FORMAT(time,'%Y-%m-%d') as statistics_time,SUM(people_num) as count",
            "</if>",
            "<if test='condition.flag == \"month\"'>",
            "SELECT scenic_name,DATE_FORMAT(time,'%Y-%m') as statistics_time,SUM(people_num) as count",
            "</if>",
            "<if test='condition.flag == \"year\"'>",
            "SELECT scenic_name,DATE_FORMAT(time,'%Y') as statistics_time,SUM(people_num) as count",
            "</if>",
            "FROM hy_data_resource where time BETWEEN #{condition.startTime} AND #{condition.endTime}",
            "<if test='condition.scenicName!=null and condition.scenicName.length>0'>",
            "and scenic_name like \"%\"#{condition.scenicName}\"%\"",
            "</if>",
            "GROUP BY scenic_name,statistics_time",
            "<if test='size>0'>ORDER BY statistics_time DESC LIMIT #{startIndex},#{size}</if>",
            "</script>"
    })
    List<DataResourceVO> historyStatisticsList(@Param("startIndex") Long startIndex, @Param("size") Long size,
                                        @Param("condition") DataResourceCondition condition);

    /**游客来源地TOP10**/
    @Select({
            "<script>",
            "SELECT city_name as name,SUM(people_num) as value FROM hy_data_resource",
            "where country_name='中国' AND time BETWEEN #{startTime} AND #{endTime}",
            "<if test='scope == 1'>",
            "and prov_name='四川'",
            "</if>",
            "<if test='scope == 2'>",
            "and prov_name!='四川'",
            "</if>",
            "GROUP BY city_name order by value desc LIMIT 10",
            "</script>"
    })
    List<ChartVO> sourceCityTop(@Param("startTime") String startTime, @Param("endTime") String endTime, @Param("scope") Integer scope);
}
