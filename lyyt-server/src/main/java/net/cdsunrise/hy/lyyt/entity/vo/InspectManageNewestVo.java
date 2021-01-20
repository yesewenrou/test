package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;

/**
 * @author YQ on 2020/1/16.
 */
@Data
public class InspectManageNewestVo {
    /** 商户名称 */
    private String merchantName;
    /** 问题类型名称 */
    private String problemTypeName;
    /** 巡检时间 */
    private String inspectTime;

}
