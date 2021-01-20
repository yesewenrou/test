package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.AutoWarningContactConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;


/**
 * @author funnylog
 */
@Repository
public interface AutoWarningContactConfigMapper extends BaseMapper<AutoWarningContactConfig> {

    /**
     * 根据手机号查询
     *
     * @param phone 手机号
     * @return smsContact
     */
    @Select({"select * from `hy_auto_warning_contact_config` where `phone` = #{phone}"})
    AutoWarningContactConfig selectByPhone(@Param("phone") String phone);
}
