package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es;

import lombok.Data;

/**
 * @author LHY
 * @date 2019/11/13 17:12
 */
@Data
public class HotelStatisticsVO {

    private String stationId;

    /**
     * 酒店名称
     * */
    private String name;

    /**
     * 所属区域
     * */
    private String area;

    /**
     * 所属商圈
     * */
    private String businessCircle;

    /**
     * 酒店地址
     * */
    private String address;

    /**
     * 酒店总数
     * */
    public Long hotelCount;

    /**
     * 估算满住
     * */
    private Long estimateFull;

    /**
     * 总床位数
     * */
    private Double bedNum;

    /**
     * 实时入住
     * */
    private Double realTimeCheckinCount;

    /**
     * 估算入住率
     * */
    private String estimateOccupancy;

    /**
     * 今日累计接待
     * */
    private Long todayReceptionCount;

    /**
     * 昨日累计接待
     * */
    private Long yesterdayReceptionCount;

    public HotelStatisticsVO() {
    }

    public HotelStatisticsVO(Double realTimeCheckinCount, Long todayReceptionCount, Long yesterdayReceptionCount) {
        this.realTimeCheckinCount = realTimeCheckinCount;
        this.todayReceptionCount = todayReceptionCount;
        this.yesterdayReceptionCount = yesterdayReceptionCount;
    }

    public HotelStatisticsVO(Long hotelCount, Long estimateFull, Double bedNum, Double realTimeCheckinCount, String estimateOccupancy, Long todayReceptionCount, Long yesterdayReceptionCount) {
        this.hotelCount = hotelCount;
        this.estimateFull = estimateFull;
        this.bedNum = bedNum;
        this.realTimeCheckinCount = realTimeCheckinCount;
        this.estimateOccupancy = estimateOccupancy;
        this.todayReceptionCount = todayReceptionCount;
        this.yesterdayReceptionCount = yesterdayReceptionCount;
    }

    public HotelStatisticsVO(String stationId, String name, String area, String businessCircle, String address, Double bedNum, Double realTimeCheckinCount, String estimateOccupancy, Long todayReceptionCount, Long yesterdayReceptionCount) {
        this.stationId = stationId;
        this.name = name;
        this.area = area;
        this.businessCircle = businessCircle;
        this.address = address;
        this.bedNum = bedNum;
        this.realTimeCheckinCount = realTimeCheckinCount;
        this.estimateOccupancy = estimateOccupancy;
        this.todayReceptionCount = todayReceptionCount;
        this.yesterdayReceptionCount = yesterdayReceptionCount;
    }
}
