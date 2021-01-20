package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.EmergencyDrill;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyDrillCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyDrillVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;

/**
 * @author lijiafeng
 * @date 2019/11/25 16:48
 */
public interface IEmergencyDrillService extends IService<EmergencyDrill> {

    /**
     * 新增应急演练
     *
     * @param emergencyDrillVO 资源
     * @return 演练ID
     */
    Long saveEmergencyDrill(EmergencyDrillVO emergencyDrillVO);

    /**
     * 删除应急演练
     *
     * @param id 演练ID
     * @return 结果
     */
    boolean deleteEmergencyDrill(Long id);

    /**
     * 更新应急演练
     *
     * @param emergencyDrillVO 演练
     * @return 结果
     */
    boolean updateEmergencyDrill(EmergencyDrillVO emergencyDrillVO);

    /**
     * 查询单个应急演练
     *
     * @param id 演练ID
     * @return 演练
     */
    EmergencyDrillVO getEmergencyDrill(Long id);

    /**
     * 分页查询
     *
     * @param pageRequest 请求
     * @return 结果
     */
    IPage<EmergencyDrillVO> listEmergencyDrill(PageRequest<EmergencyDrillCondition> pageRequest);
}
