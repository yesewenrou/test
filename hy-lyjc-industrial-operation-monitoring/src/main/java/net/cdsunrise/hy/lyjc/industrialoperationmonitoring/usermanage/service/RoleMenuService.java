package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.MessageMenuEnum;

import java.util.List;

/**
 * @author LHY
 */
public interface RoleMenuService {

    /**
     * 给某个角色分配权限
     *
     * @param roleId     角色
     * @param menuIdList 菜单列表
     */
    void update(Long roleId, List<Long> menuIdList);

    List<Object> selectByRoleId(Long roleId);

    void delete(Long roleId);

    /**
     * 根据菜单编码查找拥有权限的用户列表
     *
     * @param messageMenuEnum 菜单枚举
     * @return 结果
     */
    List<Long> findUserByMenuForWarningMessage(MessageMenuEnum messageMenuEnum);
}
