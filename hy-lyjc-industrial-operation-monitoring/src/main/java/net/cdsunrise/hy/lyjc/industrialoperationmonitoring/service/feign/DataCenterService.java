package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.feign;

import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.SharedDTO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.MenuVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author: suzhouhe @Date: 2020/3/7 14:32 @Description:
 */
@FeignClient(name = "lydsj-data-center", url = "${hy.lydsj.lydsj-data-center.url}")
public interface DataCenterService {

    @GetMapping("/common/menu")
    Result<List<MenuVO>> getMenu();

    @GetMapping("/common/shared")
    Result<List<SharedDTO>> getSharedData();
}
