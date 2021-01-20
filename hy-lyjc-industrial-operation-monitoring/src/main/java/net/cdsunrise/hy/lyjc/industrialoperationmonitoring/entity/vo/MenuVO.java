package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author: suzhouhe @Date: 2020/1/15 9:33 @Description: 数据菜单vo 获取所有数据结构和总数信息的vo
 */
@Data
public class MenuVO {

    private String code;

    private String name;

    private Integer count;

    private List<SubMenu> subMenuList;

    @Data
    public static class SubMenu{
        private String name;

        private String code;

        private Integer count;

        private Integer secondCount;
    }

}
