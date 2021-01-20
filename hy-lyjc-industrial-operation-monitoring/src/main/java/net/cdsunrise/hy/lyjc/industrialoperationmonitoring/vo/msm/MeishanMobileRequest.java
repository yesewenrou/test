package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.msm;

import com.asiainfo.crypt.CryptUtil;
import lombok.Data;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.JsonUtil;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lijiafeng
 * @date 2019/9/26 10:59
 */
@Data
public class MeishanMobileRequest {

    /**
     * UID
     */
    public static final String UID = "mshy_lv";

    /**
     * 返回眉山市洪雅县当前人流总数、游客人数和常驻人数
     */
    public static final String REQUEST_GET_MS_HY_MINUTE_LOCAL_DATA = "GetMsHyMinuteLocalData";

    /**
     * 按小时指定查询时间，反馈眉山市洪雅县小时客流数据
     */
    public static final String REQUEST_GET_MS_HY_HOUR_LOCAL_DATA = "GetMsHyHourLocalData";

    /**
     * 按天指定查询时间，反馈眉山市洪雅县日客流数据
     */
    public static final String REQUEST_GET_MS_HY_DAY_LOCAL_DATA = "GetMsHyDayLocalData";

    /**
     * 按月指定查询时间，反馈眉山市洪雅县月客流数据
     */
    public static final String REQUEST_GET_MS_HY_MONTH_LOCAL_DATA = "GetMsHyMonthLocalData";

    /**
     * 按天指定查询时间，反馈眉山市洪雅县国内各省来源地日人流数据
     */
    public static final String REQUEST_GET_MS_HY_DAY_SOURCE_PROV_DATA = "GetMsHyDaySourceProvData";

    /**
     * 按月指定查询时间，反馈眉山市洪雅县国内各省来源地月人流数据
     */
    public static final String REQUEST_GET_MS_HY_MONTH_SOURCE_PROV_DATA = "GetMsHyMonthSourceProvData";

    /**
     * 按天指定查询时间，反馈眉山市洪雅县国内各地市来源地日人流数据
     */
    public static final String REQUEST_GET_MS_HY_DAY_SOURCE_CITY_DATA = "GetMsHyDaySourceCityData";

    /**
     * 按月指定查询时间，反馈眉山市洪雅县国内各地市来源地月人流数据
     */
    public static final String REQUEST_GET_MS_HY_MONTH_SOURCE_CITY_DATA = "GetMsHyMonthSourceCityData";

    /**
     * 按天指定查询时间，反馈眉山市洪雅县市内来源地日人流数据
     */
    public static final String REQUEST_GET_MS_HY_DAY_SOURCE_COUNTY_DATA = "GetMsHyDaySourceCountyData";

    /**
     * 按月指定查询时间，反馈眉山市洪雅县市内来源地月人流数据
     */
    public static final String REQUEST_GET_MS_HY_MONTH_SOURCE_COUNTY_DATA = "GetMsHyMonthSourceCountyData";

    /**
     * 按天指定查询时间，反馈眉山市洪雅县国际来源地日人流数据
     */
    public static final String REQUEST_GET_MS_HY_DAY_SOURCE_COUNTRY_DATA = "GetMsHyDaySourceCountryData";

    /**
     * 按月指定查询时间，反馈眉山市洪雅县国际来源地月人流数据
     */
    public static final String REQUEST_GET_MS_HY_MONTH_SOURCE_COUNTRY_DATA = "GetMsHyMonthSourceCountryData";

    /**
     * 返回眉山市洪雅县当前人流热力图
     */
    public static final String REQUEST_GET_MS_HY_MINUTE_PEOPLE_HOT_DATA = "GetMsHyMinutePeopleHotData";

    private String aName;

    private String uid;

    private String timestamp;

    private String signature;

    private String param;

    private MeishanMobileRequest(String aName, String uid, String timestamp, String signature, String param) {
        this.aName = aName;
        this.uid = uid;
        this.timestamp = timestamp;
        this.signature = signature;
        this.param = param;
    }

    public static class Builder {

        private Map<String, Object> params = new HashMap<>();

        public Builder param(String name, Object value) {
            params.put(name, value);
            return this;
        }

        public MeishanMobileRequest build(String aName) {
            CryptUtil cryptUtil = new CryptUtil();
            String finalName = cryptUtil.encrypt(aName);
            String finalUid = cryptUtil.encrypt(UID);
            String timestamp = DateUtil.format(new Date(), DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS_S);
            String finalTimestamp = cryptUtil.encrypt(timestamp);
            String finalSignature = cryptUtil.stringMD5(CryptUtil.CIPHER_STRING + aName + "myhylv@#EDC33" + UID + timestamp);
            String finalParam = null;
            if (!params.isEmpty()) {
                finalParam = cryptUtil.encrypt(JsonUtil.toJsonString(params));
            }

            return new MeishanMobileRequest(finalName, finalUid, finalTimestamp, finalSignature, finalParam);
        }
    }
}
