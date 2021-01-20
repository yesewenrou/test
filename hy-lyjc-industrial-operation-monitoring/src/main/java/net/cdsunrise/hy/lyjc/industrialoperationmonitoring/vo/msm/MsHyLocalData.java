package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.msm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author lijiafeng
 * @date 2019/9/26 13:44
 */
@Data
public class MsHyLocalData {

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
     * 人数
     */
    @JsonProperty("PEOPLE_NUM")
    private Integer peopleNum;

    /**
     * 人员类型 0为游客 1 为居住 2为上班
     */
    @JsonProperty("MEMBER_TYPE")
    private Integer memberType;

    /**
     * 时间
     */
    @JsonProperty("TIME")
    private String time;
}
