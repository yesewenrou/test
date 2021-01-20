package net.cdsunrise.hy.lyyt.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import net.cdsunrise.common.utility.utils.JsonUtils;
import net.cdsunrise.hy.lyyt.entity.vo.ImportantProjectVO;
import net.cdsunrise.hy.lyyt.service.TravelEnterpriseService;
import net.cdsunrise.hy.lyyt.utils.RedisUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * TravelEnterpriseServiceImpl
 * 涉旅企业服务实现
 * @author LiuYin
 * @date 2020/1/19 14:25
 */
@Service
public class TravelEnterpriseServiceImpl implements TravelEnterpriseService {

    private static final String IMPORTANT_PROJECT_KEY = "LYDSJZX_IMPORTANT_PROJECT_BUILD";

    /**
     * 获取所有的重点建设项目
     *
     * @return 列表
     */
    @Override
    public List<ImportantProjectVO> getAllImportantProject() {
        final Map<String, String> entries = RedisUtil.hashOperations().entries(IMPORTANT_PROJECT_KEY);
        if(CollectionUtils.isEmpty(entries)){
            return new ArrayList<>(0);
        }
        final List<ImportantProjectVO> list = entries.values().stream().map(s -> JsonUtils.toObject(s, new TypeReference<ImportantProjectVO>() {
        })).sorted((o1, o2) -> o2.getId().compareTo(o1.getId())).collect(Collectors.toList());
        return list;
    }
}
