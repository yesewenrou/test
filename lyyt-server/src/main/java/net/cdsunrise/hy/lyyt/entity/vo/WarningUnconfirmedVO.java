package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author funnylog
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarningUnconfirmedVO implements Serializable {
    private String  code;
    private Integer count;
}
