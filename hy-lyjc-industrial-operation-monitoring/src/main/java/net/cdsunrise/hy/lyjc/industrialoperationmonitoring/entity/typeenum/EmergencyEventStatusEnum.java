package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.typeenum;

import com.baomidou.mybatisplus.annotation.EnumValue;

/**
 * @author lijiafeng
 * @date 2019/11/27 15:27
 */
public enum EmergencyEventStatusEnum {
    /**
     * 应急事件状态
     */
    WAIT_CHECK(0, "待审核"),
    WAIT_DEAL(1, "待处置"),
    WAIT_CLOSE(2, "待结案"),
    CLOSED(3, "已结案"),
    NO_PASS(4, "未通过");

    @EnumValue
    private final int code;
    private final String desc;

    EmergencyEventStatusEnum(int code, String desc) {
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
