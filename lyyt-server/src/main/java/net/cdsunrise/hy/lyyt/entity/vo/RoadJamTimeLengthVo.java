package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;
import net.cdsunrise.hy.lyyt.domain.vo.RoadDetailVo;
import net.cdsunrise.hy.lyyt.enums.JamLevelEnum;

/**
 * @author YQ on 2019/12/18.
 */
@Data
public class RoadJamTimeLengthVo {

    private String sectionId;
    private JamLevelEnum jamLevel;
    private String roadName;
    /**
     * 拥堵时长(小时)
     */
    private Integer jamTimeLength;

    public static RoadJamTimeLengthVo build(RoadDetailVo roadDetailVo) {
        RoadJamTimeLengthVo vo = new RoadJamTimeLengthVo();
        vo.setSectionId(roadDetailVo.getSectionId());
        vo.setJamLevel(roadDetailVo.getJamLevel());
        vo.setRoadName(roadDetailVo.getRoadName());
        vo.setJamTimeLength((int) (roadDetailVo.getJamCount() * 5 / 60));
        return vo;
    }
}
