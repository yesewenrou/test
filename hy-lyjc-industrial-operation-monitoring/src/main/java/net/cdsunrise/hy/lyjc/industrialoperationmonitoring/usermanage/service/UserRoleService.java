package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service;

import java.util.List;

/**
 * @author LHY
 */
public interface UserRoleService {
    /**
     * 给某个用户分配角色
     *
     * @param userId     用户
     * @param roleIdList 角色列表
     */
    void update(Long userId, List<Long> roleIdList);

    /**
     * 根据roleId查询用户列表
     */
    List<Long> findByRoleId(Long roleId);

    /**
     * 根据权限查询用户ID列表
     *
     * @param permission 权限
     * @return 用户ID列表
     */
    List<Long> findByPermission(String permission);
}
