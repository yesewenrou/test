package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.msm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author lijiafeng
 * @date 2019/9/26 14:20
 */
@Data
public class MsHySourceCountyData {

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
    @JsonProperty("COUNTY_ID")
    private String countyId;

    /**
     * 区域名称
     */
    @JsonProperty("COUNTY_NAME")
    private String countyName;

    /**
     * 人数
     */
    @JsonProperty("PEOPLE_NUM")
    private Integer peopleNum;

    /**
     * 时间
     */
    @JsonProperty("TIME")
    private String time;
}
