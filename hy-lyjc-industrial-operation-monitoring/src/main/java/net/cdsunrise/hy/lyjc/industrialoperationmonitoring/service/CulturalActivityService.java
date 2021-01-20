package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req.CulturalActivityAddReq;

/**
 * @Author: suzhouhe @Date: 2020/3/26 14:10 @Description: 文化活动
 */
public interface CulturalActivityService {
    /**
     * 新增文化活动
     *
     * @param culturalActivityAddReq 新增实体
     * @return 新增id
     */
    Result add(CulturalActivityAddReq culturalActivityAddReq);

    /**
     * 通过id删除
     *
     * @param id id
     * @return 删除
     */
    Result delete(Long id);

    /**
     * 分页获取文化活动
     *
     * @param culturalName 文化活动名称
     * @param beginTime    开始时间
     * @param endTime      结束时间
     * @param page         页码
     * @param size         条数
     * @return
     */
    Result list(String culturalName, Long beginTime, Long endTime, int page, int size);
}
