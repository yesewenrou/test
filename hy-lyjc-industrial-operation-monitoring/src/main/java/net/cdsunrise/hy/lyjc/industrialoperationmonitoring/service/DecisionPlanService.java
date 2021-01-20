package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DecisionPlanDetailVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DecisionPlanRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DecisionPlanVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;

/**
 * @author fang yun long
 * on 2021/1/18
 */
public interface DecisionPlanService {

    /**
     * 新增决策计划
     * @param request 请求信息
     * @return id
     */
    Long addDecisionPlan(DecisionPlanRequest.Add request);

    /**
     * 修改决策计划
     * @param request 请求信息
     * @return id
     */
    Long updateDecisionPlan(DecisionPlanRequest.Update request);

    /**
     * 删除决策计划
     * @param id 请求信息
     * @return id
     */
    Long deleteDecisionPlan(Long id);

    /**
     * 决策计划详情
     * @param id 请求信息
     * @return DecisionPlanDetailVO
     */
    DecisionPlanDetailVO detailDecisionPlan(Long id);

    /**
     * 决策计划分页
     * @param request 查询条件
     * @return PageResult
     */
    PageResult<DecisionPlanVO> pageDecisionPlan(PageRequest request);
}
