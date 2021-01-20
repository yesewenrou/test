package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.typeenum;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author lijiafeng
 * @date 2019/11/27 15:27
 */
public enum EmergencyEventCheckStatusEnum {
    /**
     * 应急事件审核状态
     */
    PASS(0, "通过"),
    NO_PASS(1, "未通过");

    @EnumValue
    private final int code;
    private final String desc;

    EmergencyEventCheckStatusEnum(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
