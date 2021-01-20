package net.cdsunrise.hy.lyyt.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author YQ on 2019/12/14.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OftenJamTimeVo {
    private String time;
    private Long count;
}
