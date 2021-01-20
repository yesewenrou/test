package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant;

/**
 * @author lijiafeng
 * @date 2019/6/27 18:54
 */
public enum ResultCodeEnum {

    /**
     * 操作成功
     */
    SUCCESS("0000", "操作成功"),

    /**
     * 操作失败
     */
    FAIL("0001", "操作失败"),

    /**
     * 参数校验不通过
     */
    PARAM_ERROR("0002", "参数错误"),

    /**
     * 无权限访问
     */
    NO_PERMISSION("0003", "无权限访问");

    /**
     * 模块ID 在gitlab相应group的描述里定义
     */
    private static final String MODULE_ID = "08";

    /**
     * 服务ID 在gitlab相应微服务代码仓库的描述里定义
     */
    private static final String SERVICE_ID = "01";

    private final String code;
    private final String message;

    ResultCodeEnum(String code, String message) {
        this.code = MODULE_ID + SERVICE_ID + code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
