package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 商户诚信红黑榜
 * @author Binke Zhang
 * @date 2020/1/9 13:54
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditTrendVO implements Serializable {
    /** 商户名称 */
    private String merchantName ;
    /** 榜单类型 */
    private Integer trendType ;
}
