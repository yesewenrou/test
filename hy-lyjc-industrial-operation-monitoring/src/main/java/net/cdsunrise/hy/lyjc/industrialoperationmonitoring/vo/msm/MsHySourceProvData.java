package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.msm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author lijiafeng
 * @date 2019/9/26 13:51
 */
@Data
public class MsHySourceProvData {

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
    @JsonProperty("PROV_ID")
    private String provId;

    /**
     * 区域名称
     */
    @JsonProperty("PROV_NAME")
    private String provName;

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
