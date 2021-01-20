package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.msm;

import lombok.Data;

/**
 * @author lijiafeng
 * @date 2019/9/26 10:45
 */
@Data
public class MeishanMobileResult<T> {

    private Boolean success;

    private T result;


}
