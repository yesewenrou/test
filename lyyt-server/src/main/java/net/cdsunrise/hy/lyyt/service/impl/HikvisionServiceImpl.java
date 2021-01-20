package net.cdsunrise.hy.lyyt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import net.cdsunrise.common.utility.AssertUtil;
import net.cdsunrise.common.utility.utils.JsonUtils;
import net.cdsunrise.hy.lyyt.entity.dto.HikvisionDTO;
import net.cdsunrise.hy.lyyt.exception.business.BusinessException;
import net.cdsunrise.hy.lyyt.exception.business.BusinessExceptionEnum;
import net.cdsunrise.hy.lyyt.service.HikvisionService;
import net.cdsunrise.hy.lyyt.utils.CameraNameUtil;
import net.cdsunrise.hy.lyyt.utils.HikvisionUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: suzhouhe @Date: 2020/1/18 17:50 @Description:
 */
@Service
public class HikvisionServiceImpl implements HikvisionService {
    /**
     * 海康返回成功的code码
     */
    private static final String RESULT_CODE_SUCCESS = "0";

    @Override
    public String getCameras(String url, JSONObject jsonObject) {
        try {
            return this.convertName(HikvisionUtil.getPostResult(url, jsonObject));
        } catch (Exception e) {
            return JsonUtils.toJsonString(HikvisionDTO.createZeroValue());
        }
    }

    /**
     * 远程摄像头名称转换为本地摄像头名称
     *
     * @param result 远程返回的结果
     * @return 处理后的结果
     */
    private String convertName(String result) {
        AssertUtil.notEmpty(result, () -> new BusinessException(BusinessExceptionEnum.HIKVISION_ERROR));
        HikvisionDTO hikvisionDTO = JsonUtils.toObject(result, new TypeReference<HikvisionDTO>() {
        });
        AssertUtil.notNull(hikvisionDTO, () -> new BusinessException(BusinessExceptionEnum.HIKVISION_ERROR));
        AssertUtil.isTrue(RESULT_CODE_SUCCESS.equals(hikvisionDTO.getCode()),
                () -> new BusinessException(BusinessExceptionEnum.HIKVISION_ERROR));

        HikvisionDTO.HikvisionPage hikvisionPage = JsonUtils.toObject(JsonUtils.toJsonString(hikvisionDTO.getData()),
                new TypeReference<HikvisionDTO.HikvisionPage>() {
                });
        AssertUtil.notNull(hikvisionPage, () -> new BusinessException(BusinessExceptionEnum.HIKVISION_ERROR));
        AssertUtil.notEmpty(hikvisionPage.getList(), () -> new BusinessException(BusinessExceptionEnum.HIKVISION_ERROR));

        List<HikvisionDTO.HikvisionCamera> hikvisionCameras = JsonUtils.toObject(JsonUtils.toJsonString(hikvisionPage.getList()),
                new TypeReference<List<HikvisionDTO.HikvisionCamera>>() {
                });
        hikvisionCameras.forEach(hikvisionCamera -> {
            hikvisionCamera.setCameraName(CameraNameUtil.getRealName(hikvisionCamera.getCameraName()));
        });
        hikvisionPage.setList(new ArrayList<>(hikvisionCameras));
        hikvisionDTO.setData(hikvisionPage);
        return JsonUtils.toJsonString(hikvisionDTO);
    }

}
