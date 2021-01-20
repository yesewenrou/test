package net.cdsunrise.hy.lyyt.entity.vo.reids;

import lombok.Data;
import net.cdsunrise.hy.lyyt.utils.DateUtil;

import java.sql.Time;
import java.time.LocalDateTime;

/**
 * TimeValueForecastDTO
 *
 * @author LiuYin
 * @date 2020/4/30 14:30
 */
@Data
public class TimeValueForecastDTO {

    private String time;
    private LocalDateTime localDateTime;
    private Double value;

    public static TimeValueForecastDTO from(String time, Double value){
        final TimeValueForecastDTO dto = new TimeValueForecastDTO();
        dto.setTime(time);
        dto.setValue(value == null ? 0D : value);
        dto.setLocalDateTime(DateUtil.stringToLocalDateTime(dto.getTime()));
        return dto;
    }

}
