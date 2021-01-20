package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.EmergencyPlan;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyPlanCondition;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.EmergencyPlanVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author lijiafeng
 * @date 2019/11/25 16:48
 */
public interface IEmergencyPlanService extends IService<EmergencyPlan> {

    /**
     * 新增应急资源
     *
     * @param emergencyPlanVO 资源
     * @return 资源ID
     */
    Long saveEmergencyPlan(EmergencyPlanVO emergencyPlanVO);

    /**
     * 删除应急资源
     *
     * @param id 资源ID
     * @return 结果
     */
    boolean deleteEmergencyPlan(Long id);

    /**
     * 更新应急资源
     *
     * @param emergencyPlanVO 资源
     * @return 结果
     */
    boolean updateEmergencyPlan(EmergencyPlanVO emergencyPlanVO);

    /**
     * 查询单个应急资源
     *
     * @param id 资源ID
     * @return 资源
     */
    EmergencyPlanVO getEmergencyPlan(Long id);

    /**
     * 分页查询
     *
     * @param pageRequest 请求
     * @return 结果
     */
    IPage<EmergencyPlanVO> listEmergencyPlan(PageRequest<EmergencyPlanCondition> pageRequest);

    /**
     * 查询对应事件类型的应急预案
     *
     * @param eventType 事件类型
     * @return 结果
     */
    List<EmergencyPlan> listForEvaluate(String eventType);

    /**
     * 查询关键词
     *
     * @param emergencyPlanIds 预案ID
     * @return 结果
     */
    Map<Long, Set<String>> getKeywordMap(Set<Long> emergencyPlanIds);
}
