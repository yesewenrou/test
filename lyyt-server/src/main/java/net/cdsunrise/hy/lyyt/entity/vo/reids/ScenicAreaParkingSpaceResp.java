package net.cdsunrise.hy.lyyt.entity.vo.reids;

import lombok.Data;

import java.util.List;

/**
 * ScenicAreaParkingSpaceResp
 * 景区停车监测响应(摘要和列表）
 * <p>因为我也不知道字段具体意思，所有没有字段注释，只是为了排序，把原来返回的Object转换成明确对象</p>
 * @author LiuYin
 * @date 2020/6/8 15:52
 */
@Data
public class ScenicAreaParkingSpaceResp {

    private ScenicAreaParkingSpaceVO summary;
    private List<ScenicAreaParkingSpaceVO> list;

}
