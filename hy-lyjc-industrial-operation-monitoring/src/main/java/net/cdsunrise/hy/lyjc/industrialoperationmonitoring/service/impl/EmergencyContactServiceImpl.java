package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import cn.hutool.core.convert.Convert;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.EmergencyContact;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.helper.EmergencyContactHelper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.EmergencyContactService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.PageUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyContactDetailVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyContactRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyContactVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @author fang yun long
 * on 2021/1/19
 */
@Service
@Slf4j
public class EmergencyContactServiceImpl implements EmergencyContactService {
    private final EmergencyContactHelper emergencyContactHelper;

    public EmergencyContactServiceImpl(EmergencyContactHelper emergencyContactHelper) {
        this.emergencyContactHelper = emergencyContactHelper;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveEmergencyContact(EmergencyContactRequest.Add request) {
        log.info("saveEmergencyContact ... [{}]", request.toString());
        EmergencyContact contact = new EmergencyContact();
        BeanUtils.copyProperties(request, contact);
        emergencyContactHelper.save(contact);
        return contact.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long updateEmergencyContact(EmergencyContactRequest.Update request) {
        log.info("updateEmergencyContact ... [{}]", request.toString());

        EmergencyContact contact = emergencyContactHelper.getById(request.getId());
        if (Objects.isNull(contact)) {
            throw new ParamErrorException("id不存在");
        }
        BeanUtils.copyProperties(request, contact);
        emergencyContactHelper.updateById(contact);
        return contact.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long deleteEmergencyContact(Long id) {
        log.info("deleteEmergencyContact ... [{}]", id);
        emergencyContactHelper.removeById(id);
        return id;
    }

    @Override
    public PageResult<EmergencyContactVO> pageEmergencyContact(EmergencyContactRequest.Page request) {
        log.info("pageEmergencyContact ... [{}]", request.toString());
        IPage<EmergencyContact> page = emergencyContactHelper.pageSelect(request.getCurrent(), request.getSize(), request.getName());
        return PageUtil.page(page, this::convertEmergencyContactVO);
    }

    @Override
    public EmergencyContactDetailVO detailEmergencyContact(Long id) {
        log.info("detailEmergencyContact ... [{}]", id);
        EmergencyContact contact = emergencyContactHelper.getById(id);
        if (Objects.isNull(contact)) {
            throw new ParamErrorException("id不存在");
        }
        return Convert.convert(EmergencyContactDetailVO.class, contact);
    }

    private EmergencyContactVO convertEmergencyContactVO(EmergencyContact contact) {
        return Convert.convert(EmergencyContactVO.class, contact);
    }
}
