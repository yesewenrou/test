package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @author LHY
 * @date 2019/10/30 17:19
 */
@Data
@Document(indexName = "hotel_inventory", type = "doc", replicas = 2)
public class HotelInventory {

    @Id
    @Field(type = FieldType.Keyword)
    private String stationId;

    /**
    * 酒店实时入住人数
    * */
    @Field(type = FieldType.Integer)
    private Integer checkinNum;

    /**
     * 酒店状态，1：已满客，0：未满
     * */
    @Field(type = FieldType.Integer)
    private Integer status = 0;
}
