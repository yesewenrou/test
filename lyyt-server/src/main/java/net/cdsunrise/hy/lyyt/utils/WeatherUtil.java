package net.cdsunrise.hy.lyyt.utils;

/**
 * WeatherUtil
 *
 * @author LiuYin
 * @date 2020/5/14 10:12
 */
public final class WeatherUtil {

    private static final String KEY_FORECAST_PREFIX = "FORECAST_WEATHER_";

    public static String getForecast24HoursKey(String weatherAddr){
        return KEY_FORECAST_PREFIX + weatherAddr.toUpperCase() + "_24";
    }

}
