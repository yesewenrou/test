package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

/**
 * @author fangyunlong
 * @date 2020/3/8 1:04
 */
public enum AutoWarningEnum {

    /**
     * 处理类型  1 申请发布 2 发送短信
     */
    HANDLE_TYPE_1("1", "申请发布"),
    HANDLE_TYPE_2("2", "发送短信"),
    HANDLE_TYPE_3("3", "正式发布"),
    HANDLE_TYPE_4("4", "忽略申请"),
    HANDLE_TYPE_5("5", "自动过期")
    ;

    private String code;
    private String name;

     AutoWarningEnum() {
     }
     AutoWarningEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }
    public String getName() {
        return name;
    }

    public static AutoWarningEnum getByCode(String code) {
         for (AutoWarningEnum autoWarningEnum : AutoWarningEnum.values()) {
             if (code.equals(autoWarningEnum.getCode())) {
                 return autoWarningEnum;
             }
         }
         return null;
    }
}
