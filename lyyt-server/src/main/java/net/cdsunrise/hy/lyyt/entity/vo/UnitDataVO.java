package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;
import net.cdsunrise.common.utility.AssertUtil;
import net.cdsunrise.hy.lyyt.enums.DataDescriptionEnum;
import net.cdsunrise.hy.lyyt.enums.UnitEnum;
import net.cdsunrise.hy.lyyt.exception.business.BusinessException;
import net.cdsunrise.hy.lyyt.exception.business.BusinessExceptionEnum;

import java.util.Objects;

/**
 * @ClassName UnitDataVO
 * @Description 单位数据视图对象
 * @Author LiuYin
 * @Date 2019/7/22 16:34
 */
@Data
public class UnitDataVO {

    /** 数据描述类型*/
    private DataDescriptionEnum type;
    /** 数据描述类型索引*/
    private Integer typeIndex;
    /** 数据类型名称*/
    private String typeName;
    /** 数量*/
    private Integer number;
    /** 单位*/
    private String unit;
    /** 数量的字符串表示*/
    private String value;



    /**
     * 创建一个非存储位置类型的单位数据视图对象
     * @param type 数据描述类型
     * @param count 数量
     * @return 单位视图对象
     */
    public static UnitDataVO create(DataDescriptionEnum type, Long count){
        AssertUtil.isFalse(DataDescriptionEnum.STORE_LOCATION.equals(type), BusinessException.fail(BusinessExceptionEnum.TYPE_ERROR));
        return create(type, count, null);
    }


    /**
     * 创建一个单位数据视图对象
     * @param location 存储位置
     * @return 单位视图对象
     */
    public static UnitDataVO createStoreLocationType(String location){
        return create(DataDescriptionEnum.STORE_LOCATION, 0L, location);
    }


    /**
     * 创建一个单位数据视图对象
     * @param type 数据描述类型
     * @param count 数量
     * @param value 值（在数据描述类型是“存储位置”的时候，必须有value）
     * @return
     */
    private static UnitDataVO create(DataDescriptionEnum type, Long count, String value){
        final UnitDataVO vo = new UnitDataVO();
        vo.setType(type);
        vo.setTypeIndex(type.getIndex());
        vo.setTypeName(type.getName());

        final UnitEnum unitEnum = type.getUnitEnums()[0];
        switch (unitEnum){
            case B:
            case KB:
            case MB:
            case GB:
            case TB:
                final UnitEnum capacityUnit = UnitEnum.getCapacityUnit(count);
                vo.setNumber(capacityUnit.getUnitNumber(count).intValue());
                vo.setUnit(capacityUnit.getName());
                vo.setValue(String.valueOf(vo.getNumber()));
                break;
            case TIAO:
            case WAN_TIAO:
            case BAI_WAN_TIAO:
            case YI_TIAO:
                final UnitEnum tiaoUnit = UnitEnum.getTiaoUnit(count);
                vo.setNumber(tiaoUnit.getUnitNumber(count).intValue());
                vo.setUnit(tiaoUnit.getName());
                vo.setValue(String.valueOf(vo.getNumber()));
                break;
            case CI:
            case WAN_CI:
            case BAI_WAN_CI:
            case YI_CI:
                final UnitEnum ciUnit = UnitEnum.getCiUnit(count);
                vo.setNumber(ciUnit.getUnitNumber(count).intValue());
                vo.setUnit(ciUnit.getName());
                vo.setValue(String.valueOf(vo.getNumber()));
                break;
            case LOCATION:
                vo.setNumber(0);
                vo.setUnit(unitEnum.getName());
                vo.setValue(Objects.isNull(value) ? "" : value);
                break;
            default:
                vo.setUnit(unitEnum.getName());
                vo.setNumber(Objects.isNull(count) ? 0 : count.intValue());
                vo.setValue(String.valueOf(vo.getNumber()));
        }
        return vo;


    }


    public static UnitDataVO random(DataDescriptionEnum descType){
        return random(descType,null);
    }

    public static UnitDataVO random(DataDescriptionEnum descType,String value){
        final UnitDataVO vo = new UnitDataVO();
        vo.setType(descType);
        vo.setTypeIndex(descType.getIndex());
        vo.setTypeName(descType.getName());

        final UnitEnum randomUnit = descType.getRandomUnit();
        vo.setUnit(randomUnit.getName());
        vo.setNumber(randomUnit.getNumber());

        if(Objects.isNull(value)){
            vo.setValue(String.valueOf(vo.getNumber()));
        }

        return vo;
    }

}
