package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.EmergencyPlanKeyword;

import java.util.Map;
import java.util.Set;

/**
 * @author lijiafeng 2021/01/19 10:44
 */
public interface IEmergencyPlanKeywordService extends IService<EmergencyPlanKeyword> {

    /**
     * 保存关键词
     *
     * @param emergencyPlanId 应急预案ID
     * @param keywords        关键词
     */
    void save(Long emergencyPlanId, Set<String> keywords);

    /**
     * 删除关键词
     *
     * @param emergencyPlanId 应急预案ID
     */
    void delete(Long emergencyPlanId);

    /**
     * 查询关键词
     *
     * @param emergencyPlanIds 应急预案ID
     * @return 结果
     */
    Map<Long, Set<String>> getMap(Set<Long> emergencyPlanIds);

    /**
     * 查询关键词
     *
     * @param emergencyPlanId 应急预案ID
     * @return 结果
     */
    Set<String> get(Long emergencyPlanId);
}
