package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.KeyScenicVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.PageRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.KeyTicketVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.es.KeyTouristVO;

/**
 * @author LHY
 * @date 2020/1/13 15:26
 *
 * 重点景区数据（包含票务数据）
 */
public interface IKeyScenicService {

    /**
     * 游客数据
     * */
    PageResult<KeyTouristVO> touristList(PageRequest<KeyScenicVO> pageRequest);

    /**
     * 票务数据
     * */
    PageResult<KeyTicketVO> ticketList(PageRequest<KeyScenicVO> pageRequest);
}
