package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.enums;

/**
 * 字典异常枚举
 * @author LiuYin
 */
public enum DictionaryExceptionEnum {

    /**
     *
     */
    INNER_TRANSFER_ERROR("01", "内部接口调用异常"),
    PARENT_CODE_IS_NULL("02","父级字典编码为空" ),
    DICTIONARY_CODE_NOT_FOUND("03","字典编码未找到" ),
    ILLEGAL_DICTIONARY_CODE("04","非法字典编码" );

    /**
     * 业务异常
     */
    private static final String BUSINESS_CODE = "04";

    DictionaryExceptionEnum(String code, String msg) {
        this.code = "01" + "00" + BUSINESS_CODE + code;
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
