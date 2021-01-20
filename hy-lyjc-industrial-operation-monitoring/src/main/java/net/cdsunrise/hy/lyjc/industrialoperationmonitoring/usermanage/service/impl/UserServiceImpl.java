package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.req.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.resp.RoleVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.resp.UserVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.enums.BusinessExceptionEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.exception.BusinessException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.RoleService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.UserRoleService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.UserService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.service.feign.SsoFeignService;
import net.cdsunrise.hy.sso.starter.domain.UserResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author LHY
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    /**
     * 提交商户问题整改结果的接口的权限标识
     */
    private static final String PERMISSION_MERCHANT_PROBLEM_RECTIFICATION = "merchant:warn:rectification";

    @Autowired
    private SsoFeignService ssoFeignService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserRoleService userRoleService;
    @Value("${sso.service.defaultMoudle}")
    private String platformCode;

    @Override
    public Page<UserVO> list(PageRequest pageRequest) {
        Page<UserVO> returnPage = new Page<>(pageRequest.getPage(), pageRequest.getSize());
        /*
         * 搜索条件加上角色，处理逻辑如下：先根据角色去本地查出用户ID列表，接着作为参数feign单点登录获取带分页信息的用户列表;
         * 因为要是先拿到分页数据后，再在本地做角色筛选，会出现第一页查询不到，但第二页有数据的情况！！！
         * */
        if (pageRequest.getRoleId() != null) {
            List<Long> userIdList = userRoleService.findByRoleId(pageRequest.getRoleId());
            /**角色在当前平台没有对应用户，则直接返回，不去单点登录请求用户详情**/
            if (CollectionUtils.isEmpty(userIdList)) {
                return returnPage;
            }
            pageRequest.setUserIds(userIdList);
        }
        System.out.println("===参数信息：" + pageRequest.toString());
        Result<Page<UserVO>> pageResult = ssoFeignService.userList(pageRequest);
        Long count = pageResult.getData().getTotal();
        List<UserVO> userList = pageResult.getData().getRecords();
        List<UserVO> collect = userList.stream().map(userVO -> {
            List<RoleVO> roleList = roleService.findByUserId(userVO.getId());
            userVO.setRoleList(roleList);
            return userVO;
        }).collect(Collectors.toList());
        returnPage.setRecords(collect);
        returnPage.setTotal(count);
        return returnPage;
    }

    @Override
    public UserResp getUserById(Long id) {
        Result<UserResp> userRespResult = ssoFeignService.find(id);
        return userRespResult.getData();
    }

    @Override
    public List<UserVO> findByPermission(String permission) {
        List<UserVO> userVoList = new ArrayList<>();
        List<Long> userIdList = userRoleService.findByPermission(permission);
        if (CollectionUtils.isEmpty(userIdList)) {
            return userVoList;
        }
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPlatformCode(platformCode);
        pageRequest.setPage(1);
        pageRequest.setSize(Integer.MAX_VALUE);
        pageRequest.setUserIds(userIdList);
        try {
            Result<Page<UserVO>> result = ssoFeignService.userList(pageRequest);
            if (result.getSuccess()) {
                Page<UserVO> userVoPage = result.getData();
                if (userVoPage != null) {
                    userVoList = result.getData().getRecords();
                }
            } else {
                log.error("查询用户列表失败: {}", result.getMessage());
                BusinessException businessException = new BusinessException(BusinessExceptionEnum.FAILED);
                businessException.setMessage("查询用户列表失败");
                throw businessException;
            }
        } catch (Exception e) {
            log.error("查询用户列表异常", e);
            BusinessException businessException = new BusinessException(BusinessExceptionEnum.FAILED);
            businessException.setMessage("查询用户列表异常");
            throw businessException;
        }
        return userVoList;
    }
}
