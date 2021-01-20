package net.cdsunrise.hy.lyyt.service.impl;

import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyyt.annatation.DataType;
import net.cdsunrise.hy.lyyt.consts.RedisKeyConsts;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.service.TransferService;
import net.cdsunrise.hy.lyyt.utils.DateUtil;
import org.hibernate.validator.internal.util.privilegedactions.LoadClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import reactor.util.annotation.Nullable;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author YQ on 2019/11/15.
 */
@Service
@Slf4j
public class TransferServiceImpl implements TransferService {

    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public TransferServiceImpl(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    @PostConstruct
    public void init(){
        for (DataTypeEnum value : DataTypeEnum.values()) {
            final Boolean hasKey = redisTemplate.opsForHash().hasKey(fetchTotalNormalRedisKey(), value.toString());
            if(!hasKey){
                redisTemplate.opsForHash().increment(fetchTotalNormalRedisKey(), value.toString(), 1);
            }
        }
    }

    @Override
    public void normalTransfer(Set<DataTypeEnum> dataTypes) {
        dataTypes.forEach(dataType -> {
            log.info("normal transfer dataType=[{}]", dataType);
            redisTemplate.opsForHash().increment(fetchNormalRedisKey(null), dataType.toString(), 1);
            redisTemplate.opsForHash().increment(fetchTotalNormalRedisKey(), dataType.toString(), 1);
        });
    }

    @Override
    public void errorTransfer() {
        log.error("error transfer");
//        redisTemplate.opsForValue().increment(fetchErrorRedisKey(null), 1);
//        redisTemplate.opsForValue().increment(fetchTotalErrorRedisKey(), 1);
    }

    @Override
    public Long normal(DataTypeEnum dataType, Date date) {
        String ret = (String) redisTemplate.opsForHash().get(fetchNormalRedisKey(date), dataType.toString());
        if (ret != null) {
            return Long.parseLong(ret);
        }
        return 0L;
    }

    @Override
    public Long normalTotal(DataTypeEnum dataType) {
        String ret = (String) redisTemplate.opsForHash().get(fetchTotalNormalRedisKey(), dataType.toString());
        if (ret != null) {
            return Long.parseLong(ret);
        }
        return 0L;
    }

    @Override
    public Long error(Date date) {
        String ret = redisTemplate.opsForValue().get(fetchErrorRedisKey(date));
        if (ret != null) {
            return Long.parseLong(ret);
        }
        return 0L;
    }

    @Override
    public Long errorTotal() {
        String ret = redisTemplate.opsForValue().get(fetchTotalErrorRedisKey());
        if (ret != null) {
            return Long.parseLong(ret);
        }
        return 0L;
    }

    @Override
    public void initDayCount(LocalDate localDate) {
        if(Objects.isNull(localDate)){
            return ;
        }
        for (DataTypeEnum value : DataTypeEnum.values()) {
            redisTemplate.opsForHash().putIfAbsent(RedisKeyConsts.LYDSJ_TRANSFER_CALL_COUNT + DateUtil.localDateToString(localDate), value.toString(), "0");
        }
    }

    private String fetchNormalRedisKey(@Nullable Date date) {
        if (date == null) {
            return RedisKeyConsts.LYDSJ_TRANSFER_CALL_COUNT + DateUtil.getToday();
        }else{
            return RedisKeyConsts.LYDSJ_TRANSFER_CALL_COUNT + DateUtil.format(date, DateUtil.PATTERN_YYYY_MM_DD);
        }
    }

    private String fetchErrorRedisKey(@Nullable Date date) {
        if (date == null) {
            return RedisKeyConsts.LYDSJ_TRANSFER_ERROR_COUNT + DateUtil.getToday();
        }else{
            return RedisKeyConsts.LYDSJ_TRANSFER_ERROR_COUNT + DateUtil.format(date, DateUtil.PATTERN_YYYY_MM_DD);
        }

    }

    private String fetchTotalNormalRedisKey() {
        return RedisKeyConsts.LYDSJ_TRANSFER_TOTAL_CALL_COUNT;
    }

    private String fetchTotalErrorRedisKey() {
        return RedisKeyConsts.LYDSJ_TRANSFER_TOTAL_ERROR_COUNT;
    }


}
