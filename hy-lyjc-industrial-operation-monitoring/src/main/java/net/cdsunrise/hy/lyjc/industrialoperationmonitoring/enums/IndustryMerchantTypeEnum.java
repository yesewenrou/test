package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums;


/**
 * 运行监测的 行业类型和  商户的行业类型
 * @author missu
 */

public enum IndustryMerchantTypeEnum {
    /** 景区 **/
    JING_QU("010001", ""),
    /** 住宿 **/
    ZHU_SU("010002", "006002"),
    /** 餐饮 **/
    CAN_YIN("010003","006001"),
    /** 购物 **/
    GOU_WU("010004", "006004"),
    /** 娱乐 **/
    YU_LE("010005", "006003"),
    /** 交通 **/
    JIAO_TONG("010006", ""),
    /** 综合服务 **/
    ZONG_HE_FU_WU("010007", ""),
    /** 其他 **/
    QI_TA("001008", "");

    private String lyjcCode;
    private String shCode;

    IndustryMerchantTypeEnum(String lyjcCode, String shCode) {
        this.lyjcCode = lyjcCode;
        this.shCode = shCode;
    }

    public String getLyjcCode() {
        return lyjcCode;
    }

    public void setLyjcCode(String lyjcCode) {
        this.lyjcCode = lyjcCode;
    }

    public String getShCode() {
        return shCode;
    }

    public void setShCode(String shCode) {
        this.shCode = shCode;
    }

    public static String getShCodeByLyjcCode(String lyjcCode) {
        for (IndustryMerchantTypeEnum typeEnum : values()) {
            if (typeEnum.getLyjcCode().equals(lyjcCode)) {
                return typeEnum.getShCode();
            }
        }
        return "";
    }

}
