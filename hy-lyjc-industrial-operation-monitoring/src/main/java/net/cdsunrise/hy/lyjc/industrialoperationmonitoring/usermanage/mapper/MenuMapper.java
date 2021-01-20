package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.dto.MenuDTO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.resp.MenuVO;
import org.apache.ibatis.annotations.Many;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author LHY
 */
@Repository
public interface MenuMapper extends BaseMapper<MenuDTO> {
    @Select("SELECT count(DISTINCT(m.id)) as num FROM hy_role r"
            + ",hy_user_role ur,hy_role_menu rm,hy_menu m WHERE r.id=ur.role_id and ur.role_id=rm.role_id AND "
            + "rm.menu_id=m.id and ur.user_id=#{userId} and r.status=1 and r.deleted=0")
    Integer countByUserId(Long userId);

    /**
     * 递归生成菜单树
     */
    @Select("SELECT * FROM hy_menu WHERE parent_id = #{parentId} order by weight")
    @Results({
            @Result(id = true, column = "id", property = "id", javaType = Long.class),
            @Result(column = "name", property = "name", javaType = String.class),
            @Result(column = "code", property = "code", javaType = String.class),
            @Result(column = "path", property = "path", javaType = String.class),
            @Result(column = "permission", property = "permission", javaType = String.class),
            @Result(column = "parent_id", property = "parentId", javaType = Long.class),
            @Result(column = "module_type", property = "moduleType", javaType = String.class),
            @Result(column = "icon", property = "icon", javaType = String.class),
            @Result(column = "mark", property = "mark", javaType = String.class),
            @Result(column = "weight", property = "weight", javaType = Long.class),
            @Result(column = "id", property = "children", javaType = List.class,
                    many = @Many(select = "getMenuList"))
    })
    List<MenuVO> getMenuList(Long parentId);

    @Select("SELECT DISTINCT(m.id) FROM hy_role r"
            + ",hy_user_role ur,hy_role_menu rm,hy_menu m WHERE r.id=ur.role_id and ur.role_id=rm.role_id AND "
            + "rm.menu_id=m.id and ur.user_id=#{userId} and r.status=1 and r.deleted=0")
    List<Long> getMenuByUserId(Long userId);
}
