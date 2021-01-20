package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;

/**
 * RoadSectionTpiForecastVO
 * 路段拥堵预测对象
 * @author LiuYin
 * @date 2020/4/30 15:35
 */
@Data
public class RoadSectionTpiForecastVO {

    private Long id;
    private String sectionId;
    private String sectionName;
    private Double tpi;

    public static RoadSectionTpiForecastVO from(RoadSectionExtDO extDO){
        final RoadSectionTpiForecastVO vo = new RoadSectionTpiForecastVO();
        vo.setId(extDO.getId());
        vo.setSectionId(extDO.getSectionId());
        vo.setSectionName(extDO.getRoadSectionName());
        return vo;
    }
}
