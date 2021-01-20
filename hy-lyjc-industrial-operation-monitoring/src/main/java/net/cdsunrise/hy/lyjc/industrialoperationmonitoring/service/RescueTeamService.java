package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.RescueTeam;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.RescueTeamPageReq;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.RescueTeamVO;

/**
 * @author lijiafeng 2021/01/18 15:01
 */
public interface RescueTeamService extends IService<RescueTeam> {

    /**
     * 新增救援队伍
     *
     * @param req 请求
     * @return 结果
     */
    Long add(RescueTeamVO req);

    /**
     * 更新救援队伍
     *
     * @param req 请求
     */
    void update(RescueTeamVO req);

    /**
     * 查询救援队伍详情
     *
     * @param id 主键ID
     * @return 结果
     */
    RescueTeamVO get(Long id);

    /**
     * 分页查询救援队伍列表
     *
     * @param req 请求
     * @return 结果
     */
    IPage<RescueTeamVO> page(RescueTeamPageReq req);
}
