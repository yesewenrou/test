package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lyyx.precisionmarketing.autoconfigure.feign.vo.NameAndValueVO;

import java.util.List;

/**
 * @author LHY
 * @date 2020/5/9 16:54
 */
@Data
public class HotelTrendReportVO {

    /**
     * 累计接待人次
     **/
    private Double checkInAmount;

    /**
     * 接待趋势
     **/
    private List<KeyAndValue<String, Integer>> lineCheckInAmount;

    /**
     * 累计接待酒店排行
     **/
    private String top5CheckInAmount;
}
