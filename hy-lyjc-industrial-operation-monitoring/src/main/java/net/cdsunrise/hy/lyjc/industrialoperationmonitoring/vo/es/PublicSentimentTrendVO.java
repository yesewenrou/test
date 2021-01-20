package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es;

import lombok.Data;

/**
 * @author LHY
 * @date 2019/11/15 10:04
 */
@Data
public class PublicSentimentTrendVO {

    private String name;

    private Long value;

    public PublicSentimentTrendVO() {
    }

    public PublicSentimentTrendVO(String name, Long value) {
        this.name = name;
        this.value = value;
    }
}
