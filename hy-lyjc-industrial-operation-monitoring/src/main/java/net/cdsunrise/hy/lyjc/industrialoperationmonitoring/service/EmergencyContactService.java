package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyContactDetailVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyContactRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyContactVO;

/**
 * @author fang yun long
 * on 2021/1/19
 */
public interface EmergencyContactService {
    /**
     * 新增应急联系人
     * @param request 请求信息
     * @return ID
     */
    Long saveEmergencyContact(EmergencyContactRequest.Add request);

    /**
     * 修改应急联系人
     * @param request 请求信息
     * @return ID
     */
    Long updateEmergencyContact(EmergencyContactRequest.Update request);

    /**
     * 删除应急联系人
     * @param id 请求信息
     * @return ID
     */
    Long deleteEmergencyContact(Long id);

    /**
     * 分页查询
     * @param request 请求信息
     * @return page
     */
    PageResult<EmergencyContactVO> pageEmergencyContact(EmergencyContactRequest.Page request);

    /**
     * 详情
     * @param id id
     * @return {@link EmergencyContactDetailVO}
     */
    EmergencyContactDetailVO detailEmergencyContact(Long id);
}
