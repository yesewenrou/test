package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;

/**
 * 当前预警状态可以做的操作
 * @author fangyunlong
 * @date 2020/3/8 1:13
 */
public enum AutoWarningStatusCheckEnum {

    /**
     * 待确认 可以做所有操作
     */
    WARN_CODE_033001(AutoWarningStatusEnum.WARN_CODE_033001.getCode(), AutoWarningOperateEnum.values()),
    /**
     * 已忽略 不能做任何操作
     */
    WARN_CODE_033002(AutoWarningStatusEnum.WARN_CODE_033002.getCode(), new AutoWarningOperateEnum[0]),
    /**
     *  已申请丶待发布  可以做短信操作和忽略申请操作
     */
    WARN_CODE_033003(AutoWarningStatusEnum.WARN_CODE_033003.getCode(), new AutoWarningOperateEnum[]{AutoWarningOperateEnum.SMS, AutoWarningOperateEnum.REQUEST_PUBLISH_IGNORE}),
    /**
     * 已过期 不能做任何操作
     */
    WARN_CODE_033004(AutoWarningStatusEnum.WARN_CODE_033004.getCode(), new AutoWarningOperateEnum[0]),
    /**
     * 已发布 不能做任何操作
     */
    WARN_CODE_033005(AutoWarningStatusEnum.WARN_CODE_033005.getCode(), new AutoWarningOperateEnum[0]),

    /**
     * 忽略申请 不能做任何操作
     */
    WARN_CODE_033006(AutoWarningStatusEnum.WARN_CODE_033006.getCode(), new AutoWarningOperateEnum[0]),
    ;

    AutoWarningStatusCheckEnum() {}
    private String currentStatus;
    private AutoWarningOperateEnum[] ableOperates;
    AutoWarningStatusCheckEnum(String currentStatus, AutoWarningOperateEnum[] ableOperates) {
        this.currentStatus = currentStatus;
        this.ableOperates = ableOperates;
    }

    public String getCurrentStatus() {
        return currentStatus;
    }

    public AutoWarningOperateEnum[] getAbleOperates() {
        return ableOperates;
    }
}
