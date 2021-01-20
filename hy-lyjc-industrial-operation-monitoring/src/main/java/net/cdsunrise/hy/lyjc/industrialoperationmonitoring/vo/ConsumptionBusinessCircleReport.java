package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.ConsumptionStatisticsVO;

import java.util.List;

/**
 * 消费商圈报告
 *
 * @author lijiafeng
 * @date 2020/05/11 08:47
 */
@Data
public class ConsumptionBusinessCircleReport {

    /**
     * 洪雅县域总消费
     */
    private Double consumption;

    /**
     * 各大商圈旅游消费
     */
    private String consumptionByBusinessCircle;

    /**
     * 原始数据
     */
    private List<ConsumptionStatisticsVO> businessCircleData;
}
