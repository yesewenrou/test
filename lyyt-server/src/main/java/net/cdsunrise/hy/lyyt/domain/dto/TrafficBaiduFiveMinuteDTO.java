package net.cdsunrise.hy.lyyt.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.cdsunrise.hy.lyyt.utils.DateUtil;
import org.springframework.data.elasticsearch.annotations.Document;

import java.util.Date;

/**
 * 百度API接入过来后,存到ES里面的数据
 * @author YQ on 2019/12/12.
 */
@Data
@Document(indexName = "hy_mock_data_five_minute", type = "_doc")
public class TrafficBaiduFiveMinuteDTO {
    /** 路段id*/
    private String sectionId;

    /** 日期时间*/
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date rangeTime;
    /** 拥堵长度*/
    private Double jamLength;
    /** 平均速度*/
    private Double avgSpeed;
    /** 拥堵指数*/
    private Double tpi;

    public static TrafficBaiduFiveMinuteDTO createZero(String sectionId,Long time){
        String strTime = DateUtil.convertLong2String(time);
        TrafficBaiduFiveMinuteDTO trafficBaiduFiveMinute = new TrafficBaiduFiveMinuteDTO();
        trafficBaiduFiveMinute.setTpi(0.0);
        trafficBaiduFiveMinute.setSectionId(sectionId);
        trafficBaiduFiveMinute.setJamLength(0.0);
        trafficBaiduFiveMinute.setAvgSpeed(0.0);
        trafficBaiduFiveMinute.setRangeTime(DateUtil.stringToDate(strTime));
        return trafficBaiduFiveMinute;
    }

}
