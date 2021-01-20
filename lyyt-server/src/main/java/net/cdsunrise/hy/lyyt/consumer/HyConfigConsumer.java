package net.cdsunrise.hy.lyyt.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.common.utility.AssertUtil;
import net.cdsunrise.common.utility.utils.JsonUtils;
import net.cdsunrise.hy.lyyt.entity.dto.ImportRoadSectionMessageDTO;
import net.cdsunrise.hy.lyyt.entity.dto.YdpInfoDTO;
import net.cdsunrise.hy.lyyt.entity.dto.YdpStatusDTO;
import net.cdsunrise.hy.lyyt.enums.ImportRoadSectionMessageTypeEnum;
import net.cdsunrise.hy.lyyt.exception.business.BusinessException;
import net.cdsunrise.hy.lyyt.exception.business.BusinessExceptionEnum;
import net.cdsunrise.hy.lyyt.utils.RoadSectionUtil;
import net.cdsunrise.hy.lyyt.utils.YdpInfoUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * @ClassName HyConfigConsumer
 * @Description 洪雅配置项消费
 * @Author LiuYin
 * @Date 2019/12/13 15:13
 */
@Component
@Slf4j
public class HyConfigConsumer {

    /** 洪雅配置项topic*/
    private static final String CONFIGURATION_TOPIC = "hy_configuration";

    private static final String YDP_STATUS_TOPIC = "pba-travel_hardware-info_screen_network";

    /** key:重点路段*/
    private static final String KEY_IMPORTANT_ROAD_SECTION = "import_road_section";
    /** key:诱导屏信息*/
    private static final String KEY_YDP_INFO ="ydp_info";

    @PostConstruct
    public void init(){

    }

    /**
     * 消费洪雅配置项
     * @param record 消费对象
     */
    @KafkaListener(topics = {CONFIGURATION_TOPIC,YDP_STATUS_TOPIC})
    public void consumer(ConsumerRecord<String, String > record){
        final String value = record.value();
        if(StringUtils.isEmpty(value)){
            return;
        }
        final String key = record.key();
        final String topic = record.topic();
        switch (topic){
            case CONFIGURATION_TOPIC:
                handleConfiguration(value, key, topic);
                break;
            case YDP_STATUS_TOPIC:
                handleYdpStatus(value,topic);
                break;
             default:
                 log.error("topic [{}] is illegal", topic);
        }

    }

    /**
     * 处理诱导屏状态
     * @param value kafka的message
     * @param topic kafka的消费topic
     */
    private void handleYdpStatus(String value, String topic){
        try {
            final YdpStatusDTO ydpStatusDTO = JsonUtils.toObject(value, new TypeReference<YdpStatusDTO>() {
            });
            // 校验非空
            AssertUtil.notNull(ydpStatusDTO, bizError(BusinessExceptionEnum.YDP_STATUS_DTO_IS_NULL));

            final String netStatus = ydpStatusDTO.getNetStatus();
            AssertUtil.notEmpty(netStatus, bizError(BusinessExceptionEnum.YDP_NET_STATUS_IS_EMPTY));

            final Long id = ydpStatusDTO.getId();
            // 因为有之前老数据影响，这里要判断id是否为空（老数据用的是deviceId）
            if(Objects.isNull(id)){
                log.warn("ydp status dto id is null, do nothing");
                return ;
            }
            YdpInfoUtil.updateStatus(id, netStatus);

        }catch (Exception e){
            log.error("update ydp status failed, topic [{}], cause", topic, e);
        }
    }


    /**
     * 处理洪雅配置相关项
     * @param value kafka的message
     * @param key kafka的key
     * @param topic kafka的topic
     */
    private void handleConfiguration(String value, String key, String topic){
        switch (key){
            // 重点路段
            case KEY_IMPORTANT_ROAD_SECTION:
//                setImportantRoadSection(value);
                break;
            case KEY_YDP_INFO:
                updateYDPInfo(value);
                break;
            default:
                log.error("unknown key [{}] at topic [{}]", key, topic);
        }
    }



    /**
     * 更新诱导屏信息（带校验）
     * @param value 诱导屏信息message
     */
    private void updateYDPInfo(String value){
        try {
            final YdpInfoDTO ydpInfoDTO = JsonUtils.toObject(value, new TypeReference<YdpInfoDTO>() {
            });
            checkYdpInfo(ydpInfoDTO);
            doUpdateYdpInfo(ydpInfoDTO);

        }catch (Exception e){
            log.error("update ydp info error, value is [{}], cause", value, e);
        }
    }

    /**
     * 更新诱导屏信息（无校验）
     * @param ydpInfoDTO 诱导屏信息对象
     */
    private void doUpdateYdpInfo(YdpInfoDTO ydpInfoDTO) {
        YdpInfoUtil.updateName(ydpInfoDTO.getId(), ydpInfoDTO.getName());
    }

    private void checkYdpInfo(YdpInfoDTO ydpInfoDTO){
        AssertUtil.notNull(ydpInfoDTO, bizError(BusinessExceptionEnum.YDP_INFO_IS_NULL));
        AssertUtil.notNull(ydpInfoDTO.getId(), bizError(BusinessExceptionEnum.YDP_INFO_ID_IS_NULL));
        AssertUtil.notEmpty(ydpInfoDTO.getName(), bizError(BusinessExceptionEnum.YDP_INFO_NAME_IS_EMPTY));
    }



    /**
     * 设置重点路段
     * @param value 消费的值
     */
    private void setImportantRoadSection(String value){
        final ImportRoadSectionMessageDTO importRoadSectionMessageDTO = JsonUtils.toObject(value, new TypeReference<ImportRoadSectionMessageDTO>() {
        });
        if(Objects.isNull(importRoadSectionMessageDTO)){
            log.error("parse import road section error, value is [{}]",value);
            return ;
        }
        final ImportRoadSectionMessageTypeEnum typeEnum = ImportRoadSectionMessageTypeEnum.getByType(importRoadSectionMessageDTO.getType());
        switch (typeEnum){
            case DELETE:
                doDeleteImportantRoadSection();
                break;
            case UPDATE:
                doUpdateImportantRoadSection(importRoadSectionMessageDTO);
                break;
            default:
                log.error("unknown import road section message type [{}]", typeEnum);

        }
    }

    /**
     * 删除所有重点路段
     */
    private void doDeleteImportantRoadSection(){
        RoadSectionUtil.deleteAllImportRoadSection();
    }

    /**
     * 更新重点路段
     * @param importRoadSectionMessageDTO 重点路段对象
     */
    private void doUpdateImportantRoadSection(ImportRoadSectionMessageDTO importRoadSectionMessageDTO){
        if(Objects.isNull(importRoadSectionMessageDTO)){
            return ;
        }
        final String sectionIdString = importRoadSectionMessageDTO.getSectionIdString();
        if(Objects.isNull(sectionIdString)){
            return ;
        }
        final String[] split = sectionIdString.split(",");
        RoadSectionUtil.updateImportRoadSection(split);
    }

    private static Supplier<BusinessException> bizError(BusinessExceptionEnum businessExceptionEnum){
        return BusinessException.fail(businessExceptionEnum);
    }
}
