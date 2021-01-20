package net.cdsunrise.hy.lyyt.entity.dto;

import lombok.Data;
import net.cdsunrise.hy.lyyt.entity.vo.reids.TimeValueForecastDTO;

import java.time.LocalDateTime;

/**
 * RoadSectionTimeVlaueForecastDTO
 *
 * @author LiuYin
 * @date 2020/4/30 16:02
 */
@Data
public class RoadSectionTimeValueForecastDTO {

    private String sectionId;
    private String time;
    private LocalDateTime localDateTime;
    private Double value;

    public static RoadSectionTimeValueForecastDTO from(TimeValueForecastDTO dto, String sectionId){
        final RoadSectionTimeValueForecastDTO rsDto = new RoadSectionTimeValueForecastDTO();
        rsDto.setLocalDateTime(dto.getLocalDateTime());
        rsDto.setTime(dto.getTime());
        rsDto.setValue(dto.getValue());
        rsDto.setSectionId(sectionId);
        return rsDto;
    }
}
