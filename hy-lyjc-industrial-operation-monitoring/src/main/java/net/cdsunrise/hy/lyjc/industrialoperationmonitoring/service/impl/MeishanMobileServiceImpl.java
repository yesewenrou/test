package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IMeishanMobileService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.feign.MeishanMobileFeignClient;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.JsonUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.msm.*;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2019/9/26 11:21
 */
@Service
@Slf4j
public class MeishanMobileServiceImpl implements IMeishanMobileService {

    private MeishanMobileFeignClient meishanMobileFeignClient;

    public MeishanMobileServiceImpl(MeishanMobileFeignClient meishanMobileFeignClient) {
        this.meishanMobileFeignClient = meishanMobileFeignClient;
    }

    @Override
    public MeishanMobileResult<List<MsHyLocalData>> getMsHyMinuteLocalData() {
        return getMsHyLocalData(MeishanMobileRequest.REQUEST_GET_MS_HY_MINUTE_LOCAL_DATA, null);
    }

    @Override
    public MeishanMobileResult<List<MsHyLocalData>> getMsHyHourLocalData(String queryTime) {
        return getMsHyLocalData(MeishanMobileRequest.REQUEST_GET_MS_HY_HOUR_LOCAL_DATA, queryTime);
    }

    @Override
    public MeishanMobileResult<List<MsHyLocalData>> getMsHyDayLocalData(String queryTime) {
        return getMsHyLocalData(MeishanMobileRequest.REQUEST_GET_MS_HY_DAY_LOCAL_DATA, queryTime);
    }

    @Override
    public MeishanMobileResult<List<MsHyLocalData>> getMsHyMonthLocalData(String queryTime) {
        return getMsHyLocalData(MeishanMobileRequest.REQUEST_GET_MS_HY_MONTH_LOCAL_DATA, queryTime);
    }

    private MeishanMobileResult<List<MsHyLocalData>> getMsHyLocalData(String aName, String queryTime) {
        MeishanMobileRequest.Builder builder = new MeishanMobileRequest.Builder();
        MeishanMobileRequest request = builder
                .param("queryTime", queryTime)
                .build(aName);
        String jsonResult = meishanMobileFeignClient.getSafetySupervision(request.getAName(), request.getUid(), request.getTimestamp(), request.getSignature(), request.getParam());
        log.debug("请求结果json: {}", jsonResult);
        return JsonUtil.parse(jsonResult, new TypeReference<MeishanMobileResult<List<MsHyLocalData>>>() {
        });
    }

    @Override
    public MeishanMobileResult<List<MsHySourceProvData>> getMsHyDaySourceProvData(String queryTime) {
        return getMsHySourceProvData(MeishanMobileRequest.REQUEST_GET_MS_HY_DAY_SOURCE_PROV_DATA, queryTime);
    }

    @Override
    public MeishanMobileResult<List<MsHySourceProvData>> getMsHyMonthSourceProvData(String queryTime) {
        return getMsHySourceProvData(MeishanMobileRequest.REQUEST_GET_MS_HY_MONTH_SOURCE_PROV_DATA, queryTime);
    }

    private MeishanMobileResult<List<MsHySourceProvData>> getMsHySourceProvData(String aName, String queryTime) {
        MeishanMobileRequest.Builder builder = new MeishanMobileRequest.Builder();
        MeishanMobileRequest request = builder
                .param("queryTime", queryTime)
                .build(aName);
        String jsonResult = meishanMobileFeignClient.getSafetySupervision(request.getAName(), request.getUid(), request.getTimestamp(), request.getSignature(), request.getParam());
        log.debug("请求结果json: {}", jsonResult);
        return JsonUtil.parse(jsonResult, new TypeReference<MeishanMobileResult<List<MsHySourceProvData>>>() {
        });
    }

    @Override
    public MeishanMobileResult<List<MsHySourceCityData>> getMsHyDaySourceCityData(String queryTime) {
        return getMsHySourceCityData(MeishanMobileRequest.REQUEST_GET_MS_HY_DAY_SOURCE_CITY_DATA, queryTime);
    }

    @Override
    public MeishanMobileResult<List<MsHySourceCityData>> getMsHyMonthSourceCityData(String queryTime) {
        return getMsHySourceCityData(MeishanMobileRequest.REQUEST_GET_MS_HY_MONTH_SOURCE_CITY_DATA, queryTime);
    }

    private MeishanMobileResult<List<MsHySourceCityData>> getMsHySourceCityData(String aName, String queryTime) {
        MeishanMobileRequest.Builder builder = new MeishanMobileRequest.Builder();
        MeishanMobileRequest request = builder
                .param("queryTime", queryTime)
                .build(aName);
        String jsonResult = meishanMobileFeignClient.getSafetySupervision(request.getAName(), request.getUid(), request.getTimestamp(), request.getSignature(), request.getParam());
        log.debug("请求结果json: {}", jsonResult);
        return JsonUtil.parse(jsonResult, new TypeReference<MeishanMobileResult<List<MsHySourceCityData>>>() {
        });
    }

    @Override
    public MeishanMobileResult<List<MsHySourceCountyData>> getMsHyDaySourceCountyData(String queryTime) {
        return getMsHySourceCountyData(MeishanMobileRequest.REQUEST_GET_MS_HY_DAY_SOURCE_COUNTY_DATA, queryTime);
    }

    @Override
    public MeishanMobileResult<List<MsHySourceCountyData>> getMsHyMonthSourceCountyData(String queryTime) {
        return getMsHySourceCountyData(MeishanMobileRequest.REQUEST_GET_MS_HY_MONTH_SOURCE_COUNTY_DATA, queryTime);
    }

    private MeishanMobileResult<List<MsHySourceCountyData>> getMsHySourceCountyData(String aName, String queryTime) {
        MeishanMobileRequest.Builder builder = new MeishanMobileRequest.Builder();
        MeishanMobileRequest request = builder
                .param("queryTime", queryTime)
                .build(aName);
        String jsonResult = meishanMobileFeignClient.getSafetySupervision(request.getAName(), request.getUid(), request.getTimestamp(), request.getSignature(), request.getParam());
        log.debug("请求结果json: {}", jsonResult);
        return JsonUtil.parse(jsonResult, new TypeReference<MeishanMobileResult<List<MsHySourceCountyData>>>() {
        });
    }

    @Override
    public MeishanMobileResult<List<MsHySourceCountryData>> getMsHyDaySourceCountryData(String queryTime) {
        return getMsHySourceCountryData(MeishanMobileRequest.REQUEST_GET_MS_HY_DAY_SOURCE_COUNTRY_DATA, queryTime);
    }

    @Override
    public MeishanMobileResult<List<MsHySourceCountryData>> getMsHyMonthSourceCountryData(String queryTime) {
        return getMsHySourceCountryData(MeishanMobileRequest.REQUEST_GET_MS_HY_MONTH_SOURCE_COUNTRY_DATA, queryTime);
    }

    private MeishanMobileResult<List<MsHySourceCountryData>> getMsHySourceCountryData(String aName, String queryTime) {
        MeishanMobileRequest.Builder builder = new MeishanMobileRequest.Builder();
        MeishanMobileRequest request = builder
                .param("queryTime", queryTime)
                .build(aName);
        String jsonResult = meishanMobileFeignClient.getSafetySupervision(request.getAName(), request.getUid(), request.getTimestamp(), request.getSignature(), request.getParam());
        log.debug("请求结果json: {}", jsonResult);
        return JsonUtil.parse(jsonResult, new TypeReference<MeishanMobileResult<List<MsHySourceCountryData>>>() {
        });
    }

    @Override
    public MeishanMobileResult<List<MsHyPeopleHotData>> getMsHyMinutePeopleHotData() {
        MeishanMobileRequest.Builder builder = new MeishanMobileRequest.Builder();
        MeishanMobileRequest request = builder.build(MeishanMobileRequest.REQUEST_GET_MS_HY_MINUTE_PEOPLE_HOT_DATA);
        String jsonResult = meishanMobileFeignClient.getSafetySupervision(request.getAName(), request.getUid(), request.getTimestamp(), request.getSignature(), request.getParam());
        log.debug("请求结果json: {}", jsonResult);
        return JsonUtil.parse(jsonResult, new TypeReference<MeishanMobileResult<List<MsHyPeopleHotData>>>() {
        });
    }
}
