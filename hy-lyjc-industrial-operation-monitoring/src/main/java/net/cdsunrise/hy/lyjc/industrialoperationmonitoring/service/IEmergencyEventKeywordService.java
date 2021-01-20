package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.EmergencyEventKeyword;

import java.util.Map;
import java.util.Set;

/**
 * @author lijiafeng 2021/01/19 10:44
 */
public interface IEmergencyEventKeywordService extends IService<EmergencyEventKeyword> {

    /**
     * 保存关键词
     *
     * @param emergencyEventId 应急事件ID
     * @param keywords         关键词
     */
    void save(Long emergencyEventId, Set<String> keywords);

    /**
     * 删除关键词
     *
     * @param emergencyEventId 应急事件ID
     */
    void delete(Long emergencyEventId);

    /**
     * 查询关键词
     *
     * @param emergencyEventIds 应急事件ID
     * @return 结果
     */
    Map<Long, Set<String>> getMap(Set<Long> emergencyEventIds);

    /**
     * 查询关键词
     *
     * @param emergencyEventId 应急预案ID
     * @return 结果
     */
    Set<String> get(Long emergencyEventId);
}
