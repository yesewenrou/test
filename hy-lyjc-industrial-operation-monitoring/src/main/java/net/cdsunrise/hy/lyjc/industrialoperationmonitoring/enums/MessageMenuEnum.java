package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

/**
 * @author LHY
 * @date 2019/12/10 11:42
 */
public enum MessageMenuEnum {

    /**
     * 景区超负荷运营
     */
    OPERATION_SCENIC_TOURISTS_OVERLOAD("050101", "/#/home/OperationMonitoring/TouristHeatMap"),

    /**
     * 投诉管理 - 待受理
     */
    OPERATION_COMPLAINT_NOT_HANDLE("020203", "/#/home/industry/complainManagement"),

    /**
     * 投诉管理 - 待处理
     */
    OPERATION_COMPLAINT_NOT_FINISH("020205", "/#/home/industry/complainManagement"),

    /**
     * 交通热力图
     */
    TRAFFIC_HEAT_MAP("0505", "/#/home/OperationMonitoring/Traffic"),

    /**
     * 停车热力图
     */
    PARK_HEAT_MAP("0509", "/#/home/OperationMonitoring/Park"),

    /**
     * 预警管理
     */
    OPERATION_WARNING_MANAGE("0706", "/#/home/CommandAndControl/Management");

    private String code;
    private String uri;

    MessageMenuEnum(String code, String uri) {
        this.code = code;
        this.uri = uri;
    }

    public String getCode() {
        return code;
    }

    public String getUri() {
        return uri;
    }

}
