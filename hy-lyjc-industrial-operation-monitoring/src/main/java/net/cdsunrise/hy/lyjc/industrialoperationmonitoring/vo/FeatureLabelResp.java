package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Binke Zhang
 * @date 2020/1/16 9:28
 */
@Data
@AllArgsConstructor
public class FeatureLabelResp {
    /** 主键 */
    private Long id ;
    /** 标签名称 */
    private String labelName ;

}
