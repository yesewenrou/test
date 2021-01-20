package net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.entity.DayLocal;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.mapper.DayLocalMapper;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.msm.operated.TouristLocalData;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.service.DayLocalService;
import net.cdsunrise.hy.lyjc.industrialoperationmonitoring.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

/**
 * @Author: LHY
 * @Date: 2019/9/29 17:32
 */
@Service
@Slf4j
public class DayLocalServiceImpl extends ServiceImpl<DayLocalMapper,DayLocal> implements DayLocalService{

    private DayLocalMapper dayLocalMapper;

    public DayLocalServiceImpl(DayLocalMapper dayLocalMapper){
        this.dayLocalMapper = dayLocalMapper;
    }

    @Override
    public void dealTouristLocalOperated(TouristLocalData touristLocalData) {
        Integer num = checkExist(touristLocalData.getScenicId(),touristLocalData.getTime());
        if (num > 0){
            // 已成功插入数据，不进行更新
        }else {
            DayLocal dayLocal = new DayLocal();
            BeanUtils.copyProperties(touristLocalData,dayLocal);
            dayLocal.setTime(new Timestamp(DateUtil.parse(touristLocalData.getTime(),DateUtil.PATTERN_YYYY_MM_DD).getTime()));
            save(dayLocal);
        }
    }

    private Integer checkExist(String scenicId,String time){
        QueryWrapper<DayLocal> wrapper = new QueryWrapper<>();
        wrapper.eq("scenic_id",scenicId)
                .eq("time",time);
        return dayLocalMapper.selectCount(wrapper);
    }
}
