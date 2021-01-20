package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.DutyRoster;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DutyRosterRequest;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.DutyRosterVO;

import java.util.List;

/**
 * @author lijiafeng
 * @date 2019/11/25 10:35
 */
public interface IDutyRosterService extends IService<DutyRoster> {

    /**
     * 查询值班表
     *
     * @return 结果
     */
    List<DutyRosterVO> listDutyRoster();

    /**
     * 更新值班表
     *
     * @param dutyRosterRequest 值班信息
     * @return 结果
     */
    boolean updateDutyRoster(DutyRosterRequest dutyRosterRequest);

    DutyRosterVO getDutyRoster(String dutyTime);
}
