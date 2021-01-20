package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * @author funnylog
 * @date 2020/3/8 3:34
 */
@Data
public class WarningRecordDetailVO {
    /**
     * 景区详情
     */
    private ScenicDetail scenicDetail;

    /**
     * 交通详情
     */
    private TrafficDetail trafficDetail;
    /**
     * 停车详情
     */
    private ParkingCarDetail parkingCarDetail;

    /**
     * 流程记录列表
     */
    private List<RecordVO> records;

    /** 预警类型  **/
    private String type;


    @Data
    public static class TrafficDetail {
        /** ID **/
        @TableId(type= IdType.AUTO)
        private Long id;
        /** 预警ID **/
        private Long warningId;
        /** 路段名称 **/
        private String roadName;
        /** 总里程 **/
        private String totalMileage;
        /** 路段描述 **/
        private String roadDesc;
        /** 告警原因 **/
        private String warningReason;
        /** 拥堵指数 **/
        private String congestionIndex;
        /** 平均速度 **/
        private String averageSpeed;
        private Date warningTime;
        private String object;
        private Date createTime;
        private Date updateTime;
    }

    @Data
    public static class ParkingCarDetail {

    }

    @Data
    public static class RecordVO {
        /** 预警id **/
        private Long warningId;
        /** 处理时间 **/
        private Date updateTime;
        /** 处理人姓名 **/
        private String handler;
        /** 处理类型 1 申请发布 2 发送短信 **/
        private Integer handleType;

        /** 内容 **/
        private String content;
        /** 人员或诱导屏名称 多个的话会以逗号连接 **/
        private String receiver;

        /** 节目名称 **/
        private String programName;
        /** 推送渠道 **/
        private String messageChannel;
        /** 信息屏内容**/
        private String programContent;
        private List<AutoWarningCondition.Attach> attaches;

    }

    @Data
    public static class ScenicDetail {
        /** 景区详情id **/
        private Long scenicDetailId;
        /**景区名称 **/
        private String scenicName;
        /** 实时游客数 **/
        private Integer peopleNum;
        /** 告警时间 **/
        private Long warningTime;
    }
}
