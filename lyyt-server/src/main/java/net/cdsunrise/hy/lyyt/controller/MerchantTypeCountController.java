package net.cdsunrise.hy.lyyt.controller;

import net.cdsunrise.hy.lyyt.annatation.DataType;
import net.cdsunrise.hy.lyyt.entity.resp.MerchantTypeCountResponse;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.service.MerchantTypeCountService;
import net.cdsunrise.hy.lyyt.utils.SortUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sh
 * @date 2020-01-18 16:42
 */
@RestController
@RequestMapping("/merchant/type")
public class MerchantTypeCountController {
    private final MerchantTypeCountService merchantTypeCountService;

    @Autowired
    public MerchantTypeCountController(MerchantTypeCountService merchantTypeCountService) {
        this.merchantTypeCountService = merchantTypeCountService;
    }

    @GetMapping("/count")
    @DataType({DataTypeEnum.LYSHZYJCSJ})
    public List<MerchantTypeCountResponse> count() {
        final List<MerchantTypeCountResponse> list = merchantTypeCountService.query();
        // 按照商圈名称排序
        return list.stream().sorted((o1, o2) -> SortUtil.getAreaChartNameSortResult(o1.getBusinessCircleName(), o2.getBusinessCircleName())).collect(Collectors.toList());
    }
}
