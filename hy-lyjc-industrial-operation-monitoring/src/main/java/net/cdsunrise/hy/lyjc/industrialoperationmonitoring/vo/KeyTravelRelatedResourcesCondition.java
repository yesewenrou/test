package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lijiafeng
 * @date 2020/3/8 16:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KeyTravelRelatedResourcesCondition extends Pageable {

    /**
     * 名称
     */
    private String name;

    /**
     * 资源大类
     */
    private String mainType;
}

