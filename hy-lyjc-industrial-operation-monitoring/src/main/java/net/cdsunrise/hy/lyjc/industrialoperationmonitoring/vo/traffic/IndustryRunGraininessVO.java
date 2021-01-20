package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.traffic;

import lombok.Data;

import java.util.List;

/**
 * @author : suzhouhe  @date : 2019/9/11 16:55  @description : 产业运行颗粒度查询VO
 */
@Data
public class IndustryRunGraininessVO {

    private List<ExternalTableVO> externalTableVOList;

    private Integer flowCount;

    private Integer graininess;
}
