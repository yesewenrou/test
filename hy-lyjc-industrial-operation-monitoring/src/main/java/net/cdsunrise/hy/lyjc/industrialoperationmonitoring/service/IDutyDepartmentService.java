package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.DutyDepartment;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DutyDepartmentCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DutyDepartmentVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;

import java.util.Map;

/**
 * @author lixiao
 */
public interface IDutyDepartmentService extends IService<DutyDepartment> {

    Long saveDutyDepartment(DutyDepartmentVO dutyDepartmentVO);

    boolean deleteDutyDepartment(Long id);

    boolean updateDutyDepartment(DutyDepartmentVO dutyDepartmentVO);

    DutyDepartmentVO getDutyDepartment(Long id);

    IPage<DutyDepartmentVO> listDepartment(PageRequest<DutyDepartmentCondition> pageRequest);

    Map<String, String> getInstitutions();

    Map<Long, String> getDepartmentByInstitutions(String institutions);
}
