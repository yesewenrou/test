package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.cdsunrise.hy.lyyt.domain.vo.TouristLocalData;

/**
 * @author FangYunLong
 * @date in 2020/4/29
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TouristLocalDataVO extends TouristLocalData {
    /**
     * 在园游客数
     */
    private Integer peopleInPark;

    /**
     * 累计游客数
     */
    private Integer todayTotalPeopleNum;

    /**
     * 累计入园数
     */
    private Integer todayTotalPeopleInPark;

    public Integer getPeopleInPark() {
        return peopleInPark == null ? 0 : peopleInPark;
    }

    public Integer getTodayTotalPeopleNum() {
        return todayTotalPeopleNum == null ? 0 : todayTotalPeopleNum;
    }

    public Integer getTodayTotalPeopleInPark() {
        return todayTotalPeopleInPark == null ? 0 : todayTotalPeopleInPark;
    }
}
