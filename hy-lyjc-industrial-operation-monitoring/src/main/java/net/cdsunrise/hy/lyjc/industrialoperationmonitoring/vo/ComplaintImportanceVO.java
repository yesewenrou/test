package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.ComplaintImportanceEnum;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lijiafeng 2021/01/14 19:53
 */
@Data
public class ComplaintImportanceVO {

    private static final List<ComplaintImportanceVO> list;

    static {
        list = Arrays.stream(ComplaintImportanceEnum.values()).map(ComplaintImportanceVO::fromEnum).collect(Collectors.toList());
    }

    /**
     * 重要性
     */
    private Integer importance;

    /**
     * 重要性描述
     */
    private String ImportanceDesc;

    private static ComplaintImportanceVO fromEnum(ComplaintImportanceEnum src) {
        ComplaintImportanceVO dest = new ComplaintImportanceVO();
        dest.setImportance(src.getImportance());
        dest.setImportanceDesc(src.getImportanceDesc());
        return dest;
    }

    public static List<ComplaintImportanceVO> getList() {
        return list;
    }
}
