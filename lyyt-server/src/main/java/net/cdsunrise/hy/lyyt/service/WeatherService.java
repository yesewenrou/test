package net.cdsunrise.hy.lyyt.service;


import net.cdsunrise.hy.lyyt.entity.resp.WeatherResponse;

import java.util.List;

/**
 * @author sh
 * @date 2020-02-14 14:52
 */
public interface WeatherService {
    /**
     * 获取所有景区实时天气
     * @return 景区天气
     */
    List<WeatherResponse> getAllWeather();

    /**
     * 根据景区编码获取单个景区天气
     * @param scenicName 景区编码
     * @return 景区天气
     */
    WeatherResponse getWeatherByScenicName(String scenicName);
}
