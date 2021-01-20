package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.ComplaintManage;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ChartVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.MerchantComplaintVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.StatisticsComplaintCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.StatisticsVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author LHY
 */
@Repository
public interface ComplaintManageMapper extends BaseMapper<ComplaintManage>{

    @Select({
            "<script>",
            "<if test='condition.flag == \"day\"'>",
            "SELECT type,channel,industry_type,COUNT(*) as count,DATE_FORMAT(m.complaint_time,'%Y-%m-%d') as statistics_time",
            "</if>",
            "<if test='condition.flag == \"month\"'>",
            "SELECT type,channel,industry_type,COUNT(*) as count,DATE_FORMAT(m.complaint_time,'%Y-%m') as statistics_time",
            "</if>",
            "<if test='condition.flag == \"year\"'>",
            "SELECT type,channel,industry_type,COUNT(*) as count,DATE_FORMAT(m.complaint_time,'%Y') as statistics_time",
            "</if>",
            "FROM hy_complaint_manage m LEFT JOIN hy_handle_result r ON m.id=r.complaint_id",
            "WHERE m.complaint_time BETWEEN #{condition.startTime} AND #{condition.endTime}",
            "<if test='condition.industryType!=null and condition.industryType.length>0'>",
            "and r.industry_type=#{condition.industryType}",
            "</if>",
            "<if test='condition.type!=null and condition.type.length>0'>",
            "and m.type=#{condition.type}",
            "</if>",
            "<if test='condition.channel!=null and condition.channel.length>0'>",
            "and m.channel=#{condition.channel}",
            "</if>",
            "GROUP BY type,channel,industry_type,statistics_time",
            "<if test='size>0'>ORDER BY statistics_time DESC LIMIT #{startIndex},#{size}</if>",
            "</script>"
    })
    List<StatisticsVO> statisticsList(@Param("startIndex") Long startIndex, @Param("size") Long size,
                                      @Param("condition") StatisticsComplaintCondition condition);

    @Select({
            "<script>",
            "SELECT cm.complainant,cm.complaint_time,cm.content,cm.channel,hr.handler,hr.handle_time",
            "FROM hy_complaint_manage cm,hy_handle_result hr",
            "WHERE cm.id=hr.complaint_id AND cm.status=4 AND hr.complaint_object_fullname=#{complaintObject}",
            "</script>"
    })
    List<MerchantComplaintVO> merchantComplaintList(String complaintObject);

    @Select({
            "<script>",
            "select count(*) FROM hy_complaint_manage m LEFT JOIN hy_handle_result r ON m.id=r.complaint_id",
            "WHERE m.complaint_time BETWEEN #{condition.startTime} AND #{condition.endTime}",
            "<if test='condition.industryType!=null and condition.industryType.length>0'>",
            "and r.industry_type=#{condition.industryType}",
            "</if>",
            "<if test='condition.type!=null and condition.type.length>0'>",
            "and m.type=#{condition.type}",
            "</if>",
            "<if test='condition.channel!=null and condition.channel.length>0'>",
            "and m.channel=#{condition.channel}",
            "</if>",
            "</script>"
    })
    int count(@Param("condition") StatisticsComplaintCondition condition);

    @Select({
            "<script>",
            "select r.industry_type,count(*) as num FROM hy_handle_result r LEFT JOIN hy_complaint_manage m ON m.id=r.complaint_id",
            "WHERE m.complaint_time BETWEEN #{beginTime} AND #{endTime}",
            "GROUP BY r.industry_type",
            "</script>"
    })
    List<Map<String,Object>> queryIndustryByTime(@Param("beginTime") String beginTime,
                                                 @Param("endTime") String endTime);

    @Select({
            "<script>",
            "SELECT DATE_FORMAT(complaint_time,'%Y-%m') as name,COUNT(*) as value",
            "FROM hy_complaint_manage",
            "WHERE complaint_time BETWEEN #{beginTime} AND #{endTime}",
            "GROUP BY name",
            "</script>"
    })
    List<ChartVO> complaintTrend(@Param("beginTime") String beginTime,
                                 @Param("endTime") String endTime);
}
