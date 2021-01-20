package net.cdsunrise.hy.lyyt.es.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

/**
 * @author LHY
 * @date 2020/2/7 10:21
 */
@Data
@Document(indexName = "hotel_inventory", type = "doc", replicas = 2)
public class HotelInventory {

    @Id
    private String stationId;

    /**
     * 酒店实时入住人数
     * */
    private Integer checkinNum;

    /**
     * 酒店状态，1：已满客，0：未满
     * */
    private Integer status = 0;
}
