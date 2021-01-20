package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

/**
 * @author fangyunlong
 * @date 2020/3/8 1:27
 */
public enum AutoWarningStatusEnum {
    /**
     * 预警状态
     */
    WARN_CODE_033001("033001", "待确认"),
    WARN_CODE_033002("033002", "已忽略"),
    /** 已申请丶待发布 **/
    WARN_CODE_033003("033003", "待发布"),
    WARN_CODE_033004("033004", "已过期"),
    WARN_CODE_033005("033005", "已发布"),
    WARN_CODE_033006("033006", "忽略申请")
    ;

    private String code;
    private String name;
    AutoWarningStatusEnum(){}
    AutoWarningStatusEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }


    /**
     * 判断状态是否有效
     * @param code 状态码
     * @return boolean
     */
    public static boolean checkStatusIsValid(String code) {
        for (AutoWarningStatusEnum statusEnum : AutoWarningStatusEnum.values()) {
            if (statusEnum.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
}
