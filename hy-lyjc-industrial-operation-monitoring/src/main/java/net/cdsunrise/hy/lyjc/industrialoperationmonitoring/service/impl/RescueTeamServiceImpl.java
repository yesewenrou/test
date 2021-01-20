package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.RescueTeam;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.enums.RescueTypeEnum;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.exception.ParamErrorException;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.RescueTeamMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.RescueTeamService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.RescueTeamPageReq;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.vo.RescueTeamVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

/**
 * @author lijiafeng 2021/01/18 15:02
 */
@Service
public class RescueTeamServiceImpl extends ServiceImpl<RescueTeamMapper, RescueTeam> implements RescueTeamService {

    @Override
    public Long add(RescueTeamVO req) {
        nameCannotDuplicate(req.getName(), null);
        RescueTeam rescueTeam = convertFromVO(req);
        save(rescueTeam);
        return rescueTeam.getId();
    }

    @Override
    public void update(RescueTeamVO req) {
        nameCannotDuplicate(req.getName(), req.getId());
        RescueTeam rescueTeam = convertFromVO(req);
        updateById(rescueTeam);
    }

    @Override
    public RescueTeamVO get(Long id) {
        RescueTeam rescueTeam = getById(id);
        if (Objects.isNull(rescueTeam)) {
            throw new ParamErrorException("指定ID救援队伍不存在");
        }
        return convertToVO(rescueTeam);
    }

    @Override
    public IPage<RescueTeamVO> page(RescueTeamPageReq req) {
        LambdaQueryWrapper<RescueTeam> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(Objects.nonNull(req.getType()), RescueTeam::getType, req.getType())
                .like(StringUtils.hasText(req.getName()), RescueTeam::getName, req.getName());
        Page<RescueTeam> page = new Page<>(req.getCurrent(), req.getSize());
        page(page, queryWrapper);
        return page.convert(this::convertToVO);
    }

    /**
     * 名称不能重复
     *
     * @param name      名称
     * @param excludeId 排除ID，更新时排除自身
     */
    private void nameCannotDuplicate(String name, Long excludeId) {
        LambdaQueryWrapper<RescueTeam> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(RescueTeam::getName, name)
                .ne(Objects.nonNull(excludeId), RescueTeam::getId, excludeId);
        if (count(queryWrapper) > 0) {
            throw new ParamErrorException("已存在同名救援队伍");
        }
    }

    private RescueTeam convertFromVO(RescueTeamVO src) {
        RescueTeam dest = new RescueTeam();
        BeanUtils.copyProperties(src, dest);
        return dest;
    }

    private RescueTeamVO convertToVO(RescueTeam src) {
        RescueTeamVO dest = new RescueTeamVO();
        BeanUtils.copyProperties(src, dest);
        RescueTypeEnum rescueTypeEnum = RescueTypeEnum.getByType(src.getType());
        if (Objects.nonNull(rescueTypeEnum)) {
            dest.setTypeDesc(rescueTypeEnum.getTypeDesc());
        }
        return dest;
    }
}
