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
public class RoadDetailVo {
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

    public void update() {
        if (tpi != null) {
            this.jamLevel = JamLevelEnum.fetch(tpi);
        }
        roadName = RoadSectionUtil.getNameBySectionId(sectionId);
    }

    public void setJamCount(Long jamCount) {
        this.jamCount = jamCount;
        this.jamTimeLength = Math.toIntExact((jamCount * 5));
    }

    public static RoadDetailVo build(RoadSectionJamStatisticsDo ro) {
        RoadDetailVo vo = new RoadDetailVo();
        vo.setSectionId(ro.getSectionId());
        vo.setJamCount(Long.valueOf(ro.getJamCount()));
        vo.update();
        return vo;
    }

    public static RoadDetailVo build(RoadSectionTpiDto ro) {
        RoadDetailVo vo = new RoadDetailVo();
        vo.setSectionId(ro.getSectionId());
        vo.setTpi(ro.getTpi());
        vo.setAvgSpeed(ro.getAvgSpeed());
        vo.setRangeTime(DateUtil.stringToDate(ro.getRangTime()));
        vo.update();
        return vo;
    }



}
