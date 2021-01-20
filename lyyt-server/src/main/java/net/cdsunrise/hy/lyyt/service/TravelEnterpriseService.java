package net.cdsunrise.hy.lyyt.service;

import net.cdsunrise.hy.lyyt.entity.vo.ImportantProjectVO;

import java.util.List;

/**
 * TravelEnterpriseService
 * 涉旅企业服务接口
 * @author LiuYin
 * @date 2020/1/19 14:17
 */
public interface TravelEnterpriseService {

    /**
     * 获取所有的重点建设项目
     * @return
     */
    List<ImportantProjectVO> getAllImportantProject();

}
