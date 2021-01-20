package net.cdsunrise.hy.lyyt.entity.dto;

import lombok.Data;

import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;

/**
 * @ClassName DataTypeDTO
 * @Description 数据类型转换对象
 * @Author LiuYin
 * @Date 2019/11/6 9:06
 */
@Data
public class DataTypeDTO {

    /** 编码*/
    private String code;
    /** 标题*/
    private String title;
    /** 索引*/
    private Integer index;

    public static DataTypeDTO from(DataTypeEnum dataTypeEnum){
        final DataTypeDTO dto = new DataTypeDTO();
        dto.setCode(dataTypeEnum.name().toUpperCase());
        dto.setIndex(dataTypeEnum.getIndex());
        dto.setTitle(dataTypeEnum.getTitle());

        return dto;
    }

}
