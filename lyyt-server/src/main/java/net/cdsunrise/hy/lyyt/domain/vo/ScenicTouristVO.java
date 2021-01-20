package net.cdsunrise.hy.lyyt.domain.vo;

import lombok.Data;

/**
 * @author LHY
 * @date 2019/11/5 19:55
 */
@Data
public class ScenicTouristVO {
    private String name;
    private Double value;

    public ScenicTouristVO() {
    }

    public ScenicTouristVO(String name, Double value) {
        this.name = name;
        this.value = value;
    }
}
