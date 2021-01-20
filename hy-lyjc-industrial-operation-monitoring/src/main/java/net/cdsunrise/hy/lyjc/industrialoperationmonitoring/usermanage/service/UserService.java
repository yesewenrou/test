package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.req.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.resp.UserVO;
import net.cdsunrise.hy.sso.starter.domain.UserResp;

import java.util.List;

/**
 * @author LHY
 */
public interface UserService {
    Page<UserVO> list(PageRequest pageRequest);

    /**
     * 根据ID获取用户信息
     *
     * @param id 用户ID
     * @return 结果
     */
    UserResp getUserById(Long id);

    /**
     * 查询拥有某权限的用户列表
     *
     * @param permission 权限
     * @return 结果
     */
    List<UserVO> findByPermission(String permission);
}
