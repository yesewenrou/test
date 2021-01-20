package net.cdsunrise.hy.lyyt.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author YQ on 2019/12/14.
 */
@Data
public class RoadDetailMonitorVo {
    private List<RoadDetailVo> oftenJamList;
    private List<RoadDetailVo> unOftenJamList;
}
