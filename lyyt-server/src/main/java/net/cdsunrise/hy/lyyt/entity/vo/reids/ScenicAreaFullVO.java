package net.cdsunrise.hy.lyyt.entity.vo.reids;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.Data;
import net.cdsunrise.common.utility.utils.JsonUtils;

import java.util.Map;
import java.util.Objects;

/**
 * ScenicAreaFullVO
 * 景区饱和天数VO
 * @author LiuYin
 * @date 2020/6/8 17:02
 */
@Data
public class ScenicAreaFullVO {

    private String scenicAreaCode;
    private String scenicArea;
    private Long thisMonthDaysNumber;
    private Long lastMonthDaysNumber;

    public static ScenicAreaFullVO fromMap(Map<String,Object> map){
        Objects.requireNonNull(map,"map is null");

        final String json = JsonUtils.toJsonString(map);
        return JsonUtils.toObject(json, new TypeReference<ScenicAreaFullVO>() {
        });

    }

}
