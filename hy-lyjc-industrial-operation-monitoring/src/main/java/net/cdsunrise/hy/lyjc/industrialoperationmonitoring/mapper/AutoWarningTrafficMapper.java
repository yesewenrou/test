package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarningTraffic;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author funnylog
 */
@Repository
public interface AutoWarningTrafficMapper extends BaseMapper<AutoWarningTraffic> {

    /**
     * 根据预警ID查询
     * @param warningId
     * @return AutoWarningTraffic
     */
    @Select("select * from `hy_auto_warning_traffic` where `warning_id` = #{warningId}")
    AutoWarningTraffic selectByWarningId(Long warningId);
}
