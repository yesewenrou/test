package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lyyx.precisionmarketing.autoconfigure.feign.vo.NameAndValueVO;

import java.util.List;

/**
 * @author LHY
 * @date 2020/5/11 10:07
 */
@Data
public class TouristPortraitVO {

    /**
     * 性别
     **/
    private String gender;

    /**
     * 占比
     **/
    private Double genderRatio;

    /**
     * 年龄分布
     **/
    private String top3AgeDistribution;

    /**
     * 年龄段
     **/
    private List<KeyAndValue<String, Integer>> barCheckInAgeDistribution;

    /**
     * 过夜天数分布
     **/
    private String top3StayOvernight;

    /**
     * 过夜天数段
     **/
    private List<KeyAndValue<String, Integer>> pieDataStayOvernight;
}
