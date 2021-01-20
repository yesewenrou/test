package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

/**
 * 游客画像请求
 *
 * @author lijiafeng
 * @date 2020/04/30 13:55
 */
@Data
public class TouristPortraitReq {

    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空")
    private LocalDate beginDate;

    /**
     * 结束时间
     */
    @NotNull(message = "结束时间不能为空")
    private LocalDate endDate;
}
