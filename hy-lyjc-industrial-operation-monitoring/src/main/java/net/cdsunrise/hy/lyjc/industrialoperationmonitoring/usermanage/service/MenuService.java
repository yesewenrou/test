package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.dto.MenuDTO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.resp.MenuVO;

import java.util.List;

/**
 * @author LHY
 */
public interface MenuService extends IService<MenuDTO> {

    Long add(MenuDTO menuDTO);

    /**
     * 菜单结构
     */
    List<MenuVO> getMenuList();

    /**
     * 根据userId，生成菜单列表树
     */
    List<MenuVO> findByUserId(Long userId);

    MenuDTO findById(Long id);

    /**
     * AOP权限校验
     */
    String checkAuth(String permission);

    /**
     * 检查某个用户是否有权限
     *
     * @param userId     用户Id
     * @param permission 权限
     * @return 结果
     */
    boolean userHasPermission(Long userId, String permission);

    /**
     * 查看用户可见菜单数量
     */
    Integer countByUserId(Long userId);
}
