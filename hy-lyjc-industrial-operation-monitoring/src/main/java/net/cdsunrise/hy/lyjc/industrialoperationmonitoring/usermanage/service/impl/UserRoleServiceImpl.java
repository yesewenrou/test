package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.OperationEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.dto.MenuDTO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.dto.PlatformUserLgDTO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.dto.UserRoleDTO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.resp.RoleVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.mapper.UserRoleMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.MenuService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.RoleService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.UserRoleService;
import net.cdsunrise.hy.record.starter.service.RecordService;
import net.cdsunrise.hy.sso.starter.common.CustomContext;
import net.cdsunrise.hy.sso.starter.domain.Page;
import net.cdsunrise.hy.sso.starter.domain.UserReq;
import net.cdsunrise.hy.sso.starter.domain.UserResp;
import net.cdsunrise.hy.sso.starter.service.SsoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LHY
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {

    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RecordService recordService;
    @Autowired
    private SsoService ssoService;
    @Autowired
    private RoleService roleService;

    @Override
    public void update(Long userId, List<Long> roleIdList) {
        // 先删除该userId关联的角色
        delete(userId);
        roleIdList.forEach(roleId -> {
            UserRoleDTO userRoleDTO = new UserRoleDTO();
            userRoleDTO.setUserId(userId);
            userRoleDTO.setRoleId(roleId);
            userRoleMapper.insert(userRoleDTO);
        });
        // 增加日志记录
        UserReq req = new UserReq();
        req.setUserIds(Collections.singletonList(userId));
        req.setAdmin(true);
        Result<Page<UserResp>> pageResult = ssoService.fetchUserByReq(req);
        String userName = pageResult.getData().getRecords().get(0).getUserName();
        List<RoleVO> roleList = roleService.findByRoleIdList(roleIdList);
        String roleNames = roleList.stream().map(RoleVO::getName).collect(Collectors.joining(","));
        PlatformUserLgDTO platformUserLgDTO = new PlatformUserLgDTO();
        platformUserLgDTO.setUserName(userName);
        platformUserLgDTO.setRoleNames(roleNames);
        recordService.operation(OperationEnum.PLATFORM_USER,platformUserLgDTO,
                CustomContext.getTokenInfo().getUser().getId(),"角色配置");
    }

    @Override
    public List<Long> findByRoleId(Long roleId) {
        return userRoleMapper.findByRoleId(roleId);
    }

    private void delete(Long userId) {
        QueryWrapper<UserRoleDTO> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserRoleDTO::getUserId, userId);
        userRoleMapper.delete(wrapper);
    }

    @Override
    public List<Long> findByPermission(String permission) {
        // 获取拥有相应权限的菜单项ID
        List<MenuDTO> menuDtoList = menuService.list();
        if (CollectionUtils.isEmpty(menuDtoList)) {
            return null;
        }
        List<Long> menuIdList = menuDtoList.stream().filter(menuDTO -> {
            String permissionString = menuDTO.getPermission();
            if (StringUtils.isEmpty(permissionString)) {
                return false;
            }
            String[] splitPermissions = permissionString.split(",");
            return Arrays.asList(splitPermissions).contains(permission);
        }).map(MenuDTO::getId).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(menuIdList)) {
            return null;
        }
        //查询拥有任意菜单项的用户ID
        QueryWrapper<Object> queryWrapper = Wrappers.query();
        queryWrapper.eq("`hy_role`.`status`", 1);
        queryWrapper.eq("`hy_role`.`deleted`", 0);
        queryWrapper.in("`hy_role_menu`.`menu_id`", menuIdList);
        queryWrapper.having("`hy_user_role`.`user_id` IS NOT NULL");
        return userRoleMapper.findByMenuIds(queryWrapper);
    }
}
