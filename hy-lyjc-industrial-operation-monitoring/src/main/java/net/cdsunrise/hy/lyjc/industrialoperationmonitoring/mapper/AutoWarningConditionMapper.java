package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarningCondition;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author funnylog
 */
@Repository
public interface AutoWarningConditionMapper extends BaseMapper<AutoWarningCondition> {

    /**
     * 根据景区名称查询
     * @param scenicName 景区名称
     * @return AutoWarningCondition
     */
    @Select("select * from `hy_auto_warning_condition` where scenic_name = #{scenicName}")
    AutoWarningCondition selectByScenicName(String scenicName);
}
