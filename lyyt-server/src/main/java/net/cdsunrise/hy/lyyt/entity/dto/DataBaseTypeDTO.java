package net.cdsunrise.hy.lyyt.entity.dto;

import lombok.Data;
import net.cdsunrise.hy.lyyt.enums.DataBaseTypeEnum;


/**
 * @ClassName DataBaseTypeDTO
 * @Description
 * @Author LiuYin
 * @Date 2019/11/6 10:54
 */
@Data
public class DataBaseTypeDTO {
    private String code;
    private String name;
    private Integer index;

    public static DataBaseTypeDTO from(DataBaseTypeEnum typeEnum){
        final DataBaseTypeDTO dto = new DataBaseTypeDTO();
        dto.setCode(typeEnum.name().toUpperCase());
        dto.setName(typeEnum.getName());
        dto.setIndex(typeEnum.getIndex());

        return dto;
    }
}
