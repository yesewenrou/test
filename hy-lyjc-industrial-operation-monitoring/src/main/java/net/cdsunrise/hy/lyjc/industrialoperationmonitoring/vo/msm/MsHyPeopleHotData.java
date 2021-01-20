package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.msm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author lijiafeng
 * @date 2019/9/26 14:24
 */
@Data
public class MsHyPeopleHotData {

    /**
     * 区域ID
     */
    @JsonProperty("SCENIC_ID")
    private String scenicId;

    /**
     * 区域名称
     */
    @JsonProperty("SCENIC_NAME")
    private String scenicName;

    /**
     * 区域ID
     */
    @JsonProperty("LNG")
    private String lng;

    /**
     * 区域名称
     */
    @JsonProperty("LAT")
    private String lat;

    /**
     * 人数
     */
    @JsonProperty("PEOPLE_NUM")
    private Integer peopleNum;
}
