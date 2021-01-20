package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.dto.RoleDTO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.resp.RoleVO;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author LHY
 */
public interface RoleMapper extends BaseMapper<RoleDTO> {
    @Select("select r.id,r.name,r.status from hy_role r,hy_user_role ur where r.id=ur.role_id and ur.user_id=#{userId} ")
    List<RoleVO> findByUserId(Long userId);

    @Select("select count(*) from hy_user_role where role_id=#{roleId}")
    int findByRoleId(Long roleId);
}
