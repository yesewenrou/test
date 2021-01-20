package net.cdsunrise.hy.lyyt.entity.resp;

import lombok.Data;
import net.cdsunrise.hy.lyyt.entity.vo.KeyCountVO;

import java.util.List;

/**
 * 涉旅资源响应
 * @author LiuYin 2020/2/5
 */
@Data
public class TravelResourceResponse {

    /** 总数*/
    private Long total;
    /** 列表*/
    private List<KeyCountVO> list;

}
