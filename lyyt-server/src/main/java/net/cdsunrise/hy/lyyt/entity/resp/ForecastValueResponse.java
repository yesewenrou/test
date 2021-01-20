package net.cdsunrise.hy.lyyt.entity.resp;

import lombok.Data;

/**
 * ForecastValueResponse
 *
 * @author LiuYin
 * @date 2020/4/30 14:34
 */
@Data
public class ForecastValueResponse<T> {

    private Long time;
    private Integer from;
    private Integer to;
    private T data;

    public static <T>  ForecastValueResponse<T> create(Integer from, Integer to, T data){
        final ForecastValueResponse<T> r = new ForecastValueResponse<>();
        r.setFrom(from);
        r.setTo(to);
        r.setData(data);
        r.setTime(0L);
        return r;
    }
}
