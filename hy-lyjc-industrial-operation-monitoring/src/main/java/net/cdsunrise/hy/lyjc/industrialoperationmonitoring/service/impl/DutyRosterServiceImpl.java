package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.DutyDepartment;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.DutyMember;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.typeenum.DayOfWeek;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.DutyRoster;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.DutyRosterMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IDutyDepartmentService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IDutyMemberService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IDutyRosterService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DutyRosterRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DutyRosterVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lijiafeng
 * @date 2019/11/25 10:35
 */
@Service
public class DutyRosterServiceImpl extends ServiceImpl<DutyRosterMapper, DutyRoster> implements IDutyRosterService {

    private IDutyDepartmentService dutyDepartmentService;
    private IDutyMemberService dutyMemberService;

    public DutyRosterServiceImpl(IDutyDepartmentService dutyDepartmentService, IDutyMemberService dutyMemberService) {
        this.dutyDepartmentService = dutyDepartmentService;
        this.dutyMemberService = dutyMemberService;
    }

    @Override
    public List<DutyRosterVO> listDutyRoster() {
        // 根据枚举查询值班表
        return Arrays.stream(DayOfWeek.values()).map(dayOfWeek -> {
            DutyRosterVO dutyRosterVO = new DutyRosterVO();
            dutyRosterVO.setDutyTime(dayOfWeek);
            dutyRosterVO.setDutyTimeDesc(dayOfWeek.getDesc());
            QueryWrapper<DutyRoster> queryWrapper = Wrappers.query();
            queryWrapper.lambda().eq(DutyRoster::getDutyTime, dayOfWeek);
            DutyRoster dutyRoster = super.getOne(queryWrapper);
            if (dutyRoster != null) {
                BeanUtils.copyProperties(dutyRoster, dutyRosterVO);
                //查人員信息
                DutyMember dutyMember = dutyMemberService.getById(dutyRoster.getMemberId());
                dutyRosterVO.setDutyPerson(dutyMember.getDutyPerson());
                dutyRosterVO.setContact(dutyMember.getDutyContact());

                //查部門信息
                DutyDepartment dutyDepartment = dutyDepartmentService.getById(dutyMember.getDepartmentId());
                dutyRosterVO.setDutyInstitutions(dutyDepartment.getDutyInstitutions());
                dutyRosterVO.setDutyDepartment(dutyDepartment.getDepartmentName());
                dutyRosterVO.setDepartmentLeader(dutyDepartment.getDepartmentLeader());
                dutyRosterVO.setLeaderContact(dutyDepartment.getLeaderContact());
            }

            return dutyRosterVO;
        }).collect(Collectors.toList());
    }

    @Override
    public DutyRosterVO getDutyRoster(String dutyTime) {
        if (dutyTime == null) {
            throw new ParamErrorException("资源ID不能为空");
        }
        QueryWrapper<DutyRoster> queryWrapper = Wrappers.query();
        queryWrapper.lambda().eq(DutyRoster::getDutyTime, dutyTime);
        DutyRoster dutyRoster = super.getOne(queryWrapper);
        if(dutyRoster == null){
            throw new ParamErrorException("资源不存在");
        }

        DutyRosterVO dutyRosterVO = new DutyRosterVO();
        BeanUtils.copyProperties(dutyRoster, dutyRosterVO);

        //查人員信息
        DutyMember dutyMember = dutyMemberService.getById(dutyRoster.getMemberId());
        dutyRosterVO.setMemberId(dutyMember.getId());
        dutyRosterVO.setDutyPerson(dutyMember.getDutyPerson());
        dutyRosterVO.setContact(dutyMember.getDutyContact());

        //查部門信息
        DutyDepartment dutyDepartment = dutyDepartmentService.getById(dutyMember.getDepartmentId());
        dutyRosterVO.setDutyInstitutions(dutyDepartment.getDutyInstitutions());
        dutyRosterVO.setDutyDepartment(dutyDepartment.getDepartmentName());

        return dutyRosterVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDutyRoster(DutyRosterRequest dutyRosterRequest) {
        DutyRosterVO dutyRosterVO = new DutyRosterVO();
        DayOfWeek dutyTime = dutyRosterRequest.getDutyTime();
        QueryWrapper<DutyRoster> queryWrapper = Wrappers.query();
        queryWrapper.lambda().eq(DutyRoster::getDutyTime, dutyTime);
        DutyRoster dutyRoster = super.getOne(queryWrapper);
        if (dutyRoster == null) {
            dutyRoster = new DutyRoster();
            BeanUtils.copyProperties(dutyRosterVO, dutyRoster);
            return super.save(dutyRoster);
        }
        dutyRoster.setMemberId(dutyRosterRequest.getMemberId());
        return super.updateById(dutyRoster);
    }


}
