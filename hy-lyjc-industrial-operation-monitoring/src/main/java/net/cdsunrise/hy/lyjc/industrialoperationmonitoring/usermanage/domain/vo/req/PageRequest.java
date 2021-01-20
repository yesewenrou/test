package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.req;

import lombok.Data;

import java.util.List;

/**
 * @author LHY
 */
@Data
public class PageRequest {
    private Integer page;
    private Integer size;
    private String userName;
    private Long roleId;
    private String roleName;
    private Long companyId;
    private Long departmentId;
    private List<Long> userIds;
    private String platformCode;
}
