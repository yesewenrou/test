package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.DutyMember;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.*;

import java.util.List;
import java.util.Map;

/**
 * @author lixiao
 */
public interface IDutyMemberService extends IService<DutyMember> {

    Long saveDutyMember(DutyMemberRequest dutyMemberRequest);

    boolean deleteDutyMember(Long id);

    DutyMemberVO getDutyMember(Long id);

    boolean updateDutyMember(DutyMemberRequest dutyMemberRequest);

    IPage<DutyMemberVO> listDutyMember(PageRequest<DutyMemberCondition> pageRequest);

    Map<Long, String> getMemberByDepartment(Long departmentId);

    Map<Long, String> getContactByMember(Long memberId);
}
