package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.dto.RoleDTO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.resp.RoleVO;

import java.util.List;
import java.util.Map;

/**
 * @author LHY
 */
public interface RoleService {

    /**
     * 新增角色
     *
     * @param menuIdList
     * @param name       角色名称
     */
    Long add(String name, List<Long> menuIdList);

    /**
     * 更新角色
     *
     * @param id
     * @param menuIdList
     * @param name       角色名称
     */
    void update(Long id, String name, List<Long> menuIdList);

    /**
     * 删除角色
     */
    void delete(Long id);

    /**
     * 根据主键查询
     */
    Map<String, Object> findById(Long id);

    /**
     * 启用角色
     */
    void enable(Long id);

    /**
     * 禁用角色
     */
    void disable(Long id);

    /**
     * 根据userId查询
     */
    List<RoleVO> findByUserId(Long userId);

    /**
     * 分页查询
     *
     * @param page 当前页
     * @param size 页数
     * @param name 角色名称
     */
    IPage<RoleDTO> list(Integer page, Integer size, String name);

    /**
     * 根据List<Long> roleIdList查询List<Role> roleList
     */
    List<RoleVO> findByRoleIdList(List<Long> roleIdList);
}
