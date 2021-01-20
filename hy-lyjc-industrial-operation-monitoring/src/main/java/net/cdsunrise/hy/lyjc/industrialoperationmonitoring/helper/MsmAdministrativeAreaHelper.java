package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.helper;

import net.cdsunrise.common.utility.AssertUtil;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.MsmAdministrativeAreaFeignClient;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.MsmCityVO;
import net.cdsunrise.hy.lydd.datadictionary.autoconfigure.feign.vo.MsmProvinceVO;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.xml.ws.Holder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * MsmAdministrativeAreaHelper
 * 移动地区数据帮助类
 * @author LiuYin
 * @date 2020/3/26 16:00
 */
public class MsmAdministrativeAreaHelper {

    private static MsmAdministrativeAreaFeignClient client;

    private static List<MsmProvinceVO> list;

    private static Map<String, Set<String>> nameMap;

    public static List<MsmProvinceVO> getList() {
        return list;
    }

    private static String[] OUTSIDE_PROVINCE_NAME_ARRAY = {"中国香港","中国台湾","中国澳门"};

    /**
     * 获取名称map
     * @return 省市对应map
     */
    public static Map<String, Set<String>> getNameMap() {
        return nameMap;
    }

    /**
     * 是否是大陆外的省
     * @param provName 省名称
     * @return true or false
     */
    public static boolean isOutsideProvince(String provName){
        return Objects.nonNull(provName) && (provName.contains("香港") || provName.contains("澳门") || provName.contains("台湾"));
    }


    /**
     * 加载数据，系统初始化时运行
     * @param msmAdministrativeAreaFeignClient 客户端
     */
    public static void init(MsmAdministrativeAreaFeignClient msmAdministrativeAreaFeignClient){
        AssertUtil.notNull(msmAdministrativeAreaFeignClient, () -> new RuntimeException("msmAdministrativeAreaFeignClient is null"));
        client = msmAdministrativeAreaFeignClient;
        final Result<List<MsmProvinceVO>> listResult = client.listProvinces();
        AssertUtil.notNull(listResult, () -> new RuntimeException("result is null"));
        AssertUtil.isTrue(listResult.getSuccess(), () -> new RuntimeException("result is failed cause :" + listResult.getMessage()));
        final List<MsmProvinceVO> data = listResult.getData();
        AssertUtil.notNull(data, () -> new RuntimeException("result data is null"));

        list = new ArrayList<>(data);
        toMap();
    }


    private static void toMap(){
        if(CollectionUtils.isEmpty(list)){
            return ;
        }

        Map<String, Set<String>> map = new HashMap<>(list.size());

        list.forEach(p -> {
            final List<MsmCityVO> cities = p.getCities();
            map.putIfAbsent(p.getProvName(), CollectionUtils.isEmpty(cities) ? new HashSet<>(0) : cities.stream().map(MsmCityVO::getCityName).collect(Collectors.toSet()));
        });
        final String[] outsideProvinceNames = getOutsideProvinceNames();
        for (String outsideProvinceName : outsideProvinceNames) {
            map.put(outsideProvinceName, new HashSet<>(0));
        }
        nameMap = Collections.unmodifiableMap(map);
    }

    public static String[] getOutsideProvinceNames() {
        return Arrays.copyOf(OUTSIDE_PROVINCE_NAME_ARRAY, OUTSIDE_PROVINCE_NAME_ARRAY.length);
    }
}
