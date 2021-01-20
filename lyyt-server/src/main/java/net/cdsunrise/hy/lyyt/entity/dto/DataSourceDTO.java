package net.cdsunrise.hy.lyyt.entity.dto;

import lombok.Data;
import net.cdsunrise.hy.lyyt.enums.DataSourceUnitEnum;


/**
 * @ClassName DataSourceDTO
 * @Description
 * @Author LiuYin
 * @Date 2019/11/6 9:10
 */
@Data
public class DataSourceDTO {

    /** 编码*/
    private String code;
    /** 名称*/
    private String name;
    /** 索引*/
    private Integer index;


    public static DataSourceDTO from(DataSourceUnitEnum unitEnum){
        final DataSourceDTO dto = new DataSourceDTO();
        dto.setCode(unitEnum.name().toUpperCase());
        dto.setName(unitEnum.getName());
        dto.setIndex(unitEnum.getIndex());

        return dto;
    }
}
