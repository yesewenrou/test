package net.cdsunrise.hy.lyyt.controller;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyyt.config.WeatherCustomConfig;
import net.cdsunrise.hy.lyyt.entity.resp.WeatherResponse;
import net.cdsunrise.hy.lyyt.service.WeatherService;
import net.cdsunrise.hy.lyyt.utils.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author sh
 * @date 2020-02-14 15:08
 */
@RestController
@RequestMapping("/weather")
@Slf4j
public class WeatherController {

    private final WeatherService weatherService;

    @Autowired
    private WeatherCustomConfig weatherCustomConfig;

    @Autowired
    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }


    @GetMapping("/all")
    public List<WeatherResponse> all() {
        final List<WeatherResponse> allWeather = weatherService.getAllWeather();
        // 按照业主要求，设置自定义的玉屏山温度和pm2.5
        setYuPingMountainWeatherByCustom(allWeather);

        // 按照业主要求，设置天气范围
        allWeather.forEach(WeatherController::setWeatherRange);

        // 设置玉屏山温度最大值、最小值与对应柳江古镇的温度偏移量一致
        postSetYuPingMountainMinAndMaxTemperature(allWeather);

        // 按照景区名称再排序一次
        return sortedByWeatherName(allWeather);
    }

    @GetMapping("/get/{code}")
    public WeatherResponse getByScenicName(@PathVariable("code") String code) {
//        final WeatherResponse weatherByScenicName = weatherService.getWeatherByScenicName(code);
//        // 按照业主要求，设置天气范围
//        setWeatherRange(weatherByScenicName);
//        return weatherByScenicName;

        final List<WeatherResponse> allWeather = weatherService.getAllWeather();
        // 按照业主刘书记要求，设置自定义的玉屏山温度和pm2.5
        setYuPingMountainWeatherByCustom(allWeather);
        // 按照业主要求，设置天气范围
        allWeather.forEach(WeatherController::setWeatherRange);
        // 设置玉屏山温度最大值、最小值与对应柳江古镇的温度偏移量一致
        postSetYuPingMountainMinAndMaxTemperature(allWeather);

        final Map<String, WeatherResponse> map = allWeather.stream().collect(Collectors.toMap(WeatherResponse::getWeatherAddr, Function.identity()));
        return map.get(code);
    }

    /**
     * 设置玉屏山最大值与最小值
     * （该方法要在调整了最大值与最小值之后设置）
     * @param list
     */
    private void postSetYuPingMountainMinAndMaxTemperature(List<WeatherResponse> list){
        final Map<String, WeatherResponse> map = list.stream().collect(Collectors.toMap(WeatherResponse::getWeatherAddr, t -> t));
        final WeatherResponse liujiangWeather = map.get("liu_jiang_town");
        final WeatherResponse yupingMountain = map.get("yu_ping_mountain");
        if(Objects.nonNull(liujiangWeather) && Objects.nonNull(yupingMountain)){
            final Integer temperatureOffset = weatherCustomConfig.getYupingMountainTemperatureOffset();
            if(Objects.nonNull(temperatureOffset)){
                yupingMountain.setMinTemp(liujiangWeather.getMinTemp() + temperatureOffset);
                yupingMountain.setMaxTemp(liujiangWeather.getMaxTemp() + temperatureOffset);
            }
        }
    }


    /**
     * 按照2020-06-08早上开会要求，做一个固定排序，这里以景区名称为参考
     * @param list list
     * @return new list sorted by scenicName;
     */
    private List<WeatherResponse> sortedByWeatherName(List<WeatherResponse> list){
        return list.stream().sorted((o1, o2) -> SortUtil.getWeatherNameSortResult(o1.getScenicName(),o2.getScenicName())).collect(Collectors.toList());
    }

    /**
     * 按照2020年10月27日刘书记要求：
     * 玉屏山的气温，在柳江古镇的基础上-2℃；
     * 玉屏山的pm2.5，在柳江古镇基础上乘以0.9 。
     * @param list
     */
    private void setYuPingMountainWeatherByCustom(List<WeatherResponse> list){
        final Map<String, WeatherResponse> map = list.stream().collect(Collectors.toMap(WeatherResponse::getWeatherAddr, t -> t));
        final WeatherResponse liujiangWeather = map.get("liu_jiang_town");
        final WeatherResponse yupingMountain = map.get("yu_ping_mountain");
        if(Objects.nonNull(liujiangWeather) && Objects.nonNull(yupingMountain)){
            final Integer temperatureOffset = weatherCustomConfig.getYupingMountainTemperatureOffset();
            if(Objects.nonNull(temperatureOffset)){
                yupingMountain.setRealTemp(liujiangWeather.getRealTemp() + temperatureOffset);
            }

            final Double pm25Ratio = weatherCustomConfig.getYupingMountainPm25Ratio();
            if(Objects.nonNull(pm25Ratio)){
                final double newPm25 = liujiangWeather.getPm25() * pm25Ratio;
                yupingMountain.setPm25(BigDecimal.valueOf(newPm25).setScale(1, RoundingMode.HALF_UP).doubleValue());
            }
        }
    }


    /**
     * 设置天气范围
     *
     * ！ 在从天气网30ttq.com抓取的数据中，实时温度的值，有可能超出预测的白天最高温度、夜间最低温度形成的范围值（例如：实时温度17，最低12，最高14）
     * ！ 业主（刘书记）提出说这种情况数据不准，为了满足业主需求，这里结合实时温度，把最高温度、最低温度的值调整到能够包含实时温度
     * ！ 具体的逻辑是：如果实时温度比最低温度低，那么最低温度是实时温度减1度；如果实时温度比最高温度高，那么最高温度是实时温度加1度
     *
     * @param weatherResponse 温度响应对象
     */
    private static void setWeatherRange(WeatherResponse weatherResponse){
        if(Objects.isNull(weatherResponse)){
            return;
        }
        try {
            final Integer realTemp = weatherResponse.getRealTemp();

            if(Objects.nonNull(realTemp)){
                final int minTemp = Objects.isNull(weatherResponse.getMinTemp()) ? realTemp - 1 : weatherResponse.getMinTemp();
                final int maxTemp = Objects.isNull(weatherResponse.getMaxTemp()) ? realTemp + 1 : weatherResponse.getMaxTemp();
                weatherResponse.setMinTemp( realTemp < minTemp ? realTemp - 1 : minTemp);
                weatherResponse.setMaxTemp( realTemp > maxTemp ? realTemp + 1 : maxTemp);
            }

        }catch (Exception e){
            log.error("set weather range error: ", e);
        }
    }
}
