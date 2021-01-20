package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.RescueTypeEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lijiafeng 2021/01/18 15:23
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RescueTypeVO {
    private static final List<RescueTypeVO> LIST = buildList();

    /**
     * 类型
     */
    private Integer type;

    /**
     * 类型描述
     */
    private String typeDesc;

    private static List<RescueTypeVO> buildList() {
        return Arrays.stream(RescueTypeEnum.values())
                .map(typeEnum -> new RescueTypeVO(typeEnum.getType(), typeEnum.getTypeDesc()))
                .collect(Collectors.toList());
    }

    public static List<RescueTypeVO> getList() {
        return LIST;
    }
}
