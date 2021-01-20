package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * KEY-VALUE VO
 *
 * @author YQ on 2020/1/2.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyValueVo {
    private String key;
    private String value;
}
