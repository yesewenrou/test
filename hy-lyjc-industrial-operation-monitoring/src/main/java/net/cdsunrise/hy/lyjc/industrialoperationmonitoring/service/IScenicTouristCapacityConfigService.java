package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ScenicStatusEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.ScenicTouristCapacityConfig;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ScenicTouristCapacityConfigVO;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2019/11/20 15:15
 */
public interface IScenicTouristCapacityConfigService extends IService<ScenicTouristCapacityConfig> {

    /**
     * 查询所有景区承载量配置
     *
     * @return 所有景区承载量配置
     */
    List<ScenicTouristCapacityConfigVO> listAllConfig();

    /**
     * 更新景区承载量配置
     *
     * @param scenicTouristCapacityConfigVo 某景区承载量配置
     */
    void updateConfig(ScenicTouristCapacityConfigVO scenicTouristCapacityConfigVo);


}
