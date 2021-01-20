package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author funnylog
 */
@Data
public class WarningUnconfirmedVO implements Serializable {
    private String  code;
    private Integer count;
}
