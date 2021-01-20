package net.cdsunrise.hy.lyyt.enums;

import net.cdsunrise.hy.lyyt.common.UnitCommon;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;
import java.util.SplittableRandom;

/**
 * @ClassName UnitEnum
 * @Description
 * @Author LiuYin
 * @Date 2019/7/22 17:08
 */
public enum UnitEnum {

    /** 单位B*/
    B("B", UnitCommon.ZERO){
        @Override
        public Integer getNumber(){
            return new SplittableRandom(System.nanoTime()).nextInt(1,1024);
        }
    },

    /** 单位KB*/
    KB("KB", UnitCommon.ONE_KB){
        @Override
        public Integer getNumber(){
            return new SplittableRandom(System.nanoTime()).nextInt(1,1024);
        }
    },
    /** 单位MB*/
    MB("MB",UnitCommon.ONE_MB){
        @Override
        public Integer getNumber(){
            return new SplittableRandom(System.nanoTime()).nextInt(1,1024);
        }
    },
    /** 单位GB*/
    GB("GB",UnitCommon.ONE_GB){
        @Override
        public Integer getNumber(){
            return new SplittableRandom(System.nanoTime()).nextInt(1,1024);
        }
    },
    /** 单位TB*/
    TB("TB",UnitCommon.ONE_TB){
        @Override
        public Integer getNumber(){
            return new SplittableRandom(System.nanoTime()).nextInt(1,100);
        }
    },
    TIAO("条", UnitCommon.ZERO){
        @Override
        public Integer getNumber(){
            return new SplittableRandom(System.nanoTime()).nextInt(1,10000);
        }
    },
    WAN_TIAO("万条",UnitCommon.NUM_10000){
        @Override
        public Integer getNumber(){
            return new SplittableRandom(System.nanoTime()).nextInt(1,50);
        }
    },
    BAI_WAN_TIAO("百万条",UnitCommon.NUM_1000000){
        @Override
        public Integer getNumber(){
            return new SplittableRandom(System.nanoTime()).nextInt(1,50);
        }
    },
    YI_TIAO("亿条",UnitCommon.NUM_100000000){
        @Override
        public Integer getNumber(){
            return new SplittableRandom(System.nanoTime()).nextInt(1,50);
        }
    },
    CI("次",UnitCommon.ZERO){
        @Override
        public Integer getNumber(){
            return new SplittableRandom(System.nanoTime()).nextInt(1,10000);
        }
    },
    WAN_CI("万次",UnitCommon.NUM_10000){
        @Override
        public Integer getNumber(){
            return new SplittableRandom(System.nanoTime()).nextInt(1,80);
        }
    },
    BAI_WAN_CI("百万次",UnitCommon.NUM_1000000){
        @Override
        public Integer getNumber(){
            return new SplittableRandom(System.nanoTime()).nextInt(1,20);
        }
    },
    YI_CI("亿次", UnitCommon.NUM_100000000){
        @Override
        public Integer getNumber(){
            return new SplittableRandom(System.nanoTime()).nextInt(1,20);
        }
    },
    TYPE("类",UnitCommon.ZERO){
        @Override
        public Integer getNumber(){
            return new SplittableRandom(System.nanoTime()).nextInt(7,13);
        }
    },
    GE("个",UnitCommon.ZERO){
        @Override
        public Integer getNumber(){
            return new SplittableRandom(System.nanoTime()).nextInt(10,13);
        }
    },
    MS("MS",UnitCommon.ZERO){
        @Override
        public Integer getNumber(){
            return new SplittableRandom(System.nanoTime()).nextInt(10,200);
        }
    },
    LOCATION("",UnitCommon.ZERO){

    };

    /** 名称*/
    private String name;
    /** 边界值*/
    private Long boundaryValue;




    UnitEnum(String name, Long boundaryValue) {
        this.name = name;
        this.boundaryValue = boundaryValue;
    }

    public String getName() {
        return name;
    }

    public Integer getNumber(){
        return 0;
    }

    public Long getBoundaryValue() {
        return boundaryValue;
    }

    /**
     * 根据单位计算对应单位要显示的数字
     * @param count
     * @return
     */
    public Long getUnitNumber(Long count){
        if(Objects.isNull(count)){
            return 0L;
        }

        final Long boundaryValue = getBoundaryValue();
        if(0 == boundaryValue){
            return count;
        }

        final double value = (count + 0D) / boundaryValue;
        return BigDecimal.valueOf(value).setScale(0, RoundingMode.HALF_UP).longValue();
    }



    /**
     * 根据数量得到容量单位
     * @param count 数量
     */
    public static UnitEnum getCapacityUnit(Long count){

        UnitEnum ue;
        if(Objects.isNull(count) || count < KB.getBoundaryValue()){
            ue = B;
        }else if(count >= KB.getBoundaryValue() && count < MB.getBoundaryValue()){
            ue = KB;
        }else if(count >= MB.getBoundaryValue() && count < GB.getBoundaryValue()){
            ue = MB;
        }else if(count >= GB.getBoundaryValue() && count < TB.getBoundaryValue()){
            ue = GB;
        }else{
            ue = TB;
        }
        return ue;
    }

    /**
     * 根据数量得到“次”单位
     * @param count
     * @return
     */
    public static UnitEnum getCiUnit(Long count){
        UnitEnum ue;
        if(Objects.isNull(count) || count < WAN_CI.getBoundaryValue()){
            ue = CI;
        }else if(count >= WAN_CI.getBoundaryValue() && count < BAI_WAN_CI.getBoundaryValue()){
            ue = WAN_CI;
        }else if(count >= BAI_WAN_CI.getBoundaryValue() && count < YI_CI.getBoundaryValue()){
            ue = BAI_WAN_CI;
        }else {
            ue = YI_CI;
        }
        return ue;
    }

    /**
     * 根据数量得到“条”单位
     * @param count
     * @return
     */
    public static UnitEnum getTiaoUnit(Long count){
        UnitEnum ue;
        if(Objects.isNull(count) || count < WAN_TIAO.getBoundaryValue()){
            ue = TIAO;
        }else if(count >= WAN_TIAO.getBoundaryValue() && count < BAI_WAN_TIAO.getBoundaryValue()){
            ue = WAN_TIAO;
        }else if(count >=BAI_WAN_TIAO.getBoundaryValue() && count < YI_TIAO.getBoundaryValue()){
            ue = BAI_WAN_TIAO;
        }else {
            ue = YI_TIAO;
        }
        return ue;


    }




    public static void main(String[] args) {
        System.out.println(getCapacityUnit(1234562938422L));
    }

}
