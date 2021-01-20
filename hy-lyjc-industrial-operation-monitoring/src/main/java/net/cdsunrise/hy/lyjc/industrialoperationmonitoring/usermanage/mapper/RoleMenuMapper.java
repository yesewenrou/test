package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.dto.RoleMenuDTO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author LHY
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenuDTO> {
    /**
     * 集成消息提醒
     * getUserIdByMenuCode
     *
     * @param code 菜单code
     * @return List
     */
    @Select("SELECT DISTINCT ur.user_id FROM hy_role_menu rm JOIN hy_user_role ur ON rm.role_id = ur.role_id JOIN hy_menu m ON rm.menu_id = m.id WHERE m.`code`=#{code}")
    List<Long> getUserIdByMenuCode(String code);

}
