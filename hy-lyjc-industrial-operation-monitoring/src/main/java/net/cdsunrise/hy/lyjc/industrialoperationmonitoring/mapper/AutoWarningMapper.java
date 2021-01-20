package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarning;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author funnylog
 */
@Repository
public interface AutoWarningMapper extends BaseMapper<AutoWarning> {

    /**
     * 根据状态分组
     * @return Map
     */
    @Select("select `status`, count(`status`) count from `hy_auto_warning` group by `status`")
    List<Map<String, Integer>> countGroupByStatus();

    /**
     * 根据类型和状态查询数量
     * @param type 类型编码
     * @param status  状态编码
     * @return 数量
     */
    @Select("select count(*) count from `hy_auto_warning` where `type` = #{type} and `status` = #{status} ")
    Integer selectCountByTypeAndStatus(String type, String status);

    /**
     * 根据状态查询
     * @param status 预警状态
     * @return 列表
     */
    @Select("select * from `hy_auto_warning` where `status` = #{status} ")
    List<AutoWarning> selectByStatus(String status);

    /**
     * 根据应急事件id查询 自动预警信息
     * @param emergencyEventId 预警事件id
     * @return AutoWarning
     */
    @Select("select * from `hy_auto_warning` where `emergency_event_id` = ${emergencyEventId}")
    AutoWarning selectByEmergencyEventId(@Param("emergencyEventId") Long emergencyEventId);

    
}
