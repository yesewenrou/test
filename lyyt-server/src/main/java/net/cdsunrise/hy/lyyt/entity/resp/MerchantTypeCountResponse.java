package net.cdsunrise.hy.lyyt.entity.resp;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author sh
 * @date 2020-01-18 16:32
 */
@Data
public class MerchantTypeCountResponse {
    private String businessCircleCode;
    private String businessCircleName;
    private List<Count> counts = new ArrayList<>();

    @Data
    public static class Count {
        private String merchantTypeCode;
        private String merchantTypeName;
        private Long count;
    }
}
