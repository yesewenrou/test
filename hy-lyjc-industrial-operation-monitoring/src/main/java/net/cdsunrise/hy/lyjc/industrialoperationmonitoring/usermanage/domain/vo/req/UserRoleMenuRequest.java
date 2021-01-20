package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @author LHY
 */
@Data
public class UserRoleMenuRequest {
    private Long userId;
    private String roleName;
    private String path;
    private Long roleId;
    private List<Long> menuIdList;
    private List<Long> roleIdList;
}
