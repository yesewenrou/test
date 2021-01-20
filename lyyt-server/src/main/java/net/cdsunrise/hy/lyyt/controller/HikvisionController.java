package net.cdsunrise.hy.lyyt.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyyt.annatation.DataType;
import net.cdsunrise.hy.lyyt.enums.DataTypeEnum;
import net.cdsunrise.hy.lyyt.enums.HikvisionApiEnum;
import net.cdsunrise.hy.lyyt.service.HikvisionService;
import net.cdsunrise.hy.lyyt.utils.HikvisionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName OptionController
 * @Description
 * @Author LiuYin
 * @Date 2019/10/18 16:13
 */
@RestController
@RequestMapping("hikvision")
@Slf4j
public class HikvisionController {

    @Autowired
    private HikvisionService hikvisionService;

    @GetMapping("/eventSubscriptionView")
    @DataType({DataTypeEnum.SLYJJCSJ, DataTypeEnum.LYJTZYSJ})
    public String getEventOpenInterface() {
        String url = HikvisionApiEnum.EVENT_SUBSCRIPTION_VIEW.getUrl();
        log.info(url);
        return HikvisionUtil.getPostResult(url, new JSONObject());
    }

    @GetMapping("/eventSubscriptionByEventTypes")
    @DataType({DataTypeEnum.SLYJJCSJ, DataTypeEnum.LYJTZYSJ})
    public String subEventsByTypes(@RequestParam("eventTypes") String eventTypes, @RequestParam("eventDest") String eventDest) {
        String url = HikvisionApiEnum.EVENT_SUBSCRIPTION_BY_EVENT_TYPES.getUrl();

        final JSONObject jsonObject = new JSONObject();
        final List<Integer> eventTypeList = Arrays.stream(eventTypes.split(",")).map(Integer::parseInt).collect(Collectors.toList());
        jsonObject.put("eventTypes", eventTypeList);
        jsonObject.put("eventDest", eventDest);

        log.info("{},{}", url, jsonObject);
        return HikvisionUtil.getPostResult(url, jsonObject);
    }

    @GetMapping("/cameras")
    @DataType({DataTypeEnum.SLYJJCSJ, DataTypeEnum.LYJTZYSJ})
    public String cameras(@RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize") Integer pageSize) {
        String url = HikvisionApiEnum.CAMERAS.getUrl();

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("pageNo", pageNo);
        jsonObject.put("pageSize", pageSize);

        log.info("{},{}", url, jsonObject);
        return hikvisionService.getCameras(url,jsonObject);
    }


    @GetMapping("/cameras/previewURLs")
    @DataType({DataTypeEnum.SLYJJCSJ, DataTypeEnum.LYJTZYSJ})
    public String cameras(@RequestParam("cameraIndexCode") String cameraIndexCode, @RequestParam("streamType") Integer streamType
            , @RequestParam("protocol") String protocol) {
        String url = HikvisionApiEnum.CAMERAS_PREVIEW_URLS.getUrl();

        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("cameraIndexCode", cameraIndexCode);
        jsonObject.put("streamType", streamType);
        jsonObject.put("protocol", protocol);

        log.info("{},{}", url, jsonObject);
        return HikvisionUtil.getPostResult(url, jsonObject);
    }


}
