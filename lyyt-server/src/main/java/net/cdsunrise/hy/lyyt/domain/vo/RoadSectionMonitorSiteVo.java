package net.cdsunrise.hy.lyyt.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.cdsunrise.hy.lyyt.entity.vo.reids.RoadSectionJamStatisticsDo;
import net.cdsunrise.hy.lyyt.entity.vo.reids.RoadSectionTpiDto;
import net.cdsunrise.hy.lyyt.enums.JamLevelEnum;
import net.cdsunrise.hy.lyyt.utils.DateUtil;
import net.cdsunrise.hy.lyyt.utils.RoadSectionUtil;

import java.util.Date;

/**
 * @author YQ on 2019/12/12.
 */
@Data
public class RoadSectionMonitorSiteVo {
    private String sectionId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date rangeTime;
    private Double jamLength;
    private Double avgSpeed;
    private Double tpi;

    private JamLevelEnum jamLevel;
    private String roadName;

    private Date startTime;
    private Date endTime;
    /**
     * 开始时间和结束时间里面的拥堵的次数
     */
    private Long jamCount;
    /**
     * 拥堵时长(分钟)
     */
    private Integer jamTimeLength;

    private String deviceCode;

}
