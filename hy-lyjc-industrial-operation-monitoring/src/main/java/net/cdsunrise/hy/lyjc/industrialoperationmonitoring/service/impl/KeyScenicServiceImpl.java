package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.es.TouristPassengerTicket;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.es.mapper.TouristPassengerTicketMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.IKeyScenicService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.KeyScenicVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.KeyTicketVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.KeyTouristVO;
import net.cdsunrise.hy.record.starter.util.PageUtil;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @author LHY
 * @date 2020/1/13 15:37
 */
@Service
public class KeyScenicServiceImpl implements IKeyScenicService{

    private TouristPassengerTicketMapper passengerTicketMapper;

    public KeyScenicServiceImpl(TouristPassengerTicketMapper passengerTicketMapper) {
        this.passengerTicketMapper = passengerTicketMapper;
    }

    @SuppressWarnings("all")
    @Override
    public PageResult<KeyTouristVO> touristList(PageRequest<KeyScenicVO> pageRequest) {
        Page<TouristPassengerTicket> passengerTicketPage = commonQuery(pageRequest);
        return PageUtil.page(passengerTicketPage,touristPassengerTicket -> {
            KeyTouristVO keyTouristVO = new KeyTouristVO();
            BeanUtils.copyProperties(touristPassengerTicket,keyTouristVO);
            return keyTouristVO;
        },passengerTicketPage.getContent());
    }

    private Page commonQuery(PageRequest<KeyScenicVO> pageRequest){
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        KeyScenicVO condition = pageRequest.getCondition();
        if (!StringUtils.isEmpty(condition.getScenicName())){
            boolQueryBuilder.must(QueryBuilders.termQuery("scenicName",condition.getScenicName()));
        }
        if (condition.getStartTime()!=null && condition.getEndTime()!=null) {
            boolQueryBuilder.filter(QueryBuilders.rangeQuery("responseTime").gte(DateUtil.format(condition.getStartTime(), DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS))
                    .lte(DateUtil.format(condition.getEndTime(), DateUtil.PATTERN_YYYY_MM_DD_HH_MM_SS)));
        }
        int page = Integer.parseInt(String.valueOf(pageRequest.getCurrent()))-1;
        int size = Integer.parseInt(String.valueOf(pageRequest.getSize()));
        return passengerTicketMapper.search(boolQueryBuilder, org.springframework.data.domain.PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "responseTime")));
    }

    @SuppressWarnings("all")
    @Override
    public PageResult<KeyTicketVO> ticketList(PageRequest<KeyScenicVO> pageRequest) {
        Page<TouristPassengerTicket> passengerTicketPage = commonQuery(pageRequest);
        return PageUtil.page(passengerTicketPage,touristPassengerTicket -> {
            KeyTicketVO keyTicketVO = new KeyTicketVO();
            keyTicketVO.setOnlineTicketCount(touristPassengerTicket.getOnlineTicketCount());
            keyTicketVO.setOfflineTicketCount(touristPassengerTicket.getOfflineTicketCount());
            // 通过 线上门票销售数+线上门票销售占比，倒推出索道和观光车
            keyTicketVO.setOnlineCablewayCount(Math.round(touristPassengerTicket.getOnlineTicketCount()/touristPassengerTicket.getTicketPercent()*touristPassengerTicket.getCablewayPercent()));
            keyTicketVO.setOnlineSightseeingCarCount(Math.round(touristPassengerTicket.getOnlineTicketCount()/touristPassengerTicket.getTicketPercent()*touristPassengerTicket.getSightseeingCarPercent()));
            keyTicketVO.setResponseTime(touristPassengerTicket.getResponseTime());
            return keyTicketVO;
        },passengerTicketPage.getContent());
    }
}
