package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.EmergencyEvent;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.resp.UserVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.*;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2019/11/27 17:21
 */
public interface IEmergencyEventService extends IService<EmergencyEvent> {

    /**
     * 添加事件
     *
     * @param emergencyEventAddVO 事件
     * @return 事件ID
     */
    Long saveEmergencyEvent(EmergencyEventAddVO emergencyEventAddVO);

    /**
     * 添加系统产生的事件
     *
     * @param emergencyEventAddVO 事件
     * @return 事件ID
     */
    Long saveSystemEmergencyEvent(EmergencyEventAddVO emergencyEventAddVO);

    /**
     * 编辑事件
     *
     * @param emergencyEventEditVO 事件
     */
    void editEmergencyEvent(EmergencyEventEditVO emergencyEventEditVO);

    /**
     * 审核事件
     *
     * @param emergencyEventCheckVO 审核信息
     * @return 结果
     */
    boolean checkEmergencyEvent(EmergencyEventCheckVO emergencyEventCheckVO);

    /**
     * 查询所有可以指派的用户列表
     * 拥有反馈权限的用户
     *
     * @return 结果
     */
    List<UserVO> listAllCanAssignUser();

    /**
     * 指派事件
     *
     * @param emergencyEventAssignVO 指派信息
     * @return 结果
     */
    boolean assignEmergencyEvent(EmergencyEventAssignVO emergencyEventAssignVO);

    /**
     * 反馈事件
     *
     * @param emergencyEventFeedbackVO 反馈信息
     * @return 结果
     */
    boolean feedbackEmergencyEvent(EmergencyEventFeedbackVO emergencyEventFeedbackVO);

    /**
     * 结案
     *
     * @param emergencyEventCloseVO 结案信息
     * @return 结果
     */
    boolean closeEmergencyEvent(EmergencyEventCloseVO emergencyEventCloseVO);

    /**
     * 查询单个事件详情
     *
     * @param id 事件ID
     * @return 结果
     */
    EmergencyEventVO getEmergencyEvent(Long id);

    /**
     * 分页查询事件列表
     *
     * @param pageRequest 分页请求
     * @return 结果
     */
    IPage<EmergencyEventVO> listEmergencyEvent(PageRequest<EmergencyEventCondition> pageRequest);

    /**
     * 删除事件
     *
     * @param id 事件ID
     */
    void deleteEmergencyEvent(Long id);

    /**
     * 获取事件评估结果
     *
     * @param id 事件ID
     * @return 结果
     */
    List<EmergencyEventEvaluationVO> evaluate(Long id);
}
