package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.EmergencyEventKeyword;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.EmergencyEventKeywordMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IEmergencyEventKeywordService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lijiafeng 2021/01/19 10:46
 */
@Service
public class EmergencyEventKeywordServiceImpl extends ServiceImpl<EmergencyEventKeywordMapper, EmergencyEventKeyword> implements IEmergencyEventKeywordService {

    @Override
    public void save(Long emergencyEventId, Set<String> keywords) {
        if (Objects.isNull(emergencyEventId) || CollectionUtils.isEmpty(keywords)) {
            return;
        }
        List<EmergencyEventKeyword> list = keywords.stream()
                .filter(StringUtils::hasText)
                .map(keyword -> {
                    EmergencyEventKeyword emergencyEventKeyword = new EmergencyEventKeyword();
                    emergencyEventKeyword.setEmergencyEventId(emergencyEventId);
                    emergencyEventKeyword.setKeyword(keyword);
                    return emergencyEventKeyword;
                }).collect(Collectors.toList());
        saveBatch(list);
    }

    @Override
    public void delete(Long emergencyEventId) {
        if (Objects.isNull(emergencyEventId)) {
            return;
        }
        LambdaQueryWrapper<EmergencyEventKeyword> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(EmergencyEventKeyword::getEmergencyEventId, emergencyEventId);
        remove(queryWrapper);
    }

    @Override
    public Map<Long, Set<String>> getMap(Set<Long> emergencyEventIds) {
        if (CollectionUtils.isEmpty(emergencyEventIds)) {
            return new HashMap<>();
        }
        LambdaQueryWrapper<EmergencyEventKeyword> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.in(EmergencyEventKeyword::getEmergencyEventId, emergencyEventIds);
        List<EmergencyEventKeyword> list = list(queryWrapper);
        return list.stream().collect(
                Collectors.groupingBy(EmergencyEventKeyword::getEmergencyEventId,
                        Collectors.mapping(EmergencyEventKeyword::getKeyword,
                                Collectors.toSet()
                        )
                )
        );
    }

    @Override
    public Set<String> get(Long emergencyEventId) {
        LambdaQueryWrapper<EmergencyEventKeyword> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.select(EmergencyEventKeyword::getEmergencyEventId, EmergencyEventKeyword::getKeyword)
                .eq(EmergencyEventKeyword::getEmergencyEventId, emergencyEventId);
        return list(queryWrapper).stream()
                .map(EmergencyEventKeyword::getKeyword)
                .collect(Collectors.toSet());
    }
}
