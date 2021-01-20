package net.cdsunrise.hy.lyyt.enums;

/**
 * @author : suzhouhe  @date : 2019/8/23 11:36  @description : 是否是重要路段枚举
 */
public enum ImportantEnum {
    /**
     * 是重要路段
     */
    YES(1),
    /**
     * 不是总要路段
     */
    NO(2),
    ;

    ImportantEnum(Integer type) {
        this.type = type;
    }

    private Integer type;

    public Integer getType() {
        return type;
    }
}
