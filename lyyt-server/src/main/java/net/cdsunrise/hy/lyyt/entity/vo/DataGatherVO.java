package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;

import net.cdsunrise.hy.lyyt.enums.DataDescriptionEnum;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.enums.GatherStatusEnum;
import net.cdsunrise.hy.lyyt.enums.UnitEnum;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @ClassName DataGatherVO
 * @Description 数据（采集）来源视图对象
 * @Author LiuYin
 * @Date 2019/7/24 9:51
 */
@Data
public class DataGatherVO {

    /** 数据类型*/
    private DataTypeEnum type;
    /** 数据类型名称*/
    private String name;
    /** 数据状态*/
    private Integer status;
    /** 实时采集数据对象*/
    private UnitDataVO realTimeSize;
    /** 采集耗时*/
    private UnitDataVO gatherDuration;
    /** 存储位置*/
    private UnitDataVO storeLocation;


    public static DataGatherVO create(DataTypeEnum typeEnum){
        final DataGatherVO vo = new DataGatherVO();
        vo.setType(typeEnum);
        vo.setName(typeEnum.getTitle());
        vo.setStatus(GatherStatusEnum.SUCCESS.getStatus());

        return vo;


    }


    public void fail(){
        this.status = GatherStatusEnum.FAIL.getStatus();
        this.storeLocation = UnitDataVO.random(DataDescriptionEnum.STORE_LOCATION);
        this.storeLocation.setUnit(UnitEnum.KB.getName());
        this.storeLocation.setNumber(0);
        this.storeLocation.setValue("");
    }

    public static DataGatherVO  random(DataTypeEnum type){
        if(Objects.isNull(type)){
            type = DataTypeEnum.random();
        }
        final DataGatherVO vo = new DataGatherVO();
        vo.setType(type);
        vo.setName(type.getTitle());
        vo.setStatus(GatherStatusEnum.SUCCESS.getStatus());

        vo.setRealTimeSize(UnitDataVO.random(DataDescriptionEnum.REAL_TIME_GATHER_SIZE));
        vo.setGatherDuration(UnitDataVO.random(DataDescriptionEnum.GATHER_DURATION));
        vo.setStoreLocation(UnitDataVO.random(DataDescriptionEnum.STORE_LOCATION));

        final int n = ThreadLocalRandom.current().nextInt(1, 4);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < n; i++){
            sb.append(randomIp());
            if(i < n - 1){
                sb.append(",");
            }
        }

        vo.getStoreLocation().setValue(sb.toString());

        final UnitDataVO realTimeSize = vo.getRealTimeSize();
        if(UnitEnum.MB.getName().equalsIgnoreCase(realTimeSize.getUnit())){
            realTimeSize.setNumber(ThreadLocalRandom.current().nextInt(1,12));
            realTimeSize.setValue(String.valueOf(realTimeSize.getNumber()));
        }


        return vo;
    }

    private static String randomIp(){
        return "192.168." +
                ThreadLocalRandom.current().nextInt(1, 200) +
                "." +
                ThreadLocalRandom.current().nextInt(2, 254);
    }





}
