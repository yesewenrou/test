package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.common.utility.StringUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.DutyDepartment;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.DutyMember;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.DutyMemberMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IDutyDepartmentService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IDutyMemberService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.PageUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
/**
 * @author lixiao
 */
@Service
public class DutyMemberServiceImpl extends ServiceImpl<DutyMemberMapper, DutyMember> implements IDutyMemberService {

    private static final List<String> ORDER_COLUMNS = Arrays.asList("dutyInstitutions", "departmentName", "gmt_modified");

    private IDutyDepartmentService dutyDepartmentService;

    public DutyMemberServiceImpl(IDutyDepartmentService dutyDepartmentService) {
        this.dutyDepartmentService = dutyDepartmentService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveDutyMember(DutyMemberRequest dutyMemberRequest) {
        DutyMember dutyMember = new DutyMember();
        BeanUtils.copyProperties(dutyMemberRequest, dutyMember);
        super.save(dutyMember);
        return dutyMember.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDutyMember(Long id) {
        if (id == null) {
            throw new ParamErrorException("资源ID不能为空");
        }
        return super.removeById(id);
    }

    @Override
    public DutyMemberVO getDutyMember(Long id) {
        if (id == null) {
            throw new ParamErrorException("资源ID不能为空");
        }
        DutyMember dutyMember = super.getById(id);
        if(dutyMember == null){
            throw new ParamErrorException("资源不存在");
        }
        DutyDepartmentVO departmentVO = dutyDepartmentService.getDutyDepartment(dutyMember.getDepartmentId());

        DutyMemberVO dutyMemberVO = new DutyMemberVO();
        dutyMemberVO.setDutyInstitutions(departmentVO.getDutyInstitutions());
        dutyMemberVO.setDutyDepartment(departmentVO.getDepartmentName());
        BeanUtils.copyProperties(dutyMember, dutyMemberVO);
        return dutyMemberVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDutyMember(DutyMemberRequest dutyMemberRequest) {
        DutyMember dutyMember = new DutyMember();
        BeanUtils.copyProperties(dutyMemberRequest, dutyMember);
        return super.updateById(dutyMember);
    }

    @Override
    public IPage<DutyMemberVO> listDutyMember(PageRequest<DutyMemberCondition> pageRequest) {
        Page<DutyMember> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        QueryWrapper<DutyMember> queryWrapper = Wrappers.query();
        DutyMemberCondition condition = pageRequest.getCondition();
        if (condition != null) {

        }
        // 排序
        PageUtil.setOrders(queryWrapper, ORDER_COLUMNS, pageRequest.getOrderItemList());
        super.page(page, queryWrapper);
        Set<String> set = new HashSet<>();
        page.getRecords().stream().forEach(e -> {
            set.add(e.getDepartmentId() + "");
        });

        QueryWrapper<DutyDepartment> qw = Wrappers.query();
        qw.lambda().select(DutyDepartment::getId, DutyDepartment::getDutyInstitutions, DutyDepartment::getDepartmentName).in(!set.isEmpty(), DutyDepartment::getId, set);
        List<DutyDepartment> idList = dutyDepartmentService.list(qw);

        Map<Long, DutyDepartment> map = idList.stream().collect(Collectors.toMap(DutyDepartment::getId, Function.identity()));

        return page.convert(dutyMember -> {
            DutyMemberVO dutyMemberVO = new DutyMemberVO();
            BeanUtils.copyProperties(dutyMember, dutyMemberVO);
            if(map.containsKey(dutyMember.getDepartmentId())){
                dutyMemberVO.setDutyInstitutions(map.get(dutyMember.getDepartmentId()).getDutyInstitutions());
                dutyMemberVO.setDutyDepartment(map.get(dutyMember.getDepartmentId()).getDepartmentName());
            }

            return dutyMemberVO;
        });
    }

    @Override
    public Map<Long, String> getMemberByDepartment(Long departmentId) {
        QueryWrapper<DutyMember> queryWrapper = Wrappers.query();
        queryWrapper.lambda().eq(DutyMember::getDepartmentId, departmentId);
        List<DutyMember> source = super.list(queryWrapper);
        Map<Long, String> map = source.stream().collect(Collectors.toMap(DutyMember::getId, DutyMember::getDutyPerson, (value1, value2)-> {
            return value2;
        }));
//        List<String> list = source.stream().map(e -> e.getDutyPerson()).distinct().collect(Collectors.toList());
        return map;
    }

    @Override
    public Map<Long, String> getContactByMember(Long memberId) {
        QueryWrapper<DutyMember> queryWrapper = Wrappers.query();
        queryWrapper.lambda().eq(DutyMember::getId, memberId);
        List<DutyMember> source = super.list(queryWrapper);
        Map<Long, String> map = source.stream().collect(Collectors.toMap(DutyMember::getId, DutyMember::getDutyContact, (value1, value2)-> {
            return value2;
        }));
//        List<String> list = source.stream().map(e -> e.getDutyPerson()).distinct().collect(Collectors.toList());
        return map;
    }


}