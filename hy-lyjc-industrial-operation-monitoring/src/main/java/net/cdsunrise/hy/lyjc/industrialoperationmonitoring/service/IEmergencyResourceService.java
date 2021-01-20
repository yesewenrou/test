package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.EmergencyResource;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyResourceCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyResourceVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;

/**
 * @author lijiafeng
 * @date 2019/11/22 9:50
 */
public interface IEmergencyResourceService extends IService<EmergencyResource> {

    /**
     * 新增应急资源
     *
     * @param emergencyResourceVO 资源
     * @return 资源ID
     */
    Long saveEmergencyResource(EmergencyResourceVO emergencyResourceVO);

    /**
     * 删除应急资源
     *
     * @param id 资源ID
     * @return 结果
     */
    boolean deleteEmergencyResource(Long id);

    /**
     * 更新应急资源
     *
     * @param emergencyResourceVO 资源
     * @return 结果
     */
    boolean updateEmergencyResource(EmergencyResourceVO emergencyResourceVO);

    /**
     * 查询单个应急资源
     *
     * @param id 资源ID
     * @return 资源
     */
    EmergencyResourceVO getEmergencyResource(Long id);

    /**
     * 分页查询
     *
     * @param pageRequest 请求
     * @return 结果
     */
    IPage<EmergencyResourceVO> listEmergencyResource(PageRequest<EmergencyResourceCondition> pageRequest);
}
