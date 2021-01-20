package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.DutyDepartment;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.DutyDepartmentMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IDutyDepartmentService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.PageUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.*;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
/**
 * @author lixiao
 */
@Service
public class DutyDepartmentServiceImpl extends ServiceImpl<DutyDepartmentMapper, DutyDepartment> implements IDutyDepartmentService {

    private static final List<String> ORDER_COLUMNS = Arrays.asList("dutyInstitutions", "departmentName", "gmt_modified");

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveDutyDepartment(DutyDepartmentVO dutyDepartmentVO) {

        DutyDepartment dutyDepartment = new DutyDepartment();
        BeanUtils.copyProperties(dutyDepartmentVO, dutyDepartment);
        super.save(dutyDepartment);
        return dutyDepartment.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDutyDepartment(Long id) {
        if (id == null) {
            throw new ParamErrorException("资源ID不能为空");
        }
        return super.removeById(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateDutyDepartment(DutyDepartmentVO dutyDepartmentVO) {
        DutyDepartment dutyDepartment = new DutyDepartment();
        BeanUtils.copyProperties(dutyDepartmentVO, dutyDepartment);
        return super.updateById(dutyDepartment);
    }

    @Override
    public DutyDepartmentVO getDutyDepartment(Long id) {
        if (id == null) {
            throw new ParamErrorException("资源ID不能为空");
        }

        DutyDepartment dutyDepartment = super.getById(id);
        if(dutyDepartment == null){
            throw new ParamErrorException("资源不存在");
        }
        DutyDepartmentVO dutyDepartmentVO = new DutyDepartmentVO();
        BeanUtils.copyProperties(dutyDepartment, dutyDepartmentVO);

        return dutyDepartmentVO;
    }

    @Override
    public IPage<DutyDepartmentVO> listDepartment(PageRequest<DutyDepartmentCondition> pageRequest) {
        Page<DutyDepartment> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        QueryWrapper<DutyDepartment> queryWrapper = Wrappers.query();
        DutyDepartmentCondition condition = pageRequest.getCondition();
        if (condition != null) {

        }
        // 排序
        PageUtil.setOrders(queryWrapper, ORDER_COLUMNS, pageRequest.getOrderItemList());
        super.page(page, queryWrapper);
        return page.convert(dutyDepartment -> {
            DutyDepartmentVO dutyDepartmentVO = new DutyDepartmentVO();
            BeanUtils.copyProperties(dutyDepartment, dutyDepartmentVO);
            return dutyDepartmentVO;
        });
    }

    @Override
    public Map<String, String> getInstitutions() {
        List<DutyDepartment> source = super.list(Wrappers.emptyWrapper());
        Map<String, String> map = source.stream().collect(Collectors.toMap(DutyDepartment::getDutyInstitutions, DutyDepartment::getDutyInstitutions, (value1, value2)->{
            return value2;
        }));
//        List<String> list = source.stream().map(e -> e.getDutyInstitutions()).distinct().collect(Collectors.toList());
        return map;
    }

    @Override
    public Map<Long, String> getDepartmentByInstitutions(String institutions) {
        QueryWrapper<DutyDepartment> queryWrapper = Wrappers.query();
        queryWrapper.lambda().eq(StringUtils.hasText(institutions), DutyDepartment::getDutyInstitutions, institutions);
        List<DutyDepartment> source = super.list(queryWrapper);
        Map<Long, String> map = source.stream().collect(Collectors.toMap(DutyDepartment::getId, DutyDepartment::getDepartmentName, (value1, value2)-> {
            return value2;
        }));
//        List<String> list = source.stream().map(e -> e.getDepartmentName()).distinct().collect(Collectors.toList());
        return map;
    }
}