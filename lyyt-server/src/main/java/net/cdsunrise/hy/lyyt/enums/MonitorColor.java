package net.cdsunrise.hy.lyyt.enums;


import lombok.AllArgsConstructor;

/**
 * @author YQ on 2019/12/17.
 */
@AllArgsConstructor
public enum MonitorColor {
    /**
     * 绿色
     */
    GREEN(0),
    /**
     * 黄色
     */
    YELLOW(50),

    RED(100),

    ;

    private Integer count;

    public static MonitorColor fetchMonitorColor(Integer count) {
        if(count == null){
            return GREEN;
        }

        if (count < YELLOW.count) {
            return GREEN;
        }else if(count < RED.count){
            return YELLOW;
        }else{
            return RED;
        }
    }

    public static MonitorColor fetchMonitorColor(Integer count, Long min, Long max){
        if(min == null || max == null){
            return fetchMonitorColor(count);
        }
        if(count == null){
            return GREEN;
        }
        if(count < min){
            return GREEN;
        }else if(count < max){
            return YELLOW;
        }else{
            return RED;
        }

    }
}
