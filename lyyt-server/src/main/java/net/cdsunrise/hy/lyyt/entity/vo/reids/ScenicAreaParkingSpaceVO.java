package net.cdsunrise.hy.lyyt.entity.vo.reids;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import net.cdsunrise.common.utility.utils.JsonUtils;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * scenicAreaParkingSpaceVO
 * 停车监测对象
 * <p>因为我也不知道字段具体意思，所有没有字段注释，只是为了排序，把原来返回的Object转换成明确对象</p>
 * @author LiuYin
 * @date 2020/6/8 15:41
 */
@Data
public class ScenicAreaParkingSpaceVO {

    private String summaryCode;
    private String summary;
    private Integer parkingLotNumber;
    private Integer parkingSpaceNumber;
    private Integer inUseNumber;
    private Integer remainingNumber;
    private Double inUseDegree;
    private String saturationLevelCode;
    private String saturationLevel;


    /**
     * 把map转换成停车监控对象
     * @param map map
     * @return vo
     */
    public static ScenicAreaParkingSpaceVO fromMap(Map<String,Object> map){
        Objects.requireNonNull(map, "map is null");
        final String json = JsonUtils.toJsonString(map);
        return JsonUtils.toObject(json, new TypeReference<ScenicAreaParkingSpaceVO>() {
        });
    }

}
