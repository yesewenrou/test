package net.cdsunrise.hy.lyyt.entity.dto;

import lombok.Data;
import net.cdsunrise.hy.lyyt.utils.CameraNameUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: suzhouhe @Date: 2019/11/12 16:51 @Description:
 */
@Data
public class HikvisionDTO {

    private String code;

    private String msg;

    private Object data;

    @Data
    public static class HikvisionPage{
        private Integer total;

        private Integer pageNo;

        private Integer pageSize;

        private List<Object> list;
    }

    @Data
    public static class HikvisionCamera{
        private String altitude;
        private String cameraIndexCode;
        private String cameraName;
        private String cameraType;
        private String cameraTypeName;
        private String capabilitySet;
        private String capabilitySetName;
        private String intelligentSet;
        private String intelligentSetName;
        private String channelNo;
        private String channelType;
        private String channelTypeName;
        private String createTime;
        private String encodeDevIndexCode;
        private String encodeDevResourceType;
        private String encodeDevResourceTypeName;
        private String gbIndexCode;
        private String installLocation;
        private String keyBoardCode;
        private String latitude;
        private String longitude;
        private String pixel;
        private String ptz;
        private String ptzName;
        private String ptzController;
        private String ptzControllerName;
        private String recordLocation;
        private String recordLocationName;
        private String regionIndexCode;
        private String status;
        private String statusName;
        private String transType;
        private String transTypeName;
        private String treatyType;
        private String treatyTypeName;
        private String viewshed;
        private String updateTime;
    }

    @Data
    public static class HikvisionCameraStatus {
        private String cameraIndexCode;
        private String cameraName;
        private String onlineStatus;
        private String regionIndexCode;
        private String encodeDevIndexCode;
        private String treatyType;
    }

    public static HikvisionDTO createZeroValue(){
        HikvisionDTO hikvisionDTO = new HikvisionDTO();
        hikvisionDTO.setCode("0");
        hikvisionDTO.setMsg("成功");

        HikvisionPage hikvisionPage = new HikvisionPage();
        hikvisionPage.setPageNo(1);
        hikvisionPage.setPageSize(100);
        hikvisionPage.setTotal(11);

        Set<String> cameraNames = CameraNameUtil.REAL_REMOTE.keySet();

        List<Object> collect = cameraNames.stream().map(cameraName -> {
            HikvisionCamera hikvisionCamera = new HikvisionCamera();
            hikvisionCamera.setCameraName(cameraName);
            return hikvisionCamera;
        }).collect(Collectors.toList());

        hikvisionPage.setList(collect);
        hikvisionDTO.setData(hikvisionPage);
        return hikvisionDTO;
    }
}
