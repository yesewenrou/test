package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.MessageMenuEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.dto.RoleMenuDTO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.mapper.RoleMenuMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LHY
 */
@Service
public class RoleMenuServiceImpl implements RoleMenuService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Override
    public void update(Long roleId, List<Long> menuIdList) {
        // 先删除该roleId关联的数据
        delete(roleId);
        menuIdList.forEach(menuId -> {
            RoleMenuDTO roleMenuDTO = new RoleMenuDTO();
            roleMenuDTO.setRoleId(roleId);
            roleMenuDTO.setMenuId(menuId);
            roleMenuMapper.insert(roleMenuDTO);
        });
    }

    @Override
    public List<Object> selectByRoleId(Long roleId) {
        QueryWrapper<RoleMenuDTO> wrapper = new QueryWrapper<>();
        wrapper.select("menu_id").eq("role_id", roleId);
        return roleMenuMapper.selectObjs(wrapper);
    }

    @Override
    public void delete(Long roleId) {
        QueryWrapper<RoleMenuDTO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(RoleMenuDTO::getRoleId, roleId);
        roleMenuMapper.delete(wrapper);
    }

    @Override
    public List<Long> findUserByMenuForWarningMessage(MessageMenuEnum messageMenuEnum) {
        // 拥有目标菜单权限
        List<Long> userList1 = roleMenuMapper.getUserIdByMenuCode(messageMenuEnum.getCode());
        // 或者预警管理菜单权限
        List<Long> userList2 = roleMenuMapper.getUserIdByMenuCode(MessageMenuEnum.OPERATION_WARNING_MANAGE.getCode());
        // 汇总去重
        List<Long> userList = new ArrayList<>();
        userList.addAll(userList1);
        userList.addAll(userList2);

        return userList.stream().distinct().collect(Collectors.toList());
    }
}
