package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.constant.CommonConst;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.dto.MenuDTO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.resp.MenuVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.enums.BusinessExceptionEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.exception.BusinessException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.mapper.MenuMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.MenuService;
import net.cdsunrise.hy.sso.starter.common.CustomContext;
import net.cdsunrise.hy.sso.starter.domain.UserResp;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author LHY
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, MenuDTO> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;
    private static final String INNER_REQUEST = "inner";

    /**
     * 由于存在按钮菜单，因此允许菜单名称重复（不对外开放界面操作）
     */
    @Override
    public Long add(MenuDTO menuDTO) {
        try {
            menuMapper.insert(menuDTO);
            return menuDTO.getId();
        } catch (DuplicateKeyException e) {
            if (e.getMessage().contains(CommonConst.MENU_CODE_EXCEPTION)) {
                throw new BusinessException(BusinessExceptionEnum.MENU_CODE_EXIST);
            }
        }
        return 0L;
    }

    // 展示表中所有菜单列表，无任何筛选条件
    @Override
    public List<MenuVO> getMenuList() {
        Long parentId = 0L;
        return menuMapper.getMenuList(parentId);
    }

    // 根据userId，生成菜单列表树
    @Override
    public List<MenuVO> findByUserId(Long userId) {
        List<Long> menuIdList = menuMapper.getMenuByUserId(userId);
        // 完整菜单树
        List<MenuVO> list = getMenuList();
        return generateTree(list, menuIdList);
    }

    @Override
    public MenuDTO findById(Long id) {
        return menuMapper.selectById(id);
    }

    /**
     * AOP权限校验
     */
    @Override
    public String checkAuth(String permission) {
        String result = CommonConst.FAIL;
        // ThreadLocal中获取用户信息
        UserResp userResp = CustomContext.getTokenInfo().getUser();
        // 获取header信息，内部调用
        String header = CustomContext.getRequestSourceHeader();
        // 超管不做校验
        if (INNER_REQUEST.equals(header) || CommonConst.ADMIN_NAME.equals(userResp.getUserName())) {
            result = CommonConst.SUCCESS;
        } else {
            // 判断当前用户能见的权限列表，是否包含请求URL所需权限
            List<String> permissionList = findPermissionByUserId(userResp.getId());
            if (permissionList.contains(permission)) {
                result = CommonConst.SUCCESS;
            }
        }
        return result;
    }

    @Override
    public boolean userHasPermission(Long userId, String permission) {
        // 判断当前用户能见的权限列表，是否包含请求URL所需权限
        List<String> permissionList = findPermissionByUserId(userId);
        return permissionList.contains(permission);
    }

    // 用于权限验证
    private List<String> findPermissionByUserId(Long userId) {
        List<String> pathList = new ArrayList<>();
        // 树状菜单列表
        List<MenuVO> menuList = findByUserId(userId);
        getPermission(menuList, pathList);
        return pathList;
    }

    // 递归查询菜单permission
    private void getPermission(List<MenuVO> menuList, List<String> pathList) {
        menuList.forEach(menuVO -> {
            String permission = menuVO.getPermission();
            if (!Strings.isEmpty(permission)) {
                // 一个页面对应多个权限
                String[] split = permission.split(",");
                pathList.addAll(Arrays.asList(split));
            }
            List<MenuVO> childrenMenu = menuVO.getChildren();
            if (childrenMenu.size() > 0) {
                getPermission(childrenMenu, pathList);
            }
        });
    }

    /**
     * 生成部分树
     *
     * @param treeList   完整树
     * @param menuIdList 可见菜单ID
     */
    private List<MenuVO> generateTree(List<MenuVO> treeList, List<Long> menuIdList) {
        List<MenuVO> newTreeList = new ArrayList<>();
        treeList.forEach(menuVO -> {
            List<MenuVO> childTree = new ArrayList<>();
            if (menuVO.getChildren().size() > 0) {
                childTree = generateTree(menuVO.getChildren(), menuIdList);
            }
            if (menuIdList.contains(menuVO.getId()) || childTree.size() > 0) {
                MenuVO newMenuVO = new MenuVO();
                BeanUtils.copyProperties(menuVO, newMenuVO);
                newMenuVO.setChildren(childTree);
                newTreeList.add(newMenuVO);
            }
        });
        return newTreeList;
    }

    @Override
    public Integer countByUserId(Long userId) {
        return menuMapper.countByUserId(userId);
    }
}
