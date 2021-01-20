package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.dto;

import lombok.Data;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.DataDictionaryVO;

import java.util.Objects;

/**
 * @ClassName SimpleDataDictionaryDto
 * @Description 简单数据字典对象
 * @Author LiuYin
 * @Date 2020/1/8 15:07
 */
@Data
public class SimpleDataDictionaryDto {

    private Long id;
    private String code;
    private String name;
    private Integer orderNumber;
    private String parentCode;


    public static SimpleDataDictionaryDto from(DataDictionaryVO dataDictionaryVO){
        Objects.requireNonNull(dataDictionaryVO);

        final SimpleDataDictionaryDto dto = new SimpleDataDictionaryDto();
        dto.setId(dataDictionaryVO.getId());
        dto.setCode(dataDictionaryVO.getCode());
        dto.setName(dataDictionaryVO.getName());
        dto.setOrderNumber(dataDictionaryVO.getOrderNumber());
        dto.setParentCode(dataDictionaryVO.getParentCode());

        return dto;


    }

}
