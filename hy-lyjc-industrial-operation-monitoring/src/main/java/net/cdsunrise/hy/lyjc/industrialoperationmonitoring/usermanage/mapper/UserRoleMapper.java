package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.dto.UserRoleDTO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author LHY
 */
public interface UserRoleMapper extends BaseMapper<UserRoleDTO> {
    @Select("select user_id from hy_user_role where role_id=#{roleId}")
    List<Long> findByRoleId(Long roleId);

    /**
     * 根据菜单id查询用户ID
     *
     * @param wrapper 查询条件
     * @return 用户ID列表
     */
    @Select("SELECT DISTINCT(`hy_user_role`.`user_id`) " +
            "FROM `hy_role_menu` " +
            "LEFT JOIN `hy_role` ON `hy_role_menu`.`role_id` = `hy_role`.`id` " +
            "LEFT JOIN `hy_user_role` ON `hy_role`.`id` = `hy_user_role`.`role_id` " +
            "${ew.customSqlSegment}")
    List<Long> findByMenuIds(@Param(Constants.WRAPPER) Wrapper wrapper);
}
