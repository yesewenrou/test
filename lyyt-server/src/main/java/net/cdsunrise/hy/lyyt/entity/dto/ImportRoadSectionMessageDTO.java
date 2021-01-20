package net.cdsunrise.hy.lyyt.entity.dto;

import lombok.Data;

/**
 * @ClassName ImportRoadSectionMessageDTO
 * @Description 重点路段消息对象
 * @Author LiuYin
 * @Date 2019/12/13 14:49
 */
@Data
public class ImportRoadSectionMessageDTO {

    /** 类型："update","delete" */
    private String type;

    /** 重点道路id列表的字符串形式，用半角逗号隔开，如：1-1,1-2,2-3*/
    private String sectionIdString;

}
