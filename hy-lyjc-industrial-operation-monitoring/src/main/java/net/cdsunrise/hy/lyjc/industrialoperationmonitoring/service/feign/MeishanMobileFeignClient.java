package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author lijiafeng
 * @date 2019/9/26 10:29
 */
@FeignClient(name = "meishan-mobile", url = "${msm.service.address}")
public interface MeishanMobileFeignClient {

    /**
     * 眉山移动接口
     *
     * @param aName     接口名称
     * @param uid       用户key
     * @param timestamp 时间戳
     * @param signature 校验值
     * @param param     参数
     * @return 结果
     */
    @RequestMapping(value = "ExternalInterfacePlatform/getSafetySupervision.do", method = RequestMethod.GET)
    String getSafetySupervision(
            @RequestParam("aName")
                    String aName,
            @RequestParam("uid")
                    String uid,
            @RequestParam("timestamp")
                    String timestamp,
            @RequestParam("signature")
                    String signature,
            @RequestParam("param")
                    String param
    );
}
