package net.cdsunrise.hy.lyyt.domain.vo;

import lombok.Data;
import net.cdsunrise.hy.lyyt.enums.MonitorStatus;

/**
 * @author YQ on 2019/12/21.
 */
@Data
public class MonitorDetailVo {
    private String monitorCode;
    private String monitorName;
    private MonitorStatus monitorStatus;

    public static MonitorDetailVo buildDefault(String monitorCode, String monitorName) {
        MonitorDetailVo vo = new MonitorDetailVo();
        vo.setMonitorCode(monitorCode);
        vo.setMonitorName(monitorName);
        vo.setMonitorStatus(MonitorStatus.ONLINE);
        return vo;
    }
}
