package net.cdsunrise.hy.lyyt.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyyt.consts.RedisKeyConsts;
import net.cdsunrise.hy.lyyt.entity.resp.MerchantTypeCountResponse;
import net.cdsunrise.hy.lyyt.service.MerchantTypeCountService;
import net.cdsunrise.hy.lyyt.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author sh
 * @date 2020-01-18 16:33
 */
@Slf4j
@Service
public class MerchantTypeCountServiceImpl implements MerchantTypeCountService {

    private final ObjectMapper objectMapper;

    @Autowired
    public MerchantTypeCountServiceImpl(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<MerchantTypeCountResponse> query() {
        List<String> values = RedisUtil.hashOperations().values(RedisKeyConsts.MERCHANT_BUSINESS_COUNT);
        List<MerchantTypeCountResponse> responses = new ArrayList<>();
        try {
            for (String value : values) {
                MerchantTypeCountResponse response = objectMapper.readValue(value, MerchantTypeCountResponse.class);
                responses.add(response);
            }
            return responses;
        } catch (IOException e) {
            log.error("", e);
        }
        return null;
    }
}
