package net.cdsunrise.hy.lyyt.exception.business;


/**
 * @author : suzhouhe  @date : 2019/7/10 11:55  @description : 业务异常枚举
 */
public enum BusinessExceptionEnum {

    /**
     * 无权访问
     */
    NO_PERMISSION("0001", "无权访问"),
    /**
     * 第三方摄像头接口错误
     */
    HIKVISION_ERROR("0002", "第三方摄像头接口错误"),

    FAILED("0003", "操作失败"),
    INDEX_ERROR("0004", "编号错误"),
    TYPE_ERROR("0005", "类型错误"),
    YDP_INFO_IS_NULL("0006", "诱导屏信息为空"),
    YDP_INFO_ID_IS_NULL("0007", "诱导屏id为空"),
    YDP_INFO_NAME_IS_EMPTY("0008", "诱导屏名称为空"),
    YDP_STATUS_DTO_IS_NULL("0009", "诱导屏状态对象为空"),
    YDP_NET_STATUS_IS_EMPTY("0010", "诱导屏状态内容为空"),
    HOLIDAY_IS_NULL("0011", "节假日不能为空"),
    NO_DATA("0012", "暂无数据");


    BusinessExceptionEnum(String code, String msg) {
        this.code = "1001" + code;
        this.msg = msg;
    }

    private String code;

    private String msg;

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
