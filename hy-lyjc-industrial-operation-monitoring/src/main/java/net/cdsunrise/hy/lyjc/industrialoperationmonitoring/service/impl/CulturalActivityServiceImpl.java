package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import net.cdsunrise.common.utility.vo.PageResult;
import net.cdsunrise.common.utility.vo.Result;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.CulturalActivityDO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.req.CulturalActivityAddReq;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.vo.CulturalActivityVO;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.CulturalActivityMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.CulturalActivityService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.PageUtil;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @Author: suzhouhe @Date: 2020/3/26 14:10 @Description: 文化活动
 */
@Service
public class CulturalActivityServiceImpl implements CulturalActivityService {

    private static final String CHAR = ",";

    @Autowired
    private CulturalActivityMapper culturalActivityMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result add(CulturalActivityAddReq culturalActivityAddReq) {
        Result result = this.checkAdd(culturalActivityAddReq);
        if (!result.getSuccess()) {
            return result;
        }
        CulturalActivityDO culturalActivityDO = this.dtoToDo(culturalActivityAddReq);
        culturalActivityMapper.insert(culturalActivityDO);
        return ResultUtil.buildSuccessResultWithData(culturalActivityDO.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result delete(Long id) {
        culturalActivityMapper.deleteById(id);
        return ResultUtil.success();
    }

    @Override
    public Result list(String culturalName, Long beginTime, Long endTime, int page, int size) {
        LambdaQueryWrapper<CulturalActivityDO> queryWrapper = new QueryWrapper<CulturalActivityDO>().lambda()
                .like(!StringUtils.isEmpty(culturalName), CulturalActivityDO::getCulturalName, culturalName)
                .ge(beginTime != null, CulturalActivityDO::getCreateTime, beginTime)
                .le(endTime != null, CulturalActivityDO::getCreateTime, endTime)
                .orderByDesc(CulturalActivityDO::getCulturalTime);
        IPage<CulturalActivityDO> culturalActivityPage = culturalActivityMapper.selectPage(new Page<>(page, size), queryWrapper);
        PageResult<CulturalActivityVO> pageVO = PageUtil.page(culturalActivityPage, this::doToVo);
        return ResultUtil.buildSuccessResultWithData(pageVO);
    }

    /**
     * do转vo
     *
     * @param culturalActivityDO do
     * @return vo
     */
    private CulturalActivityVO doToVo(CulturalActivityDO culturalActivityDO) {
        CulturalActivityVO culturalActivity = new CulturalActivityVO();
        culturalActivity.setCoverPhotoUrl(culturalActivityDO.getCoverPhotoUrl());
        culturalActivity.setCulturalName(culturalActivityDO.getCulturalName());
        culturalActivity.setCulturalTime(DateUtil.dateTimeToLong(culturalActivityDO.getCulturalTime()));
        if (!StringUtils.isEmpty(culturalActivityDO.getFileNames())) {
            culturalActivity.setFileNames(Stream.of(culturalActivityDO.getFileNames().split(CHAR)).collect(Collectors.toList()));
        }
        if (!StringUtils.isEmpty(culturalActivityDO.getFileUrls())) {
            culturalActivity.setFileUrls(Stream.of(culturalActivityDO.getFileUrls().split(CHAR)).collect(Collectors.toList()));
        }
        culturalActivity.setId(culturalActivityDO.getId());
        return culturalActivity;
    }

    /**
     * 验证新增参数
     *
     * @param culturalActivityAddReq 新增实体
     * @return 返回验证结果
     */
    private Result checkAdd(CulturalActivityAddReq culturalActivityAddReq) {
        if (StringUtils.isEmpty(culturalActivityAddReq.getCulturalName())) {
            return ResultUtil.buildResult(false, "文化活动名称不能为空");
        }
        if (culturalActivityAddReq.getCulturalTime() == null) {
            return ResultUtil.buildResult(false, "文化活动时间不能为空");
        }
        if (CollectionUtils.isEmpty(culturalActivityAddReq.getFileUrls()) || CollectionUtils.isEmpty(culturalActivityAddReq.getFileNames())) {
            return ResultUtil.buildResult(false, "附件不能为空");
        }
        return ResultUtil.success();
    }

    private CulturalActivityDO dtoToDo(CulturalActivityAddReq culturalActivityAddReq) {
        CulturalActivityDO culturalActivityDO = new CulturalActivityDO();
        culturalActivityDO.setCulturalName(culturalActivityAddReq.getCulturalName());
        culturalActivityDO.setCoverPhotoUrl(culturalActivityAddReq.getCoverPhotoUrl());
        culturalActivityDO.setCreateTime(LocalDateTime.now());
        culturalActivityDO.setCulturalTime(DateUtil.longToLocalDateTime(culturalActivityAddReq.getCulturalTime()));
        culturalActivityDO.setFileNames(String.join(CHAR, culturalActivityAddReq.getFileNames()));
        culturalActivityDO.setFileUrls(String.join(CHAR, culturalActivityAddReq.getFileUrls()));
        return culturalActivityDO;
    }
}
