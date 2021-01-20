package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.constant.ScenicStatusEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.ScenicTouristNewestWithStatusVO;

import java.util.List;

/**
 * 游客热力图fix
 * @author funnylog
 */
public interface IHotMapFixService {


    /**
     * 查询景区实时游客数及运营状态
     *
     * @return 结果
     */
    List<ScenicTouristNewestWithStatusVO> scenicTouristHotMapStatistics();


    /**
     * 查询景区饱和度
     *
     * @param scenicCode 景区编码
     * @param peopleNum  游客数
     * @return 结果
     */
    ScenicStatusEnum getScenicStatus(String scenicCode, Integer peopleNum);

}
