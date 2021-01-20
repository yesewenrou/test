package net.cdsunrise.hy.lyyt.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.cdsunrise.hy.lyyt.enums.MonitorColor;

/**
 * @author YQ on 2019/12/17.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonitorRealTimeData {
    private String monitorCode;
    private MonitorColor monitorColor;
    private Integer count;

}
