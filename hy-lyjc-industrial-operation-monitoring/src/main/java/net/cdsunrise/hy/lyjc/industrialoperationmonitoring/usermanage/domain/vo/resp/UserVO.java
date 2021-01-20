package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.resp;

import lombok.Data;

import java.util.List;

/**
 * @author LHY
 */
@Data
public class UserVO {
    private Long id;
    private String userName;
    private String phoneNum;
    private CompanyVO company;
    private DepartmentVO department;
    private List<RoleVO> roleList;
}
