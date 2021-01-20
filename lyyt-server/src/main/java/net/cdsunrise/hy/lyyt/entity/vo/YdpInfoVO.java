package net.cdsunrise.hy.lyyt.entity.vo;

import lombok.Data;

/**
 * @ClassName YdpInfoVO
 * @Description 诱导屏信息视图对象
 * @Author LiuYin
 * @Date 2019/12/20 18:22
 */
@Data
public class YdpInfoVO {

    /** 唯一标识*/
    private Long id;
    /** 名称*/
    private String name;
    /** 状态描述*/
    private String status;

    public static YdpInfoVO create(Long id, String name){
        final YdpInfoVO ydpInfoVO = new YdpInfoVO();
        ydpInfoVO.setId(id);
        ydpInfoVO.setName(name);
        return ydpInfoVO;
    }

}
