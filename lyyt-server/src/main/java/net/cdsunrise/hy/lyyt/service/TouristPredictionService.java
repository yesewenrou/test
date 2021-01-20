package net.cdsunrise.hy.lyyt.service;

import net.cdsunrise.hy.lyyt.entity.vo.TouristPredictionVO;

import java.time.LocalDate;

/**
 * 游客预测服务
 *
 * @author lijiafeng
 * @date 2020/04/22 16:13
 */
public interface TouristPredictionService {

    /**
     * 预测某日游客数
     *
     * @param day 预测日期
     * @return 预测结果
     */
    TouristPredictionVO<? extends TouristPredictionVO.ScenicVO> prediction(LocalDate day);
}
