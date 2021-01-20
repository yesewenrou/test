package net.cdsunrise.hy.lyyt.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.AssertUtil;
import net.cdsunrise.common.utility.StringUtil;
import net.cdsunrise.common.utility.utils.JsonUtils;
import net.cdsunrise.hy.lyyt.entity.dto.EnvBureauDTO;
import net.cdsunrise.hy.lyyt.entity.dto.HumidityInfoDTO;
import net.cdsunrise.hy.lyyt.entity.resp.WeatherResponse;
import net.cdsunrise.hy.lyyt.entity.vo.WeatherVo;
import net.cdsunrise.hy.lyyt.service.WeatherService;
import net.cdsunrise.hy.lyyt.utils.DateUtil;
import net.cdsunrise.hy.lyyt.utils.WeatherUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author sh
 * @date 2020-02-14 14:55
 */
@Slf4j
@Service
public class WeatherServiceImpl implements WeatherService {

    private static final String REDIS_HASH_KEY_HUMIDITY = "HY_WEATHER_HUMIDITY";

    private static final String HY_WEATHER_CURRENT = "HY_WEATHER_CURRENT";
    private static final String HY_ENV_CURRENT = "HY_ENV_CURRENT";
    private static final String YU_PING_SHAN = "玉屏山";
    private static final String LIU_JIANG_GU_ZHENG = "柳江古镇";
    private static final String CAO_YU_TAN = "槽渔滩";
    private static final String HONG_YA ="洪雅";



    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public WeatherServiceImpl(StringRedisTemplate stringRedisTemplate, ObjectMapper objectMapper) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.objectMapper = objectMapper;
    }

    @Override
    public List<WeatherResponse> getAllWeather() {

        // 获取从天气采集过来的数据（网站爬取）
        List<String> fromWebList = stringRedisTemplate.<String,String>opsForHash().values(HY_WEATHER_CURRENT);
        // 获取从环保过来的数据
        List<String> fromEnvList = stringRedisTemplate.<String,String>opsForHash().values(HY_ENV_CURRENT);

        if (!fromWebList.isEmpty()) {
            final List<WeatherResponse> list = fromWebList.stream().map(r -> wrapWeatherAndCurrent(r, fromEnvList)).collect(Collectors.toList());
            // 批量设置湿度
            batchSetHumidity(list);
            return list;
        }
        return new ArrayList<>(0);
    }

    @Override
    public WeatherResponse getWeatherByScenicName(String scenicName) {
        String o = stringRedisTemplate.<String,String>opsForHash().get(HY_WEATHER_CURRENT, scenicName);
        List<String> currentValues = stringRedisTemplate.<String,String>opsForHash().values(HY_ENV_CURRENT);
        return wrapWeatherAndCurrent(o, currentValues);
    }


    /**
     * 得到某个景点的湿度
     * @param weatherAddr 景点地址，如seven_ping、hong_ya
     * @return nullable dto
     */
    private HumidityInfoDTO getHumidity(String weatherAddr){
        final String s = stringRedisTemplate.<String, String>opsForHash().get(REDIS_HASH_KEY_HUMIDITY, weatherAddr);
        if(StringUtils.isEmpty(s)){
            return null;
        }
        return JsonUtils.toObject(s, new TypeReference<HumidityInfoDTO>() {
        });
    }

    /**
     * 得到所有的景点湿度
     * @return ["hong_ya", HumidityInfoDTO]
     */
    private Map<String,HumidityInfoDTO> getAllHumidity(){
        final Map<String, String> entries = stringRedisTemplate.<String, String>opsForHash().entries(REDIS_HASH_KEY_HUMIDITY);
        if(CollectionUtils.isEmpty(entries)){
            return new HashMap<>(0);
        }
        return entries.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> JsonUtils.toObject(e.getValue(), new TypeReference<HumidityInfoDTO>() {
        })));
    }


    private WeatherResponse wrapWeatherAndCurrent(String formWeb, List<String> fromEnvList) {
        // 得到天气对象
        final WeatherResponse weatherResponse = this.wrapWeather(formWeb);
        // 得到景区名称
        String scenicName = weatherResponse.getScenicName();
        // 玉屏山、七里坪、瓦屋山、柳江古镇必须用环保过来的数据，若没有，则默认是零
        // 槽渔滩使用web页面数据，洪雅县使用web数据

        // 这里为了配合迎检，因为从环保过来的数据没有，就默认用抓取的数据，所以下面这段注释了
        // 如果环保过来的数据有了，则这一段要放开
//        if (!CAO_YU_TAN.equals(scenicName) && !HONG_YA.equals(scenicName)){
//            weatherResponse.setPm25(0D);
//        }

        // 如果从环保来的数据没有，那么直接返回从页面过来的天气
        if (CollectionUtils.isEmpty(fromEnvList)) {
            return weatherResponse;
        }

        // 得到今天的环境数据
        // 如果是今天的数据，且景区名称相同，就做pm2.5替换（注意，玉屏山对应了 玉屏山和柳江古镇）
        final LocalDate now = LocalDate.now();
        fromEnvList.stream()
                .map(this::toEnv)
                .filter(env -> now.equals(DateUtil.stringToLocalDateTime(env.getRecordTime()).toLocalDate()))
                .filter(envBureauDTO -> {
                    final String recordSite = envBureauDTO.getRecordSite();
                    return recordSite.equals(scenicName) || (YU_PING_SHAN.equals(recordSite) && LIU_JIANG_GU_ZHENG.equals(scenicName));
                })
                .findAny()
                .ifPresent(env -> weatherResponse.setPm25(env.getPm25()));

        // 设置当前小时的描述
        setCurrentHourWeather(weatherResponse);

        // 设置湿度
        setHumidity(weatherResponse);

        return weatherResponse;
    }


    /**
     * 设置湿度
     * @param response
     */
    private void setHumidity(WeatherResponse response){

        final String weatherAddr = response.getWeatherAddr();
        final HumidityInfoDTO humidity = getHumidity(weatherAddr);
        if(Objects.nonNull(humidity)){
            setHumidity(response, humidity);
        }else{
            log.warn("humidity is null for response [{}]", response);
        }
    }


    /**
     * 批量设置湿度
     * @param responseList
     */
    private void batchSetHumidity(List<WeatherResponse> responseList){
        if(CollectionUtils.isEmpty(responseList)){
            return;
        }
        final Map<String, HumidityInfoDTO> allHumidity = getAllHumidity();
        if(CollectionUtils.isEmpty(allHumidity)){
            log.warn("not found any humidity, map is empty");
            return ;
        }
        responseList.forEach(r -> {
            final HumidityInfoDTO humidityInfoDTO = allHumidity.get(r.getWeatherAddr());
            setHumidity(r, humidityInfoDTO);
        });
    }


    private void setHumidity(WeatherResponse response, HumidityInfoDTO humidityInfoDTO){
        if(Objects.nonNull(humidityInfoDTO)){
            response.setHumidity(humidityInfoDTO.getValue().doubleValue());
            response.setHumidityGatherTime(humidityInfoDTO.getCollectTime());
            response.setHumidityUrl(humidityInfoDTO.getUrl());
        }
    }




    /**
     * 设置当前小时的天气
     * @param weatherResponse 天气响应
     */
    private void setCurrentHourWeather(WeatherResponse weatherResponse) {
        if(Objects.isNull(weatherResponse)){
            return;
        }
        final String weatherAddr = weatherResponse.getWeatherAddr();
        final String hashKey = WeatherUtil.getForecast24HoursKey(weatherAddr);

        final String currentWeather = stringRedisTemplate.<String, String>opsForHash().get(hashKey, String.valueOf(LocalDateTime.now().getHour()));
        if(!StringUtils.isEmpty(currentWeather)){
            weatherResponse.setRealWeather(currentWeather);
        }else{
            log.warn("not found 24 hour weather with hash key [{}]", hashKey);
        }
    }


    private WeatherResponse wrapWeather(String str) {
        AssertUtil.notEmpty(str, () -> new RuntimeException("weather string is empty"));
        try {
            WeatherVo vo = objectMapper.readValue(str, WeatherVo.class);
            WeatherResponse resp = new WeatherResponse();
            BeanUtils.copyProperties(vo, resp);
            resp.setUpdateTime(DateUtil.convert(vo.getUpdateTime()));
            resp.setGatherTime(DateUtil.convert(vo.getGatherTime()));
            return resp;
        } catch (IOException e) {
            log.error("[WeatherServiceImpl] convert string to object", e);
            throw new RuntimeException(e);
        }
    }

    private EnvBureauDTO toEnv(String envString){
        AssertUtil.notEmpty(envString, () -> new RuntimeException("env string is empty"));
        try {
            return objectMapper.readValue(envString, EnvBureauDTO.class);
        } catch (IOException e) {
            log.error("convert string to env bureau dto", e);
            throw new RuntimeException(e);
        }
    }
}
