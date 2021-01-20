package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant;

/**
 * @author lijiafeng
 * @date 2019/11/29 15:58
 */
public enum ScenicStatusEnum {

    /**
     * 景区运营状态
     */
    COMFORTABLE("舒适"),
    LESS_COMFORTABLE("较舒适"),
    NORMAL("一般"),
    SATURATED("较拥挤"),
    OVERLOAD("拥挤");

    private final String desc;

    ScenicStatusEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }


    /** 最大承载量 **/
    private Integer maxCapacity;
    public Integer getMaxCapacity() {
        return this.maxCapacity;
    }

    public void setMaxCapacity(Integer maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
}
