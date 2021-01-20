package net.cdsunrise.hy.lyyt.enums;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author liuyin
 */
public enum DataDescriptionEnum {

    /** 数据总量*/
    DATA_TOTAL(1,"数据总量",new UnitEnum[]{UnitEnum.TB,UnitEnum.GB,UnitEnum.MB}),
    DATA_COUNT(2,"数据数量",new UnitEnum[]{UnitEnum.TIAO,UnitEnum.WAN_TIAO,UnitEnum.BAI_WAN_TIAO,UnitEnum.YI_TIAO}),
    TOTAL_CALL_TIMES(3,"总调用次数",new UnitEnum[]{UnitEnum.WAN_CI,UnitEnum.BAI_WAN_CI,UnitEnum.YI_TIAO}),
    TODAY_CALL_TIMES(4,"今日调用次数",new UnitEnum[]{UnitEnum.CI}),
    CURRENT_DAY_SHARE_ERROR(5,"当日共享异常",new UnitEnum[]{UnitEnum.CI}),
    CURRENT_DAY_CALL_TIMES(6,"当日接入数",new UnitEnum[]{UnitEnum.TIAO}),
    REAL_TIME_GATHER_SIZE(7,"实时采集量",new UnitEnum[]{UnitEnum.KB,UnitEnum.MB}),
    GATHER_DURATION(8,"采集耗时",new UnitEnum[]{UnitEnum.MS}),
    STORE_LOCATION(9,"存储位置",new UnitEnum[]{UnitEnum.LOCATION}),
    GATHER_TOTAL(10,"采集总量", new UnitEnum[]{UnitEnum.GB,UnitEnum.TB}),
    ;

    private Integer index;
    private String name;
    private UnitEnum[] unitEnums;

    public Integer getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    DataDescriptionEnum(Integer index, String name, UnitEnum[] unitEnums) {
        this.index = index;
        this.name = name;
        this.unitEnums = unitEnums;
    }

    public UnitEnum[] getUnitEnums() {
        return unitEnums;
    }

    public UnitEnum getRandomUnit(){
        return  unitEnums.length == 1 ? unitEnums[0] : unitEnums[ThreadLocalRandom.current().nextInt(0, unitEnums.length)];
    }


}
