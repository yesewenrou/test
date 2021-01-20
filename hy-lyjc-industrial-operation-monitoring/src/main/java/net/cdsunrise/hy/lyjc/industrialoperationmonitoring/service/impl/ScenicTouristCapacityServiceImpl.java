package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.DataDictionaryFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.DataDictionaryVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ScenicStatusEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.ScenicTouristCapacityConfig;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.ScenicTouristCapacityConfigMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IScenicTouristCapacityConfigService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ScenicTouristCapacityConfigVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lijiafeng
 * @date 2019/11/20 15:32
 */
@Service
public class ScenicTouristCapacityServiceImpl extends ServiceImpl<ScenicTouristCapacityConfigMapper, ScenicTouristCapacityConfig> implements IScenicTouristCapacityConfigService {

    /** 默认舒适容量 **/
    protected static final int DEFAULT_COMFORTABLE_CAPACITY = 100;
    /** 默认较舒适容量 **/
    protected static final int DEFAULT_LESS_COMFORTABLE_CAPACITY = 200;
    /** 默认一般容量 **/
    protected static final int DEFAULT_ORDINARY_CAPACITY = 300;

    /**
     * 默认较拥挤容量
     */
    protected static final int DEFAULT_SATURATION_CAPACITY = 500;

    /**
     * 默认拥挤容量
     */
    protected static final int DEFAULT_OVERLOAD_CAPACITY = 1000;



    private DataDictionaryFeignClient dataDictionaryFeignClient;

    public ScenicTouristCapacityServiceImpl(DataDictionaryFeignClient dataDictionaryFeignClient) {
        this.dataDictionaryFeignClient = dataDictionaryFeignClient;
    }

    @Override
    public List<ScenicTouristCapacityConfigVO> listAllConfig() {
        // 调用数据字典获取所有景区
        String code = "SCENIC_AREA";
        Result<Map<String, DataDictionaryVO>> result = dataDictionaryFeignClient.getByCodes(new String[]{code});
        Map<String, DataDictionaryVO> data = result.getData();
        DataDictionaryVO dataDictionaryVO = data.get(code);
        List<DataDictionaryVO> children = dataDictionaryVO.getChildren();
        return children.stream().map(scenicArea -> {
            String scenicCode = scenicArea.getCode();
            QueryWrapper<ScenicTouristCapacityConfig> queryWrapper = Wrappers.query();
            queryWrapper.lambda().eq(ScenicTouristCapacityConfig::getScenicCode, scenicCode);
            ScenicTouristCapacityConfig scenicTouristCapacityConfig = super.getOne(queryWrapper);
            ScenicTouristCapacityConfigVO scenicTouristCapacityConfigVO = new ScenicTouristCapacityConfigVO();
            if (scenicTouristCapacityConfig == null) {
                scenicTouristCapacityConfigVO.setScenicCode(scenicCode);
                scenicTouristCapacityConfigVO.setScenicName(scenicArea.getName());
                return scenicTouristCapacityConfigVO;
            }
            BeanUtils.copyProperties(scenicTouristCapacityConfig, scenicTouristCapacityConfigVO);
            scenicTouristCapacityConfigVO.setScenicName(scenicArea.getName());
            return scenicTouristCapacityConfigVO;
        }).collect(Collectors.toList());
    }

    @Override
    public void updateConfig(ScenicTouristCapacityConfigVO scenicTouristCapacityConfigVo) {
        String scenicCode = scenicTouristCapacityConfigVo.getScenicCode();
        Integer comfortableCapacity = scenicTouristCapacityConfigVo.getComfortableCapacity();
        Integer lessComfortableCapacity = scenicTouristCapacityConfigVo.getLessComfortableCapacity();
        Integer ordinaryCapacity = scenicTouristCapacityConfigVo.getOrdinaryCapacity();
        Integer saturationCapacity = scenicTouristCapacityConfigVo.getSaturationCapacity();
        Integer overloadCapacity = scenicTouristCapacityConfigVo.getOverloadCapacity();
        if (comfortableCapacity >= lessComfortableCapacity) {
            throw new ParamErrorException("舒适容量不能大于等于一般容量");
        }

        if (lessComfortableCapacity >= ordinaryCapacity) {
            throw new ParamErrorException("较舒适容量不能大于或等于一般容量");
        }

        if (ordinaryCapacity >= saturationCapacity) {
            throw new ParamErrorException("一般容量不能大于或等于较拥挤容量");
        }

        if (saturationCapacity >= overloadCapacity) {
            throw new ParamErrorException("饱和容量不能大于或等于超负荷容量");
        }

        Result<Map<String, DataDictionaryVO>> result = dataDictionaryFeignClient.getByCodes(new String[]{scenicCode});
        Map<String, DataDictionaryVO> data = result.getData();
        DataDictionaryVO dataDictionaryVO = data.get(scenicCode);
        if (dataDictionaryVO == null) {
            throw new ParamErrorException("景区编码不存在");
        }
        QueryWrapper<ScenicTouristCapacityConfig> queryWrapper = Wrappers.query();
        queryWrapper.lambda().eq(ScenicTouristCapacityConfig::getScenicCode, scenicCode);
        ScenicTouristCapacityConfig scenicTouristCapacityConfig = super.getOne(queryWrapper);
        if (scenicTouristCapacityConfig == null) {
            scenicTouristCapacityConfig = new ScenicTouristCapacityConfig();
            BeanUtils.copyProperties(scenicTouristCapacityConfigVo, scenicTouristCapacityConfig);
            super.save(scenicTouristCapacityConfig);
        } else {
            BeanUtils.copyProperties(scenicTouristCapacityConfigVo, scenicTouristCapacityConfig);
            super.updateById(scenicTouristCapacityConfig);
        }
    }

}
