package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.EmergencyPlanKeyword;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.EmergencyPlanKeywordMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IEmergencyPlanKeywordService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author lijiafeng 2021/01/19 10:46
 */
@Service
public class EmergencyPlanKeywordServiceImpl extends ServiceImpl<EmergencyPlanKeywordMapper, EmergencyPlanKeyword> implements IEmergencyPlanKeywordService {

    @Override
    public void save(Long emergencyPlanId, Set<String> keywords) {
        if (CollectionUtils.isEmpty(keywords)) {
            return;
        }
        List<EmergencyPlanKeyword> list = keywords.stream()
                .filter(StringUtils::hasText)
                .map(keyword -> {
                    EmergencyPlanKeyword emergencyPlanKeyword = new EmergencyPlanKeyword();
                    emergencyPlanKeyword.setEmergencyPlanId(emergencyPlanId);
                    emergencyPlanKeyword.setKeyword(keyword);
                    return emergencyPlanKeyword;
                }).collect(Collectors.toList());
        saveBatch(list);
    }

    @Override
    public void delete(Long emergencyPlanId) {
        LambdaQueryWrapper<EmergencyPlanKeyword> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(EmergencyPlanKeyword::getEmergencyPlanId, emergencyPlanId);
        remove(queryWrapper);
    }

    @Override
    public Map<Long, Set<String>> getMap(Set<Long> emergencyPlanIds) {
        if (CollectionUtils.isEmpty(emergencyPlanIds)) {
            return new HashMap<>();
        }
        LambdaQueryWrapper<EmergencyPlanKeyword> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(EmergencyPlanKeyword::getEmergencyPlanId, EmergencyPlanKeyword::getKeyword)
                .in(EmergencyPlanKeyword::getEmergencyPlanId, emergencyPlanIds);
        List<EmergencyPlanKeyword> list = list(queryWrapper);
        return list.stream().collect(
                Collectors.groupingBy(EmergencyPlanKeyword::getEmergencyPlanId,
                        Collectors.mapping(EmergencyPlanKeyword::getKeyword,
                                Collectors.toSet()
                        )
                )
        );
    }

    @Override
    public Set<String> get(Long emergencyPlanId) {
        LambdaQueryWrapper<EmergencyPlanKeyword> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(EmergencyPlanKeyword::getEmergencyPlanId, EmergencyPlanKeyword::getKeyword)
                .eq(EmergencyPlanKeyword::getEmergencyPlanId, emergencyPlanId);
        return list(queryWrapper).stream()
                .map(EmergencyPlanKeyword::getKeyword)
                .collect(Collectors.toSet());
    }
}
