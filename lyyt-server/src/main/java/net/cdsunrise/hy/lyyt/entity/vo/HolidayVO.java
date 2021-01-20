package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;
import net.cdsunrise.hy.lyyt.utils.DateUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author YQ on 2020/1/19.
 */
@Data
public class HolidayVO {
    private Long id;
    private Integer year;
    private String name;
    private Long startDate;
    private Long endDate;
    List<Long> dateList;

    public LocalDate fetchStartDate() {
        return DateUtil.convert(startDate).toLocalDate();
    }

    public LocalDate fetchEndDate() {
        return DateUtil.convert(endDate).toLocalDate();
    }

    public List<LocalDate> fetchDateList() {
        return dateList.stream().map(e -> DateUtil.convert(e).toLocalDate()).collect(Collectors.toList());
    }
}
