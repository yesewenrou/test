package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.usermanage.domain.vo.resp;

import lombok.Data;

import java.util.List;

/**
 * @author LHY
 */
@Data
public class MenuVO {
    private Long id;
    private String name;
    private String code;
    private String path;
    private String permission;
    private Long parentId;
    private String moduleType;
    private String icon;
    private String mark;
    private Long weight;
    /**
     * 子菜单
     */
    private List<MenuVO> children;
}
